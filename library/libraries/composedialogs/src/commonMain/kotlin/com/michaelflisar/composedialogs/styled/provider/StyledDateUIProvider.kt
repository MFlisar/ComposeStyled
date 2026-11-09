package com.michaelflisar.composedialogs.styled.provider

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.dialogs.date.DateColor
import com.michaelflisar.composedialogs.dialogs.date.DatePickerButtonType
import com.michaelflisar.composedialogs.dialogs.date.DatePickerIcon
import com.michaelflisar.composedialogs.dialogs.date.DateTextStyle
import com.michaelflisar.composedialogs.dialogs.date.DateUIProvider
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.CustomColors
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledCard
import com.michaelflisar.composestyled.ui.components.StyledCardDefaults
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledIconButton
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.layout.StyledLazyColumn
import com.michaelflisar.composestyled.ui.primitives.StyledHorizontalDivider
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.primitives.StyledVerticalDivider
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import kotlinx.datetime.LocalDate

internal object StyledDateUIProvider : DateUIProvider {

    @Composable
    override fun Text(
        text: String,
        modifier: Modifier,
        style: DateTextStyle,
        color: Color?,
        textAlign: TextAlign?,
        maxLines: Int,
    ) {
        StyledText(
            text = text,
            modifier = modifier,
            style = when (style) {
                DateTextStyle.BodySmall -> StyledTheme.typography.bodySmall
                DateTextStyle.BodyMedium -> StyledTheme.typography.bodyMedium
                DateTextStyle.TitleMedium -> StyledTheme.typography.titleMedium
                DateTextStyle.HeadlineSmall -> StyledTheme.typography.headlineSmall
            },
            color = color ?: Color.Unspecified,
            textAlign = textAlign,
            maxLines = maxLines,
        )
    }

    @Composable
    override fun Icon(
        icon: DatePickerIcon,
        contentDescription: String?,
        modifier: Modifier,
        tint: Color?,
    ) {
        StyledIcon(
            icon = when (icon) {
                DatePickerIcon.Previous -> StyledIcons.NavigateBack
                DatePickerIcon.Next -> StyledIcons.NavigateForward
                DatePickerIcon.DropDown -> StyledIcons.Dropdown
                DatePickerIcon.Check -> StyledIcons.Check
            },
            contentDescription = contentDescription,
            modifier = modifier,
            tint = tint ?: LocalStyledContentColor.current,
        )
    }

    @Composable
    override fun IconButton(
        modifier: Modifier,
        onClick: () -> Unit,
        enabled: Boolean,
        content: @Composable () -> Unit,
    ) {
        StyledIconButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            variant = StyledButton.Variant.Plain,
            content = content,
        )
    }

    @Composable
    override fun Button(
        type: DatePickerButtonType,
        modifier: Modifier,
        onClick: () -> Unit,
        enabled: Boolean,
        content: @Composable () -> Unit,
    ) {
        StyledButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            variant = when (type) {
                DatePickerButtonType.SelectedDate -> StyledButton.Variant.Filled
                DatePickerButtonType.Today -> StyledButton.Variant.Outlined
            },
            content = { content() },
        )
    }

    @Composable
    override fun Divider(modifier: Modifier, vertical: Boolean) {
        if (vertical) {
            StyledVerticalDivider(modifier = modifier)
        } else {
            StyledHorizontalDivider(modifier = modifier)
        }
    }

    @Composable
    override fun color(color: DateColor): Color = when (color) {
        DateColor.Primary -> StyledTheme.colorScheme.primary
        DateColor.OnPrimary -> StyledTheme.colorScheme.onPrimary
        DateColor.Outline -> StyledTheme.colorScheme.outline
    }

    @Composable
    override fun smallShape(): Shape = StyledTheme.shapes.small

    override fun Modifier.minimumInteractiveComponentSize(): Modifier =
        heightIn(min = 48.dp)

    @Composable
    override fun ScrollableList(state: LazyListState, content: LazyListScope.() -> Unit) {
        StyledLazyColumn(
            modifier = Modifier.heightIn(max = 320.dp),
            state = state,
            content = content,
        )
    }

    @Composable
    override fun CalendarCell(
        date: LocalDate,
        selected: Boolean,
        today: Boolean,
        modifier: Modifier,
        onClick: () -> Unit,
    ) {
        val shape = StyledTheme.shapes.small
        val cellModifier = if (today && !selected) {
            modifier.border(2.dp, StyledTheme.colorScheme.primary, shape)
        } else {
            modifier
        }
        StyledCard(
            modifier = cellModifier,
            shape = shape,
            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
            customization = if (selected) {
                StyledCardDefaults.customize(CustomColors.Primary)
            } else {
                null
            },
            onClick = if (selected) null else onClick,
            applyContentPadding = false,
            variant = if (selected) StyledCard.Variant.Filled else StyledCard.Variant.Outlined,
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                StyledText(
                    text = date.day.toString(),
                    color = if (selected) StyledTheme.colorScheme.onPrimary else Color.Unspecified,
                )
            }
        }
    }

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun DialogTitle(content: @Composable () -> Unit) {
        UpdateStyledThemeLocals(
            textStyle = StyledTheme.typography.headlineSmall,
            content = content,
        )
    }
}
