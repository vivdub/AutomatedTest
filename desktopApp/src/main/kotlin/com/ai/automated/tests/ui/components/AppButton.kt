package com.ai.automated.tests.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppButton(
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