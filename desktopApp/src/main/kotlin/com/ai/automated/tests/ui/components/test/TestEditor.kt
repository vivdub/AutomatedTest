package com.ai.automated.tests.ui.components.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
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
import com.ai.automated.tests.cases.TestCase
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

private const val MaxTestTitleLength = 120
private const val MaxTestDescriptionLength = 500

@Composable
fun TestEditor(
    test: TestCase,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var title by remember(test.id) {
        mutableStateOf(test.title.take(MaxTestTitleLength))
    }
    var description by remember(test.id) {
        mutableStateOf(test.description.take(MaxTestDescriptionLength))
    }
    var showTitleError by remember(test.id) {
        mutableStateOf(false)
    }

    val trimmedTitle = title.trim()
    val hasTitleError = showTitleError && trimmedTitle.isEmpty()
    val scrollState = rememberScrollState()

    ModalSurface(onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .widthIn(min = 440.dp, max = 540.dp)
                .heightIn(max = 540.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Surface)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 28.dp, vertical = 24.dp)
            ) {
                BasicText(
                    text = "Edit test",
                    style = TextStyle(
                        color = TextPrimary,
                        fontSize = 22.sp,
                        lineHeight = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                BasicText(
                    text = "Update the details used to identify and describe this test.",
                    style = TextStyle(
                        color = TextSecondary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                )

                Spacer(modifier = Modifier.height(22.dp))

                FieldLabel("Test title *")
                Spacer(modifier = Modifier.height(7.dp))

                AppTextField(
                    value = title,
                    onValueChange = {
                        title = it.take(MaxTestTitleLength)
                        if (it.isNotBlank()) {
                            showTitleError = false
                        }
                    },
                    placeholder = "Enter a descriptive test title",
                    isError = hasTitleError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                )

                if (hasTitleError) {
                    Spacer(modifier = Modifier.height(6.dp))
                    BasicText(
                        text = "A test title is required.",
                        style = TextStyle(
                            color = Danger,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FieldLabel("Description")

                    BasicText(
                        text = "Optional",
                        style = TextStyle(
                            color = TextSecondary.copy(alpha = 0.75f),
                            fontSize = 12.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(7.dp))

                AppTextField(
                    value = description,
                    onValueChange = {
                        description = it.take(MaxTestDescriptionLength)
                    },
                    placeholder = "Describe the purpose and expected outcome",
                    singleLine = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(92.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
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
                    .background(Outline.copy(alpha = 0.65f))
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFCFBFD))
                    .padding(horizontal = 28.dp, vertical = 16.dp),
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

                Spacer(modifier = Modifier.width(10.dp))

                AppButton(
                    text = "Save changes",
                    onClick = {
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
