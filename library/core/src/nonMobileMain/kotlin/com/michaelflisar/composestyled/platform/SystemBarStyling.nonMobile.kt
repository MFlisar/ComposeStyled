package com.michaelflisar.composestyled.platform

import androidx.compose.runtime.Composable

@Composable
actual fun rememberWindowEnvironment(): WindowEnvironment =
    WindowEnvironment(WindowMode.Unsupported)

@Composable
actual fun SystemBarStyleEffect(
    statusBar: SystemBarIconStyle?,
    navigationBar: SystemBarIconStyle?,
) = Unit
