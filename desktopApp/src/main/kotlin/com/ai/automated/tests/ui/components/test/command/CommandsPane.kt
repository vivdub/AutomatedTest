package com.ai.automated.tests.ui.components.test.command

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.automated.tests.cases.TestCase
import com.ai.automated.tests.ui.theme.Theme.Companion.TextSecondary
import com.ai.automated.tests.util.test.node.properties.CanReceiveDataFromPrevNode
import com.ai.automated.tests.util.test.node.CommandNode
import com.ai.automated.tests.util.test.node.properties.OutputsData
import kotlin.collections.forEach

@Composable
fun RowScope.CommandsPane(
    commandNodes: SnapshotStateList<CommandNode>,
    availableCommands: List<CommandNode>,
    test: TestCase, deviceClickedAt: Pair<Int, Int>?,
) {
    var configureNode by remember { mutableStateOf<CommandNode?>(null) }

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
                val nodeBeingConfigured = configureNode
                if (nodeBeingConfigured != null) {
                    ConfigureCommandCard(
                        commandNode = nodeBeingConfigured,
                        deviceClickedAt,
                        onCancel = {
                            configureNode = null
                        },
                        onSave = {
                            configureNode = null
                        },
                        onRun = {
                            nodeBeingConfigured.setData(it)
                            nodeBeingConfigured.run()
                        }
                    )
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
                                    configureNode = command
                                },
                                onRun = {
                                    // Command execution behavior will be added separately.
                                    command.run()
                                },
                                onRemove = {
                                    commandNodes.removeAt(index)
                                    test.commandNodes.removeAt(index)
                                }
                            )
                        }
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
                            val newCommand = command.clone()
                            if(commandNodes.isNotEmpty() && commandNodes.last() is OutputsData && newCommand is CanReceiveDataFromPrevNode) {
                                newCommand.setReceivingPrevNodeData(true)
                                newCommand.configurable = false
                            }
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