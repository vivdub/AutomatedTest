package com.ai.automated.tests.ui.components.test.command

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ai.automated.tests.util.test.CommandNode


@Composable
fun ConfigureCommandCard(commandNode: CommandNode) {
    Box(modifier = Modifier.fillMaxWidth()) {
        commandNode.requires.forEach { field ->
            Text(field)
            BasicTextField(value = "",onValueChange = {})
            Spacer(modifier = Modifier.height(14.dp))
        }
        Row {
            Button(onClick = {}){Text("Cancel")}
            Button(onClick = {}){Text("Save")}
        }
    }
}