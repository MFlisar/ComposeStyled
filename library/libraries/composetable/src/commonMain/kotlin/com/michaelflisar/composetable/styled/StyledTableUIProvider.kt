package com.michaelflisar.composetable.styled

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Deselect
import androidx.compose.material.icons.filled.FirstPage
import androidx.compose.material.icons.filled.LastPage
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.toIconComposable
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor
import com.michaelflisar.composestyled.tokens.LocalStyledTextStyle
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledCheckbox
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledIconButton
import com.michaelflisar.composestyled.ui.components.StyledOutlinedButton
import com.michaelflisar.composestyled.ui.components.StyledOutlinedInput
import com.michaelflisar.composestyled.ui.components.StyledPopupMenu
import com.michaelflisar.composestyled.ui.components.StyledPopupMenuScope
import com.michaelflisar.composestyled.ui.components.StyledProgressIndicator
import com.michaelflisar.composestyled.ui.components.StyledRadioButton
import com.michaelflisar.composestyled.ui.components.StyledSelectableChip
import com.michaelflisar.composestyled.ui.components.StyledSurface
import com.michaelflisar.composestyled.ui.components.StyledTextButton
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.layout.StyledLazyColumn
import com.michaelflisar.composestyled.ui.layout.StyledScrollbar
import com.michaelflisar.composestyled.ui.overlay.StyledPopupContainer
import com.michaelflisar.composestyled.ui.primitives.StyledHorizontalDivider
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.primitives.StyledVerticalDivider
import com.michaelflisar.composestyled.ui.state.rememberDialogState
import com.michaelflisar.composetable.TableColors
import com.michaelflisar.composetable.implementation.TableIcon
import com.michaelflisar.composetable.implementation.TableTextStyle
import com.michaelflisar.composetable.implementation.TableUI
import com.michaelflisar.composestyled.classes.IconComposable as StyledIconContent

private val LocalTableContentColor = compositionLocalOf { Color.Unspecified }
private val LocalTablePopupMenuScope = compositionLocalOf<StyledPopupMenuScope?> { null }

object StyledTableUIProvider : TableUI.Provider {

    fun install() {
        TableUI.set(StyledTableUIProvider)
    }

    @Composable
    override fun tableColors() = TableColors(
        container = StyledTheme.colorScheme.surface,
        content = StyledTheme.colorScheme.onSurface,
        header = StyledTheme.colorScheme.surfaceVariant,
        headerContent = StyledTheme.colorScheme.onSurfaceVariant,
        selected = StyledTheme.colorScheme.primarySurface,
        selectedContent = StyledTheme.colorScheme.onPrimarySurface,
        divider = StyledTheme.colorScheme.outlineVariant,
    )

    @Composable
    override fun tableShape(): Shape = StyledTheme.shapes.medium

    @Composable
    override fun scrollbarWidth(): Dp = StyledScrollbar.scrollbarSize

    @Composable
    override fun primaryColor(): Color = StyledTheme.colorScheme.primary

    @Composable
    override fun errorColor(): Color = StyledTheme.colorScheme.error

    @Composable
    override fun textStyle(style: TableTextStyle): TextStyle = when (style) {
        TableTextStyle.LabelSmall -> StyledTheme.typography.labelSmall
        TableTextStyle.TitleMedium -> StyledTheme.typography.titleMedium
    }

    @Composable
    override fun buttonIconSize(): Dp = StyledTheme.sizes.iconSize

    @Composable
    override fun buttonIconSpacing(): Dp = StyledTheme.spacing.small

    @Composable
    override fun contentColor(): Color =
        LocalTableContentColor.current.takeUnless { it == Color.Unspecified }
            ?: LocalStyledContentColor.current

    @Composable
    override fun ProvideContentColor(color: Color, content: @Composable () -> Unit) {
        CompositionLocalProvider(LocalTableContentColor provides color, content = content)
    }

    @Composable
    override fun HorizontalDivider(modifier: Modifier, color: Color) {
        StyledHorizontalDivider(
            modifier = modifier,
            color = color.takeUnless { it == Color.Unspecified }
                ?: StyledTheme.colorScheme.outlineVariant,
        )
    }

    @Composable
    override fun VerticalDivider(modifier: Modifier, color: Color) {
        StyledVerticalDivider(
            modifier = modifier,
            color = color.takeUnless { it == Color.Unspecified }
                ?: StyledTheme.colorScheme.outlineVariant,
        )
    }

    @Composable
    override fun CircularProgressIndicator(modifier: Modifier) {
        StyledProgressIndicator(
            type = StyledProgressIndicator.Type.Circular,
            modifier = modifier,
        )
    }

    @Composable
    override fun LinearProgressIndicator(modifier: Modifier) {
        StyledProgressIndicator(
            type = StyledProgressIndicator.Type.Linear,
            modifier = modifier,
        )
    }

    @Composable
    override fun LazyColumn(
        state: LazyListState,
        modifier: Modifier,
        paddingForScrollbar: Dp,
        itemSpacing: Dp,
        horizontalAlignment: Alignment.Horizontal,
        content: LazyListScope.() -> Unit,
    ) {
        StyledLazyColumn(
            itemSpacing = itemSpacing,
            modifier = modifier.fillMaxWidth().padding(end = paddingForScrollbar),
            state = state,
            horizontalAlignment = horizontalAlignment,
            content = content,
        )
    }

    @Composable
    override fun Surface(color: Color, contentColor: Color, content: @Composable () -> Unit) {
        ProvideContentColor(contentColor) {
            StyledSurface(
                color = color,
                contentColor = contentColor,
                content = content,
            )
        }
    }

    @Composable
    override fun Text(
        text: String,
        modifier: Modifier,
        color: Color,
        style: TextStyle?,
        maxLines: Int,
    ) {
        StyledText(
            text = text,
            modifier = modifier,
            color = color.takeUnless { it == Color.Unspecified } ?: contentColor(),
            style = style ?: LocalStyledTextStyle.current,
            maxLines = maxLines,
        )
    }

    @Composable
    override fun Icon(
        icon: TableIcon,
        contentDescription: String?,
        modifier: Modifier,
        tint: Color?,
    ) {
        StyledIcon(
            icon = icon.iconComposable,
            contentDescription = contentDescription,
            modifier = modifier,
            tint = tint ?: contentColor(),
        )
    }

    @Composable
    override fun TextButton(
        onClick: () -> Unit,
        enabled: Boolean,
        content: @Composable RowScope.() -> Unit,
    ) {
        StyledTextButton(
            onClick = onClick,
            enabled = enabled,
            content = content,
        )
    }

    @Composable
    override fun OutlinedButton(onClick: () -> Unit, content: @Composable RowScope.() -> Unit) {
        StyledOutlinedButton(
            onClick = onClick,
            content = content,
        )
    }

    @Composable
    override fun IconButton(
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        content: @Composable () -> Unit,
    ) {
        StyledIconButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            content = content,
            variant = StyledButton.Variant.Plain
        )
    }

    @Composable
    override fun OutlinedTextField(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier,
        label: (@Composable () -> Unit)?,
        leadingIcon: (@Composable () -> Unit)?,
        singleLine: Boolean,
        isError: Boolean,
        supportingText: (@Composable () -> Unit)?,
    ) {
        StyledOutlinedInput(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            singleLine = singleLine,
            isError = isError,
            label = label,
            leadingIcon = leadingIcon,
            supportingText = supportingText,
        )
    }

    @Composable
    override fun InputChip(
        selected: Boolean,
        onClick: () -> Unit,
        label: @Composable () -> Unit,
        trailingIcon: (@Composable () -> Unit)?,
    ) {
        val styledTrailingIcon: StyledIconContent? = trailingIcon?.let { icon ->
            { _, modifier, _ ->
                Box(modifier) {
                    icon()
                }
            }
        }
        StyledSelectableChip(
            selected = selected,
            onClick = onClick,
            trailingIcon = styledTrailingIcon,
            content = label,
        )
    }

    @Composable
    override fun DropdownMenu(
        expanded: Boolean,
        onDismissRequest: () -> Unit,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        StyledPopupContainer(
            popup = {
                StyledPopupMenu(
                    expanded = expanded,
                    onDismissRequest = onDismissRequest,
                    widthConfig = StyledPopupMenu.WidthConfig.WrapContent,
                    content = {
                        val menuScope = this
                        CompositionLocalProvider(LocalTablePopupMenuScope provides menuScope) {
                            customColumn { content() }
                        }
                    },
                )
            },
            content = {
                Spacer(Modifier.width(1.dp).height(1.dp))
            },
        )
    }

    @Composable
    override fun DropdownMenuItem(
        text: @Composable () -> Unit,
        onClick: () -> Unit,
        leadingIcon: (@Composable () -> Unit)?,
        trailingIcon: (@Composable () -> Unit)?,
    ) {
        checkNotNull(LocalTablePopupMenuScope.current) {
            "DropdownMenuItem must be composed inside DropdownMenu"
        }.item(
            text = text,
            onClick = onClick,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
        )
    }

    @Composable
    override fun AlertDialog(
        onDismissRequest: () -> Unit,
        title: (@Composable () -> Unit)?,
        text: (@Composable () -> Unit)?,
        confirmText: String,
        onConfirmClick: () -> Unit,
        confirmEnabled: Boolean,
        dismissText: String?,
        onDismissClick: (() -> Unit)?,
        dismissEnabled: Boolean,
    ) {
        val state = rememberDialogState(initialVisible = true)
        StyledDialog(
            state = state,
            onDismissRequest = {
                state.dismiss()
                onDismissRequest()
            },
            title = title,
            buttons = StyledDialog.Buttons(
                positive = StyledDialog.Button(
                    label = confirmText,
                    onClick = onConfirmClick,
                    enabled = confirmEnabled,
                ),
                negative = if (dismissText != null && onDismissClick != null) {
                    StyledDialog.Button(
                        label = dismissText,
                        onClick = onDismissClick,
                        enabled = dismissEnabled,
                    )
                } else null,
            ),
            content = { padding ->
                Column(
                    modifier = Modifier.fillMaxWidth().padding(padding),
                    verticalArrangement = Arrangement.spacedBy(StyledTheme.spacing.medium),
                ) {
                    text?.invoke()
                }
            },
        )
    }

    @Composable
    override fun Checkbox(checked: Boolean, onCheckedChange: ((Boolean) -> Unit)?) {
        StyledCheckbox(checked = checked, onCheckedChange = onCheckedChange)
    }

    @Composable
    override fun RadioButton(selected: Boolean, onClick: (() -> Unit)?) {
        StyledRadioButton(selected = selected, onClick = onClick)
    }
}

private val TableIcon.iconComposable: StyledIconContent
    get() = when (this) {
        TableIcon.Search -> StyledIcons.Search
        TableIcon.Clear -> StyledIcons.Clear
        TableIcon.ArrowUpward -> Icons.Default.ArrowUpward.toIconComposable()
        TableIcon.ArrowDownward -> Icons.Default.ArrowDownward.toIconComposable()
        TableIcon.FilterAlt -> StyledIcons.Filter
        TableIcon.Check -> StyledIcons.Check
        TableIcon.SelectAll -> Icons.Default.SelectAll.toIconComposable()
        TableIcon.Deselect -> Icons.Default.Deselect.toIconComposable()
        TableIcon.FirstPage -> Icons.Default.FirstPage.toIconComposable()
        TableIcon.ChevronLeft -> Icons.Default.ChevronLeft.toIconComposable()
        TableIcon.ChevronRight -> Icons.Default.ChevronRight.toIconComposable()
        TableIcon.LastPage -> Icons.Default.LastPage.toIconComposable()
        TableIcon.Refresh -> StyledIcons.Refresh
    }
