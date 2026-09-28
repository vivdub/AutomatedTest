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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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

private const val MaxDescriptionLength = 500

@Composable
fun ProjectEditor(
    project: Project?,
    onDismiss: () -> Unit,
    onSave: (String, String, ProjectStatus) -> Unit
) {
    val isEditing = project != null

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

    val trimmedName = name.trim()
    val hasNameError = showNameError && trimmedName.isEmpty()

    ModalSurface(onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .widthIn(min = 500.dp, max = 620.dp)
                .heightIn(max = 680.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(Surface)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 30.dp, vertical = 28.dp)
            ) {
                ProjectEditorHeader(isEditing = isEditing)

                Spacer(modifier = Modifier.height(26.dp))

                FormSection(
                    title = "Project details",
                    description = "Give your project a clear name and describe what its tests cover."
                ) {
                    RequiredFieldLabel("Project name")
                    Spacer(modifier = Modifier.height(8.dp))

                    AppTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            if (it.isNotBlank()) {
                                showNameError = false
                            }
                        },
                        placeholder = "For example, Android smoke tests",
                        isError = hasNameError
                    )

                    if (hasNameError) {
                        Spacer(modifier = Modifier.height(7.dp))
                        BasicText(
                            text = "Enter a project name before saving.",
                            style = TextStyle(
                                color = Danger,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    } else {
                        Spacer(modifier = Modifier.height(7.dp))
                        BasicText(
                            text = "Use a short, recognizable name for this test project.",
                            style = TextStyle(
                                color = TextSecondary.copy(alpha = 0.78f),
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    FieldLabel("Description")
                    Spacer(modifier = Modifier.height(8.dp))

                    AppTextField(
                        value = description,
                        onValueChange = {
                            if (it.length <= MaxDescriptionLength) {
                                description = it
                            }
                        },
                        placeholder = "Describe the test scope, target application, or release goals",
                        singleLine = false,
                        modifier = Modifier.height(112.dp)
                    )

                    Spacer(modifier = Modifier.height(7.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BasicText(
                            text = "Optional",
                            style = TextStyle(
                                color = TextSecondary.copy(alpha = 0.72f),
                                fontSize = 12.sp
                            )
                        )
                        BasicText(
                            text = "${description.length}/$MaxDescriptionLength",
                            style = TextStyle(
                                color = TextSecondary.copy(alpha = 0.72f),
                                fontSize = 12.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                FormSection(
                    title = "Project status",
                    description = "Choose the stage that best represents the project."
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
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
                }
            }

            ProjectEditorFooter(
                onCancel = onDismiss,
                onSave = {
                    if (trimmedName.isEmpty()) {
                        showNameError = true
                    } else {
                        onSave(
                            trimmedName,
                            description.trim(),
                            status
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun ProjectEditorHeader(isEditing: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Primary.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            BasicText(
                text = if (isEditing) "E" else "+",
                style = TextStyle(
                    color = Primary,
                    fontSize = if (isEditing) 20.sp else 28.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            BasicText(
                text = if (isEditing) "Edit project" else "Create a project",
                style = TextStyle(
                    color = TextPrimary,
                    fontSize = 24.sp,
                    lineHeight = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            BasicText(
                text = if (isEditing) {
                    "Update the details and status of this project."
                } else {
                    "Set up a workspace for your automated tests."
                },
                style = TextStyle(
                    color = TextSecondary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            )
        }
    }
}

@Composable
private fun FormSection(
    title: String,
    description: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFFAF9FC))
            .border(
                width = 1.dp,
                color = Outline.copy(alpha = 0.65f),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(18.dp)
    ) {
        BasicText(
            text = title,
            style = TextStyle(
                color = TextPrimary,
                fontSize = 15.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        BasicText(
            text = description,
            style = TextStyle(
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        content()
    }
}

@Composable
private fun RequiredFieldLabel(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        FieldLabel(text)
        Spacer(modifier = Modifier.width(4.dp))
        BasicText(
            text = "*",
            style = TextStyle(
                color = Danger,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun ProjectEditorFooter(
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Outline.copy(alpha = 0.6f))
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFCFBFD))
                .padding(horizontal = 30.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppButton(
                text = "Cancel",
                onClick = onCancel,
                backgroundColor = Color.Transparent,
                contentColor = TextSecondary,
                borderColor = Outline
            )

            Spacer(modifier = Modifier.width(12.dp))

            AppButton(
                text = "Save",
                onClick = onSave,
                backgroundColor = Primary,
                contentColor = Color.White
            )
        }
    }
}
