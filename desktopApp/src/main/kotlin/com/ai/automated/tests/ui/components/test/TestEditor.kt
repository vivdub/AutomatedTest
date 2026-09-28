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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
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

private const val MaxTestDescriptionLength = 500

@Composable
fun TestEditor(
    test: TestCase,
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
