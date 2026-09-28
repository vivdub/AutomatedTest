package com.ai.automated.tests.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.automated.tests.project.Project
import com.ai.automated.tests.ui.components.AppButton
import com.ai.automated.tests.ui.components.AppHeader
import com.ai.automated.tests.ui.components.AppTextField
import com.ai.automated.tests.ui.components.FieldLabel
import com.ai.automated.tests.ui.components.ModalSurface
import com.ai.automated.tests.ui.theme.Theme.Companion.AppBackground
import com.ai.automated.tests.ui.theme.Theme.Companion.Danger
import com.ai.automated.tests.ui.theme.Theme.Companion.Outline
import com.ai.automated.tests.ui.theme.Theme.Companion.Primary
import com.ai.automated.tests.ui.theme.Theme.Companion.PrimaryContainer
import com.ai.automated.tests.ui.theme.Theme.Companion.PrimaryDark
import com.ai.automated.tests.ui.theme.Theme.Companion.Surface
import com.ai.automated.tests.ui.theme.Theme.Companion.TextPrimary
import com.ai.automated.tests.ui.theme.Theme.Companion.TextSecondary
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

private const val MaxTestDescriptionLength = 500

private val timestampFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy 'at' h:mm a")
private val TestCardBackground = Color(0xFFFCFBFD)
private val SuccessBackground = Color(0xFFE3F3E8)
private val SuccessText = Color(0xFF246B3D)
private val DangerBackground = Color(0xFFFFDAD6)

private data class ProjectTest(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val timestamp: String
)

@Composable
fun ProjectTestsScreen(
    project: Project,
    onBack: () -> Unit
) {
    val tests = remember(project.id) {
        mutableStateListOf(
            ProjectTest(
                title = "Launch application",
                description = "Verify that the application launches successfully and displays the expected initial screen.",
                timestamp = "Updated ${formatCurrentTimestamp()}"
            ),
            ProjectTest(
                title = "Authenticate user",
                description = "Validate the sign-in flow using a valid test account and confirm that the home screen opens.",
                timestamp = "Updated ${formatCurrentTimestamp()}"
            ),
            ProjectTest(
                title = "Navigate primary screens",
                description = "Open each primary navigation destination and verify that its core content is visible.",
                timestamp = "Updated ${formatCurrentTimestamp()}"
            )
        )
    }

    var testBeingEdited by remember(project.id) {
        mutableStateOf<ProjectTest?>(null)
    }
    var testPendingDeletion by remember(project.id) {
        mutableStateOf<ProjectTest?>(null)
    }
    var runMessage by remember(project.id) {
        mutableStateOf<String?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        AppHeader(
            title = project.name,
            subtitle = "${tests.size} ${if (tests.size == 1) "test" else "tests"} in this project",
            actionText = "▶  Run all tests",
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

@Composable
private fun TestCard(
    test: ProjectTest,
    onRun: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(TestCardBackground)
            .border(
                width = 1.dp,
                color = Outline.copy(alpha = 0.55f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(PrimaryContainer),
            contentAlignment = Alignment.Center
        ) {
            BasicText(
                text = "T",
                style = TextStyle(
                    color = PrimaryDark,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            BasicText(
                text = test.title,
                style = TextStyle(
                    color = TextPrimary,
                    fontSize = 17.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            BasicText(
                text = test.description.ifBlank { "No description provided." },
                style = TextStyle(
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                ),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(TextSecondary.copy(alpha = 0.55f))
                )

                Spacer(modifier = Modifier.width(7.dp))

                BasicText(
                    text = test.timestamp,
                    style = TextStyle(
                        color = TextSecondary.copy(alpha = 0.82f),
                        fontSize = 12.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.width(20.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppButton(
                text = "Run",
                onClick = onRun,
                backgroundColor = Primary,
                contentColor = Color.White
            )

            AppButton(
                text = "Edit",
                onClick = onEdit,
                backgroundColor = PrimaryContainer,
                contentColor = PrimaryDark
            )

            AppButton(
                text = "Delete",
                onClick = onDelete,
                backgroundColor = DangerBackground,
                contentColor = Danger
            )
        }
    }
}

@Composable
private fun RunMessage(
    message: String,
    onDismiss: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SuccessBackground)
            .border(
                width = 1.dp,
                color = SuccessText.copy(alpha = 0.25f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicText(
            text = "✓",
            style = TextStyle(
                color = SuccessText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.width(10.dp))

        BasicText(
            text = message,
            modifier = Modifier.weight(1f),
            style = TextStyle(
                color = SuccessText,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        )

        BasicText(
            text = "Dismiss",
            modifier = Modifier.clickable(onClick = onDismiss),
            style = TextStyle(
                color = SuccessText,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Composable
private fun EmptyTests() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 480.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Surface)
                .border(
                    width = 1.dp,
                    color = Outline.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(18.dp)
                )
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(PrimaryContainer),
                contentAlignment = Alignment.Center
            ) {
                BasicText(
                    text = "T",
                    style = TextStyle(
                        color = PrimaryDark,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            BasicText(
                text = "No tests in this project",
                style = TextStyle(
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            BasicText(
                text = "Tests added to this project will appear here and can be run individually or as a batch.",
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
private fun TestEditor(
    test: ProjectTest,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var title by remember(test.id) {
        mutableStateOf(test.title)
    }
    var description by remember(test.id) {
        mutableStateOf(test.description)
    }
    var showTitleError by remember(test.id) {
        mutableStateOf(false)
    }

    val hasTitleError = showTitleError && title.isBlank()

    ModalSurface(onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .widthIn(min = 480.dp, max = 590.dp)
                .heightIn(max = 650.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(Surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 28.dp)
            ) {
                BasicText(
                    text = "Edit test",
                    style = TextStyle(
                        color = TextPrimary,
                        fontSize = 24.sp,
                        lineHeight = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(5.dp))

                BasicText(
                    text = "Update the test title and description.",
                    style = TextStyle(
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                )

                Spacer(modifier = Modifier.height(26.dp))

                FieldLabel("Test title *")
                Spacer(modifier = Modifier.height(8.dp))

                AppTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        if (it.isNotBlank()) {
                            showTitleError = false
                        }
                    },
                    placeholder = "Enter a descriptive test title",
                    isError = hasTitleError
                )

                if (hasTitleError) {
                    Spacer(modifier = Modifier.height(7.dp))
                    BasicText(
                        text = "Enter a title before saving.",
                        style = TextStyle(
                            color = Danger,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                FieldLabel("Description")
                Spacer(modifier = Modifier.height(8.dp))

                AppTextField(
                    value = description,
                    onValueChange = {
                        if (it.length <= MaxTestDescriptionLength) {
                            description = it
                        }
                    },
                    placeholder = "Describe the purpose and expected outcome of this test",
                    singleLine = false,
                    modifier = Modifier.height(120.dp)
                )

                Spacer(modifier = Modifier.height(7.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    BasicText(
                        text = "Optional",
                        style = TextStyle(
                            color = TextSecondary.copy(alpha = 0.75f),
                            fontSize = 12.sp
                        )
                    )

                    BasicText(
                        text = "${description.length}/$MaxTestDescriptionLength",
                        style = TextStyle(
                            color = TextSecondary.copy(alpha = 0.75f),
                            fontSize = 12.sp
                        )
                    )
                }
            }

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
                    onClick = onDismiss,
                    backgroundColor = Color.Transparent,
                    contentColor = TextSecondary,
                    borderColor = Outline
                )

                Spacer(modifier = Modifier.width(12.dp))

                AppButton(
                    text = "Save",
                    onClick = {
                        val trimmedTitle = title.trim()
                        if (trimmedTitle.isEmpty()) {
                            showTitleError = true
                        } else {
                            onSave(trimmedTitle, description.trim())
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
private fun DeleteTestConfirmation(
    test: ProjectTest,
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
                    .background(DangerBackground),
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
                text = "Delete test?",
                style = TextStyle(
                    color = TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            BasicText(
                text = "“${test.title}” will be permanently removed from this project.",
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

private fun formatCurrentTimestamp(): String =
    LocalDateTime.now().format(timestampFormatter)
