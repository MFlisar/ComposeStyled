package com.michaelflisar.composedialogs.styled.provider

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.dialogs.time.TimePickerPeriod
import com.michaelflisar.composedialogs.dialogs.time.TimeUIProvider
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor
import com.michaelflisar.composestyled.ui.components.StyledInput
import com.michaelflisar.composestyled.ui.components.StyledPopupDropdown
import com.michaelflisar.composestyled.ui.components.StyledRadioButton
import com.michaelflisar.composestyled.ui.primitives.StyledText

internal object StyledTimeUIProvider : TimeUIProvider {

    @Composable
    override fun LabeledField(
        label: String?,
        modifier: Modifier,
        errorMessage: String?,
        content: @Composable () -> Unit,
    ) {
        StyledLabeledField(
            label = label,
            modifier = modifier,
            errorMessage = errorMessage,
            content = content,
        )
    }

    @Composable
    override fun NumberInput(
        modifier: Modifier,
        value: String,
        onValueChange: (String) -> Unit,
        title: String,
        onFocusChanged: (Boolean) -> Unit,
    ) {
        StyledInput(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .heightIn(min = 48.dp)
                .onFocusChanged { onFocusChanged(it.isFocused) }
                .semantics { contentDescription = title },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number,
            ),
            textStyle = StyledTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
            variant = StyledInput.Variant.Outlined,
        )
    }

    @Composable
    override fun NumberDropdown(
        modifier: Modifier,
        title: String,
        items: List<Int>,
        selected: Int,
        selectedText: String,
        itemText: (Int) -> String,
        onItemSelected: (Int) -> Unit,
    ) {
        StyledPopupDropdown(
            items = items,
            selected = selected,
            onSelectionChange = { _, item -> onItemSelected(item) },
            modifier = modifier,
            title = title,
            content = {
                selection {
                    StyledText(
                        text = selectedText,
                        modifier = Modifier.fillMaxWidth(),
                        style = StyledTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                    )
                }
                item { value, isSelected ->
                    StyledText(
                        text = itemText(value),
                        modifier = Modifier.fillMaxWidth(),
                        color = if (isSelected) StyledTheme.colorScheme.primary else LocalStyledContentColor.current,
                        style = StyledTheme.typography.bodyLarge,
                        maxLines = 1,
                    )
                }
            },
        )
    }

    @Composable
    override fun PeriodSelector(
        modifier: Modifier,
        selected: TimePickerPeriod,
        amText: String,
        pmText: String,
        onItemSelected: (TimePickerPeriod) -> Unit,
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(StyledTheme.spacing.small),
        ) {
            listOf(TimePickerPeriod.AM, TimePickerPeriod.PM).forEach { period ->
                val text = if (period == TimePickerPeriod.AM) amText else pmText
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = StyledTheme.padding.small),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    StyledRadioButton(
                        selected = period == selected,
                        onClick = { onItemSelected(period) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { StyledText(text) },
                    )
                }
            }
        }
    }
}
