package com.ai.automated.tests.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.automated.tests.cases.TestCase
import com.ai.automated.tests.cases.TestCaseRunner
import com.ai.automated.tests.ui.components.AppHeader
import com.ai.automated.tests.ui.components.Chip
import com.ai.automated.tests.ui.components.test.command.CommandsPane
import com.ai.automated.tests.ui.theme.Theme
import com.ai.automated.tests.ui.theme.Theme.Companion.AppBackground
import com.ai.automated.tests.ui.theme.Theme.Companion.Outline
import com.ai.automated.tests.ui.theme.Theme.Companion.Primary
import com.ai.automated.tests.util.helper.AdbHelper
import com.ai.automated.tests.util.test.node.CommandNode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

@Composable
fun TestCommandNodesScreen(test: TestCase, onBack: () -> Unit) {
    val commandNodes = remember(test.id) {
        mutableStateListOf<CommandNode>().apply {
            addAll(test.commandNodes)
        }
    }
    val availableCommands = remember {
        CommandNode.all().distinctBy { it.code }
    }
    var isDevicePaneExpanded by remember { mutableStateOf(true) }
    var deviceScreen by remember { mutableStateOf<ImageBitmap?>(null) }
    var job by remember { mutableStateOf<Job?>(null) }
    var deviceClickAt by remember { mutableStateOf<Pair<Int,Int>?>(null) }
    val testCaseRunner by remember { mutableStateOf(TestCaseRunner()) }
    var currentRunningNode by remember { mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        job = getDeviceScreen { deviceScreen = it }
    }
    val commandCount = commandNodes.size
    Column(modifier = Modifier.fillMaxSize().background(AppBackground)) {
        AppHeader(
            title = test.title,
            subtitle = "$commandCount ${if (commandCount == 1) "command" else "commands"} in this test",
            actionText = "▶  Run all",
            onAction = {
                testCaseRunner.execute(commandNodes){ currentRunningNode = it?.id?:0L }
            },
            onBack = onBack,
            actionBackgroundColor = if (commandNodes.isNotEmpty()) Primary else Outline
        )
        Row(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.weight(.7f)) {
                testCaseRunner.verificationResult?.let { result ->
                    Box(modifier = Modifier.padding(horizontal = 32.dp, vertical = 10.dp)){
                        Chip(color = Theme.SuccessText.copy(.5f)) {
                            Text("${result.passed}/${result.total} Verified", fontSize = 11.sp, color = Color.White)
                        }
                    }
                }
                CommandsPane(commandNodes, currentRunningNode, availableCommands, test, deviceClickAt)
            }
            AndroidDevicePane(
                expanded = isDevicePaneExpanded,
                onToggle = {
                    isDevicePaneExpanded = !isDevicePaneExpanded
                    when (isDevicePaneExpanded) {
                        true -> job = getDeviceScreen { deviceScreen = it }
                        else -> job?.cancel()
                    }
                },
                onDeviceClick = { x,y ->
                    deviceClickAt = Pair(x,y)
                    AdbHelper.click(x,y)
                                },
                bitmap = deviceScreen
            )
        }
    }
}

private fun getDeviceScreen(onFetched: (ImageBitmap) -> Unit): Job {
    var job: Job? = null
    job = CoroutineScope(Dispatchers.IO).launch {
        while (job == null || job?.isActive == true) {
            AdbHelper.takeScreenshot()?.let {
                onFetched.invoke(it.toComposeImageBitmap())
            }
            sleep(300)
        }
    }
    return job
}
