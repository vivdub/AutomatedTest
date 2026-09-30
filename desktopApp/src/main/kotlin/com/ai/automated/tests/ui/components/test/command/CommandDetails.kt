package com.ai.automated.tests.ui.components.test.command

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.automated.tests.ui.theme.Theme.Companion.AppBackground
import com.ai.automated.tests.ui.theme.Theme.Companion.TextPrimary
import com.ai.automated.tests.ui.theme.Theme.Companion.TextSecondary
import com.ai.automated.tests.util.test.CommandNode

@Composable
fun CommandDetails(
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