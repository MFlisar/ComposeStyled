package com.michaelflisar.composestyled.cupertino.liquid

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeInput
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import dev.chrisbanes.haze.glass.GlassStyle
import dev.chrisbanes.haze.glass.hazeGlass

internal val LocalCupertinoLiquidHazeState = staticCompositionLocalOf<HazeState?> { null }

@Composable
@OptIn(ExperimentalHazeApi::class)
internal fun Modifier.liquidGlass(
    shape: Shape,
    tint: Color,
): Modifier {
    val glassShape = shape as? RoundedCornerShape ?: RoundedCornerShape(16.dp)
    val hazeState = LocalCupertinoLiquidHazeState.current ?: return background(tint, shape)
    val style = remember(glassShape, tint) {
        GlassStyle.regular.then {
            backgroundColor(tint.copy(alpha = 0.18f))
            tint(tint.copy(alpha = 0.22f))
            shape(glassShape)
        }
    }
    return hazeGlass(
        input = HazeInput.Sources(hazeState),
        style = style,
    )
}

@Composable
internal fun CupertinoLiquidContent(content: @Composable () -> Unit) {
    val hazeState = rememberHazeState()
    CompositionLocalProvider(LocalCupertinoLiquidHazeState provides hazeState) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(hazeState),
        ) {
            content()
        }
    }
}
