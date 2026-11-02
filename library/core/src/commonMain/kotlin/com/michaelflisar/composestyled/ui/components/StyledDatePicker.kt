package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.layout.StyledRow
import com.michaelflisar.composestyled.ui.primitives.StyledText
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

// -------------------------
// public API
// -------------------------

/** State-backed calendar picker; optional date bounds disable choices outside the inclusive range. */
@Composable
fun StyledDatePicker(
    value: MutableState<LocalDate>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    minimumDate: LocalDate? = null,
    maximumDate: LocalDate? = null,
) {
    StyledDatePicker(
        value = value.value,
        onValueChange = { value.value = it },
        modifier = modifier,
        enabled = enabled,
        minimumDate = minimumDate,
        maximumDate = maximumDate,
    )
}

/** Calendar picker that reports selected dates through [onValueChange]. */
@Composable
fun StyledDatePicker(
    value: LocalDate,
    onValueChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    minimumDate: LocalDate? = null,
    maximumDate: LocalDate? = null,
) {
    require(minimumDate == null || maximumDate == null || minimumDate <= maximumDate) {
        "minimumDate must not be after maximumDate."
    }

    var visibleMonth by rememberSaveable(
        value.year,
        value.month.ordinal,
        stateSaver = LocalDateStateSaver,
    ) {
        mutableStateOf(LocalDate(value.year, value.month.ordinal + 1, 1))
    }
    val firstDay = LocalDate(visibleMonth.year, visibleMonth.month.ordinal + 1, 1)
    val daysInMonth = daysInMonth(firstDay)
    val leadingDays = firstDay.dayOfWeek.ordinal

    StyledColumn(modifier = modifier, itemSpacing = 8.dp) {
        StyledRow(
            modifier = Modifier.fillMaxWidth(),
            itemSpacing = 8.dp,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StyledButton(
                onClick = { visibleMonth = shiftMonth(visibleMonth, -1) },
                role = StyledButton.Role.Tertiary,
                enabled = enabled,
                modifier = Modifier.weight(1f).semantics {
                    contentDescription = "Previous month"
                },
            ) {
                StyledText("‹")
            }
            StyledText(
                text = "${visibleMonth.year}-${(visibleMonth.month.ordinal + 1).toString().padStart(2, '0')}",
                modifier = Modifier.weight(3f),
            )
            StyledButton(
                onClick = { visibleMonth = shiftMonth(visibleMonth, 1) },
                role = StyledButton.Role.Tertiary,
                enabled = enabled,
                modifier = Modifier.weight(1f).semantics {
                    contentDescription = "Next month"
                },
            ) {
                StyledText("›")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            weekDays.forEach { day ->
                Box(
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    StyledText(day.name.lowercase().take(2))
                }
            }
        }

        repeat(6) { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                repeat(7) { weekday ->
                    val dayOfMonth = week * 7 + weekday - leadingDays + 1
                    if (dayOfMonth in 1..daysInMonth) {
                        val date = LocalDate(visibleMonth.year, visibleMonth.month.ordinal + 1, dayOfMonth)
                        val inRange = (minimumDate == null || date >= minimumDate) &&
                                (maximumDate == null || date <= maximumDate)
                        val dayModifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .semantics {
                                contentDescription = date.toString()
                                selected = date == value
                            }
                        if (date == value) {
                            StyledButton(
                                onClick = { onValueChange(date) },
                                role = StyledButton.Role.Primary,
                                modifier = dayModifier,
                                enabled = enabled && inRange,
                            ) {
                                StyledText(dayOfMonth.toString())
                            }
                        } else {
                            StyledButton(
                                onClick = { onValueChange(date) },
                                role = StyledButton.Role.Tertiary,
                                modifier = dayModifier,
                                enabled = enabled && inRange,
                            ) {
                                StyledText(dayOfMonth.toString())
                            }
                        }
                    } else {
                        Spacer(Modifier.weight(1f).aspectRatio(1f))
                    }
                }
            }
        }
    }
}

// -------------------------
// private functions
// -------------------------

private val LocalDateStateSaver: Saver<LocalDate, List<Int>> = Saver(
    save = { listOf(it.year, it.monthNumber, it.dayOfMonth) },
    restore = { LocalDate(it[0], it[1], it[2]) },
)

private val weekDays = listOf(
    DayOfWeek.MONDAY,
    DayOfWeek.TUESDAY,
    DayOfWeek.WEDNESDAY,
    DayOfWeek.THURSDAY,
    DayOfWeek.FRIDAY,
    DayOfWeek.SATURDAY,
    DayOfWeek.SUNDAY,
)

private fun daysInMonth(firstDay: LocalDate): Int {
    val nextMonth = shiftMonth(firstDay, 1)
    return (nextMonth.toEpochDays() - firstDay.toEpochDays()).toInt()
}

private fun shiftMonth(date: LocalDate, amount: Int): LocalDate {
    val month = date.month.ordinal + 1 + amount
    val year = date.year + when {
        month < 1 -> -1
        month > 12 -> 1
        else -> 0
    }
    val normalizedMonth = when {
        month < 1 -> month + 12
        month > 12 -> month - 12
        else -> month
    }
    return LocalDate(year, normalizedMonth, 1)
}
