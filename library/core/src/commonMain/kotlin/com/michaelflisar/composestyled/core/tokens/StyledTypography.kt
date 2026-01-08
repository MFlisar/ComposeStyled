package com.michaelflisar.composestyled.core.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Definiert die Standard-Typografie für das Designsystem.
 */
@Immutable
data class StyledTypography(
    val titleLarge: TextStyle = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold),
    val titleMedium: TextStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
    val titleSmall: TextStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
    val bodyLarge: TextStyle = TextStyle(fontSize = 20.sp),
    val bodyMedium: TextStyle = TextStyle(fontSize = 16.sp),
    val bodySmall: TextStyle = TextStyle(fontSize = 12.sp),
    val labelLarge: TextStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
    val labelMedium: TextStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
    val labelSmall: TextStyle = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Medium),
)

internal val LocalStyledTypography = staticCompositionLocalOf { StyledTypography() }
