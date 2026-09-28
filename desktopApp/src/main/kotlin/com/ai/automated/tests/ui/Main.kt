package com.ai.automated.tests.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.time.LocalDate
import java.util.UUID

private val AppBackground = Color(0xFFF5F7FB)
private val Surface = Color.White
private val Primary = Color(0xFF6750A4)
private val PrimaryDark = Color(0xFF4F378B)
private val PrimaryContainer = Color(0xFFEADDFF)
private val TextPrimary = Color(0xFF1D1B20)
private val TextSecondary = Color(0xFF625F68)
private val Outline = Color(0xFFD0CCD5)
private val Danger = Color(0xFFB3261E)
private val DangerContainer = Color(0xFFFFDAD6)

private enum class ProjectStatus(val label: String) {
    PLANNING("Planning"),
    ACTIVE("Active"),
    COMPLETED("Completed")
}

private data class Project(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val status: ProjectStatus,
    val updatedAt: String = LocalDate.now().toString()
)

private sealed interface EditorState {
    data object Adding : EditorState
    data class Editing(val project: Project) : EditorState
}

fun main() = application {
    val windowState = rememberWindowState(
        size = DpSize(width = 1180.dp, height = 760.dp)
    )

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "AI Automated Tests"
    ) {
        ProjectApplication()
    }
}

@Composable
private fun ProjectApplication() {
    val projects = remember {
        mutableStateListOf(
            Project(
                name = "Android Smoke Tests",
                description = "Core smoke tests for authentication, navigation, and account settings.",
                status = ProjectStatus.ACTIVE
            ),
            Project(
                name = "Checkout Regression",
                description = "Regression coverage for cart, checkout, payment, and order confirmation.",
                status = ProjectStatus.PLANNING
            ),
            Project(
                name = "Release Validation",
                description = "Final automated validation suite for the current product release.",
                status = ProjectStatus.COMPLETED
            )
        )
    }

    var editorState by remember { mutableStateOf<EditorState?>(null) }
    var projectPendingDeletion by remember { mutableStateOf<Project?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppHeader(onAddProject = { editorState = EditorState.Adding })

            if (projects.isEmpty()) {
                EmptyProjects(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    onAddProject = { editorState = EditorState.Adding }
                )
            } else {
                ProjectGrid(
                    projects = projects,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp, vertical = 28.dp),
                    onEdit = { editorState = EditorState.Editing(it) },
                    onDelete = { projectPendingDeletion = it }
                )
            }
        }

        editorState?.let { state ->
            val existingProject = (state as? EditorState.Editing)?.project

            ProjectEditor(
                project = existingProject,
                onDismiss = { editorState = null },
                onSave = { name, description, status ->
                    if (existingProject == null) {
                        projects.add(
                            0,
                            Project(
                                name = name,
                                description = description,
                                status = status
                            )
                        )
                    } else {
                        val index = projects.indexOfFirst { it.id == existingProject.id }
                        if (index >= 0) {
                            projects[index] = existingProject.copy(
                                name = name,
                                description = description,
                                status = status,
                                updatedAt = LocalDate.now().toString()
                            )
                        }
                    }

                    editorState = null
                }
            )
        }

        projectPendingDeletion?.let { project ->
            DeleteConfirmation(
                project = project,
                onDismiss = { projectPendingDeletion = null },
                onConfirm = {
                    projects.removeAll { it.id == project.id }
                    projectPendingDeletion = null
                }
            )
        }
    }
}

@Composable
private fun AppHeader(onAddProject: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface)
            .border(
                border = BorderStroke(1.dp, Outline.copy(alpha = 0.55f)),
                shape = RoundedCornerShape(0.dp)
            )
            .padding(horizontal = 32.dp, vertical = 22.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            BasicText(
                text = "Projects",
                style = TextStyle(
                    color = TextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            BasicText(
                text = "Create and manage your automated test projects.",
                style = TextStyle(
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            )
        }

        AppButton(
            text = "+  Add project",
            onClick = onAddProject,
            backgroundColor = Primary,
            contentColor = Color.White
        )
    }
}

@Composable
private fun ProjectGrid(
    projects: List<Project>,
    modifier: Modifier = Modifier,
    onEdit: (Project) -> Unit,
    onDelete: (Project) -> Unit
) {
    BoxWithConstraints(modifier = modifier) {
        val gap = 20.dp
        val minimumCardWidth = 290.dp
        val columnCount = ((maxWidth + gap) / (minimumCardWidth + gap))
            .toInt()
            .coerceAtLeast(1)
        val cardWidth = (maxWidth - gap * (columnCount - 1)) / columnCount
        val projectRows = projects.chunked(columnCount)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(gap)
        ) {
            projectRows.forEach { rowProjects ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(gap)
                ) {
                    rowProjects.forEach { project ->
                        ProjectCard(
                            project = project,
                            modifier = Modifier.width(cardWidth),
                            onEdit = { onEdit(project) },
                            onDelete = { onDelete(project) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun ProjectCard(
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

@Composable
private fun StatusBadge(status: ProjectStatus) {
    val backgroundColor = when (status) {
        ProjectStatus.PLANNING -> Color(0xFFE8DEF8)
        ProjectStatus.ACTIVE -> Color(0xFFD3E4FF)
        ProjectStatus.COMPLETED -> Color(0xFFC4EED0)
    }
    val contentColor = when (status) {
        ProjectStatus.PLANNING -> Color(0xFF4A4458)
        ProjectStatus.ACTIVE -> Color(0xFF174A7E)
        ProjectStatus.COMPLETED -> Color(0xFF1D6B3A)
    }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(contentColor)
        )
        BasicText(
            text = status.label,
            style = TextStyle(
                color = contentColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

@Composable
private fun EmptyProjects(
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

@Composable
private fun ProjectEditor(
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
                    StatusOption(
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

@Composable
private fun DeleteConfirmation(
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

@Composable
private fun ModalSurface(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.42f))
            .clickable(onClick = onDismiss)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .shadow(
                    elevation = 18.dp,
                    shape = RoundedCornerShape(22.dp)
                )
                .clickable(onClick = {}),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    BasicText(
        text = text,
        style = TextStyle(
            color = TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    )
}

@Composable
private fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    isError: Boolean = false
) {
    val shape = RoundedCornerShape(10.dp)
    val borderColor = if (isError) Danger else Outline

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Color(0xFFFCFAFF))
            .border(
                width = 1.dp,
                color = borderColor,
                shape = shape
            )
            .padding(horizontal = 14.dp, vertical = 12.dp),
        textStyle = TextStyle(
            color = TextPrimary,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        singleLine = singleLine,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = if (singleLine) {
                    Alignment.CenterStart
                } else {
                    Alignment.TopStart
                }
            ) {
                if (value.isEmpty()) {
                    BasicText(
                        text = placeholder,
                        style = TextStyle(
                            color = TextSecondary.copy(alpha = 0.65f),
                            fontSize = 14.sp
                        )
                    )
                }
                innerTextField()
            }
        }
    )
}

@Composable
private fun StatusOption(
    status: ProjectStatus,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) PrimaryContainer else Color.Transparent
    val contentColor = if (selected) PrimaryDark else TextSecondary
    val borderColor = if (selected) Primary else Outline

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 11.dp),
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = status.label,
            style = TextStyle(
                color = contentColor,
                fontSize = 13.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            )
        )
    }
}

@Composable
private fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    contentColor: Color,
    borderColor: Color? = null
) {
    val shape = RoundedCornerShape(10.dp)
    var buttonModifier = modifier
        .height(42.dp)
        .clip(shape)
        .background(backgroundColor)

    if (borderColor != null) {
        buttonModifier = buttonModifier.border(
            width = 1.dp,
            color = borderColor,
            shape = shape
        )
    }

    Box(
        modifier = buttonModifier
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = text,
            style = TextStyle(
                color = contentColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}
