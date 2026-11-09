package com.michaelflisar.demo.demos.libraries

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.dialogs.date.ui.DatePickerRange
import com.michaelflisar.composedialogs.dialogs.date.ui.DatePickerSetup
import com.michaelflisar.composedialogs.dialogs.frequency.FrequencyPickerType
import com.michaelflisar.composedialogs.styled.StyledDialogInfo
import com.michaelflisar.composedialogs.styled.StyledDialogInput
import com.michaelflisar.composedialogs.styled.StyledDialogList
import com.michaelflisar.composedialogs.styled.StyledDialogMenu
import com.michaelflisar.composedialogs.styled.StyledDialogNumber
import com.michaelflisar.composedialogs.styled.StyledDialogProgress
import com.michaelflisar.composedialogs.styled.StyledDialogMenu.Entry
import com.michaelflisar.composedialogs.styled.StyledDialogColor
import com.michaelflisar.composedialogs.styled.StyledDialogDate
import com.michaelflisar.composedialogs.styled.StyledDialogFrequency
import com.michaelflisar.composedialogs.styled.StyledDialogTime
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.CustomColors
import com.michaelflisar.composestyled.platform.SystemBarStyleEffectIfNeeded
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledInput
import com.michaelflisar.composestyled.ui.components.StyledInputDefaults
import com.michaelflisar.composestyled.ui.components.StyledPopupDropdown
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.layout.StyledDialog.SheetOptions
import com.michaelflisar.composestyled.ui.layout.StyledFlowRow
import com.michaelflisar.composestyled.ui.layout.StyledScaffold
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.state.rememberDialogState
import com.michaelflisar.demo.helper.DemoTopBar
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month

@Composable
internal fun DemoPageComposeDialogs(onBack: () -> Unit) {
    val colorDialog = rememberDialogState()
    val dateDialog = rememberDialogState()
    val frequencyDialog = rememberDialogState()
    val timeDialog = rememberDialogState()
    val infoDialog = rememberDialogState()
    val inputDialog = rememberDialogState()
    val listDialog = rememberDialogState()
    val menuDialog = rememberDialogState()
    val numberDialog = rememberDialogState()
    val progressDialog = rememberDialogState()
    val initialColor = StyledTheme.colorScheme.primary
    val selectedColor = remember { mutableStateOf(initialColor) }
    val selectedDate = remember { mutableStateOf(LocalDate(2026, 11, 7)) }
    val selectedTime = remember { mutableStateOf(LocalTime(14, 45)) }
    val selectedFrequencyType = remember { mutableStateOf(FrequencyPickerType.Daily) }
    val selectedFrequencyTime = remember { mutableStateOf(LocalTime(8, 30)) }
    val selectedFrequencyDayOfWeek = remember { mutableStateOf(DayOfWeek.MONDAY) }
    val selectedFrequencyDayOfMonth = remember { mutableStateOf(1) }
    val selectedFrequencyMonthOfYear = remember { mutableStateOf(Month.JANUARY) }
    val selectedFrequencyInterval = remember { mutableStateOf<Int?>(1) }
    val input = remember { mutableStateOf("") }
    val number = remember { mutableStateOf(5) }
    val selectedItem = remember { mutableStateOf("") }
    val dialogStyles = remember { listOf("Dialog", "Bottom sheet", "Bottom sheet + expand to full screen", "Full screen") }
    val selectedDialogStyle = remember { mutableStateOf(dialogStyles.first()) }
    val dialogStyle = when (selectedDialogStyle.value) {
        "Bottom sheet" -> StyledDialog.Style.BottomSheet()
        "Bottom sheet + expand to full screen" -> StyledDialog.Style.BottomSheet(
            options = SheetOptions(expandToFullscreen = true)
        )
        "Full screen" -> StyledDialog.Style.Fullscreen()
        else -> StyledDialog.Style.Dialog()
    }
    val datePickerRange = DatePickerRange(1900..2100)
    val datePickerSetup = DatePickerSetup(
        buttonToday = { StyledText("Today") },
        firstDayOfWeek = DayOfWeek.MONDAY,
        formatterWeekDayLabel = { it.name.take(3) },
        formatterSelectedDate = { it.toString() },
        formatterSelectedMonth = { it.name.lowercase().replaceFirstChar { char -> char.uppercase() } },
        formatterSelectedYear = { it.toString() },
        formatterMonthSelectorList = { it.name.lowercase().replaceFirstChar { char -> char.uppercase() } },
        formatterYearSelectorList = { it.toString() },
        dateCellHeight = 48.dp,
        showNextPreviousMonthButtons = true,
        showNextPreviousYearButtons = true,
    )

    val scaffoldColor = StyledTheme.colorScheme.surface
    val topBarColor = StyledTheme.colorScheme.primary
    SystemBarStyleEffectIfNeeded(
        statusBarColor = topBarColor,
        navigationBarColor = scaffoldColor,
        backgroundColor = scaffoldColor,
    )

    StyledScaffold(
        containerColor = scaffoldColor,
        topBar = {
            DemoTopBar(
                containerColor = topBarColor,
                title = "ComposeDialogs",
                onBack = onBack,
            )
        },
    ) { paddingValues ->
        StyledColumn(
            itemSpacing = StyledTheme.spacing.medium,
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
                .padding(StyledTheme.padding.content),
        ) {
            StyledPopupDropdown(
                items = dialogStyles,
                selected = selectedDialogStyle,
                modifier = Modifier.fillMaxWidth(),
                title = "Dialog style",
            )
            StyledText(
                "These picker dialogs reuse ComposeDialogs content with Styled controls, colors, and dialog actions."
            )
            StyledFlowRow(spacing = StyledTheme.spacing.medium) {
                StyledButton(onClick = colorDialog::show) { StyledText("Color picker") }
                StyledButton(onClick = dateDialog::show) { StyledText("Date picker") }
                StyledButton(onClick = frequencyDialog::show) { StyledText("Frequency picker") }
                StyledButton(onClick = timeDialog::show) { StyledText("Time picker") }
                StyledButton(onClick = infoDialog::show) { StyledText("Info") }
                StyledButton(onClick = inputDialog::show) { StyledText("Input") }
                StyledButton(onClick = listDialog::show) { StyledText("List") }
                StyledButton(onClick = menuDialog::show) { StyledText("Menu") }
                StyledButton(onClick = numberDialog::show) { StyledText("Number") }
                StyledButton(onClick = progressDialog::show) { StyledText("Progress") }
            }
            StyledText("Selected color: ${selectedColor.value}")
            StyledText("Selected date: ${selectedDate.value}")
            StyledText("Selected time: ${selectedTime.value}")
            StyledText(
                "Selected frequency: ${selectedFrequencyType.value}, every " +
                    "${selectedFrequencyInterval.value}, at ${selectedFrequencyTime.value}"
            )
            StyledText("Selected list item: ${selectedItem.value}")
            StyledText("Selected number: ${number.value}")
        }
    }

    StyledDialogColor(
        state = colorDialog,
        color = selectedColor,
        style = dialogStyle,
        title = { StyledText("Choose a color") },
        buttons = doneButton(),
    )
    StyledDialogDate(
        state = dateDialog,
        date = selectedDate,
        dateRange = datePickerRange,
        setup = datePickerSetup,
        style = dialogStyle,
        title = { StyledText("Choose a date") },
        buttons = doneButton(),
    )
    StyledDialogFrequency(
        state = frequencyDialog,
        selectedType = selectedFrequencyType,
        selectedTime = selectedFrequencyTime,
        selectedDayOfWeek = selectedFrequencyDayOfWeek,
        selectedDayOfMonth = selectedFrequencyDayOfMonth,
        selectedMonthOfYear = selectedFrequencyMonthOfYear,
        selectedInterval = selectedFrequencyInterval,
        style = dialogStyle,
        title = { StyledText("Set a frequency") },
        buttons = doneButton(),
    )
    StyledDialogTime(
        state = timeDialog,
        time = selectedTime,
        style = dialogStyle,
        title = { StyledText("Choose a time") },
        buttons = doneButton(),
    )
    StyledDialogInfo(
        state = infoDialog,
        info = "This information content uses Styled text and typography.",
        infoLabel = "Styled dialog",
        style = dialogStyle,
        title = { StyledText("Information") },
        buttons = doneButton(),
    )
    StyledDialogInput(
        state = inputDialog,
        value = input.value,
        onValueChange = { input.value = it },
        label = { StyledText("Name") },
        placeholder = { StyledText("Enter your name") },
        singleLine = true,
        variant = StyledInput.Variant.Outlined,
        customization = StyledInputDefaults.customize(CustomColors.PrimarySurface),
        style = dialogStyle,
        title = { StyledText("Enter a name") },
        buttons = doneButton(),
    )
    StyledDialogList(
        state = listDialog,
        items = listOf("First", "Second", "Third"),
        onItemClick = { selectedItem.value = it },
        itemContent = { StyledText(it) },
        supportingContent = { StyledText("Choose this option") },
        showDividers = true,
        style = dialogStyle,
        title = { StyledText("Choose an item") },
        buttons = doneButton(),
    )
    StyledDialogMenu(
        state = menuDialog,
        items = listOf(
            Entry.Action(title = "First action", onClick = {}),
            Entry.Submenu(
                title = "More actions",
                items = listOf(
                    Entry.Action(title = "Nested action", onClick = {}),
                ),
            ),
        ),
        style = dialogStyle,
        title = { StyledText("Actions") },
    )
    StyledDialogNumber(
        state = numberDialog,
        value = number,
        stepSize = 1,
        style = dialogStyle,
        title = { StyledText("Choose a number") },
        buttons = doneButton(),
    )
    StyledDialogProgress(
        state = progressDialog,
        content = { StyledText("Working...") },
        progress = null, // infinite
        style = dialogStyle,
        title = { StyledText("Please wait") },
    )
}

private fun doneButton() = StyledDialog.Buttons(
    positive = StyledDialog.Button(
        label = "Done",
        onClick = {},
    ),
)
