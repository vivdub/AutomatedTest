package com.ai.automated.tests.ui.components.project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ai.automated.tests.project.Project

@Composable
fun ProjectGrid(
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