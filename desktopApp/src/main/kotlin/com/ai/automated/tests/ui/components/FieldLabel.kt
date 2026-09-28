package com.ai.automated.tests.ui.components

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ai.automated.tests.ui.theme.Theme.Companion.TextPrimary

@Composable
fun FieldLabel(text: String) {
    BasicText(
        text = text,
        style = TextStyle(
            color = TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    )
}
