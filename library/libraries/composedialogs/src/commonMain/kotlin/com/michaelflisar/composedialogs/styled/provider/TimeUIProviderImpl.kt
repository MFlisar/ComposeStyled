package com.michaelflisar.composedialogs.styled.provider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.dialogs.time.TimePickerPeriod
import com.michaelflisar.composedialogs.dialogs.time.TimeUIProvider
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.runtime.LocalStyledContext
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor
import com.michaelflisar.composestyled.ui.components.StyledInput
import com.michaelflisar.composestyled.ui.components.StyledPopupDropdown
import com.michaelflisar.composestyled.ui.components.StyledSegmentedButton
import com.michaelflisar.composestyled.ui.components.StyledSegmentedButtonRowDefaults
import com.michaelflisar.composestyled.ui.primitives.StyledText

internal object TimeUIProviderImpl : TimeUIProvider {

    @Composable
    override fun LabeledField(
        label: String?,
        modifier: Modifier,
        errorMessage: String?,
        content: @Composable () -> Unit,
    ) {
        LabeledDialogField(
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

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun PeriodSelector(
        modifier: Modifier,
        selected: TimePickerPeriod,
        amText: String,
        pmText: String,
        onItemSelected: (TimePickerPeriod) -> Unit,
    ) {
        CompositionLocalProvider(
            LocalStyledContext provides StyledSegmentedButton.Context.VerticalGroup,
        ) {
            Column(
                modifier = modifier.selectableGroup(),
                verticalArrangement = Arrangement.spacedBy(-1.dp),
            ) {
                val shape = StyledSegmentedButtonRowDefaults.itemShape
                listOf(TimePickerPeriod.AM, TimePickerPeriod.PM).forEachIndexed { index, period ->
                    val text = if (period == TimePickerPeriod.AM) amText else pmText
                    StyledSegmentedButton(
                        selected = period == selected,
                        onClick = { onItemSelected(period) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(StyledTheme.sizes.minimumInteractiveSize),
                        itemShape = when (index) {
                            0 -> shape.copy(
                                bottomStart = CornerSize(0.dp),
                                bottomEnd = CornerSize(0.dp),
                            )

                            else -> shape.copy(
                                topStart = CornerSize(0.dp),
                                topEnd = CornerSize(0.dp),
                            )
                        },
                    ) {
                        StyledText(text)
                    }
                }
            }
        }
    }
}
