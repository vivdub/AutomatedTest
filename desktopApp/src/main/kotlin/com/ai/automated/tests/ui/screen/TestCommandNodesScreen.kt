package com.ai.automated.tests.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.automated.tests.cases.TestCase
import com.ai.automated.tests.ui.components.AppHeader
import com.ai.automated.tests.ui.components.test.command.AvailableCommandCard
import com.ai.automated.tests.ui.components.test.command.EmptyCommandSequence
import com.ai.automated.tests.ui.components.test.command.SelectedCommandCard
import com.ai.automated.tests.ui.theme.Theme.Companion.AppBackground
import com.ai.automated.tests.ui.theme.Theme.Companion.Outline
import com.ai.automated.tests.ui.theme.Theme.Companion.Primary
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
    var isDevicePaneExpanded by remember { mutableStateOf(true) }

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
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 32.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(modifier = Modifier.fillMaxWidth().widthIn(max = 960.dp)) {
                    BasicText(
                        text = "COMMAND SEQUENCE",
                        style = TextStyle(
                            color = TextSecondary,
                            fontSize = 12.sp,
                            letterSpacing = 1.1.sp,
                            fontWeight = FontWeight.Bold
                        )
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

            AndroidDevicePane(
                expanded = isDevicePaneExpanded,
                onToggle = {
                    isDevicePaneExpanded = !isDevicePaneExpanded
                }
            )
        }
    }
}

@Composable
private fun AndroidDevicePane(
    expanded: Boolean,
    onToggle: () -> Unit
) {
    if (expanded) {
        Column(
            modifier = Modifier
                .width(360.dp)
                .fillMaxHeight()
                .background(Surface)
                .border(width = 1.dp, color = Outline)
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    BasicText(
                        text = "ANDROID DEVICE",
                        style = TextStyle(
                            color = TextSecondary,
                            fontSize = 12.sp,
                            letterSpacing = 1.1.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    BasicText(
                        text = "Live device UI",
                        style = TextStyle(
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onToggle)
                        .background(AppBackground)
                        .border(
                            width = 1.dp,
                            color = Outline,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    BasicText(
                        text = "›",
                        style = TextStyle(
                            color = TextPrimary,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(9f / 16f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF1A1C20))
                    .padding(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                        .background(AppBackground)
                        .border(
                            width = 1.dp,
                            color = Outline,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    BasicText(
                        text = "Android device UI\nwill appear here",
                        modifier = Modifier.padding(24.dp),
                        style = TextStyle(
                            color = TextSecondary,
                            fontSize = 14.sp,
                            lineHeight = 21.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .width(48.dp)
                .fillMaxHeight()
                .background(Surface)
                .border(width = 1.dp, color = Outline),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggle)
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                BasicText(
                    text = "‹",
                    style = TextStyle(
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            BasicText(
                text = "DEVICE",
                modifier = Modifier.padding(top = 8.dp),
                style = TextStyle(
                    color = TextSecondary,
                    fontSize = 10.sp,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}
