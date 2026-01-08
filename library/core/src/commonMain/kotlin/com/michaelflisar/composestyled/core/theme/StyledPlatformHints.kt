package com.michaelflisar.composestyled.core.theme

import androidx.compose.runtime.Immutable

/**
 * Optional platform hints for theme adapters.
 *
 * In the core module diese Hinweise sind absichtlich konservativ und hauptsächlich informativ.
 * Adapter-Module (z.B. Material3, Fluent, Cupertino) können sie interpretieren, um Defaults und Verhalten zu steuern.
 * Diese API ist so gestaltet, dass sie zukünftig sicher erweiterbar ist.
 */
@Immutable
data class StyledPlatformHints(
    /** Visuelle Sprache / Design-Familie */
    val family: StyledPlatformFamily = StyledPlatformFamily.Auto,
    /** Dichte-Hinweis (NICHT Layout-Abstände, nur visuelle Dichte) */
    val density: StyledDensity = StyledDensity.Default,
    /** Accessibility-Kontrast-Präferenz */
    val contrast: StyledContrast = StyledContrast.Standard,
    /** Reduzierte Bewegungs-Präferenz */
    val reducedMotion: Boolean = false,
)

// region Design language family hint
sealed interface StyledPlatformFamily {
    data object Auto : StyledPlatformFamily
    data object Material : StyledPlatformFamily
    data object Fluent : StyledPlatformFamily
    data object Cupertino : StyledPlatformFamily
    data class Custom(val id: String) : StyledPlatformFamily
}
// endregion

// region Visual density hint
sealed interface StyledDensity {
    data object Compact : StyledDensity
    data object Default : StyledDensity
    data object Comfortable : StyledDensity
}
// endregion

// region Contrast / accessibility hint
sealed interface StyledContrast {
    data object Standard : StyledContrast
    data object High : StyledContrast
}
// endregion
