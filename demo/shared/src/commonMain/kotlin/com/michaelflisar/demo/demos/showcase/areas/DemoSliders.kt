package com.michaelflisar.demo.demos.showcase.areas

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.michaelflisar.composestyled.ui.components.StyledRangeSlider
import com.michaelflisar.composestyled.ui.components.StyledSlider
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.utils.number.numberRangeSaver
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoSliders(baseId: Int, expandState: ExpandState) {
    val continuous = rememberSaveable { mutableStateOf(0.35f) }
    val stepped = rememberSaveable { mutableStateOf(50) }
    val range = rememberSaveable(stateSaver = numberRangeSaver()) {
        mutableStateOf(0.25f..0.75f)
    }
    val constrainedRange = rememberSaveable(stateSaver = numberRangeSaver()) {
        mutableStateOf(20..30)
    }

    RegionContainer(
        title = "Sliders",
        id = baseId + 1,
        expanded = expandState,
    ) {
        SubTitle("Continuous value (value range: 0..1)")
        StyledSlider(
            value = continuous,
            modifier = Modifier.fillMaxWidth(),
        )
        StyledText("Value: ${continuous.value}")

        SubTitle("Typed Int value (value range: 0..100, step size: 5)")
        StyledSlider(
            value = stepped,
            modifier = Modifier.fillMaxWidth(),
            valueRange = 0..100,
            stepSize = 5,
        )
        StyledText("Value: ${stepped.value}")

        SubTitle("Int (value range: 0..100, step size: 5)")
        TypedSliderSample<Int>(
            "Int",
            50,
            0..100,
            5
        )

        SubTitle("Long (value range: 0..100, step size: 5)")
        TypedSliderSample<Long>(
            "Long",
            50L,
            0L..100L,
            5L
        )

        SubTitle("Float (value range: 0..1, step size: 0.1)")
        TypedSliderSample<Float>(
            "Float",
            0.5f,
            0f..1f,
            0.1f
        )

        SubTitle("Double (value range: 0..1, step size: 0.1)")
        TypedSliderSample<Double>(
            "Double",
            0.5,
            0.0..1.0,
            0.1
        )

        SubTitle("Long precision near Long.MAX_VALUE")
        TypedSliderSample<Long>(
            "Long",
            Long.MAX_VALUE - 50L,
            (Long.MAX_VALUE - 100L)..Long.MAX_VALUE,
            1L,
        )

        SubTitle("Range (value range: 0..1)")
        StyledRangeSlider(
            value = range,
            modifier = Modifier.fillMaxWidth(),
            valueRange = 0f..1f,
        )
        StyledText("Selected: ${range.value.start} to ${range.value.endInclusive}")

        SubTitle("Range (value range: 0..100, window size: 5..10)")
        StyledRangeSlider<Int>(
            value = constrainedRange,
            modifier = Modifier.fillMaxWidth(),
            valueRange = 0..100,
            minimumWindowSize = 5,
            maximumWindowSize = 10,
            stepSize = 1,
        )
        StyledText(
            "Selected: ${constrainedRange.value.start} to ${constrainedRange.value.endInclusive} " +
                    "(window size: ${constrainedRange.value.endInclusive - constrainedRange.value.start})"
        )

        SubTitle("Disabled")
        StyledSlider(
            value = 0.6f,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
        )
    }

}

@Composable
private fun <T> TypedSliderSample(
    label: String,
    initial: T,
    valueRange: ClosedRange<T>,
    stepSize: T? = null,
) where T : Number, T : Comparable<T> {
    val value = rememberSaveable { mutableStateOf(initial) }
    val range = rememberSaveable(stateSaver = numberRangeSaver<T>()) {
        mutableStateOf(valueRange)
    }
    StyledText("$label: ${value.value}")
    StyledSlider(
        value = value,
        modifier = Modifier.fillMaxWidth(),
        valueRange = valueRange,
        stepSize = stepSize,
    )
    StyledRangeSlider(
        value = range,
        modifier = Modifier.fillMaxWidth(),
        valueRange = valueRange,
        stepSize = stepSize,
    )
    StyledText("Range: ${range.value.start} to ${range.value.endInclusive}")
}