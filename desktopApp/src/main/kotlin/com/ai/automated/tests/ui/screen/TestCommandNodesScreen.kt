package com.ai.automated.tests.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.automated.tests.cases.TestCase
import com.ai.automated.tests.ui.components.AppButton
import com.ai.automated.tests.ui.components.AppHeader
import com.ai.automated.tests.ui.components.test.command.AvailableCommandCard
import com.ai.automated.tests.ui.components.test.command.EmptyCommandSequence
import com.ai.automated.tests.ui.components.test.command.SelectedCommandCard
import com.ai.automated.tests.ui.theme.Theme.Companion.AppBackground
import com.ai.automated.tests.ui.theme.Theme.Companion.Danger
import com.ai.automated.tests.ui.theme.Theme.Companion.DangerBackground
import com.ai.automated.tests.ui.theme.Theme.Companion.Outline
import com.ai.automated.tests.ui.theme.Theme.Companion.Primary
import com.ai.automated.tests.ui.theme.Theme.Companion.PrimaryContainer
import com.ai.automated.tests.ui.theme.Theme.Companion.PrimaryDark
import com.ai.automated.tests.ui.theme.Theme.Companion.Surface
import com.ai.automated.tests.ui.theme.Theme.Companion.TextPrimary
import com.ai.automated.tests.ui.theme.Theme.Companion.TextSecondary
import com.ai.automated.tests.util.test.CommandNode

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

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 32.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(modifier = Modifier.fillMaxWidth().widthIn(max = 960.dp)) {
                BasicText(
                    text = "COMMAND SEQUENCE",
                    style = TextStyle(color = TextSecondary, fontSize = 12.sp, letterSpacing = 1.1.sp, fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(6.dp))

                BasicText(
                    text = "Commands run in the order shown below.",
                    style = TextStyle(
                        color = TextSecondary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (commandNodes.isEmpty()) {
                    EmptyCommandSequence()
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        commandNodes.forEachIndexed { index, command ->
                            SelectedCommandCard(
                                position = index + 1,
                                command = command,
                                onConfigure = {
                                    // Configuration behavior will be added separately.
                                },
                                onRun = {
                                    // Command execution behavior will be added separately.
                                },
                                onRemove = {
                                    commandNodes.removeAt(index)
                                    test.commandNodes.removeAt(index)
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                BasicText(
                    text = "AVAILABLE COMMANDS",
                    style = TextStyle(
                        color = TextSecondary,
                        fontSize = 12.sp,
                        letterSpacing = 1.1.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                BasicText(
                    text = "Add an operation to the end of this test’s command sequence.",
                    style = TextStyle(
                        color = TextSecondary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    availableCommands.forEach { command ->
                        AvailableCommandCard(
                            command = command,
                            onAdd = {
                                val newCommand = command.copy(
                                    values = command.values.toMutableList()
                                )
                                commandNodes.add(newCommand)
                                test.commandNodes.add(newCommand)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
}