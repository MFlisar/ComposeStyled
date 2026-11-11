package com.michaelflisar.composestyled.foundation.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.platform.SystemBarStyleEffect
import com.michaelflisar.composestyled.platform.systemBarIconStyleFor
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledCenterAlignedTopBar
import com.michaelflisar.composestyled.ui.components.StyledExpandableTopBar
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.layout.StyledDialogDefaults
import com.michaelflisar.composestyled.ui.components.StyledSurface
import com.michaelflisar.composestyled.ui.components.StyledTopBar
import com.michaelflisar.composestyled.ui.state.rememberStyledExpandableTopBarScrollBehavior
import com.michaelflisar.composestyled.ui.internal.DialogActions
import com.michaelflisar.composestyled.ui.internal.DialogButton
import com.michaelflisar.composestyled.ui.internal.DIALOG_VISIBILITY_ANIMATION_DURATION_MILLIS
import com.michaelflisar.composestyled.ui.internal.DialogScrimEffect
import com.michaelflisar.composestyled.ui.internal.edgeToEdgeDialogProperties
import com.michaelflisar.composestyled.ui.internal.dialogInput

@InternalComposeStyledApi
abstract class FoundationDialog : StyledDialog.Implementation {

    protected abstract val verticalAlertActions: Boolean
    protected abstract val fullscreenActionsInToolbar: Boolean
    protected open val centeredTitle: Boolean = false
    protected open val positiveActionFirst: Boolean = false
    protected open val presentationMapping: (StyledDialog.ActionSlot) -> StyledDialog.ButtonPresentation =
        { StyledDialog.ButtonPresentation.Text }
    protected open val alertPadding: Dp
        @Composable get() = StyledTheme.spacing.large
    protected open val alertTitleSpacing: Dp
        @Composable get() = StyledTheme.spacing.medium
    protected open val alertActionSpacing: Dp
        @Composable get() = StyledTheme.spacing.medium
    protected open val actionSpacing: Dp
        @Composable get() = StyledTheme.spacing.small
    protected open val actionRowSpacing: Dp
        @Composable get() = actionSpacing
    protected open val stackPositiveActionFirst: Boolean = false
    /** Gap between scrolled sheet content and actions in a fixed bottom-sheet footer. */
    protected open val sheetFooterTopPadding: Dp
        @Composable get() = StyledTheme.spacing.large
    protected open val alertTitleStyle: TextStyle
        @Composable get() = StyledTheme.typography.titleLarge
    protected open val containerColor: Color
        @Composable get() = StyledTheme.colorScheme.surface

    @Composable
    final override fun Render(
        style: StyledDialog.Style,
        onDismissRequest: () -> Unit,
        onDismissRequestWithoutAnimation: () -> Unit,
        modifier: Modifier,
        properties: StyledDialog.Properties,
        title: (@Composable () -> Unit)?,
        buttons: StyledDialog.Buttons,
        visible: Boolean,
        isVisible: () -> Boolean,
        content: @Composable ColumnScope.(PaddingValues) -> Unit,
    ) {
        val defaults = StyledDialogDefaults.defaults
        val toolbarExpanded = rememberSaveable { mutableStateOf(false) }
        val canExpandToolbar = style is StyledDialog.Style.ToolbarDialog && style.expandToFullscreen
        val expandedToolbar = canExpandToolbar && toolbarExpanded.value
        LaunchedEffect(canExpandToolbar) {
            if (!canExpandToolbar) toolbarExpanded.value = false
        }
        val toggleToolbarExpansion = { toolbarExpanded.value = !toolbarExpanded.value }
        if (style is StyledDialog.Style.FloatingWindow) {
            val initialSize = style.initialSize ?: defaults.floatingInitialSize
            val minSize = style.minSize ?: defaults.floatingMinSize
            FoundationFloatingDialog(
                onDismissRequest = onDismissRequest,
                modifier = modifier,
                properties = properties,
                style = style,
                initialSize = initialSize,
                minSize = minSize,
                initiallyMinimized = style.initiallyMinimized,
                shape = style.shape ?: defaults.shape,
                edgeMargin = style.edgeMargin ?: defaults.edgeMargin,
                containerColor = containerColor,
                title = title,
                buttons = buttons,
                positiveActionFirst = positiveActionFirst,
                actionSpacing = actionSpacing,
                actionRowSpacing = actionRowSpacing,
                stackPositiveActionFirst = stackPositiveActionFirst,
                content = content,
            )
            return
        }
        val shape: Shape
        val maxWidth: Dp
        val maxHeightFraction: Float
        val edgeMargin: Dp
        when (style) {
            is StyledDialog.Style.Dialog -> {
                shape = style.shape ?: defaults.shape
                maxWidth = style.maxWidth ?: defaults.maxWidth
                maxHeightFraction = style.maxHeightFraction ?: defaults.maxHeightFraction
                edgeMargin = style.edgeMargin ?: defaults.edgeMargin
            }
            is StyledDialog.Style.ToolbarDialog -> {
                shape = style.shape ?: defaults.shape
                maxWidth = style.maxWidth ?: defaults.toolbarMaxWidth
                maxHeightFraction = style.maxHeightFraction ?: defaults.maxHeightFraction
                edgeMargin = style.edgeMargin ?: defaults.edgeMargin
            }
            is StyledDialog.Style.BottomSheet -> {
                shape = style.shape ?: defaults.sheetShape
                maxWidth = style.maxWidth ?: defaults.sheetMaxWidth
                maxHeightFraction = style.maxHeightFraction ?: defaults.maxHeightFraction
                edgeMargin = style.edgeMargin ?: defaults.edgeMargin
            }
            is StyledDialog.Style.Fullscreen -> {
                shape = RectangleShape
                maxWidth = Dp.Infinity
                maxHeightFraction = 1f
                edgeMargin = 0.dp
            }
            is StyledDialog.Style.FloatingWindow -> error("FloatingWindow handled above")
        }
        requireModalBounds(maxWidth, maxHeightFraction, edgeMargin)
        if (style is StyledDialog.Style.Dialog) {
            Modal(
                onDismissRequest, modifier, shape, maxWidth, maxHeightFraction, edgeMargin,
                properties, title, buttons, content,
            )
        } else {
            Dialog(
                onDismissRequest = onDismissRequest,
                properties = if (
                    style is StyledDialog.Style.BottomSheet ||
                    style is StyledDialog.Style.Fullscreen ||
                    canExpandToolbar
                ) {
                    edgeToEdgeDialogProperties(
                        dismissOnBackPress = properties.dismissOnBackPress,
                        transparentScrim = style is StyledDialog.Style.Fullscreen,
                    )
                } else DialogProperties(
                    dismissOnBackPress = properties.dismissOnBackPress,
                    dismissOnClickOutside = false,
                    usePlatformDefaultWidth = false,
                ),
            ) {
                DialogScrimEffect(transparent = style is StyledDialog.Style.Fullscreen)
                if (style is StyledDialog.Style.BottomSheet) {
                    FoundationDialogSheet(
                        onDismissRequest, onDismissRequestWithoutAnimation,
                        modifier, shape, properties, style.options,
                        containerColor, title, buttons, visible, isVisible, content, modal = true,
                        verticalActions = verticalAlertActions,
                        fullscreenActionsInToolbar = fullscreenActionsInToolbar,
                        centeredTitle = centeredTitle,
                        presentationMapping = presentationMapping,
                        positiveActionFirst = positiveActionFirst,
                        actionSpacing = actionSpacing,
                        actionRowSpacing = actionRowSpacing,
                        stackPositiveActionFirst = stackPositiveActionFirst,
                        footerTopPadding = sheetFooterTopPadding,
                        maxWidth = maxWidth,
                        maxHeightFraction = maxHeightFraction,
                        edgeMargin = edgeMargin,
                    )
                } else {
                    FoundationDialogBounds(
                        maxWidth, maxHeightFraction, edgeMargin, properties, onDismissRequest,
                        fullscreen = style is StyledDialog.Style.Fullscreen || expandedToolbar,
                    ) { bounds ->
                        if (style is StyledDialog.Style.Fullscreen) {
                            val transition = remember { MutableTransitionState(false) }
                            LaunchedEffect(visible) { transition.targetState = visible }
                            AnimatedVisibility(
                                visibleState = transition,
                                modifier = bounds.then(modifier)
                                    .dialogInput(properties.dismissOnBackPress, onDismissRequest),
                                enter = fullscreenEnterTransition(style.animation),
                                exit = fullscreenExitTransition(style.animation),
                            ) {
                                Fullscreen(
                                    Modifier.fillMaxSize(), shape, title, buttons, content,
                                    edgeToEdge = true,
                                    expandableHeader = style.expandableHeader,
                                )
                            }
                        } else {
                            Fullscreen(
                                bounds.then(modifier).dialogInput(
                                    properties.dismissOnBackPress,
                                    onDismissRequest,
                                ),
                                shape, title, buttons, content,
                                edgeToEdge = expandedToolbar,
                                expandable = canExpandToolbar,
                                expanded = expandedToolbar,
                                onToggleExpanded = toggleToolbarExpansion,
                                animateBounds = canExpandToolbar,
                            )
                        }
                    }

                }
            }
        }
    }

    @Composable
    protected open fun Modal(
        onDismissRequest: () -> Unit,
        modifier: Modifier,
        shape: Shape,
        maxWidth: Dp,
        maxHeightFraction: Float,
        edgeMargin: Dp,
        properties: StyledDialog.Properties,
        title: (@Composable () -> Unit)?,
        buttons: StyledDialog.Buttons,
        content: @Composable ColumnScope.(PaddingValues) -> Unit,
    ) {
        Dialog(
            onDismissRequest,
            properties = DialogProperties(
                dismissOnBackPress = properties.dismissOnBackPress,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false,
            ),
        ) {
            FoundationDialogBounds(
                maxWidth, maxHeightFraction, edgeMargin, properties, onDismissRequest,
            ) { bounds ->
                Alert(
                    bounds.then(modifier).dialogInput(properties.dismissOnBackPress, onDismissRequest),
                    shape, title, buttons, content,
                )
            }
        }
    }

    @Composable
    protected open fun Alert(
        modifier: Modifier,
        shape: Shape,
        title: (@Composable () -> Unit)?,
        buttons: StyledDialog.Buttons,
        content: @Composable ColumnScope.(PaddingValues) -> Unit,
    ) {
        StyledSurface(
            modifier = modifier,
            shape = shape,
            color = containerColor,
            shadowElevation = 8.dp,
        ) {
            Column {
                if (title != null) {
                    Box(
                        Modifier.fillMaxWidth().padding(
                            start = alertPadding,
                            top = alertPadding,
                            end = alertPadding,
                            bottom = alertTitleSpacing,
                        ),
                        contentAlignment = if (centeredTitle) Alignment.Center else Alignment.CenterStart,
                    ) {
                        UpdateStyledThemeLocals(
                            textStyle = alertTitleStyle.copy(
                                textAlign = if (centeredTitle) TextAlign.Center else TextAlign.Start,
                            ),
                        ) {
                            title()
                        }
                    }
                }
                Column(
                    Modifier.weight(1f, fill = false).fillMaxWidth()
                        .padding(bottom = if (buttons.isEmpty) 0.dp else alertActionSpacing),
                ) {
                    content(PaddingValues(alertPadding))
                }
                DialogActions(
                    buttons, StyledDialog.Context.DialogAction,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = alertPadding)
                        .padding(bottom = alertPadding),
                    vertical = verticalAlertActions,
                    positiveFirst = positiveActionFirst,
                    itemSpacing = actionSpacing,
                    rowSpacing = actionRowSpacing,
                    stackPositiveFirst = stackPositiveActionFirst,
                )
            }
        }
    }

    @Composable
    @OptIn(ExperimentalFoundationApi::class)
    private fun Fullscreen(
        modifier: Modifier,
        shape: Shape,
        title: (@Composable () -> Unit)?,
        buttons: StyledDialog.Buttons,
        content: @Composable ColumnScope.(PaddingValues) -> Unit,
        edgeToEdge: Boolean,
        expandable: Boolean = false,
        expanded: Boolean = false,
        onToggleExpanded: () -> Unit = {},
        animateBounds: Boolean = false,
        expandableHeader: StyledDialog.Style.Fullscreen.ExpandableHeader? = null,
    ) {
        val expandableHeaderEnabled = expandableHeader != null && title != null
        val expandableHeaderBehavior = rememberStyledExpandableTopBarScrollBehavior()
        val headerContainerColor =
            expandableHeader?.containerColor ?: StyledTheme.colorScheme.surface
        val headerContentColor = expandableHeader?.contentColor
            ?: StyledTheme.contentColorFor(headerContainerColor)
        val expandedHeaderContainerColor =
            expandableHeader?.expandedContainerColor ?: headerContainerColor
        val expandedHeaderContentColor = expandableHeader?.expandedContentColor
            ?: if (expandableHeader?.expandedContainerColor == null) {
                headerContentColor
            } else {
                StyledTheme.contentColorFor(expandedHeaderContainerColor)
            }
        val statusBarContainerColor = if (expandableHeaderEnabled) {
            expandedHeaderContainerColor
        } else {
            StyledTheme.colorScheme.surface
        }
        val footerButtons = if (fullscreenActionsInToolbar) {
            StyledDialog.Buttons(neutral = buttons.neutral)
        } else buttons
        if (edgeToEdge) {
            SystemBarStyleEffect(
                statusBar = systemBarIconStyleFor(
                    statusBarContainerColor,
                    StyledTheme.colorScheme.background,
                ),
                navigationBar = systemBarIconStyleFor(containerColor, StyledTheme.colorScheme.background),
            )
        }
        StyledSurface(
            modifier = if (animateBounds) Modifier.animateContentSize(
                animationSpec = tween(DIALOG_VISIBILITY_ANIMATION_DURATION_MILLIS),
            ).then(modifier) else modifier,
            shape = if (expanded) RectangleShape else shape,
            color = containerColor,
        ) {
            Column(
                Modifier.fillMaxSize().then(
                    if (expandableHeaderEnabled) {
                        Modifier.nestedScroll(expandableHeaderBehavior.nestedScrollConnection)
                    } else {
                        Modifier
                    },
                ),
            ) {
                val navigation: @Composable () -> Unit = {
                    if (fullscreenActionsInToolbar) {
                        DialogButton(
                            buttons.negative, StyledDialog.ActionSlot.Negative,
                            StyledDialog.Context.FullscreenAction,
                            autoPresentation = presentationMapping(StyledDialog.ActionSlot.Negative),
                        )
                    }
                }
                val actions: @Composable RowScope.() -> Unit = {
                    if (fullscreenActionsInToolbar) {
                        DialogButton(
                            buttons.positive, StyledDialog.ActionSlot.Positive,
                            StyledDialog.Context.FullscreenAction,
                            autoPresentation = presentationMapping(StyledDialog.ActionSlot.Positive),
                        )
                    }
                    if (expandable && (!expanded || title == null)) {
                        DialogButton(
                            StyledDialog.Button(
                                onClick = onToggleExpanded,
                                label = if (expanded) "Restore" else "Expand",
                                dismissOnClick = false,
                            ),
                            StyledDialog.ActionSlot.Neutral,
                            StyledDialog.Context.FullscreenAction,
                        )
                    }
                }
                val renderedTitle: @Composable () -> Unit = {
                    if (expandable && title != null) {
                        val actionLabel = if (expanded) "Restore fullscreen dialog" else "Expand fullscreen dialog"
                        Box(
                            Modifier.combinedClickable(
                                onClick = {},
                                onDoubleClick = onToggleExpanded,
                            ).semantics {
                                customActions = listOf(
                                    CustomAccessibilityAction(actionLabel) {
                                        onToggleExpanded()
                                        true
                                    },
                                )
                            },
                        ) {
                            title.invoke()
                        }
                    } else {
                        title?.invoke()
                    }
                }
                if (expandableHeaderEnabled) {
                    StyledExpandableTopBar(
                        behavior = expandableHeaderBehavior,
                        expandedHeight = expandableHeader?.expandedHeight,
                        containerColor = headerContainerColor,
                        contentColor = headerContentColor,
                        expandedContainerColor = expandedHeaderContainerColor,
                        expandedContentColor = expandedHeaderContentColor,
                        navigationIcon = navigation,
                        actions = actions,
                        title = renderedTitle,
                    )
                } else if (centeredTitle) {
                    StyledCenterAlignedTopBar(
                        navigationIcon = navigation,
                        actions = actions,
                        title = renderedTitle,
                    )
                } else {
                    StyledTopBar(
                        navigationIcon = navigation,
                        actions = actions,
                        title = renderedTitle,
                    )
                }
                val layoutDirection = LocalLayoutDirection.current
                val safePadding = WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Horizontal,
                ).asPaddingValues()
                val safeBottomPadding = if (footerButtons.isEmpty) {
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
                        .asPaddingValues().calculateBottomPadding()
                } else 0.dp
                val largePadding = StyledTheme.spacing.large
                Column(Modifier.weight(1f).fillMaxWidth()) {
                    content(
                        PaddingValues(
                            start = largePadding + safePadding.calculateStartPadding(layoutDirection),
                            top = largePadding,
                            end = largePadding + safePadding.calculateEndPadding(layoutDirection),
                            bottom = largePadding + safeBottomPadding,
                        ),
                    )
                }
                if (!footerButtons.isEmpty) {
                    DialogActions(
                        footerButtons, StyledDialog.Context.FullscreenAction,
                        Modifier.windowInsetsPadding(
                            WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal),
                        ).padding(
                            start = StyledTheme.spacing.large,
                            end = StyledTheme.spacing.large,
                            bottom = StyledTheme.spacing.large,
                        ),
                        positiveFirst = positiveActionFirst,
                        itemSpacing = actionSpacing,
                        rowSpacing = actionRowSpacing,
                        stackPositiveFirst = stackPositiveActionFirst,
                    )
                }
            }
        }
    }

    private fun fullscreenEnterTransition(
        animation: StyledDialog.FullscreenAnimation,
    ): EnterTransition {
        val duration = DIALOG_VISIBILITY_ANIMATION_DURATION_MILLIS
        return when (animation) {
            StyledDialog.FullscreenAnimation.Fade -> fadeIn(tween(durationMillis = duration))
            is StyledDialog.FullscreenAnimation.Slide -> when (animation.direction) {
                StyledDialog.FullscreenAnimation.SlideDirection.FromLeft ->
                    slideInHorizontally(animationSpec = tween(durationMillis = duration)) { -it }
                StyledDialog.FullscreenAnimation.SlideDirection.FromRight ->
                    slideInHorizontally(animationSpec = tween(durationMillis = duration)) { it }
                StyledDialog.FullscreenAnimation.SlideDirection.FromTop ->
                    slideInVertically(animationSpec = tween(durationMillis = duration)) { -it }
                StyledDialog.FullscreenAnimation.SlideDirection.FromBottom ->
                    slideInVertically(animationSpec = tween(durationMillis = duration)) { it }
            }
        }
    }

    private fun fullscreenExitTransition(
        animation: StyledDialog.FullscreenAnimation,
    ): ExitTransition {
        val duration = DIALOG_VISIBILITY_ANIMATION_DURATION_MILLIS
        return when (animation) {
            StyledDialog.FullscreenAnimation.Fade -> fadeOut(tween(durationMillis = duration))
            is StyledDialog.FullscreenAnimation.Slide -> when (animation.direction) {
                StyledDialog.FullscreenAnimation.SlideDirection.FromLeft ->
                    slideOutHorizontally(animationSpec = tween(durationMillis = duration)) { -it }
                StyledDialog.FullscreenAnimation.SlideDirection.FromRight ->
                    slideOutHorizontally(animationSpec = tween(durationMillis = duration)) { it }
                StyledDialog.FullscreenAnimation.SlideDirection.FromTop ->
                    slideOutVertically(animationSpec = tween(durationMillis = duration)) { -it }
                StyledDialog.FullscreenAnimation.SlideDirection.FromBottom ->
                    slideOutVertically(animationSpec = tween(durationMillis = duration)) { it }
            }
        }
    }
}
