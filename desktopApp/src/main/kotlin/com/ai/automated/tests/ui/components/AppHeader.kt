package com.ai.automated.tests.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.automated.tests.ui.theme.Theme.Companion.Outline
import com.ai.automated.tests.ui.theme.Theme.Companion.Primary
import com.ai.automated.tests.ui.theme.Theme.Companion.Surface
import com.ai.automated.tests.ui.theme.Theme.Companion.TextPrimary
import com.ai.automated.tests.ui.theme.Theme.Companion.TextSecondary

@Composable
fun AppHeader(onAddProject: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface)
            .border(
                border = BorderStroke(1.dp, Outline.copy(alpha = 0.55f)),
                shape = RoundedCornerShape(0.dp)
            )
            .padding(horizontal = 32.dp, vertical = 22.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            BasicText(
                text = "Projects",
                style = TextStyle(
                    color = TextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            BasicText(
                text = "Create and manage your automated test projects.",
                style = TextStyle(
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            )
        }

        AppButton(
            text = "+  Add project",
            onClick = onAddProject,
            backgroundColor = Primary,
            contentColor = Color.White
        )
    }
}