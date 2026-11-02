package com.michaelflisar.composestyled.foundation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.CustomColors
import com.michaelflisar.composestyled.classes.asColorToken
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledSideNavigation
import com.michaelflisar.composestyled.ui.components.StyledSideNavigationDefaults
import com.michaelflisar.composestyled.ui.components.StyledIconButton
import com.michaelflisar.composestyled.ui.components.StyledIconButtonDefaults
import com.michaelflisar.composestyled.ui.internal.LocalStyledSelectionColors
import com.michaelflisar.composestyled.ui.internal.StyledSelectionColors
import com.michaelflisar.composestyled.ui.layout.StyledVerticalScrollbarContainer

@InternalComposeStyledApi
val LocalSideNavigationLayout = compositionLocalOf<SideNavigationLayoutInfo?> { null }

@InternalComposeStyledApi
@Immutable
data class SideNavigationLayoutInfo(
    val compactWidth: Dp,
    val expandedWidth: Dp,
    val expansionProgress: Float,
)

/** Shared non-modal frame; native item rendering and toggle appearance stay with the theme. */
@InternalComposeStyledApi
@Composable
fun FoundationSideNavigationLayout(
    modifier: Modifier,
    mode: StyledSideNavigation.Mode,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    scrollable: Boolean,
    scrollState: ScrollState,
    showScrollbar: Boolean,
    containerColor: Color,
    contentColor: Color,
    customization: StyledSideNavigation.Customization?,
    title: @Composable (() -> Unit)?,
    header: @Composable (() -> Unit)?,
    footer: @Composable (() -> Unit)?,
    windowInsets: WindowInsets,
    toggleIcon: @Composable (StyledSideNavigation.ToggleState) -> Unit,
    titleStartPadding: Dp? = null,
    toggleIconCenter: Dp = StyledSideNavigationDefaults.compactWidth / 2,
    content: @Composable ColumnScope.() -> Unit,
) {
    val compactWidth = StyledSideNavigationDefaults.compactWidth
    val expandedWidth = StyledSideNavigationDefaults.expandedWidth
    require(compactWidth.value.isFinite() && compactWidth.value > 0) { "compactWidth must be finite and positive" }
    require(expandedWidth.value.isFinite() && expandedWidth >= compactWidth) { "expandedWidth must be finite and at least compactWidth" }
    val widthProgress = remember { Animatable(if (expanded) 1f else 0f) }
    val heightProgress = remember { Animatable(if (expanded) 1f else 0f) }
    val hasSlots = header != null || footer != null
    LaunchedEffect(expanded, hasSlots) {
        // Cancellation preserves both current values, including reversals between phases.
        if (expanded) {
            if (hasSlots) heightProgress.animateTo(1f, tween(100))
            else heightProgress.snapTo(1f)
            widthProgress.animateTo(1f, tween(180))
        } else {
            widthProgress.animateTo(0f, tween(180))
            if (hasSlots) heightProgress.animateTo(0f, tween(100))
            else heightProgress.snapTo(0f)
        }
    }
    val selectedContent = customization?.selectedItemContentColor?.resolve()
    val unselectedContent = customization?.unselectedItemContentColor?.resolve()
    val indicator = customization?.selectedItemIndicatorColor?.resolve()
    val colors = remember(selectedContent, unselectedContent, indicator) {
        StyledSelectionColors(selectedContent, unselectedContent, indicator)
    }
    val resolvedContent = contentColor.takeUnless { it == Color.Unspecified }
        ?: StyledTheme.contentColorFor(containerColor)
    UpdateStyledThemeLocals(
        contentColor = resolvedContent,
        textStyle = StyledTheme.typography.labelLarge,
    ) {
        CompositionLocalProvider(
            LocalStyledSelectionColors provides colors,
            LocalSideNavigationLayout provides SideNavigationLayoutInfo(compactWidth, expandedWidth, widthProgress.value),
        ) {
            Column(
                modifier = modifier
                    .fillMaxHeight()
                    .background(containerColor)
                    .clipToBounds()
                    .windowInsetsPadding(windowInsets)
                    .width(compactWidth + (expandedWidth - compactWidth) * widthProgress.value)
                    .padding(vertical = StyledTheme.padding.small),
                horizontalAlignment = Alignment.Start,
            ) {
                if (mode == StyledSideNavigation.Mode.Expandable || (title != null && widthProgress.value > 0f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().heightIn(min = StyledTheme.sizes.minimumInteractiveSize),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (mode == StyledSideNavigation.Mode.Expandable) {
                            Box(
                                Modifier.width(titleStartPadding ?: compactWidth),
                                contentAlignment = Alignment.CenterStart,
                            ) {
                                Box(
                                    Modifier.wrapContentWidth(Alignment.Start, unbounded = true)
                                        .width(toggleIconCenter * 2),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    StyledIconButton(
                                        onClick = { onExpandedChange(!expanded) },
                                        role = StyledIconButton.Role.Tertiary,
                                        customization = StyledIconButtonDefaults.customize(
                                            CustomColors(
                                                filledBackground = CustomColors.Primary.filledBackground,
                                                filledForeground = CustomColors.Primary.filledForeground,
                                                textForeground = resolvedContent.asColorToken(),
                                            ),
                                        ),
                                        modifier = Modifier.semantics {
                                            contentDescription = if (expanded) "Collapse navigation" else "Expand navigation"
                                            stateDescription = if (expanded) "Expanded" else "Collapsed"
                                        },
                                    ) {
                                        Box(Modifier.clearAndSetSemantics {}) {
                                            toggleIcon(StyledSideNavigation.ToggleState(expanded, widthProgress.value))
                                        }
                                    }
                                }
                            }
                        }
                        if (title != null) {
                            ExpandedSlot(
                                width = if (mode == StyledSideNavigation.Mode.Expandable) {
                                    (expandedWidth - (titleStartPadding ?: compactWidth)).coerceAtLeast(0.dp)
                                } else expandedWidth,
                                heightFraction = 1f,
                                opacity = widthProgress.value,
                                interactive = expanded && widthProgress.value == 1f,
                                modifier = Modifier.weight(1f),
                                contentPadding = if (titleStartPadding != null) PaddingValues(
                                    start = if (mode == StyledSideNavigation.Mode.Expandable) 0.dp else titleStartPadding,
                                    end = StyledTheme.padding.medium,
                                    top = StyledTheme.padding.medium,
                                    bottom = StyledTheme.padding.medium,
                                ) else PaddingValues(StyledTheme.padding.medium),
                                content = title,
                            )
                        }
                    }
                }
                if (header != null && heightProgress.value > 0f) {
                    ExpandedSlot(
                        expandedWidth, heightProgress.value, widthProgress.value,
                        expanded && widthProgress.value == 1f, content = header,
                    )
                }
                val items: @Composable ColumnScope.() -> Unit = {
                    Column(
                        Modifier.fillMaxWidth().selectableGroup(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(StyledTheme.spacing.small),
                        content = content,
                    )
                }
                if (scrollable) {
                    StyledVerticalScrollbarContainer(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        scrollState = scrollState,
                        showScrollbar = showScrollbar,
                        overlapScrollbar = true,
                    ) { Column(content = items) }
                } else {
                    Column(Modifier.weight(1f).fillMaxWidth().clipToBounds(), content = items)
                }
                if (footer != null && heightProgress.value > 0f) {
                    ExpandedSlot(
                        expandedWidth, heightProgress.value, widthProgress.value,
                        expanded && widthProgress.value == 1f,
                        bottomAnchored = true,
                        content = footer,
                    )
                }
            }
        }
    }
}

/** Measure the item at its target width, clipping only its visible viewport. */
@InternalComposeStyledApi
@Composable
fun FoundationSideNavigationItemFrame(
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    val width = LocalSideNavigationLayout.current?.expandedWidth ?: StyledSideNavigationDefaults.expandedWidth
    Layout(modifier = modifier.fillMaxWidth().clipToBounds(), content = content) { measurables, constraints ->
        val child = measurables.single().measure(
            Constraints.fixedWidth(width.roundToPx()).copy(maxHeight = constraints.maxHeight),
        )
        layout(constraints.constrainWidth(child.width), constraints.constrainHeight(child.height)) {
            child.placeRelative(0, 0)
        }
    }
}

/** Reveal a stable-width label without scaling or reflowing its content. */
@InternalComposeStyledApi
@Composable
fun FoundationSideNavigationLabel(
    expanded: Boolean,
    labelWidth: Dp? = null,
    content: @Composable () -> Unit,
) {
    val info = LocalSideNavigationLayout.current
    val progress = info?.expansionProgress ?: if (expanded) 1f else 0f
    val width = labelWidth ?: ((info?.expandedWidth ?: StyledSideNavigationDefaults.expandedWidth) -
        (info?.compactWidth ?: StyledSideNavigationDefaults.compactWidth)).coerceAtLeast(0.dp)
    Layout(modifier = Modifier.clipToBounds(), content = content) { measurables, constraints ->
        val children = measurables.map {
            it.measure(Constraints.fixedWidth(width.roundToPx()).copy(maxHeight = constraints.maxHeight))
        }
        val height = children.maxOfOrNull { it.height } ?: 0
        layout(constraints.constrainWidth((width.roundToPx() * progress).toInt()), if (width.value > 0f) height else 0) {
            children.forEach { it.placeRelative(0, 0) }
        }
    }
}

/** Keep compact labels bounded by the icon column rather than the expanded item. */
@InternalComposeStyledApi
@Composable
fun FoundationSideNavigationCompactLabel(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val info = LocalSideNavigationLayout.current
    val width = (info?.compactWidth ?: StyledSideNavigationDefaults.compactWidth) - StyledTheme.padding.small * 2
    Layout(
        modifier = modifier.clipToBounds(),
        content = content,
    ) { measurables, constraints ->
        val children = measurables.map {
            it.measure(Constraints(
                maxWidth = minOf(width.roundToPx().coerceAtLeast(0), constraints.maxWidth),
                maxHeight = constraints.maxHeight,
            ))
        }
        val height = children.maxOfOrNull { it.height } ?: 0
        layout(
            constraints.constrainWidth(children.maxOfOrNull { it.width } ?: 0),
            constraints.constrainHeight(height),
        ) {
            children.forEach { it.placeRelative(0, 0) }
        }
    }
}

@Composable
private fun ExpandedSlot(
    width: Dp,
    heightFraction: Float,
    opacity: Float,
    interactive: Boolean,
    modifier: Modifier = Modifier,
    bottomAnchored: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(StyledTheme.padding.medium),
    content: @Composable () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    var hasFocus by remember { mutableStateOf(false) }
    LaunchedEffect(interactive) {
        if (!interactive && hasFocus) focusManager.clearFocus()
    }
    Layout(
        modifier = modifier.fillMaxWidth().clipToBounds()
            .then(if (interactive) Modifier else Modifier.clearAndSetSemantics {})
            .onFocusChanged { hasFocus = it.hasFocus }
            .focusProperties { onEnter = { if (!interactive) cancelFocusChange() } }
            .focusGroup()
            .pointerInput(interactive) {
                if (!interactive) {
                    awaitPointerEventScope {
                        while (true) {
                            awaitPointerEvent(PointerEventPass.Initial).changes.forEach { it.consume() }
                        }
                    }
                }
            }
            .graphicsLayer { alpha = opacity },
        content = { Box(Modifier.padding(contentPadding), contentAlignment = Alignment.CenterStart) { content() } },
    ) { measurables, constraints ->
        // Measure at the target width: labels do not reflow as the frame narrows.
        val child = measurables.single().measure(
            Constraints.fixedWidth(width.roundToPx()).copy(maxHeight = constraints.maxHeight),
        )
        val height = constraints.constrainHeight((child.height * heightFraction).toInt())
        layout(constraints.constrainWidth(child.width), height) {
            child.placeRelative(0, if (bottomAnchored) height - child.height else 0)
        }
    }
}
