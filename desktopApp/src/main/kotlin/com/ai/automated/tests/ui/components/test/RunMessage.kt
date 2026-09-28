package com.ai.automated.tests.ui.components.test

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.automated.tests.ui.theme.Theme.Companion.SuccessBackground
import com.ai.automated.tests.ui.theme.Theme.Companion.SuccessText

@Composable
fun RunMessage(
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