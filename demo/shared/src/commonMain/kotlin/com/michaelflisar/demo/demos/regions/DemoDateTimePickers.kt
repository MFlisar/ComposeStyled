package com.michaelflisar.demo.demos.regions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledDatePicker
import com.michaelflisar.composestyled.ui.components.StyledTimePicker
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Composable
internal fun DemoDateTimePickers(baseId: Int, expandState: ExpandState) {
    val date = rememberSaveable(stateSaver = LocalDateSaver) {
        mutableStateOf(LocalDate(2026, 6, 15))
    }
    val time = rememberSaveable(stateSaver = LocalTimeSaver) {
        mutableStateOf(LocalTime(14, 30))
    }

    RegionContainer(
        title = "Date and Time Pickers",
        id = baseId + 1,
        expanded = expandState,
    ) {
        StyledColumn(itemSpacing = StyledTheme.spacing.medium) {
            SubTitle("Date selection, Monday-first calendar")
            StyledDatePicker(
                value = date,
                minimumDate = LocalDate(2026, 1, 1),
                maximumDate = LocalDate(2026, 12, 31),
            )
            StyledText("Selected date: ${date.value}")

            SubTitle("24-hour time, 5-minute steps")
            StyledTimePicker(
                value = time,
                minuteStep = 5,
            )
            StyledText("Selected time: ${time.value}")
        }
    }
}

private val LocalDateSaver: Saver<LocalDate, List<Int>> = Saver(
    save = { listOf(it.year, it.monthNumber, it.dayOfMonth) },
    restore = { LocalDate(it[0], it[1], it[2]) },
)

private val LocalTimeSaver: Saver<LocalTime, List<Int>> = Saver(
    save = { listOf(it.hour, it.minute, it.second, it.nanosecond) },
    restore = { LocalTime(it[0], it[1], it[2], it[3]) },
)
