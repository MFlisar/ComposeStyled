package com.michaelflisar.composestyled.core.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import com.michaelflisar.composestyled.core.classes.colors.BaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.ColorRef
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi

@InternalComposeStyledApi
object StyledColorUtil {

    fun <T> pick(color: Color, dark: T, light: T, threshold: Float = .5f): T =
        if (color.luminance() >= threshold) light else dark

    enum class BlendMode {
        CompositeOver,
        Lerp
    }

    private fun applyOverlay(
        base: Color,
        overlay: Color,
        alpha: Float,
        mode: BlendMode,
    ): Color {
        val a = alpha.coerceIn(0f, 1f)
        if (a <= 0f) return base
        val ov = overlay.copy(alpha = a)

        return when (mode) {
            BlendMode.CompositeOver -> ov.compositeOver(base) // like your old emphasizedColor(...)
            BlendMode.Lerp -> lerp(base, overlay.copy(alpha = 1f), a)
        }
    }

    @Composable
    @ReadOnlyComposable
    fun overlay(
        base: BaseColorDef,
        overlayColor: Color,
        alpha: Float,
        applyToBackground: Boolean = true,
        applyToForeground: Boolean = false,
        applyToBorder: Boolean = false,
        blendMode: BlendMode = BlendMode.CompositeOver,
        borderFallback: Color? = null, // if base.border is null and you want to create one
    ): BaseColorDef {
        val bg = base.background.resolve()
        val fg = base.foreground.resolve()
        val br = base.border?.resolve()
        val ov = overlayColor

        val newBg = if (applyToBackground) applyOverlay(bg, ov, alpha, blendMode) else bg
        val newFg = if (applyToForeground) applyOverlay(fg, ov, alpha, blendMode) else fg

        val newBr: Color? = when {
            applyToBorder && br != null -> applyOverlay(br, ov, alpha, blendMode)
            applyToBorder && br == null && borderFallback != null ->
                applyOverlay(borderFallback, ov, alpha, blendMode)

            else -> br
        }

        return BaseColorDef(
            background = newBg,
            foreground = newFg,
            border = newBr
        )
    }

    @Composable
    @ReadOnlyComposable
    fun dim(
        base: BaseColorDef,
        foregroundAlpha: Float? = null,
        borderAlpha: Float? = null,
    ): BaseColorDef {
        val bg = base.background.resolve()
        val fg = base.foreground.resolve()
        val br = base.border?.resolve()

        val newFg = if (foregroundAlpha != null) fg.copy(
            alpha = (fg.alpha * foregroundAlpha).coerceIn(
                0f,
                1f
            )
        ) else fg
        val newBr = if (borderAlpha != null && br != null) br.copy(
            alpha = (br.alpha * borderAlpha).coerceIn(
                0f,
                1f
            )
        ) else br

        return BaseColorDef(bg, newFg, newBr)
    }

    @Composable
    @ReadOnlyComposable
    fun withBorder(
        base: BaseColorDef,
        border: ColorRef?,
    ): BaseColorDef {
        val bg = base.background.resolve()
        val fg = base.foreground.resolve()
        val br = border?.resolve()
        return BaseColorDef(bg, fg, br)
    }
}