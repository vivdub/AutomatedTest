package com.ai.automated.tests.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ModalSurface(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    val dialogInteractionSource = remember { MutableInteractionSource() }
    val backdropInteractionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.42f))
            .clickable(onClick = onDismiss, indication = null, interactionSource = backdropInteractionSource)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .shadow(
                    elevation = 18.dp,
                    shape = RoundedCornerShape(22.dp)
                )
                .clickable(onClick = {}, indication = null, interactionSource = dialogInteractionSource),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}