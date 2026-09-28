package com.ai.automated.tests.ui.components.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.ai.automated.tests.project.Project
import com.ai.automated.tests.ui.components.AppButton
import com.ai.automated.tests.ui.components.ModalSurface
import com.ai.automated.tests.ui.theme.Theme.Companion.Danger
import com.ai.automated.tests.ui.theme.Theme.Companion.DangerContainer
import com.ai.automated.tests.ui.theme.Theme.Companion.Outline
import com.ai.automated.tests.ui.theme.Theme.Companion.Surface
import com.ai.automated.tests.ui.theme.Theme.Companion.TextPrimary
import com.ai.automated.tests.ui.theme.Theme.Companion.TextSecondary

@Composable
fun ProjectDeleteConfirmation(
    project: Project,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    ModalSurface(onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .widthIn(min = 400.dp, max = 480.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(Surface)
                .padding(28.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(DangerContainer),
                contentAlignment = Alignment.Center
            ) {
                BasicText(
                    text = "!",
                    style = TextStyle(
                        color = Danger,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            BasicText(
                text = "Delete project?",
                style = TextStyle(
                    color = TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            BasicText(
                text = "\"${project.name}\" will be removed from the project list. This action cannot be undone.",
                style = TextStyle(
                    color = TextSecondary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            )

            Spacer(modifier = Modifier.height(26.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                AppButton(
                    text = "Cancel",
                    onClick = onDismiss,
                    backgroundColor = Color.Transparent,
                    contentColor = TextSecondary,
                    borderColor = Outline
                )

                Spacer(modifier = Modifier.width(10.dp))

                AppButton(
                    text = "Delete",
                    onClick = onConfirm,
                    backgroundColor = Danger,
                    contentColor = Color.White
                )
            }
        }
    }
}