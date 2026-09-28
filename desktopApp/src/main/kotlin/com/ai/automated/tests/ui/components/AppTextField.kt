package com.ai.automated.tests.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.automated.tests.ui.theme.Theme.Companion.Danger
import com.ai.automated.tests.ui.theme.Theme.Companion.Outline
import com.ai.automated.tests.ui.theme.Theme.Companion.TextPrimary
import com.ai.automated.tests.ui.theme.Theme.Companion.TextSecondary


@Composable
fun AppTextField(
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