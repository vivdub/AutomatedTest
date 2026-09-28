package com.ai.automated.tests.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.ai.automated.tests.project.Project
import com.ai.automated.tests.project.ProjectStatus
import com.ai.automated.tests.ui.components.AppHeader
import com.ai.automated.tests.ui.components.project.EmptyProjects
import com.ai.automated.tests.ui.components.project.ProjectDeleteConfirmation
import com.ai.automated.tests.ui.components.project.ProjectEditor
import com.ai.automated.tests.ui.components.project.ProjectGrid
import com.ai.automated.tests.ui.screen.ProjectTestsScreen
import com.ai.automated.tests.ui.theme.Theme.Companion.AppBackground
import java.time.LocalDate

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
    var selectedProject by remember { mutableStateOf<Project?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        val openProject = selectedProject

        if (openProject != null) {
            ProjectTestsScreen(
                project = openProject,
                onBack = { selectedProject = null }
            )
        } else {
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
                        onOpen = { selectedProject = it },
                        onEdit = { editorState = EditorState.Editing(it) },
                        onDelete = { projectPendingDeletion = it }
                    )
                }
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
            ProjectDeleteConfirmation(
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
