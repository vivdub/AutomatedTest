package com.ai.automated.tests.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.automated.tests.ui.theme.Theme.Companion.Outline
import com.ai.automated.tests.ui.theme.Theme.Companion.Primary
import com.ai.automated.tests.ui.theme.Theme.Companion.Surface
import com.ai.automated.tests.ui.theme.Theme.Companion.TextPrimary
import com.ai.automated.tests.ui.theme.Theme.Companion.TextSecondary

@Composable
fun AppHeader(
    title: String,
    subtitle: String,
    actionText: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    actionBackgroundColor: Color = Primary,
    actionContentColor: Color = Color.White
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Surface)
            .border(
                border = BorderStroke(1.dp, Outline.copy(alpha = 0.55f)),
                shape = RoundedCornerShape(0.dp)
            )
            .padding(horizontal = 32.dp, vertical = 22.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBack != null) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .border(
                        width = 1.dp,
                        color = Outline,
                        shape = RoundedCornerShape(11.dp)
                    )
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center
            ) {
                BasicText(
                    text = "←",
                    style = TextStyle(
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Spacer(modifier = Modifier.width(16.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            BasicText(
                text = title,
                style = TextStyle(
                    color = TextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            BasicText(
                text = subtitle,
                style = TextStyle(
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            )
        }

        Spacer(modifier = Modifier.width(20.dp))

        AppButton(
            text = actionText,
            onClick = onAction,
            backgroundColor = actionBackgroundColor,
            contentColor = actionContentColor
        )
    }
}
