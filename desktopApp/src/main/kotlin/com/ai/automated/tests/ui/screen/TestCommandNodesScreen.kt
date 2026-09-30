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
fun TestCommandNodesScreen(
    test: TestCase,
    onBack: () -> Unit
) {
    val commandNodes = remember(test.id) {
        mutableStateListOf<CommandNode>().apply {
            addAll(test.commandNodes)
        }
    }
    val availableCommands = remember {
        CommandNode.all().distinctBy { it.code }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        CommandNodesHeader(
            testTitle = test.title,
            commandCount = commandNodes.size,
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 960.dp)
            ) {
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

@Composable
private fun CommandNodesHeader(
    testTitle: String,
    commandCount: Int,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface)
            .border(
                width = 1.dp,
                color = Outline.copy(alpha = 0.6f)
            )
            .padding(horizontal = 28.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(PrimaryContainer)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center
        ) {
            BasicText(
                text = "←",
                style = TextStyle(
                    color = PrimaryDark,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            BasicText(
                text = testTitle,
                style = TextStyle(
                    color = TextPrimary,
                    fontSize = 20.sp,
                    lineHeight = 26.sp,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(2.dp))

            BasicText(
                text = "$commandCount ${if (commandCount == 1) "command" else "commands"} in this test",
                style = TextStyle(
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            )
        }
    }
}

@Composable
private fun EmptyCommandSequence() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Surface)
            .border(
                width = 1.dp,
                color = Outline.copy(alpha = 0.7f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 24.dp, vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(PrimaryContainer),
            contentAlignment = Alignment.Center
        ) {
            BasicText(
                text = "+",
                style = TextStyle(
                    color = PrimaryDark,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        BasicText(
            text = "No commands added",
            style = TextStyle(
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        )

        Spacer(modifier = Modifier.height(5.dp))

        BasicText(
            text = "Choose an available command below to build this test.",
            style = TextStyle(
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )
        )
    }
}

@Composable
private fun SelectedCommandCard(
    position: Int,
    command: CommandNode,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(14.dp)
            )
            .clip(RoundedCornerShape(14.dp))
            .background(Surface)
            .border(
                width = 1.dp,
                color = Outline.copy(alpha = 0.55f),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Primary),
            contentAlignment = Alignment.Center
        ) {
            BasicText(
                text = position.toString(),
                style = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        CommandDetails(
            command = command,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(16.dp))

        AppButton(
            text = "Remove",
            onClick = onRemove,
            backgroundColor = DangerBackground,
            contentColor = Danger
        )
    }
}

@Composable
private fun AvailableCommandCard(
    command: CommandNode,
    onAdd: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Surface)
            .border(
                width = 1.dp,
                color = Outline.copy(alpha = 0.55f),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(PrimaryContainer),
            contentAlignment = Alignment.Center
        ) {
            BasicText(
                text = command.title
                    .trim()
                    .firstOrNull()
                    ?.uppercase()
                    ?: "?",
                style = TextStyle(
                    color = PrimaryDark,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        CommandDetails(
            command = command,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(16.dp))

        AppButton(
            text = "Add",
            onClick = onAdd,
            backgroundColor = PrimaryContainer,
            contentColor = PrimaryDark
        )
    }
}

@Composable
private fun CommandDetails(
    command: CommandNode,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicText(
                text = command.title,
                style = TextStyle(
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.width(9.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(AppBackground)
                    .padding(horizontal = 7.dp, vertical = 3.dp)
            ) {
                BasicText(
                    text = command.code,
                    style = TextStyle(
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        BasicText(
            text = command.description,
            style = TextStyle(
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            ),
            maxLines = 2
        )

        if (command.values.isNotEmpty()) {
            Spacer(modifier = Modifier.height(5.dp))

            BasicText(
                text = "Values: ${command.values.joinToString()}",
                style = TextStyle(
                    color = TextSecondary.copy(alpha = 0.85f),
                    fontSize = 12.sp
                ),
                maxLines = 1
            )
        }
    }
}
