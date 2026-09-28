package com.ai.automated.tests.project

import java.time.LocalDate
import java.util.UUID

data class Project(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val status: ProjectStatus,
    val updatedAt: String = LocalDate.now().toString()
)

enum class ProjectStatus(val label: String) {
    PLANNING("Planning"),
    ACTIVE("Active"),
    COMPLETED("Completed")
}