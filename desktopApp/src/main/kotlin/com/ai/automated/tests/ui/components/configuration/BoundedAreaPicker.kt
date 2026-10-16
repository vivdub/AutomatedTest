package com.ai.automated.tests.ui.components.configuration

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

//----------
@Composable
fun BoundedAreaPicker(startPadding: Dp, topPadding: Dp, width: Dp) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val availableWidth = (maxWidth - startPadding).coerceAtLeast(0.dp)
        val boundedWidth = width.coerceIn(0.dp, availableWidth)
        if(boundedWidth>0.dp){
            Box(
                modifier = Modifier
                    .offset(x = startPadding, y = topPadding)
                    .width(boundedWidth)
                    .aspectRatio(1f)
                    .border(BorderStroke(2.dp, Color.Red))
            )
        }
    }
}