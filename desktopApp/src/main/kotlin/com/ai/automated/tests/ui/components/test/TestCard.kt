package com.ai.automated.tests.ui.components.test

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.automated.tests.cases.TestCase
import com.ai.automated.tests.ui.components.AppButton
import com.ai.automated.tests.ui.theme.Theme.Companion.Danger
import com.ai.automated.tests.ui.theme.Theme.Companion.DangerBackground
import com.ai.automated.tests.ui.theme.Theme.Companion.Outline
import com.ai.automated.tests.ui.theme.Theme.Companion.Primary
import com.ai.automated.tests.ui.theme.Theme.Companion.PrimaryContainer
import com.ai.automated.tests.ui.theme.Theme.Companion.PrimaryDark
import com.ai.automated.tests.ui.theme.Theme.Companion.TestCardBackground
import com.ai.automated.tests.ui.theme.Theme.Companion.TextPrimary
import com.ai.automated.tests.ui.theme.Theme.Companion.TextSecondary

@Composable
fun TestCard(
    test: TestCase,
    onOpen: () -> Unit,
    onRun: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(TestCardBackground)
            .border(
                width = 1.dp,
                color = Outline.copy(alpha = 0.55f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onOpen)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(PrimaryContainer),
            contentAlignment = Alignment.Center
        ) {
            BasicText(
                text = "T",
                style = TextStyle(
                    color = PrimaryDark,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            BasicText(
                text = test.title,
                style = TextStyle(
                    color = TextPrimary,
                    fontSize = 17.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            BasicText(
                text = test.description.ifBlank { "No description provided." },
                style = TextStyle(
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                ),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(TextSecondary.copy(alpha = 0.55f))
                )

                Spacer(modifier = Modifier.width(7.dp))

                BasicText(
                    text = test.timestamp,
                    style = TextStyle(
                        color = TextSecondary.copy(alpha = 0.82f),
                        fontSize = 12.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.width(20.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppButton(
                text = "Run",
                onClick = onRun,
                backgroundColor = Primary,
                contentColor = Color.White
            )

            AppButton(
                text = "Edit",
                onClick = onEdit,
                backgroundColor = PrimaryContainer,
                contentColor = PrimaryDark
            )

            AppButton(
                text = "Delete",
                onClick = onDelete,
                backgroundColor = DangerBackground,
                contentColor = Danger
            )
        }
    }
}
