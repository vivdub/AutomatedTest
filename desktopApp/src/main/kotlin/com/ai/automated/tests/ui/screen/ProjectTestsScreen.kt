package com.ai.automated.tests.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.automated.tests.cases.TestCase
import com.ai.automated.tests.project.Project
import com.ai.automated.tests.ui.components.AppHeader
import com.ai.automated.tests.ui.components.test.DeleteTestConfirmation
import com.ai.automated.tests.ui.components.test.EmptyTests
import com.ai.automated.tests.ui.components.test.RunMessage
import com.ai.automated.tests.ui.components.test.TestCard
import com.ai.automated.tests.ui.components.test.TestEditor
import com.ai.automated.tests.ui.theme.Theme.Companion.AppBackground
import com.ai.automated.tests.ui.theme.Theme.Companion.Outline
import com.ai.automated.tests.ui.theme.Theme.Companion.Primary
import com.ai.automated.tests.ui.theme.Theme.Companion.TextSecondary
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val timestampFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy 'at' h:mm a")

@Composable
fun ProjectTestsScreen(
    project: Project,
    onBack: () -> Unit,
) {
    val tests = remember(project.id) {
        mutableStateListOf(
            TestCase(
                title = "Launch application",
                description = "Verify that the application launches successfully and displays the expected initial screen.",
                timestamp = "Updated ${formatCurrentTimestamp()}"
            ),
            TestCase(
                title = "Authenticate user",
                description = "Validate the sign-in flow using a valid test account and confirm that the home screen opens.",
                timestamp = "Updated ${formatCurrentTimestamp()}"
            ),
            TestCase(
                title = "Navigate primary screens",
                description = "Open each primary navigation destination and verify that its core content is visible.",
                timestamp = "Updated ${formatCurrentTimestamp()}"
            )
        )
    }

    var selectedTest by remember(project.id) {
        mutableStateOf<TestCase?>(null)
    }
    var testBeingEdited by remember(project.id) {
        mutableStateOf<TestCase?>(null)
    }
    var testPendingDeletion by remember(project.id) {
        mutableStateOf<TestCase?>(null)
    }
    var runMessage by remember(project.id) {
        mutableStateOf<String?>(null)
    }

    val openedTest = selectedTest
    if (openedTest != null) {
        TestCommandNodesScreen(
            test = openedTest,
            onBack = { selectedTest = null }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        AppHeader(
            title = project.name,
            subtitle = "${tests.size} ${if (tests.size == 1) "test" else "tests"} in this project",
            actionText = "▶  Run",
            onAction = {
                if (tests.isNotEmpty()) {
                    val timestamp = "Last run ${formatCurrentTimestamp()}"
                    tests.indices.forEach { index ->
                        tests[index] = tests[index].copy(timestamp = timestamp)
                    }
                    runMessage = "Batch run requested for ${tests.size} tests."
                }
            },
            onBack = onBack,
            actionBackgroundColor = if (tests.isNotEmpty()) Primary else Outline
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 28.dp)
        ) {
            runMessage?.let { message ->
                RunMessage(
                    message = message,
                    onDismiss = { runMessage = null }
                )
                Spacer(modifier = Modifier.height(18.dp))
            }

            if (tests.isEmpty()) {
                EmptyTests()
            } else {
                BasicText(
                    text = "TEST SUITE",
                    style = TextStyle(
                        color = TextSecondary,
                        fontSize = 12.sp,
                        letterSpacing = 1.1.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    tests.forEach { test ->
                        TestCard(
                            test = test,
                            onOpen = { selectedTest = test },
                            onRun = {
                                val index = tests.indexOfFirst { it.id == test.id }
                                if (index >= 0) {
                                    tests[index] = test.copy(
                                        timestamp = "Last run ${formatCurrentTimestamp()}"
                                    )
                                }
                                runMessage = "Run requested for “${test.title}”."
                            },
                            onEdit = { testBeingEdited = test },
                            onDelete = { testPendingDeletion = test }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    testBeingEdited?.let { test ->
        TestEditor(
            test = test,
            onDismiss = { testBeingEdited = null },
            onSave = { title, description ->
                val index = tests.indexOfFirst { it.id == test.id }
                if (index >= 0) {
                    tests[index] = test.copy(
                        title = title,
                        description = description,
                        timestamp = "Updated ${formatCurrentTimestamp()}"
                    )
                }
                testBeingEdited = null
            }
        )
    }

    testPendingDeletion?.let { test ->
        DeleteTestConfirmation(
            test = test,
            onDismiss = { testPendingDeletion = null },
            onConfirm = {
                tests.removeAll { it.id == test.id }
                testPendingDeletion = null
                runMessage = "“${test.title}” was deleted."
            }
        )
    }
}

private fun formatCurrentTimestamp(): String =
    LocalDateTime.now().format(timestampFormatter)
