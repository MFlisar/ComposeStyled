package com.michaelflisar.demo.demos.showcase.areas

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.UpdateStyledCustomization
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.adaptive.StyledAdaptive
import com.michaelflisar.composestyled.classes.toIconComposable
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledSwitch
import com.michaelflisar.composestyled.ui.layout.StyledBottomSheet
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.layout.StyledFlowRow
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.state.rememberDialogState
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoDialogs(baseId: Int, expandState: ExpandState) {
    RegionContainer(title = "Dialogs", id = baseId + 1, expanded = expandState) {
        val dialog = rememberDialogState()
        val fullscreen = rememberDialogState()
        val toolbarDialog = rememberDialogState()
        val adaptive = rememberDialogState()
        val modalSheet = rememberDialogState()
        val inlineSheet = rememberDialogState()
        val floatingWindow = rememberDialogState()
        val dataDialog = rememberDialogState(null as String?, StringSaver)
        val customizedDialog = rememberDialogState()
        var fixedButtons by rememberSaveable { mutableStateOf(true) }
        var dismissOnButtonClick by rememberSaveable { mutableStateOf(true) }
        var dismissOnBackPress by rememberSaveable { mutableStateOf(true) }
        var dismissOnClickOutside by rememberSaveable { mutableStateOf(true) }
        var dismissOnSwipe by rememberSaveable { mutableStateOf(true) }
        var gesturesEnabled by rememberSaveable { mutableStateOf(true) }
        var expandSheetToFullscreen by rememberSaveable { mutableStateOf(false) }
        var expandToolbarToFullscreen by rememberSaveable { mutableStateOf(false) }
        var fullscreenAnimation by rememberSaveable(stateSaver = FullscreenAnimationSaver) {
            mutableStateOf<StyledDialog.FullscreenAnimation>(StyledDialog.FullscreenAnimation.Fade)
        }
        var fullscreenSlideDirection by rememberSaveable {
            mutableStateOf(StyledDialog.FullscreenAnimation.SlideDirection.FromRight)
        }
        var positiveEnabled by rememberSaveable { mutableStateOf(true) }
        var narrowDialog by rememberSaveable { mutableStateOf(false) }
        var shortContent by rememberSaveable { mutableStateOf(false) }
        var showTitle by rememberSaveable { mutableStateOf(true) }
        var visibleActionCount by rememberSaveable { mutableStateOf(3) }
        var longActionLabels by rememberSaveable { mutableStateOf(false) }
        var floatingDraggable by rememberSaveable { mutableStateOf(true) }
        var floatingResizable by rememberSaveable { mutableStateOf(true) }
        var floatingInitiallyMinimized by rememberSaveable { mutableStateOf(false) }
        var floatingWindowMinimizeOnOutsideClick by rememberSaveable { mutableStateOf(false) }
        var floatingWindowScrim by rememberSaveable { mutableStateOf(false) }
        var customizeBubble by rememberSaveable { mutableStateOf(false) }
        var actionPresentation by rememberSaveable { mutableStateOf(StyledDialog.ButtonPresentation.Auto) }
        var includeActionIcons by rememberSaveable { mutableStateOf(true) }
        var actionCount by rememberSaveable { mutableStateOf(0) }
        val useIcons =
            includeActionIcons || actionPresentation == StyledDialog.ButtonPresentation.Icon
        val buttons = StyledDialog.Buttons(
            positive = if (visibleActionCount > 0) StyledDialog.Button(
                onClick = { actionCount++ },
                label = if (longActionLabels) "Save all document changes" else "Save",
                icon = if (useIcons) Icons.Default.Check.toIconComposable() else null,
                presentation = actionPresentation,
                enabled = positiveEnabled,
            ) else null,
            negative = if (visibleActionCount > 1) StyledDialog.Button(
                onClick = {},
                label = if (longActionLabels) "Discard all changes" else "Cancel",
                icon = if (useIcons) Icons.Default.Close.toIconComposable() else null,
                presentation = actionPresentation,
            ) else null,
            neutral = if (visibleActionCount > 2) StyledDialog.Button(
                onClick = { actionCount++ },
                dismissOnClick = false
            ) {
                StyledText("Apply")
            } else null,
        )
        val properties = StyledDialog.Properties(
            dismissOnButtonClick = dismissOnButtonClick,
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside,
        )
        val options = StyledDialog.SheetOptions(
            fixedButtons = fixedButtons,
            expandToFullscreen = expandSheetToFullscreen,
            dismissOnSwipe = dismissOnSwipe,
            gesturesEnabled = gesturesEnabled,
        )

        SubTitle("Settings")
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {

            StyledButton(onClick = {
                actionPresentation = when (actionPresentation) {
                    StyledDialog.ButtonPresentation.Auto -> StyledDialog.ButtonPresentation.Text
                    StyledDialog.ButtonPresentation.Text -> StyledDialog.ButtonPresentation.Icon
                    StyledDialog.ButtonPresentation.Icon -> StyledDialog.ButtonPresentation.Auto
                }

            }) {
                StyledText("Action presentation: $actionPresentation")
            }
            StyledButton(onClick = {
                fullscreenAnimation = when (fullscreenAnimation) {
                    StyledDialog.FullscreenAnimation.Fade ->
                        StyledDialog.FullscreenAnimation.Slide(fullscreenSlideDirection)

                    is StyledDialog.FullscreenAnimation.Slide ->
                        StyledDialog.FullscreenAnimation.Fade
                }
            }) {
                val animationName = if (
                    fullscreenAnimation is StyledDialog.FullscreenAnimation.Slide
                ) "Slide" else "Fade"
                StyledText("Fullscreen animation: $animationName")
            }
            if (fullscreenAnimation is StyledDialog.FullscreenAnimation.Slide) {
                StyledButton(onClick = {
                    fullscreenSlideDirection = when (fullscreenSlideDirection) {
                        StyledDialog.FullscreenAnimation.SlideDirection.FromLeft ->
                            StyledDialog.FullscreenAnimation.SlideDirection.FromRight

                        StyledDialog.FullscreenAnimation.SlideDirection.FromRight ->
                            StyledDialog.FullscreenAnimation.SlideDirection.FromTop

                        StyledDialog.FullscreenAnimation.SlideDirection.FromTop ->
                            StyledDialog.FullscreenAnimation.SlideDirection.FromBottom

                        StyledDialog.FullscreenAnimation.SlideDirection.FromBottom ->
                            StyledDialog.FullscreenAnimation.SlideDirection.FromLeft
                    }
                    fullscreenAnimation =
                        StyledDialog.FullscreenAnimation.Slide(fullscreenSlideDirection)
                }) {
                    StyledText("Slide direction: $fullscreenSlideDirection")
                }
            }
            StyledSwitch(
                checked = includeActionIcons,
                enabled = actionPresentation != StyledDialog.ButtonPresentation.Icon,
                onCheckedChange = { includeActionIcons = it },
            ) {
                StyledText("Provide action icons (required in Icon mode)")
            }
            StyledSwitch(checked = fixedButtons, onCheckedChange = { fixedButtons = it }) {
                StyledText("Pin sheet buttons until the padded title / top corners reach them")
            }
            StyledSwitch(
                checked = dismissOnButtonClick,
                onCheckedChange = { dismissOnButtonClick = it }) {
                StyledText("Dismiss after button clicks (Apply always stays open)")
            }
            StyledSwitch(
                checked = dismissOnBackPress,
                onCheckedChange = { dismissOnBackPress = it }) {
                StyledText("Dismiss on Back / Escape")
            }
            StyledSwitch(
                checked = dismissOnClickOutside,
                onCheckedChange = { dismissOnClickOutside = it }) {
                StyledText("Dismiss on outside clicks (modal only)")
            }
            StyledSwitch(checked = dismissOnSwipe, onCheckedChange = { dismissOnSwipe = it }) {
                StyledText("Dismiss sheets by swiping down")
            }
            StyledSwitch(checked = gesturesEnabled, onCheckedChange = { gesturesEnabled = it }) {
                StyledText("Enable sheet dragging and handle clicks (peek / expanded)")
            }
            StyledSwitch(
                checked = expandSheetToFullscreen,
                onCheckedChange = { expandSheetToFullscreen = it },
            ) {
                StyledText("Allow fullscreen expansion and move actions to the top bar where supported")
            }
            StyledSwitch(
                checked = expandToolbarToFullscreen,
                onCheckedChange = { expandToolbarToFullscreen = it },
            ) {
                StyledText("Allow toolbar dialogs to expand to fullscreen")
            }
            StyledSwitch(checked = positiveEnabled, onCheckedChange = { positiveEnabled = it }) {
                StyledText("Enable Save")
            }
            StyledSwitch(checked = narrowDialog, onCheckedChange = { narrowDialog = it }) {
                StyledText("Override dialog / sheet max width to 400 dp")
            }
            StyledSwitch(checked = shortContent, onCheckedChange = { shortContent = it }) {
                StyledText("Use short workspace / sheet content")
            }
            StyledSwitch(checked = showTitle, onCheckedChange = { showTitle = it }) {
                StyledText("Show dialog title")
            }
            StyledButton(onClick = { visibleActionCount = (visibleActionCount + 1) % 4 }) {
                StyledText("Dialog actions: $visibleActionCount")
            }
            StyledSwitch(checked = longActionLabels, onCheckedChange = { longActionLabels = it }) {
                StyledText("Use long action labels (Text mode shows wrapping)")
            }
            StyledSwitch(
                checked = floatingDraggable,
                onCheckedChange = { floatingDraggable = it }) {
                StyledText("Allow floating window dragging")
            }
            StyledSwitch(
                checked = floatingResizable,
                onCheckedChange = { floatingResizable = it }) {
                StyledText("Allow floating window resizing")
            }
            StyledSwitch(
                checked = floatingInitiallyMinimized,
                onCheckedChange = { floatingInitiallyMinimized = it },
            ) {
                StyledText("Start floating window minimized")
            }
            StyledSwitch(
                checked = floatingWindowMinimizeOnOutsideClick,
                onCheckedChange = {
                    floatingWindowMinimizeOnOutsideClick = it
                    if (!it) {
                        floatingWindowScrim = false
                    }
                },
            ) {
                StyledText("Minimize floating window on outside click")
            }
            if (floatingWindowMinimizeOnOutsideClick) {
                StyledSwitch(
                    checked = floatingWindowScrim,
                    onCheckedChange = { floatingWindowScrim = it },
                ) {
                    StyledText("Show scrim for outside-click behavior")
                }
            }
            StyledSwitch(checked = customizeBubble, onCheckedChange = { customizeBubble = it }) {
                StyledText("Customize minimized bubble colors, border, shape and elevation")
            }
        }

        SubTitle("Dialogs and sheets")
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {
            StyledButton(onClick = dialog::show) { StyledText("Modal dialog") }
            StyledButton(onClick = fullscreen::show) { StyledText("Fullscreen dialog") }
            StyledButton(onClick = toolbarDialog::show) { StyledText("Toolbar dialog") }
            StyledButton(onClick = adaptive::show) { StyledText("Adaptive editor (resize the window)") }
            StyledButton(onClick = modalSheet::show) { StyledText("Modal bottom sheet") }
            StyledButton(onClick = floatingWindow::show) { StyledText("Floating window") }
            StyledButton(onClick = { dataDialog.show("Example item") }) { StyledText("Data dialog") }
            StyledButton(onClick = customizedDialog::show) { StyledText("Registry-default toolbar style") }
        }

        SubTitle("Action callbacks")
        StyledText("Action callbacks: $actionCount")

        // -------------
        // Dialogs and sheets
        // -------------

        StyledDialog(
            state = dialog, properties = properties, buttons = buttons,
            style = StyledDialog.Style.Dialog(maxWidth = if (narrowDialog) 400.dp else null),
            title = if (showTitle) ({ StyledText("Save changes?") }) else null,
        ) { contentPadding ->
            StyledColumn(
                itemSpacing = StyledTheme.spacing.medium,
                modifier = Modifier.padding(contentPadding),
            ) {
                StyledText("Actions use the active StyledButton or StyledIconButton renderer. Apply keeps custom content.")
                StyledButton(onClick = { actionCount++ }) { StyledText("Normal button in the body") }
            }
        }
        StyledDialog(
            state = fullscreen, properties = properties, buttons = buttons,
            style = StyledDialog.Style.Fullscreen(
                animation = fullscreenAnimation,
                expandableHeader = StyledDialog.Style.Fullscreen.ExpandableHeader(
                    expandedHeight = 240.dp,
                ),
            ),
            title = if (showTitle) ({ StyledText("Edit document") }) else null,
        ) { contentPadding ->
            DialogDemoContent(shortContent, contentPadding)
        }
        StyledDialog(
            state = toolbarDialog, properties = properties, buttons = buttons,
            style = StyledDialog.Style.ToolbarDialog(
                maxWidth = if (narrowDialog) 400.dp else null,
                expandToFullscreen = expandToolbarToFullscreen,
            ),
            title = if (showTitle) ({ StyledText("Bounded workspace") }) else null,
        ) { contentPadding ->
            DialogDemoContent(shortContent, contentPadding)
        }
        StyledDialog(
            state = adaptive, properties = properties, buttons = buttons,
            style = StyledAdaptive.dialog(StyledTheme.platform).value,
            title = if (showTitle) ({ StyledText("Adaptive editor") }) else null,
        ) { contentPadding ->
            DialogDemoContent(shortContent, contentPadding)
        }
        StyledDialog(
            state = floatingWindow,
            properties = properties,
            buttons = buttons,
            style = StyledDialog.Style.FloatingWindow(
                initialSize = DpSize(640.dp, 420.dp),
                minSize = DpSize(120.dp, 240.dp),
                shape = RoundedCornerShape(16.dp),
                initialPosition = StyledDialog.FloatingPosition(
                    alignment = Alignment.TopStart,
                    offset = DpOffset(24.dp, 24.dp),
                ),
                initialBubblePosition = StyledDialog.FloatingPosition(
                    alignment = Alignment.BottomStart,
                    offset = DpOffset(24.dp, (-24).dp),
                ),
                initiallyMinimized = floatingInitiallyMinimized,
                backgroundMode = if (floatingWindowMinimizeOnOutsideClick) {
                    StyledDialog.FloatingWindowBackgroundMode.Minimize(
                        showScrim = floatingWindowScrim,
                    )
                } else {
                    StyledDialog.FloatingWindowBackgroundMode.Interactive
                },
                draggable = floatingDraggable,
                resizable = floatingResizable,
                showMaximizeButton = true,
                icon = Icons.Default.Settings.toIconComposable(),
                bubble = if (customizeBubble) StyledDialog.BubbleConfig(
                    containerColor = StyledTheme.colorScheme.primary,
                    contentColor = StyledTheme.colorScheme.onPrimary,
                    borderColor = StyledTheme.colorScheme.outline,
                    shape = RoundedCornerShape(50),
                    shadowElevation = 20.dp,
                ) else StyledDialog.BubbleConfig(),
            ),
            title = if (showTitle) ({ StyledText("Floating workspace") }) else null,
        ) { contentPadding ->
            StyledColumn(
                itemSpacing = StyledTheme.spacing.medium,
                modifier = Modifier.padding(contentPadding),
            ) {
                StyledText("Drag the minimized bubble; it snaps to the nearest horizontal edge on release.")
                StyledText("Restoring keeps the last window position; minimizing returns to the same bubble position.")
                StyledText("Move the restored window: minimizing still returns to the same bubble position.")
                DialogDemoContent(shortContent, PaddingValues(0.dp))
            }
        }
        StyledDialog(
            state = modalSheet, properties = properties, buttons = buttons,
            style = StyledDialog.Style.BottomSheet(
                options = options, maxWidth = if (narrowDialog) 400.dp else 640.dp,
            ),
            title = if (showTitle) ({ StyledText("Draggable modal sheet") }) else null,
        ) { contentPadding ->
            DialogDemoContent(shortContent, contentPadding)
        }
        StyledDialog(
            state = dataDialog, properties = properties, buttons = buttons,
            title = if (showTitle) ({ StyledText("Data-backed dialog") }) else null,
        ) { contentPadding ->
            StyledText(dataDialog.requireData(), Modifier.padding(contentPadding))
        }
        UpdateStyledCustomization(
            update = {
                update(StyledDialog.DefaultsKey) {
                    copy(
                        style = StyledDialog.Style.ToolbarDialog(),
                        shape = RoundedCornerShape(4.dp),
                        toolbarMaxWidth = if (narrowDialog) 400.dp else 720.dp,
                        maxHeightFraction = 0.75f,
                    )
                }
            },
        ) {
            StyledDialog(
                state = customizedDialog, properties = properties, buttons = buttons,
                title = if (showTitle) ({ StyledText("Scoped shape and size overrides") }) else null,
            ) { contentPadding ->
                DialogDemoContent(shortContent, contentPadding)
            }
        }
        Box(Modifier.fillMaxWidth().height(480.dp)) {
            StyledButton(onClick = inlineSheet::show) { StyledText("Show embedded bottom sheet") }
            StyledBottomSheet(
                state = inlineSheet,
                properties = properties,
                style = StyledDialog.Style.BottomSheet(
                    options = options, maxWidth = if (narrowDialog) 400.dp else 640.dp,
                ),
                buttons = buttons,
                title = if (showTitle) ({ StyledText("Non-modal sheet") }) else null,
            ) { contentPadding ->
                DialogDemoContent(shortContent, contentPadding)
            }
        }
    }
}

@Composable
private fun DialogDemoContent(shortContent: Boolean, contentPadding: PaddingValues) {
    StyledColumn(
        itemSpacing = StyledTheme.spacing.medium,
        modifier = (if (shortContent) Modifier else Modifier.fillMaxSize())
            .verticalScroll(rememberScrollState())
            .padding(contentPadding),
    ) {
        StyledText(
            if (shortContent) "Short content keeps the workspace's remaining height; the footer stays at the bottom."
            else "Scroll this content. The action footer stays outside the body.",
        )
        StyledButton(onClick = {}) { StyledText("Normal body button (no action context)") }
        if (!shortContent) {
            repeat(30) { StyledText("Content row ${it + 1}") }
        }
    }

}

private val StringSaver: Saver<String, String> = Saver(
    save = { it },
    restore = { it },
)

private val FullscreenAnimationSaver: Saver<StyledDialog.FullscreenAnimation, String> = Saver(
    save = { animation ->
        when (animation) {
            StyledDialog.FullscreenAnimation.Fade -> "fade"
            is StyledDialog.FullscreenAnimation.Slide -> "slide:${animation.direction.name}"
        }
    },
    restore = { saved ->
        if (saved == "fade") {
            StyledDialog.FullscreenAnimation.Fade
        } else {
            val direction = StyledDialog.FullscreenAnimation.SlideDirection.entries
                .firstOrNull { it.name == saved.substringAfter(':') }
                ?: StyledDialog.FullscreenAnimation.SlideDirection.FromRight
            StyledDialog.FullscreenAnimation.Slide(direction)
        }
    },
)
