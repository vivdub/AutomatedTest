package com.ai.automated.tests.ui.components.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.automated.tests.project.Project
import com.ai.automated.tests.project.ProjectStatus
import com.ai.automated.tests.ui.components.AppButton
import com.ai.automated.tests.ui.components.AppTextField
import com.ai.automated.tests.ui.components.FieldLabel
import com.ai.automated.tests.ui.components.ModalSurface
import com.ai.automated.tests.ui.theme.Theme.Companion.Danger
import com.ai.automated.tests.ui.theme.Theme.Companion.Outline
import com.ai.automated.tests.ui.theme.Theme.Companion.Primary
import com.ai.automated.tests.ui.theme.Theme.Companion.Surface
import com.ai.automated.tests.ui.theme.Theme.Companion.TextPrimary
import com.ai.automated.tests.ui.theme.Theme.Companion.TextSecondary

@Composable
fun ProjectEditor(
    project: Project?,
    onDismiss: () -> Unit,
    onSave: (String, String, ProjectStatus) -> Unit
) {
    var name by remember(project?.id) {
        mutableStateOf(project?.name.orEmpty())
    }
    var description by remember(project?.id) {
        mutableStateOf(project?.description.orEmpty())
    }
    var status by remember(project?.id) {
        mutableStateOf(project?.status ?: ProjectStatus.PLANNING)
    }
    var showNameError by remember(project?.id) {
        mutableStateOf(false)
    }

    ModalSurface(onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .widthIn(min = 440.dp, max = 560.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(Surface)
                .padding(28.dp)
        ) {
            BasicText(
                text = if (project == null) "Add project" else "Edit project",
                style = TextStyle(
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            BasicText(
                text = if (project == null) {
                    "Create a project for a group of automated tests."
                } else {
                    "Update the project information below."
                },
                style = TextStyle(
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            FieldLabel("Project name")
            Spacer(modifier = Modifier.height(7.dp))
            AppTextField(
                value = name,
                onValueChange = {
                    name = it
                    if (it.isNotBlank()) {
                        showNameError = false
                    }
                },
                placeholder = "For example, Android smoke tests",
                isError = showNameError
            )

            if (showNameError) {
                Spacer(modifier = Modifier.height(6.dp))
                BasicText(
                    text = "A project name is required.",
                    style = TextStyle(
                        color = Danger,
                        fontSize = 12.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            FieldLabel("Description")
            Spacer(modifier = Modifier.height(7.dp))
            AppTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = "Describe what this project covers",
                singleLine = false,
                modifier = Modifier.height(110.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            FieldLabel("Status")
            Spacer(modifier = Modifier.height(9.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProjectStatus.entries.forEach { option ->
                    ProjectStatusOption(
                        status = option,
                        selected = option == status,
                        modifier = Modifier.weight(1f),
                        onClick = { status = option }
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

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
                    text = if (project == null) "Add project" else "Save changes",
                    onClick = {
                        val trimmedName = name.trim()
                        if (trimmedName.isEmpty()) {
                            showNameError = true
                        } else {
                            onSave(
                                trimmedName,
                                description.trim(),
                                status
                            )
                        }
                    },
                    backgroundColor = Primary,
                    contentColor = Color.White
                )
            }
        }
    }
}