package com.ai.automated.tests.ui.components.test.command

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.automated.tests.ui.components.AppButton
import com.ai.automated.tests.ui.theme.Theme.Companion.Danger
import com.ai.automated.tests.ui.theme.Theme.Companion.DangerBackground
import com.ai.automated.tests.ui.theme.Theme.Companion.Outline
import com.ai.automated.tests.ui.theme.Theme.Companion.Primary
import com.ai.automated.tests.ui.theme.Theme.Companion.PrimaryContainer
import com.ai.automated.tests.ui.theme.Theme.Companion.PrimaryDark
import com.ai.automated.tests.ui.theme.Theme.Companion.Surface
import com.ai.automated.tests.util.test.CommandNode

@Composable
fun SelectedCommandCard(
    position: Int,
    command: CommandNode,
    onConfigure: () -> Unit,
    onRun: () -> Unit,
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

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(command.configurable) {
                AppButton(
                    text = "Configure",
                    onClick = onConfigure,
                    backgroundColor = PrimaryContainer,
                    contentColor = PrimaryDark
                )
            }

            AppButton(
                text = "Run",
                onClick = onRun,
                backgroundColor = Primary,
                contentColor = Color.White,
                enabled = (command.configurable && command.values.isNotEmpty()) || !command.configurable,
            )

            AppButton(
                text = "Remove",
                onClick = onRemove,
                backgroundColor = DangerBackground,
                contentColor = Danger
            )
        }
    }
}