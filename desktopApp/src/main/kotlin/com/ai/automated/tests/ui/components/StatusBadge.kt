package com.ai.automated.tests.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import com.ai.automated.tests.project.ProjectStatus

@Composable
fun StatusBadge(status: ProjectStatus) {
    val backgroundColor = when (status) {
        ProjectStatus.PLANNING -> Color(0xFFE8DEF8)
        ProjectStatus.ACTIVE -> Color(0xFFD3E4FF)
        ProjectStatus.COMPLETED -> Color(0xFFC4EED0)
    }
    val contentColor = when (status) {
        ProjectStatus.PLANNING -> Color(0xFF4A4458)
        ProjectStatus.ACTIVE -> Color(0xFF174A7E)
        ProjectStatus.COMPLETED -> Color(0xFF1D6B3A)
    }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(contentColor)
        )
        BasicText(
            text = status.label,
            style = TextStyle(
                color = contentColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}
