package com.ai.automated.tests.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.ai.automated.tests.cases.TestCase
import com.ai.automated.tests.ui.components.AppHeader
import com.ai.automated.tests.ui.components.test.command.CommandsPane
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

    LaunchedEffect(Unit) {
        job = getDeviceScreen { deviceScreen = it }
    }

    Column(modifier = Modifier.fillMaxSize().background(AppBackground)) {
        val commandCount = commandNodes.size
        AppHeader(
            title = test.title,
            subtitle = "$commandCount ${if (commandCount == 1) "command" else "commands"} in this test",
            actionText = "▶  Run all",
            onAction = {},
            onBack = onBack,
            actionBackgroundColor = if (commandNodes.isNotEmpty()) Primary else Outline
        )

        Row(modifier = Modifier.fillMaxSize()) {
            CommandsPane(commandNodes, availableCommands, test, deviceClickAt)
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
