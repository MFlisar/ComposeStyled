package com.michaelflisar.demo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.michaelflisar.composestyled.ui.state.KeyboardModifierState
import com.michaelflisar.composestyled.ui.state.ProvideKeyboardModifierState
import com.michaelflisar.composestyled.ui.state.rememberKeyboardModifierState
import java.awt.EventQueue
import java.awt.GraphicsEnvironment
import java.awt.Toolkit
import java.awt.Window
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.Timer

fun main() {
    application {
        val keyboardModifierState = rememberKeyboardModifierState()

        val windowState = rememberWindowState(
            width = 1024.dp,
            height = 800.dp,
        )

        Window(
            onCloseRequest = ::exitApplication,
            title = "Compose Styled Components Demo",
            state = windowState,
            onPreviewKeyEvent = keyboardModifierState::onKeyEvent,
        ) {
            CenterWindowOnScreen(window, index = 3, failIfNotEnoughScreens = false)
            BringWindowToFront(window)
            ClearKeyboardModifierStateOnWindowFocusLoss(window, keyboardModifierState)
            ProvideKeyboardModifierState(keyboardModifierState) {
                DemoContent()
            }
        }
    }
}

@Composable
private fun ClearKeyboardModifierStateOnWindowFocusLoss(
    window: java.awt.Window,
    state: KeyboardModifierState,
) {
    DisposableEffect(window, state) {
        val listener = object : WindowAdapter() {
            override fun windowLostFocus(event: WindowEvent) {
                state.clear()
            }
        }
        window.addWindowFocusListener(listener)
        onDispose {
            window.removeWindowFocusListener(listener)
        }
    }
}

// Screen indices are 1-based, ordered left to right, then top to bottom.
@Composable
private fun CenterWindowOnScreen(
    window: Window,
    index: Int,
    failIfNotEnoughScreens: Boolean,
) {
    DisposableEffect(window, index) {
        val configurations = GraphicsEnvironment.getLocalGraphicsEnvironment()
            .screenDevices
            .map { it.defaultConfiguration }
            .sortedWith(compareBy({ it.bounds.x }, { it.bounds.y }))
        if (failIfNotEnoughScreens && index > configurations.size) {
            error("Screen index $index is not available; only ${configurations.size} screens found")
        }
        if (index > configurations.size) {
            return@DisposableEffect onDispose {}
        }
        val configuration = configurations[index - 1]
        val center = {
            val bounds = configuration.bounds
            val insets = Toolkit.getDefaultToolkit().getScreenInsets(configuration)
            val availableWidth = bounds.width - insets.left - insets.right
            val availableHeight = bounds.height - insets.top - insets.bottom

            window.setLocation(
                bounds.x + insets.left + (availableWidth - window.width) / 2,
                bounds.y + insets.top + (availableHeight - window.height) / 2,
            )
        }
        val listener = object : WindowAdapter() {
            override fun windowOpened(event: WindowEvent) {
                center()
            }
        }
        window.addWindowListener(listener)
        if (window.isShowing) {
            center()
        }
        onDispose {
            window.removeWindowListener(listener)
        }
    }
}

@Composable
private fun BringWindowToFront(window: java.awt.Window) {
    DisposableEffect(window) {
        val initiallyAlwaysOnTop = window.isAlwaysOnTop
        var temporarilyAlwaysOnTop = false
        var disposed = false
        val restoreWindowOrder = Timer(250) {
            if (temporarilyAlwaysOnTop) {
                window.isAlwaysOnTop = initiallyAlwaysOnTop
                temporarilyAlwaysOnTop = false
            }
        }.apply {
            isRepeats = false
        }
        val bringToFront = {
            EventQueue.invokeLater {
                if (!disposed && window.isShowing) {
                    if (window.isAlwaysOnTopSupported && !initiallyAlwaysOnTop) {
                        window.isAlwaysOnTop = true
                        temporarilyAlwaysOnTop = true
                        restoreWindowOrder.start()
                    }
                    window.toFront()
                    window.requestFocus()
                }
            }
        }
        val listener = object : WindowAdapter() {
            override fun windowOpened(event: WindowEvent) {
                bringToFront()
            }
        }
        window.addWindowListener(listener)
        if (window.isShowing) {
            bringToFront()
        }
        onDispose {
            disposed = true
            window.removeWindowListener(listener)
            restoreWindowOrder.stop()
            if (temporarilyAlwaysOnTop) {
                window.isAlwaysOnTop = initiallyAlwaysOnTop
            }
        }
    }
}