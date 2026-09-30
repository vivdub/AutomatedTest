package com.ai.automated.tests.ui.components.test.command

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.ai.automated.tests.ui.theme.Theme.Companion.Outline
import com.ai.automated.tests.ui.theme.Theme.Companion.PrimaryContainer
import com.ai.automated.tests.ui.theme.Theme.Companion.PrimaryDark
import com.ai.automated.tests.ui.theme.Theme.Companion.Surface
import com.ai.automated.tests.ui.theme.Theme.Companion.TextPrimary
import com.ai.automated.tests.ui.theme.Theme.Companion.TextSecondary

@Composable
fun EmptyCommandSequence() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Surface)
            .border(
                width = 1.dp,
                color = Outline.copy(alpha = 0.7f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 24.dp, vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(PrimaryContainer),
            contentAlignment = Alignment.Center
        ) {
            BasicText(
                text = "+",
                style = TextStyle(
                    color = PrimaryDark,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        BasicText(
            text = "No commands added",
            style = TextStyle(
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        )

        Spacer(modifier = Modifier.height(5.dp))

        BasicText(
            text = "Choose an available command below to build this test.",
            style = TextStyle(
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )
        )
    }
}