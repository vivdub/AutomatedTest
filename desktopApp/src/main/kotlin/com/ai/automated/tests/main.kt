package com.ai.automated.tests

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Ai-Automated-Testing",
    ) {
        App()
    }
}