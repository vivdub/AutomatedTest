package com.ai.automated.tests.ui.components.project

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import com.ai.automated.tests.project.ProjectStatus
import com.ai.automated.tests.ui.theme.Theme.Companion.Outline
import com.ai.automated.tests.ui.theme.Theme.Companion.Primary
import com.ai.automated.tests.ui.theme.Theme.Companion.PrimaryContainer
import com.ai.automated.tests.ui.theme.Theme.Companion.PrimaryDark
import com.ai.automated.tests.ui.theme.Theme.Companion.TextSecondary

@Composable
fun ProjectStatusOption(
    status: ProjectStatus,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) PrimaryContainer else Color.Transparent
    val contentColor = if (selected) PrimaryDark else TextSecondary
    val borderColor = if (selected) Primary else Outline

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 11.dp),
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = status.label,
            style = TextStyle(
                color = contentColor,
                fontSize = 13.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            )
        )
    }
}