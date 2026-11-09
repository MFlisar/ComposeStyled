package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.michaelflisar.composestyled.ui.layout.StyledRow
import com.michaelflisar.composestyled.ui.primitives.StyledText
import kotlinx.datetime.LocalTime

// -------------------------
// public API
// -------------------------

/** State-backed 24-hour time picker; [minuteStep] controls minute increments. */
@Composable
fun StyledTimePicker(
    value: MutableState<LocalTime>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    minuteStep: Int = 1,
) {
    StyledTimePicker(
        value = value.value,
        onValueChange = { value.value = it },
        modifier = modifier,
        enabled = enabled,
        minuteStep = minuteStep,
    )
}

/** Controlled 24-hour time picker; [minuteStep] must be between 1 and 59. */
@Composable
fun StyledTimePicker(
    value: LocalTime,
    onValueChange: (LocalTime) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    minuteStep: Int = 1,
) {
    require(minuteStep in 1..59) {
        "minuteStep must be between 1 and 59."
    }

    StyledRow(
        modifier = modifier.fillMaxWidth(),
        itemSpacing = 12.dp,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TimeField(
            label = "Hour",
            value = value.hour,
            onDecrease = { onValueChange(value.withMinuteOffset(-60)) },
            onIncrease = { onValueChange(value.withMinuteOffset(60)) },
            enabled = enabled,
            modifier = Modifier.weight(1f),
        )
        StyledText(":", fontSize = 24.sp, fontWeight = FontWeight.Medium)
        TimeField(
            label = "Minute",
            value = value.minute,
            onDecrease = { onValueChange(value.withMinuteOffset(-minuteStep)) },
            onIncrease = { onValueChange(value.withMinuteOffset(minuteStep)) },
            enabled = enabled,
            modifier = Modifier.weight(1f),
        )
    }
}

// -------------------------
// private functions
// -------------------------

@Composable
private fun TimeField(
    label: String,
    value: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        StyledButton(
            onClick = onIncrease,
            variant = StyledButton.Variant.Plain,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Increase $label" },
        ) {
            StyledText("+")
        }
        StyledText(
            text = value.toString().padStart(2, '0'),
            modifier = Modifier.padding(vertical = 8.dp),
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
        )
        StyledButton(
            onClick = onDecrease,
            variant = StyledButton.Variant.Plain,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Decrease $label" },
        ) {
            StyledText("−")
        }
    }
}

private fun LocalTime.withMinuteOffset(offset: Int): LocalTime {
    val minutes = (hour * 60 + minute + offset + 24 * 60) % (24 * 60)
    return LocalTime(
        hour = minutes / 60,
        minute = minutes % 60,
        second = second,
        nanosecond = nanosecond,
    )
}
