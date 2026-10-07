package com.ai.automated.tests.ui.components.test.command

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.automated.tests.ui.theme.Theme.Companion.Outline
import com.ai.automated.tests.ui.theme.Theme.Companion.Primary
import com.ai.automated.tests.ui.theme.Theme.Companion.PrimaryContainer
import com.ai.automated.tests.ui.theme.Theme.Companion.PrimaryDark
import com.ai.automated.tests.ui.theme.Theme.Companion.Surface
import com.ai.automated.tests.ui.theme.Theme.Companion.TextPrimary
import com.ai.automated.tests.ui.theme.Theme.Companion.TextSecondary
import com.ai.automated.tests.util.test.node.CommandNode

@Composable
fun ConfigureCommandCard(commandNode: CommandNode, deviceClickedAt:Pair<Int,Int>?, onCancel: () -> Unit, onSave: () -> Unit, onRun:(List<Any>)->Unit) {

    val fieldValues = remember(commandNode) {
        mutableStateListOf<Any>().apply {
            commandNode.requiredData.forEachIndexed { index, _ ->
                val data = commandNode.nodeData.getOrNull(index)
                when(data!=null){
                    true -> add(data)
                    else -> when(commandNode.requiredData[index].type){
                        CommandNode.NodeData.ValueType.Input -> add("")
                        CommandNode.NodeData.ValueType.Coordinate -> add(Pair(0,0))
                    }
                }
            }
        }
    }
    val canSave = fieldValues.size == commandNode.requiredData.size
    var currentFieldValueIndex by remember { mutableStateOf(-1) }
    var clickUtilised by remember { mutableStateOf(Pair(0,0)) }

    if(deviceClickedAt != null && currentFieldValueIndex!=-1 && clickUtilised!=deviceClickedAt && commandNode.requiredData[currentFieldValueIndex].type == CommandNode.NodeData.ValueType.Coordinate) {
        fieldValues[currentFieldValueIndex] = deviceClickedAt
        clickUtilised=deviceClickedAt
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Outline,
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        color = Surface,
        elevation = 2.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PrimaryContainer)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    text = commandNode.title,
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                if (commandNode.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = commandNode.description,
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (commandNode.requiredData.isEmpty()) {
                    Text(
                        text = "This command does not require any additional configuration.",
                        color = TextSecondary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                } else {
                    Text(
                        text = "Enter the required values for this command.",
                        color = TextSecondary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )

                    commandNode.requiredData.forEachIndexed { index, field ->
                        OutlinedTextField(
                            value = fieldValues[index].toString(),
                            onValueChange = {
                                fieldValues[index] = when(commandNode.requiredData[index].type) {
                                    CommandNode.NodeData.ValueType.Input -> it
                                    CommandNode.NodeData.ValueType.Coordinate -> {
                                        try {
                                            val parts = it.replace(Regex("[()]"), "").split(",")
                                            Pair(
                                                parts[0].trim().toInt(),
                                                parts[1].trim().toInt()
                                            )
                                        } catch (e: Exception) {
                                            fieldValues[index] // Retain the previous value
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth().onFocusChanged{
                                if(it.isFocused) currentFieldValueIndex = index
                            },
                            label = {
                                Text(text = field.name)
                            },
                            placeholder = {
                                Text(text = "Enter ${field.name}")
                            },
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Primary,
                                unfocusedBorderColor = Outline,
                                focusedLabelColor = Primary,
                                unfocusedLabelColor = TextSecondary,
                                textColor = TextPrimary,
                                cursorColor = Primary,
                                backgroundColor = Surface
                            )
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val onSendToDevice = {
                        if(fieldValues.size == commandNode.requiredData.size) {
                            onRun.invoke(fieldValues)
                        }
                    }
                    TextButton(onClick = onSendToDevice ) {
                        Text(
                            text = "Run",
                            color = PrimaryDark,
                            fontWeight = FontWeight.Light
                        )
                    }
                    Spacer(modifier = Modifier.width(30.dp))

                    TextButton(onClick = onCancel) {
                        Text(
                            text = "Cancel",
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            commandNode.nodeData.clear()
                            commandNode.nodeData.addAll(fieldValues)
                            onSave()
                        },
                        enabled = canSave,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Primary,
                            contentColor = Surface,
                            disabledBackgroundColor = Outline,
                            disabledContentColor = TextSecondary
                        )
                    ) {
                        Text(
                            text = "Save",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
