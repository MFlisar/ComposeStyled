package com.michaelflisar.composestyled.platform

import androidx.compose.runtime.Composable
import com.michaelflisar.composestyled.themes.StyledContrastLevel

actual fun currentPlatform(): Platform {
    val osName = System.getProperty("os.name") ?: return Platform.JvmUnknown
    return when {
        osName.startsWith("Windows", ignoreCase = true) -> Platform.JvmWindows
        osName.startsWith("Mac", ignoreCase = true) || osName.startsWith("Darwin", ignoreCase = true) -> Platform.JvmMacOS
        osName.startsWith("Linux", ignoreCase = true) -> Platform.JvmLinux
        else -> Platform.JvmUnknown
    }
}

@Composable
actual fun platformSystemContrastLevel(): StyledContrastLevel = StyledContrastLevel.Normal