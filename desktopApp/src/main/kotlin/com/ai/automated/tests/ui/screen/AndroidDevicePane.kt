package com.ai.automated.tests.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.automated.tests.ui.theme.Theme.Companion.AppBackground
import com.ai.automated.tests.ui.theme.Theme.Companion.Outline
import com.ai.automated.tests.ui.theme.Theme.Companion.Surface
import com.ai.automated.tests.ui.theme.Theme.Companion.TextPrimary
import com.ai.automated.tests.ui.theme.Theme.Companion.TextSecondary

@Composable
fun AndroidDevicePane(expanded: Boolean, onToggle: () -> Unit, bitmap: ImageBitmap?=null) {
    if (expanded) {
        ExpandedContainer(onToggle = onToggle){
            when(bitmap==null){
                true -> WaitForImage()
                else -> ShowImage(bitmap)
            }
        }
    } else {
        CollapsibleContainer(onToggle)
    }
}

@Composable
private fun ShowImage(image: ImageBitmap) {
    Image(
        bitmap = image,
        contentDescription = "Android screen"
    )
}

@Composable
private fun WaitForImage(){
    BasicText(
        text = "Android device UI\nwill appear here",
        modifier = Modifier.padding(24.dp),
        style = TextStyle(
            color = TextSecondary,
            fontSize = 14.sp,
            lineHeight = 21.sp,
            textAlign = TextAlign.Center
        )
    )
}

@Composable
private fun CollapsibleContainer(onToggle: () -> Unit) {
    Column(
        modifier = Modifier
            .width(48.dp)
            .fillMaxHeight()
            .background(Surface)
            .border(width = 1.dp, color = Outline),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggle)
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            BasicText(
                text = "‹",
                style = TextStyle(
                    color = TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        BasicText(
            text = "DEVICE",
            modifier = Modifier.padding(top = 8.dp),
            style = TextStyle(
                color = TextSecondary,
                fontSize = 10.sp,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun ExpandedContainer(onToggle: () -> Unit, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .width(360.dp)
            .fillMaxHeight()
            .background(Surface)
            .border(width = 1.dp, color = Outline)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                BasicText(
                    text = "ANDROID DEVICE",
                    style = TextStyle(
                        color = TextSecondary,
                        fontSize = 12.sp,
                        letterSpacing = 1.1.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                BasicText(
                    text = "Live device UI",
                    style = TextStyle(
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onToggle)
                    .background(AppBackground)
                    .border(
                        width = 1.dp,
                        color = Outline,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                BasicText(
                    text = "›",
                    style = TextStyle(
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(9f / 16f)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF1A1C20))
                .padding(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(AppBackground)
                    .border(
                        width = 1.dp,
                        color = Outline,
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }
    }
}