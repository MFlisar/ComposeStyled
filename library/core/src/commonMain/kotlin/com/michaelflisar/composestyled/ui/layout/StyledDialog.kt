package com.michaelflisar.composestyled.ui.layout

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledComponentWithDefaults
import com.michaelflisar.composestyled.registry.StyledKey
import com.michaelflisar.composestyled.runtime.StyledContext
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.internal.dialogButtons
import com.michaelflisar.composestyled.ui.state.DialogState

object StyledDialog {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledDialog")
    val DefaultsKey = StyledKey<Defaults>("StyledDialogDefaults")

    // -------------
    // 2) defaults
    // -------------

    @Stable
    data class Defaults(
        val shape: Shape,
        val sheetShape: Shape,
        val style: Style = Style.Dialog(),
        val maxWidth: Dp = 560.dp,
        val sheetMaxWidth: Dp = 640.dp,
        val toolbarMaxWidth: Dp = 960.dp,
        val maxHeightFraction: Float = 0.9f,
        val edgeMargin: Dp = 24.dp,
        val floatingInitialSize: DpSize = DpSize(720.dp, 480.dp),
        val floatingMinSize: DpSize = DpSize(320.dp, 240.dp),
    )

    // -------------
    // 3) roles / variants / context
    // -------------

    /** Concrete presentation; null configuration fields inherit the active registry defaults. */
    @Stable
    sealed interface Style {
        /** Content-height alert for confirmations, messages and short forms. */
        @Stable
        data class Dialog(
            val shape: Shape? = null,
            val maxWidth: Dp? = null,
            val maxHeightFraction: Float? = null,
            val edgeMargin: Dp? = null,
        ) : Style

        /** Bounded workspace with a toolbar for forms, pickers and previews. */
        @Stable
        data class ToolbarDialog(
            val shape: Shape? = null,
            val maxWidth: Dp? = null,
            val maxHeightFraction: Float? = null,
            val edgeMargin: Dp? = null,
            /** Allows title double-click or the toolbar action to expand this workspace edge-to-edge. */
            val expandToFullscreen: Boolean = false,
        ) : Style

        /** Full-window workspace for extensive workflows or compact screens. */
        @Stable
        data class Fullscreen(
            val animation: FullscreenAnimation = FullscreenAnimation.Fade,
            /** Enables a scroll-driven large-title header; null keeps the standard toolbar. */
            val expandableHeader: ExpandableHeader? = null,
        ) : Style {
            /** Optional configuration for the scroll-driven fullscreen header. */
            @Stable
            data class ExpandableHeader(
                /**
                 * Total expanded height, including the regular top bar and window insets.
                 * Null derives the height from the expanded title and its default padding.
                 */
                val expandedHeight: Dp? = null,
                /** Background color while the header is collapsed; null uses the dialog container color. */
                val containerColor: Color? = null,
                /** Content color while the header is collapsed; null derives it from [containerColor]. */
                val contentColor: Color? = null,
                /** Background color while the header is expanded; null uses [containerColor]. */
                val expandedContainerColor: Color? = null,
                /**
                 * Content color while the header is expanded; null follows [contentColor] unless an
                 * [expandedContainerColor] is provided, in which case it is derived from that color.
                 */
                val expandedContentColor: Color? = null,
            )
        }

        /** Draggable contextual choices or actions, with an optionally fixed footer. */
        @Stable
        data class BottomSheet(
            val shape: Shape? = null,
            val maxWidth: Dp? = null,
            val maxHeightFraction: Float? = null,
            val edgeMargin: Dp? = null,
            val options: SheetOptions = SheetOptions(),
        ) : Style

        /** Non-modal, movable and resizable window hosted above the active StyledTheme content. */
        @Stable
        data class FloatingWindow(
            val initialSize: DpSize? = null,
            val minSize: DpSize? = null,
            val shape: Shape? = null,
            val edgeMargin: Dp? = null,
            val draggable: Boolean = true,
            val resizable: Boolean = true,
            val showMinimizeButton: Boolean = true,
            val showMaximizeButton: Boolean = false,
            val showCloseButton: Boolean = true,
            val icon: IconComposable? = null,
            val bubble: BubbleConfig = BubbleConfig(),
            /** Initial placement of the expanded window; defaults to the host center. */
            val initialPosition: FloatingPosition = FloatingPosition(),
            /** Optional initial parking position for the minimized bubble. */
            val initialBubblePosition: FloatingPosition? = null,
            /** Starts with the bubble shown instead of the expanded window. */
            val initiallyMinimized: Boolean = false,
            /** Outside-click handling and optional dimming; defaults to passing clicks through. */
            val backgroundMode: FloatingWindowBackgroundMode =
                FloatingWindowBackgroundMode.Interactive,
            /** Snaps a dragged minimized bubble to the nearest horizontal edge on release. */
            val snapToEdge: Boolean = true,
        ) : Style
    }

    /** Defines outside-click handling and optional dimming for an expanded floating window. */
    @Stable
    sealed interface FloatingWindowBackgroundMode {
        /** Passes outside clicks through to the host content without showing a scrim. */
        object Interactive : FloatingWindowBackgroundMode

        /** Consumes outside clicks without changing the window state. */
        @Stable
        data class Block(val showScrim: Boolean = false) : FloatingWindowBackgroundMode

        /** Minimizes the window on outside clicks without dismissing it. */
        @Stable
        data class Minimize(val showScrim: Boolean = false) : FloatingWindowBackgroundMode

        /** Dismisses the dialog on outside clicks. */
        @Stable
        data class Dismiss(val showScrim: Boolean = false) : FloatingWindowBackgroundMode
    }

    /** Alignment-based placement relative to the host's available area, followed by a dp offset. */
    @Stable
    data class FloatingPosition(
        val alignment: Alignment = Alignment.Center,
        val offset: DpOffset = DpOffset.Zero,
    )

    enum class Context : StyledContext { DialogAction, FullscreenAction, BottomSheetAction }

    /** Window-level transition used when a fullscreen dialog is shown or dismissed. */
    @Stable
    sealed interface FullscreenAnimation {
        object Fade : FullscreenAnimation

        @Immutable
        data class Slide(
            val direction: SlideDirection = SlideDirection.FromBottom,
        ) : FullscreenAnimation

        enum class SlideDirection { FromLeft, FromRight, FromTop, FromBottom }
    }

    enum class ActionSlot { Positive, Negative, Neutral }

    @Immutable
    data class ActionContext(
        val context: Context,
        val slot: ActionSlot,
    ) : StyledContext

    /** Auto delegates to the theme at the action's position; Icon requires an icon. */
    enum class ButtonPresentation { Auto, Text, Icon }

    /** The callback runs before optional dismissal. Null inherits the dialog's policy. */
    @Stable
    class Button private constructor(
        val onClick: () -> Unit,
        val enabled: Boolean,
        val dismissOnClick: Boolean?,
        val label: String?,
        val icon: IconComposable?,
        val presentation: ButtonPresentation,
        val variant: StyledButton.Variant,
        val content: @Composable RowScope.() -> Unit,
    ) {
        /** Custom content is rendered unchanged as a text-style action, without automatic icons. */
        constructor(
            onClick: () -> Unit,
            enabled: Boolean = true,
            dismissOnClick: Boolean? = null,
            content: @Composable RowScope.() -> Unit,
        ) : this(
            onClick, enabled, dismissOnClick, null, null, ButtonPresentation.Text,
            StyledButton.Variant.Auto, content,
        )

        /** Custom content can opt into a fixed visual variant while retaining the trailing content slot. */
        constructor(
            onClick: () -> Unit,
            variant: StyledButton.Variant,
            enabled: Boolean = true,
            dismissOnClick: Boolean? = null,
            content: @Composable RowScope.() -> Unit,
        ) : this(
            onClick, enabled, dismissOnClick, null, null, ButtonPresentation.Text, variant, content,
        )

        /** Label also supplies the accessible name and tooltip when rendered as an icon. */
        constructor(
            onClick: () -> Unit,
            label: String,
            icon: IconComposable? = null,
            presentation: ButtonPresentation = ButtonPresentation.Auto,
            enabled: Boolean = true,
            dismissOnClick: Boolean? = null,
            variant: StyledButton.Variant = StyledButton.Variant.Auto,
        ) : this(
            onClick, enabled, dismissOnClick, label, icon, presentation, variant,
            content = { StyledText(label) },
        ) {
            require(label.isNotBlank()) { "Dialog button label must not be blank" }
            require(presentation != ButtonPresentation.Icon || icon != null) {
                "Dialog button presentation Icon requires an icon"
            }
        }

        internal fun withOnClick(onClick: () -> Unit): Button =
            Button(onClick, enabled, dismissOnClick, label, icon, presentation, variant, content)
    }

    @Stable
    data class Buttons(
        val positive: Button? = null,
        val negative: Button? = null,
        val neutral: Button? = null,
    ) {
        val isEmpty: Boolean
            get() = positive == null && negative == null && neutral == null
    }

    @Immutable
    data class Properties(
        val dismissOnButtonClick: Boolean = true,
        val dismissOnBackPress: Boolean = true,
        /** Applies only to modal styles with an outside area. */
        val dismissOnClickOutside: Boolean = true,
    )

    @Immutable
    data class SheetOptions(
        /** Pins actions until the padded title or top corners reach the footer; reserves remaining navigation insets. */
        val fixedButtons: Boolean = true,
        val gesturesEnabled: Boolean = true,
        val dismissOnSwipe: Boolean = true,
        val initiallyExpanded: Boolean = false,
        val peekHeightFraction: Float = 0.5f,
        /** Allows the expanded sheet to fill the window and transition its actions into a top bar. */
        val expandToFullscreen: Boolean = false,
    ) {
        init {
            require(peekHeightFraction > 0f && peekHeightFraction <= 1f) {
                "peekHeightFraction must be in (0, 1]; it is clamped to the available sheet height"
            }
        }
    }

    /** Optional minimized-window appearance; null fields preserve the default bubble appearance. */
    @Stable
    data class BubbleConfig(
        val containerColor: Color? = null,
        val contentColor: Color? = null,
        /** A 1 dp outline, faded out when restoring the window. Null means no border. */
        val borderColor: Color? = null,
        /** Defaults to the window shape. RoundedCornerShape pairs animate; other shapes switch directly. */
        val shape: Shape? = null,
        /** Null inherits the window's 12 dp shadow; zero disables the bubble shadow. */
        val shadowElevation: Dp? = null,
    ) {
        init {
            require(shadowElevation == null ||
                (shadowElevation.value.isFinite() && shadowElevation >= 0.dp)) {
                "Bubble shadowElevation must be finite and non-negative"
            }
        }
    }

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation>, StyledComponentWithDefaults<Defaults> {
        override val key: StyledKey<Implementation>
            get() = Key
        override val defaultsKey: StyledKey<Defaults>
            get() = DefaultsKey

        @Composable
        fun Render(
            style: Style,
            onDismissRequest: () -> Unit,
            onDismissRequestWithoutAnimation: () -> Unit,
            modifier: Modifier,
            properties: Properties,
            title: (@Composable () -> Unit)?,
            buttons: Buttons,
            visible: Boolean,
            isVisible: () -> Boolean,
            content: @Composable ColumnScope.(PaddingValues) -> Unit,
        )
    }
}

// -------------------------
// defaults
// -------------------------

object StyledDialogDefaults {
    val defaults: StyledDialog.Defaults
        @Composable get() = LocalStyledRegistry.current[StyledDialog.DefaultsKey]

    val style: StyledDialog.Style
        @Composable get() = defaults.style
}

// -------------------------
// public API
// -------------------------

/** Displays a state-controlled dialog using the selected presentation style and active theme renderer. */
@Composable
@OptIn(InternalComposeStyledApi::class)
fun StyledDialog(
    state: DialogState,
    modifier: Modifier = Modifier,
    style: StyledDialog.Style = StyledDialogDefaults.style,
    onDismissRequest: () -> Unit = state::dismiss,
    properties: StyledDialog.Properties = StyledDialog.Properties(),
    title: (@Composable () -> Unit)? = null,
    buttons: StyledDialog.Buttons = StyledDialog.Buttons(),
    content: @Composable ColumnScope.(PaddingValues) -> Unit,
) {
    val visible = state.visible
    val skipExitAnimation = remember(state) { mutableStateOf(false) }
    LaunchedEffect(state, visible) {
        if (visible) skipExitAnimation.value = false
    }
    val animateExit = (
        style is StyledDialog.Style.BottomSheet || style is StyledDialog.Style.Fullscreen
    ) && !skipExitAnimation.value
    val retained = com.michaelflisar.composestyled.ui.internal.rememberDialogCompositionVisible(
        state = state,
        animateExit = animateExit,
    )
    val dismissWithoutAnimation = {
        skipExitAnimation.value = true
        onDismissRequest()
        if (state.visible) skipExitAnimation.value = false
    }
    if (retained) {
        val registry = LocalStyledRegistry.current
        val implementation = registry[StyledDialog.Key]
        val resolvedButtons = dialogButtons(state, properties, buttons, onDismissRequest)
        if (style is StyledDialog.Style.FloatingWindow) {
            val host = checkNotNull(LocalFloatingDialogHost.current) {
                "StyledDialog.Style.FloatingWindow requires StyledTheme or StyledFloatingHost"
            }
            val entry = remember(state) { FloatingDialogEntry() }
            // The overlay host may belong to an outer theme; keep the caller's theme locals.
            val compositionLocalContext = currentCompositionLocalContext
            val render: @Composable () -> Unit = {
                CompositionLocalProvider(compositionLocalContext) {
                    CompositionLocalProvider(LocalFloatingDialogEntry provides entry) {
                        implementation.Render(
                            style, onDismissRequest, dismissWithoutAnimation, modifier, properties,
                            title, resolvedButtons, visible, { state.visible }, content,
                        )
                    }
                }
            }
            val currentRender = rememberUpdatedState(render)
            SideEffect { entry.content = { currentRender.value() } }
            DisposableEffect(host, entry) {
                host.register(entry)
                onDispose { host.unregister(entry) }
            }
        } else {
            implementation.Render(
                style, onDismissRequest, dismissWithoutAnimation, modifier, properties,
                title, resolvedButtons, visible, { state.visible }, content,
            )
        }
    }
}
