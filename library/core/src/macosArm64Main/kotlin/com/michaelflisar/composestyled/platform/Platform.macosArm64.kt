package com.michaelflisar.composestyled.platform

import androidx.compose.runtime.Composable
import com.michaelflisar.composestyled.themes.StyledContrastLevel

actual fun currentPlatform(): Platform = Platform.MacOS

@Composable
actual fun platformSystemContrastLevel(): StyledContrastLevel = StyledContrastLevel.Normal