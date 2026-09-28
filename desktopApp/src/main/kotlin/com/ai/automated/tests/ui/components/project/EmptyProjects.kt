package com.ai.automated.tests.ui.components.project

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.automated.tests.ui.components.AppButton
import com.ai.automated.tests.ui.theme.Theme.Companion.Outline
import com.ai.automated.tests.ui.theme.Theme.Companion.Primary
import com.ai.automated.tests.ui.theme.Theme.Companion.PrimaryContainer
import com.ai.automated.tests.ui.theme.Theme.Companion.PrimaryDark
import com.ai.automated.tests.ui.theme.Theme.Companion.Surface
import com.ai.automated.tests.ui.theme.Theme.Companion.TextPrimary
import com.ai.automated.tests.ui.theme.Theme.Companion.TextSecondary

@Composable
fun EmptyProjects(
    modifier: Modifier = Modifier,
    onAddProject: () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 440.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Surface)
                .border(
                    width = 1.dp,
                    color = Outline.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(PrimaryContainer),
                contentAlignment = Alignment.Center
            ) {
                BasicText(
                    text = "+",
                    style = TextStyle(
                        color = PrimaryDark,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            BasicText(
                text = "No projects yet",
                style = TextStyle(
                    color = TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            BasicText(
                text = "Create your first project to start organizing automated tests.",
                style = TextStyle(
                    color = TextSecondary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            AppButton(
                text = "Add your first project",
                onClick = onAddProject,
                backgroundColor = Primary,
                contentColor = Color.White
            )
        }
    }
}