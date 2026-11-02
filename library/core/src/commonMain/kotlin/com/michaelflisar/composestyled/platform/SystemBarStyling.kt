package com.michaelflisar.composestyled.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import com.michaelflisar.composestyled.StyledTheme

/** Whether system-bar icons should use a light or dark foreground. */
enum class SystemBarIconStyle {
    Light,
    Dark,
}

/** Best-known presentation mode for the current native window. */
enum class WindowMode {
    /** A normal, full-size app window. */
    Normal,
    /** Split-screen or floating/freeform windowing. */
    MultiWindow,
    /** Android picture-in-picture. */
    PictureInPicture,
    /** The platform cannot reliably determine the current mode. */
    Unknown,
    /** This target has no supported mobile system bars. */
    Unsupported,
}

@Immutable
data class WindowEnvironment(
    /** A best-effort snapshot; unknown modes are not treated as normal. */
    val mode: WindowMode,
)

/**
 * Chooses system-bar icons from the matching theme content color, falling back to the
 * higher-contrast black or white option after compositing [color] over [background].
 */
@Composable
fun systemBarIconStyleFor(
    color: Color,
    background: Color,
): SystemBarIconStyle {
    require(background != Color.Unspecified) {
        "A specified background color is required to calculate system-bar icon contrast."
    }
    val effectiveColor = if (color == Color.Unspecified) {
        background
    } else {
        color.compositeOver(background)
    }
    val contentColor = StyledTheme.contentColorFor(
        backgroundColor = effectiveColor,
        fallback = StyledTheme.ContentColorFallback.BlackWhite,
    )
    return if (contentColor.luminance() > 0.5f) {
        SystemBarIconStyle.Light
    } else {
        SystemBarIconStyle.Dark
    }
}

/**
 * Returns a best-effort window-mode snapshot and updates when supported native window events occur.
 */
@Composable
expect fun rememberWindowEnvironment(): WindowEnvironment

/**
 * Applies caller-selected icon styles until this effect leaves composition.
 *
 * A null style leaves that bar unchanged. This function intentionally does not gate on
 * [WindowMode]; the caller decides when a manual update is appropriate.
 * On Android, the effect targets the current dialog window when hosted in a dialog,
 * otherwise the activity window.
 *
 * On Android 10+ a styled bar also disables the system contrast scrim, so the color drawn behind
 * it by the app stays visible; the previous setting is restored when the effect leaves composition.
 */
@Composable
expect fun SystemBarStyleEffect(
    statusBar: SystemBarIconStyle? = null,
    navigationBar: SystemBarIconStyle? = null,
)
