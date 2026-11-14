package com.michaelflisar.composedialogs.styled.provider

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import com.michaelflisar.composestyled.ui.components.StyledButtonDefaults
import com.michaelflisar.composestyled.ui.components.StyledCard
import com.michaelflisar.composestyled.ui.components.StyledCardDefaults
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledIconButton
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.layout.StyledLazyColumn
import com.michaelflisar.composestyled.ui.primitives.StyledHorizontalDivider
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.primitives.StyledVerticalDivider
import kotlinx.datetime.LocalDate

internal object DateUIProviderImpl : DateUIProvider {

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
                DatePickerIcon.Previous -> StyledIcons.SubmenuBack
                DatePickerIcon.Next -> StyledIcons.Submenu
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
            customization = when (type) {
                DatePickerButtonType.SelectedDate -> StyledButtonDefaults.customize(
                    colors = CustomColors.Primary,
                    disabledColors = CustomColors.Primary,
                )

                DatePickerButtonType.Today -> null
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

    @Composable
    override fun Modifier.minimumInteractiveComponentSize(): Modifier =
        heightIn(min = StyledTheme.sizes.minimumInteractiveSize)

    @Composable
    override fun ScrollableList(state: LazyListState, content: LazyListScope.() -> Unit) {
        StyledLazyColumn(
            modifier = Modifier,//.heightIn(max = 320.dp),
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
        StyledCard(
            modifier = modifier,
            shape = StyledTheme.shapes.small,
            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
            customization = if (!selected) {
                val border = if (today) 2.dp else 1.dp
                val color =
                    if (today) StyledTheme.colorScheme.primary else StyledTheme.colorScheme.outline
                StyledCardDefaults.customize(border = color, borderThickness = border)
            } else StyledCardDefaults.customize(CustomColors.Primary),
            onClick = if (selected) null else onClick,
            applyContentPadding = false,
            variant = if (selected) StyledCard.Variant.Filled else StyledCard.Variant.Outlined,
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                StyledText(
                    text = date.day.toString(),
                    color = if (selected) StyledTheme.colorScheme.onPrimary else Color.Unspecified,
                )
            }
        }
    }

}
