package com.ai.automated.tests.ui.components.configuration

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max

//----------
@Composable
/** @param onBoxCredentials left, top, box width, container width */
fun BoundedAreaPicker(start:Int=0, top:Int=0, width: Int, onBoxCredentials:(Int, Int, Int, Int)->Unit) {

    var left by remember { mutableStateOf(start) }
    var top by remember { mutableStateOf(top) }

    val minBoxWidthDP = 20.dp
    val minBoxWidthPX = with(LocalDensity.current) { minBoxWidthDP.toPx().toInt() }

    BoxWithConstraints(Modifier.fillMaxWidth()) {

        val availableWidth = (maxWidth - left.dp).coerceAtLeast(minBoxWidthDP)

        val boundedWidth = width.dp.coerceIn(minBoxWidthDP, availableWidth)
        val boundedWidthPx = with(LocalDensity.current) {boundedWidth.toPx().toInt() }

        val maxWidth = with(LocalDensity.current) { maxWidth.toPx().toInt() }
        val maxHeight = with(LocalDensity.current) { maxHeight.toPx().toInt() }

        left = left.coerceIn(0, maxWidth-minBoxWidthPX)
        top = top.coerceIn(0, maxHeight-boundedWidthPx)

        Box(
            modifier = Modifier
                .offset(x = left.dp, y = top.dp)
                .width(boundedWidth)
                .aspectRatio(1f)
                .border(BorderStroke(2.dp, Color.Red))
                .pointerInput(Unit){
                    detectDragGestures { change, dragAmount ->
                        left = (left+dragAmount.x).toInt()
                        top = (top+dragAmount.y).toInt()
                    }
                }
        )
        onBoxCredentials.invoke(left, top, width, maxWidth)
    }
}