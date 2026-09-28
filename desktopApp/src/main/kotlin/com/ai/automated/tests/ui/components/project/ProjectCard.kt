package com.ai.automated.tests.ui.components.project

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.automated.tests.project.Project
import com.ai.automated.tests.ui.components.AppButton
import com.ai.automated.tests.ui.components.StatusBadge
import com.ai.automated.tests.ui.theme.Theme.Companion.Danger
import com.ai.automated.tests.ui.theme.Theme.Companion.DangerContainer
import com.ai.automated.tests.ui.theme.Theme.Companion.Outline
import com.ai.automated.tests.ui.theme.Theme.Companion.PrimaryContainer
import com.ai.automated.tests.ui.theme.Theme.Companion.PrimaryDark
import com.ai.automated.tests.ui.theme.Theme.Companion.Surface
import com.ai.automated.tests.ui.theme.Theme.Companion.TextPrimary
import com.ai.automated.tests.ui.theme.Theme.Companion.TextSecondary


@Composable
fun ProjectCard(
    project: Project,
    modifier: Modifier = Modifier,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = modifier
            .heightIn(min = 225.dp)
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(18.dp)
            )
            .clip(RoundedCornerShape(18.dp))
            .background(Surface)
            .border(
                width = 1.dp,
                color = Outline.copy(alpha = 0.45f),
                shape = RoundedCornerShape(18.dp)
            )
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(PrimaryContainer),
                contentAlignment = Alignment.Center
            ) {
                BasicText(
                    text = project.name.firstOrNull()?.uppercase() ?: "P",
                    style = TextStyle(
                        color = PrimaryDark,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                BasicText(
                    text = project.name,
                    style = TextStyle(
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                StatusBadge(status = project.status)
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        BasicText(
            text = project.description.ifBlank { "No description provided." },
            style = TextStyle(
                color = TextSecondary,
                fontSize = 14.sp,
                lineHeight = 20.sp
            ),
            maxLines = 3
        )

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(18.dp))

        BasicText(
            text = "Updated ${project.updatedAt}",
            style = TextStyle(
                color = TextSecondary.copy(alpha = 0.8f),
                fontSize = 12.sp
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AppButton(
                text = "Edit",
                onClick = onEdit,
                modifier = Modifier.weight(1f),
                backgroundColor = PrimaryContainer,
                contentColor = PrimaryDark
            )
            AppButton(
                text = "Delete",
                onClick = onDelete,
                modifier = Modifier.weight(1f),
                backgroundColor = DangerContainer,
                contentColor = Danger
            )
        }
    }
}