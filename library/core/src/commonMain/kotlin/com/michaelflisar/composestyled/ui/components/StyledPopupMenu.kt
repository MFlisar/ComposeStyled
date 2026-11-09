package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledKey
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.internal.LocalStyledMenuBarPopupContext
import com.michaelflisar.composestyled.ui.internal.StyledDropdownButton
import com.michaelflisar.composestyled.ui.overlay.StyledPopupContainerScope
import com.michaelflisar.composestyled.ui.primitives.StyledText
import kotlin.math.roundToInt

internal val LocalStyledPopupMenuDismiss =
    staticCompositionLocalOf<(() -> Unit)?> { null }
internal val LocalStyledPopupMenuDismissSubmenu =
    staticCompositionLocalOf<(() -> Unit)?> { null }

object StyledPopupMenu {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledPopupMenu")

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation> {
        override val key: StyledKey<Implementation>
            get() = Key

        @Composable
        @OptIn(InternalComposeStyledApi::class)
        fun Render(
            expanded: Boolean,
            onDismissRequest: () -> Unit,
            modifier: Modifier,
            offset: DpOffset,
            placement: Placement,
            horizontalAlignment: HorizontalAlignment,
            properties: PopupProperties,
            scrollState: ScrollState,
            style: StyledPopup.Style,
            contentPadding: PaddingValues,
            header: (@Composable () -> Unit)?,
            content: @Composable ColumnScope.() -> Unit,
        )
    }

    /** Placement behavior relative to the measured popup container. */
    enum class Placement {
        /** Opens below or above its anchor, depending on available space. */
        Anchor,

        /** Opens beside its anchor and flips to the other side when needed. */
        Submenu,
    }

    /** Horizontal alignment of the popup container relative to its anchor, not its content. */
    enum class HorizontalAlignment {
        /** Aligns the popup's logical start edge with the anchor's logical start edge. */
        Start,

        /** Aligns the popup's logical end edge with the anchor's logical end edge. */
        End,
    }

    enum class OutsideTapBehavior {
        /** Uses the popup platform's standard outside-tap handling. */
        PlatformDefault,

        /** Uses the full-window overlay when screen coordinates are available. */
        DismissAndConsume,
    }

    /** Width rules for a popup menu relative to its anchor and available content. */
    @Stable
    sealed interface WidthConfig {

        /** Uses the measured content width, optionally capped by `maxWidth`. */
        @Immutable
        data class Auto(
            val maxWidth: Dp? = StyledPopupMenuDefaults.maxWidth,
            val limitToContainerWidth: Boolean = false,
        ) : WidthConfig {
            init {
                require(maxWidth == null || maxWidth.value.isFinite() && maxWidth > 0.dp) {
                    "Menu maximum width must be finite and positive"
                }
            }
        }

        /** Matches the anchor width, capped by `maxWidth` when provided. */
        @Immutable
        data class MatchParent(val maxWidth: Dp? = null) : WidthConfig {
            init {
                require(maxWidth == null || maxWidth.value.isFinite() && maxWidth > 0.dp) {
                    "Menu maximum width must be finite and positive"
                }
            }
        }

        /** Uses the anchor width. */
        @Immutable
        data object MatchAnchor : WidthConfig

        /** Uses at least the anchor width, allowing wider content. */
        @Immutable
        data object AtLeastAnchor : WidthConfig

        /** Uses the menu content's intrinsic width. */
        @Immutable
        data object WrapContent : WidthConfig

        /** Uses a fixed menu width. */
        @Immutable
        data class Fixed(val width: Dp) : WidthConfig {
            init {
                require(width.value.isFinite() && width > 0.dp) {
                    "Popup width must be finite and positive"
                }
            }
        }
    }
}

// -------------------------
// defaults
// -------------------------

object StyledPopupMenuDefaults {
    val maxWidth: Dp = 280.dp
    val maxHeight: Dp = 320.dp
    val shadowElevation: Dp = 8.dp
    val offset: DpOffset = DpOffset(0.dp, 0.dp)
    val border: BorderStroke? = null
    val widthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.AtLeastAnchor

    val menuBarAnimation = StyledPopupDefaults.Animations.unfoldAwayFromAnchor

    /** Uses the menu-bar animation inside its popup context and the standard animation otherwise. */
    val animation: StyledPopup.Animation
        @Composable get() = if (LocalStyledMenuBarPopupContext.current != null) {
            menuBarAnimation
        } else {
            StyledPopupDefaults.Animations.unfoldAwayFromAnchor
        }

    val shape: Shape
        @Composable get() = StyledTheme.shapes.small

    val containerColor: Color
        @Composable get() = StyledTheme.colorScheme.surface

    val contentPadding: PaddingValues
        @Composable get() = PaddingValues(
            vertical = if (LocalStyledMenuBarPopupContext.current != null) {
                StyledTheme.padding.small
            } else {
                StyledTheme.padding.small
            },
        )

    @Composable
    fun contentColor(containerColor: Color = this.containerColor): Color =
        StyledTheme.contentColorFor(containerColor)
}

// -------------------------
// public API
// -------------------------

/** Menu-content scope for items, sections, submenus and custom rows. */
@Stable
class StyledPopupMenuScope internal constructor(
    private val dismissAll: () -> Unit,
    private val dismissSubmenu: () -> Unit = dismissAll,
    private val renderColumnContent:
    @Composable (@Composable ColumnScope.() -> Unit) -> Unit = {},
) {
    /** Adds a standard action item to this menu. */
    @Composable
    fun item(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        leadingIcon: (@Composable () -> Unit)? = null,
        trailingIcon: (@Composable () -> Unit)? = null,
        enabled: Boolean = true,
        closeOnClick: Boolean = true,
    ) = item(
        text = { StyledText(text) },
        onClick = onClick,
        modifier = modifier,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        enabled = enabled,
        closeOnClick = closeOnClick,
    )

    /** Adds an action item with fully custom text content. */
    @Composable
    @OptIn(InternalComposeStyledApi::class)
    fun item(
        text: @Composable () -> Unit,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        leadingIcon: (@Composable () -> Unit)? = null,
        trailingIcon: (@Composable () -> Unit)? = null,
        enabled: Boolean = true,
        closeOnClick: Boolean = true,
    ) {
        StyledDropdownMenuItem(
            text = text,
            onClick = {
                onClick()
                if (closeOnClick) dismissAll()
            },
            modifier = modifier,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            enabled = enabled,
        )
    }

    /** Adds a non-interactive section heading. */
    @Composable
    fun header(
        text: String,
        modifier: Modifier = Modifier,
    ) = header(content = { StyledText(text) }, modifier = modifier)

    /** Adds a non-interactive section heading with custom content. */
    @Composable
    fun header(
        content: @Composable () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        StyledDropdownMenuSectionHeader(text = content, modifier = modifier)
    }

    /** Adds a divider between groups of menu items. */
    @Composable
    fun divider(modifier: Modifier = Modifier) {
        StyledDropdownMenuDivider(modifier)
    }

    /** Adds a checkable item. The menu closes only when [closeOnClick] is true. */
    @Composable
    @OptIn(InternalComposeStyledApi::class)
    fun checkbox(
        text: String,
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
        leadingIcon: (@Composable () -> Unit)? = null,
        enabled: Boolean = true,
        closeOnClick: Boolean = false,
    ) {
        StyledDropdownMenuContent(
            onClick = {
                onCheckedChange(!checked)
                if (closeOnClick) dismissAll()
            },
            enabled = enabled,
            modifier = Modifier.semantics {
                role = Role.Checkbox
                toggleableState = ToggleableState(checked)
            },
        ) {
            leadingIcon?.invoke()
            StyledCheckbox(checked = checked, onCheckedChange = null, enabled = enabled)
            StyledText(text)
        }
    }

    /** Adds a submenu for selecting an item by its index. The selection closes this submenu. */
    @Composable
    fun selection(
        title: String,
        items: List<String>,
        selectedIndex: Int,
        onSelectionChange: (Int) -> Unit,
    ) {
        selection(
            title = title,
            items = items.indices.toList(),
            selected = selectedIndex,
            label = { items[it] },
            onSelectionChange = onSelectionChange,
        )
    }

    /**
     * Adds a submenu for selecting an item. The selection closes this submenu; call [dismiss]
     * from [onSelectionChange] to close the complete menu hierarchy instead.
     */
    @Composable
    fun <T : Any> selection(
        title: String,
        items: List<T>,
        selected: T,
        label: (T) -> String,
        onSelectionChange: (T) -> Unit,
    ) {
        submenu(title) {
            items.forEach { option ->
                item(
                    text = label(option),
                    trailingIcon = if (option == selected) {
                        { StyledIcon(StyledIcons.Check, contentDescription = null) }
                    } else {
                        null
                    },
                    closeOnClick = false,
                    onClick = {
                        onSelectionChange(option)
                        dismissSubmenu()
                    },
                )
            }
        }
    }

    /** Adds a cascading submenu. */
    @Composable
    fun submenu(
        text: String,
        leadingIcon: (@Composable () -> Unit)? = null,
        enabled: Boolean = true,
        content: @Composable StyledPopupMenuScope.() -> Unit,
    ) {
        StyledPopupMenuSubmenu(
            text = { StyledText(text) },
            leadingIcon = leadingIcon,
            enabled = enabled,
            content = content,
        )
    }

    /** Adds a row for custom composable content. */
    @Composable
    @OptIn(InternalComposeStyledApi::class)
    fun custom(
        modifier: Modifier = Modifier,
        onClick: (() -> Unit)? = null,
        enabled: Boolean = true,
        horizontalArrangement: Arrangement.Horizontal =
            Arrangement.spacedBy(StyledTheme.spacing.medium),
        verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
        content: @Composable RowScope.() -> Unit,
    ) {
        StyledDropdownMenuContent(
            onClick = onClick?.let { click ->
                {
                    click()
                    dismissAll()
                }
            },
            modifier = modifier,
            enabled = enabled,
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
            content = content,
        )
    }

    /** Composes arbitrary menu content directly in the popup column. */
    @Composable
    fun customColumn(content: @Composable ColumnScope.() -> Unit) {
        renderColumnContent(content)
    }

    /** Closes this menu and all of its parent menus. */
    fun dismiss() = dismissAll()

    /** Closes this submenu and its descendants, leaving its parent menu open. */
    fun dismissSubmenu() = dismissSubmenu.invoke()
}

/**
 * Shows a button-triggered menu. Compose menu entries through the receiver scope rather than
 * calling standalone menu-item composables.
 */
@Composable
fun StyledPopupMenu(
    triggerContent: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    variant: StyledButton.Variant = StyledButton.Variant.Auto,
    shape: Shape = StyledButtonDefaults.shape,
    customization: StyledButton.Customization? = null,
    expanded: MutableState<Boolean> = remember { mutableStateOf(false) },
    menuWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenuDefaults.widthConfig,
    menuShape: Shape = StyledPopupMenuDefaults.shape,
    menuContainerColor: Color = StyledPopupMenuDefaults.containerColor,
    menuContentColor: Color = StyledPopupMenuDefaults.contentColor(menuContainerColor),
    menuOffset: DpOffset = StyledPopupMenuDefaults.offset,
    menuHorizontalAlignment: StyledPopupMenu.HorizontalAlignment =
        StyledPopupMenu.HorizontalAlignment.Start,
    menuMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    menuAnimation: StyledPopup.Animation = StyledPopupDefaults.Animations.unfoldAwayFromAnchor,
    menu: @Composable StyledPopupMenuScope.() -> Unit,
) {
    StyledDropdownButton(
        menu = { menu() },
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        variant = variant,
        shape = shape,
        customization = customization,
        expanded = expanded,
        menuWidthConfig = menuWidthConfig,
        menuShape = menuShape,
        menuContainerColor = menuContainerColor,
        menuContentColor = menuContentColor,
        menuOffset = menuOffset,
        menuHorizontalAlignment = menuHorizontalAlignment,
        menuMaxHeight = menuMaxHeight,
        menuAnimation = menuAnimation,
        content = triggerContent,
    )
}

/**
 * Shows a menu anchored to this popup-container scope with configurable width, animation
 * and dismissal behavior.
 */
@Composable
fun StyledPopupContainerScope.StyledPopupMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    popupHeader: @Composable (() -> Unit)? = null,
    properties: PopupProperties = StyledPopupDefaults.properties(),
    widthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenuDefaults.widthConfig,
    offset: DpOffset = StyledPopupMenuDefaults.offset,
    placement: StyledPopupMenu.Placement = StyledPopupMenu.Placement.Anchor,
    horizontalAlignment: StyledPopupMenu.HorizontalAlignment =
        StyledPopupMenu.HorizontalAlignment.Start,
    scrollState: ScrollState = rememberScrollState(),
    shape: Shape = StyledPopupMenuDefaults.shape,
    containerColor: Color = StyledPopupMenuDefaults.containerColor,
    contentColor: Color = StyledPopupMenuDefaults.contentColor(containerColor),
    shadowElevation: Dp = StyledPopupMenuDefaults.shadowElevation,
    border: BorderStroke? = StyledPopupMenuDefaults.border,
    maxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    contentPadding: PaddingValues = StyledPopupMenuDefaults.contentPadding,
    outsideTapBehavior: StyledPopupMenu.OutsideTapBehavior =
        StyledPopupMenu.OutsideTapBehavior.PlatformDefault,
    animation: StyledPopup.Animation = StyledPopupMenuDefaults.animation,
    content: @Composable StyledPopupMenuScope.() -> Unit,
) {
    require(maxHeight.value.isFinite() && maxHeight > 0.dp) {
        "Menu maximum height must be finite and positive"
    }
    require(offset.x.value.isFinite() && offset.y.value.isFinite()) {
        "Menu offset must be finite"
    }
    val menuBarContext = LocalStyledMenuBarPopupContext.current
    val lastAncestor = menuBarContext?.ancestors?.lastOrNull()
    val animationLevel = lastAncestor?.level ?: menuBarContext?.rootLevel
    val animationId = lastAncestor?.id ?: menuBarContext?.rootId
    val onTransitionFinished: ((Boolean) -> Unit)? =
        remember(animationLevel, animationId) {
            val level = animationLevel
            val id = animationId
            if (level != null && id != null) {
                { finished -> level.setPopupAnimationFinished(id, finished) }
            } else {
                null
            }
        }
    val rootDismiss = LocalStyledPopupMenuDismiss.current ?: onDismissRequest
    val menuColumnContent: @Composable ColumnScope.() -> Unit = {
        val popupColumn = this
        CompositionLocalProvider(LocalStyledPopupMenuDismiss provides rootDismiss) {
            content(
                StyledPopupMenuScope(
                    dismissAll = rootDismiss,
                    dismissSubmenu = LocalStyledPopupMenuDismissSubmenu.current ?: rootDismiss,
                    renderColumnContent = { columnContent -> popupColumn.columnContent() },
                )
            )
        }
    }
    val density = LocalDensity.current
    val windowOrigin = parentWindowOriginOnScreen
    val useWindowOverlay =
        outsideTapBehavior == StyledPopupMenu.OutsideTapBehavior.DismissAndConsume &&
                windowOrigin != null
    if (containerWidth > 0) {
        val anchorWidth = with(density) { containerWidth.toDp() }
        LocalStyledRegistry.current[StyledPopupMenu.Key].Render(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = popupMenuWidthModifier(widthConfig, anchorWidth).then(modifier),
            offset = offset,
            placement = placement,
            horizontalAlignment = horizontalAlignment,
            properties = properties,
            scrollState = scrollState,
            style = StyledPopup.Style(
                shape = shape,
                containerColor = containerColor,
                contentColor = contentColor,
                shadowElevation = shadowElevation,
                border = border,
                maxHeight = maxHeight,
                animation = animation,
                windowOverlay = useWindowOverlay,
                onTransitionFinished = onTransitionFinished,
                parentWindowOriginOnScreen = windowOrigin
                    ?.takeIf { useWindowOverlay }
                    ?.let {
                        IntOffset(
                            x = it.x.roundToInt(),
                            y = it.y.roundToInt(),
                        )
                    } ?: IntOffset.Zero,
            ),
            contentPadding = contentPadding,
            header = popupHeader,
            content = menuColumnContent,
        )
    }
}

private fun popupMenuWidthModifier(
    config: StyledPopupMenu.WidthConfig,
    anchorWidth: Dp,
): Modifier = when (config) {
    StyledPopupMenu.WidthConfig.MatchAnchor -> Modifier.width(anchorWidth)
    StyledPopupMenu.WidthConfig.AtLeastAnchor -> Modifier.widthIn(min = anchorWidth)
    StyledPopupMenu.WidthConfig.WrapContent -> Modifier
    is StyledPopupMenu.WidthConfig.Fixed -> Modifier.width(config.width)
    is StyledPopupMenu.WidthConfig.Auto -> {
        val maximum = when {
            config.limitToContainerWidth -> minOf(config.maxWidth ?: anchorWidth, anchorWidth)
            else -> config.maxWidth
        }
        if (maximum != null) Modifier.widthIn(max = maximum) else Modifier
    }

    is StyledPopupMenu.WidthConfig.MatchParent -> {
        val width = config.maxWidth?.let { minOf(anchorWidth, it) } ?: anchorWidth
        Modifier.width(width)
    }
}
