package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledKey
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.internal.SliderScale
import com.michaelflisar.composestyled.utils.number.NumberUtil

object StyledSlider {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledSlider")

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation> {
        override val key: StyledKey<Implementation>
            get() = Key

        @Composable
        fun Render(
            value: Float,
            onValueChange: (Float) -> Unit,
            onValueChangeFinished: (() -> Unit)?,
            modifier: Modifier,
            enabled: Boolean,
            valueRange: ClosedFloatingPointRange<Float>,
            steps: Int,
            interactionSource: MutableInteractionSource?,
            activeColor: Color,
            inactiveColor: Color,
        )
    }
}

// -------------------------
// public API
// -------------------------

/** Typed slider. Integral values use unit steps by default; Float/Double are continuous. */
@OptIn(InternalComposeStyledApi::class)
@Composable
fun <T> StyledSlider(
    value: MutableState<T>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedRange<T> = NumberUtil.range(NumberUtil.zero(value.value), NumberUtil.one(value.value)),
    stepSize: T? = null,
    interactionSource: MutableInteractionSource? = null,
    activeColor: Color = StyledTheme.colorScheme.primary,
    inactiveColor: Color = activeColor.copy(alpha = 0.24f),
    onValueChangeFinished: (() -> Unit)? = null,
) where T : Number, T : Comparable<T> {
    StyledSlider(
        value = value.value,
        onValueChange = { value.value = it },
        onValueChangeFinished = onValueChangeFinished,
        modifier = modifier,
        enabled = enabled,
        valueRange = valueRange,
        stepSize = stepSize,
        interactionSource = interactionSource,
        activeColor = activeColor,
        inactiveColor = inactiveColor,
    )
}

/** [stepSize] is a positive increment anchored at the start of [valueRange], not a tick count. */
@OptIn(InternalComposeStyledApi::class)
@Composable
fun <T> StyledSlider(
    value: T,
    onValueChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedRange<T> = NumberUtil.range(NumberUtil.zero(value), NumberUtil.one(value)),
    stepSize: T? = null,
    interactionSource: MutableInteractionSource? = null,
    activeColor: Color = StyledTheme.colorScheme.primary,
    inactiveColor: Color = activeColor.copy(alpha = 0.24f),
    onValueChangeFinished: (() -> Unit)? = null,
) where T : Number, T : Comparable<T> {
    val scale = SliderScale(valueRange, stepSize)
    LocalStyledRegistry.current[StyledSlider.Key].Render(
        value = scale.position(value).toFloat(),
        onValueChange = { onValueChange(scale.value(it.toDouble())) },
        onValueChangeFinished = onValueChangeFinished,
        modifier = modifier,
        enabled = enabled,
        valueRange = scale.rendererRange,
        steps = scale.steps,
        interactionSource = interactionSource,
        activeColor = activeColor,
        inactiveColor = inactiveColor,
    )
}
