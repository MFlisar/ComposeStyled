package com.michaelflisar.demo

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.michaelflisar.composestyled.demo.DemoApp

fun main() {

    application {
        Window(
            title = BuildKonfig.appName,
            onCloseRequest = ::exitApplication,
            state = rememberWindowState(
                position = WindowPosition(Alignment.Center),
                width = 1024.dp,
                height = 800.dp
            )
        ) {
            DemoApp("Windows")
        }
    }
}