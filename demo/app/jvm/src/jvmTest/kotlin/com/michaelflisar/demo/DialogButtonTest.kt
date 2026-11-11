package com.michaelflisar.demo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.click
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performMouseInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.runDesktopComposeUiTest
import androidx.compose.ui.test.swipe
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.classes.StyledThemeProvider
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatButton
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatIconButton
import com.michaelflisar.composestyled.cupertino.flat.layout.CupertinoFlatDialog
import com.michaelflisar.composestyled.cupertino.flat.rememberCupertinoFlatThemeProvider
import com.michaelflisar.composestyled.fluent2.components.Fluent2Button
import com.michaelflisar.composestyled.fluent2.components.Fluent2IconButton
import com.michaelflisar.composestyled.fluent2.rememberFluent2ThemeProvider
import com.michaelflisar.composestyled.material3.components.Material3Button
import com.michaelflisar.composestyled.material3.components.Material3IconButton
import com.michaelflisar.composestyled.material3.layout.Material3Dialog
import com.michaelflisar.composestyled.material3.rememberMaterial3ThemeProvider
import com.michaelflisar.composestyled.platform.Platform
import com.michaelflisar.composestyled.platform.PlatformDefaults
import com.michaelflisar.composestyled.registry.StyledRegistry
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.runtime.LocalStyledContext
import com.michaelflisar.composestyled.runtime.StyledContext
import com.michaelflisar.composestyled.themes.StyledThemeAppearance
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor
import com.michaelflisar.composestyled.tokens.LocalStyledTextStyle
import com.michaelflisar.composestyled.tokens.StyledTypography
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledIconButton
import com.michaelflisar.composestyled.ui.layout.StyledBottomSheet
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.state.dialogStateOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalTestApi::class, InternalComposeStyledApi::class)
@RunWith(Parameterized::class)
class DialogButtonTest(private val theme: Int) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "theme={0}")
        fun themes() = listOf(0, 1, 2)

        private val styles = listOf(
            StyledDialog.Style.Dialog(),
            StyledDialog.Style.ToolbarDialog(),
            StyledDialog.Style.Fullscreen(),
            StyledDialog.Style.BottomSheet(),
            StyledDialog.Style.FloatingWindow(
                initialSize = DpSize(720.dp, 480.dp),
                minSize = DpSize(320.dp, 240.dp),
            ),
        )

        private val icon: IconComposable = { description, modifier, _ ->
            Box(
                modifier.then(
                    Modifier
                        .size(24.dp)
                        .semantics { contentDescription = requireNotNull(description) },
                ),
            )
        }
    }

    @Test
    fun validatesStructuredActionsAndPreservesCustomContent() {
        assertThrows(IllegalArgumentException::class.java) {
            StyledDialog.Button({}, " \n")
        }
        assertThrows(IllegalArgumentException::class.java) {
            StyledDialog.Button({}, "Save", presentation = StyledDialog.ButtonPresentation.Icon)
        }
        for (presentation in listOf(
            StyledDialog.ButtonPresentation.Auto, StyledDialog.ButtonPresentation.Text,
        )) {
            val button = StyledDialog.Button({}, "Save", presentation = presentation)
            assertEquals("Save", button.label)
            assertEquals(presentation, button.presentation)
        }
        val custom = StyledDialog.Button({}) { StyledText("Custom") }
        assertEquals(StyledDialog.ButtonPresentation.Text, custom.presentation)
        assertEquals(null, custom.label)
    }

    @Test
    fun iconButtonVariantsRespectDialogContexts() {
        val filled = StyledButton.Variant.Filled
        assertEquals(
            Material3IconButton.Variant.Filled,
            Material3IconButton.DefaultVariantMapping(filled, StyledContext.Default),
        )
        assertEquals(
            CupertinoFlatIconButton.Variant.Filled,
            CupertinoFlatIconButton.DefaultVariantMapping(filled, StyledContext.Default),
        )
        assertEquals(
            Fluent2IconButton.Variant.Filled,
            Fluent2IconButton.DefaultVariantMapping(filled, StyledContext.Default),
        )
        assertEquals(
            Material3IconButton.Variant.Standard,
            Material3IconButton.DefaultVariantMapping(StyledButton.Variant.Auto, StyledContext.Default),
        )
        assertEquals(
            CupertinoFlatIconButton.Variant.FilledTonal,
            CupertinoFlatIconButton.DefaultVariantMapping(StyledButton.Variant.Auto, StyledContext.Default),
        )
        assertEquals(
            Fluent2IconButton.Variant.Filled,
            Fluent2IconButton.DefaultVariantMapping(StyledButton.Variant.Auto, StyledContext.Default),
        )
        for (context in StyledDialog.Context.entries) {
            assertEquals(
                Material3IconButton.Variant.Standard,
                Material3IconButton.DefaultVariantMapping(StyledButton.Variant.Auto, context),
            )
            assertEquals(
                CupertinoFlatIconButton.Variant.Standard,
                CupertinoFlatIconButton.DefaultVariantMapping(StyledButton.Variant.Auto, context),
            )
            assertEquals(
                Fluent2IconButton.Variant.Standard,
                Fluent2IconButton.DefaultVariantMapping(StyledButton.Variant.Auto, context),
            )
            assertEquals(
                Material3IconButton.Variant.Filled,
                Material3IconButton.DefaultVariantMapping(filled, context),
            )
            assertEquals(
                CupertinoFlatIconButton.Variant.Filled,
                CupertinoFlatIconButton.DefaultVariantMapping(filled, context),
            )
            assertEquals(
                Fluent2IconButton.Variant.Filled,
                Fluent2IconButton.DefaultVariantMapping(filled, context),
            )
        }
    }

    @Test
    fun dialogAutoVariantsUseThemeAndActionSlotPolicies() {
        val expectedFluentVariants = mapOf(
            StyledDialog.ActionSlot.Positive to Fluent2Button.Variant.Primary,
            StyledDialog.ActionSlot.Negative to Fluent2Button.Variant.Secondary,
            StyledDialog.ActionSlot.Neutral to Fluent2Button.Variant.Subtle,
        )
        for (context in StyledDialog.Context.entries) {
            for (slot in StyledDialog.ActionSlot.entries) {
                val actionContext = StyledDialog.ActionContext(context, slot)
                assertEquals(
                    Material3Button.Variant.Text,
                    Material3Button.DefaultVariantMapping(StyledButton.Variant.Auto, actionContext),
                )
                assertEquals(
                    CupertinoFlatButton.Variant.Plain,
                    CupertinoFlatButton.DefaultVariantMapping(StyledButton.Variant.Auto, actionContext),
                )
                assertEquals(
                    expectedFluentVariants.getValue(slot),
                    Fluent2Button.DefaultVariantMapping(StyledButton.Variant.Auto, actionContext),
                )
                assertEquals(
                    Material3Button.Variant.Primary,
                    Material3Button.DefaultVariantMapping(StyledButton.Variant.Filled, actionContext),
                )
            }
        }
    }

    @Test
    fun autoUsesActualToolbarPositionAndThemePolicy() = runDesktopComposeUiTest(
        width = 1000, height = 800,
    ) {
        var style by mutableStateOf<StyledDialog.Style>(styles.first())
        val state = dialogStateOf(true)
        setContent {
            Theme {
                StyledDialog(
                    state = state, style = style,
                    buttons = StyledDialog.Buttons(
                        positive = StyledDialog.Button({}, "Save", icon),
                        negative = StyledDialog.Button({}, "Cancel", icon),
                        neutral = StyledDialog.Button({}, "Apply", icon),
                    ),
                ) { StyledText("Body") }
            }
        }
        for (nextStyle in styles) {
            runOnIdle { style = nextStyle }
            val toolbar = nextStyle is StyledDialog.Style.ToolbarDialog ||
                nextStyle is StyledDialog.Style.Fullscreen
            onNodeWithText("Save").assertIsDisplayed()
            onNodeWithText("Apply").assertIsDisplayed()
            if (theme == 0 && toolbar) {
                onNodeWithContentDescription("Cancel").assertIsDisplayed()
                onNodeWithText("Cancel").assertDoesNotExist()
            } else {
                onNodeWithText("Cancel").assertIsDisplayed()
                onNodeWithContentDescription("Cancel").assertDoesNotExist()
            }
        }
    }

    @Test
    fun autoWithoutIconFallsBackAndCustomContentStaysText() = runDesktopComposeUiTest {
        setContent {
            Theme {
                StyledDialog(
                    state = dialogStateOf(true), style = StyledDialog.Style.Fullscreen(),
                    buttons = StyledDialog.Buttons(
                        positive = StyledDialog.Button({}) { StyledText("Custom save") },
                        negative = StyledDialog.Button({}, "Cancel"),
                    ),
                ) { StyledText("Body") }
            }
        }
        onNodeWithText("Cancel").assertIsDisplayed()
        onNodeWithText("Custom save").assertIsDisplayed()
        onNodeWithContentDescription("Cancel").assertDoesNotExist()
    }

    @Test
    fun floatingWindowStaysNonModalAndMinimizesRestoresAndCloses() = runDesktopComposeUiTest(
        width = 1000, height = 800,
    ) {
        val state = dialogStateOf(true)
        var backgroundClicks = 0
        setContent {
            Theme {
                Box(Modifier.fillMaxSize()) {
                    StyledButton(onClick = { backgroundClicks++ }) {
                        StyledText("Background action")
                    }
                    StyledDialog(
                        state = state,
                        modifier = Modifier.testTag("Floating window"),
                        style = StyledDialog.Style.FloatingWindow(
                            icon = icon,
                            showMinimizeButton = true,
                            showCloseButton = true,
                        ),
                        title = { StyledText("Inspector") },
                    ) {
                        StyledText("Floating body")
                    }
                }
            }
        }

        onNodeWithText("Background action").performClick()
        runOnIdle { assertEquals(1, backgroundClicks) }
        val initialWindowBounds = onNodeWithTag("Floating window")
            .fetchSemanticsNode().boundsInRoot
        onNodeWithContentDescription("Minimize").performClick()
        mainClock.advanceTimeBy(300)
        waitForIdle()
        val bubbleBounds = onNodeWithTag("Floating window").fetchSemanticsNode().boundsInRoot
        assertEquals(bubbleBounds.width, bubbleBounds.height, 0.1f)
        val restoreIconBounds = onNodeWithContentDescription("Restore window")
            .assertIsDisplayed()
            .fetchSemanticsNode()
            .boundsInRoot
        assertEquals(500f, restoreIconBounds.center.x, 1f)
        assertEquals(400f, restoreIconBounds.center.y, 1f)
        onNodeWithContentDescription("Restore window").performTouchInput {
            swipe(center, center + Offset(-180f, -120f))
        }
        waitForIdle()
        val movedBubbleCenter = onNodeWithContentDescription("Restore window")
            .fetchSemanticsNode().boundsInRoot.center
        assertTrue(movedBubbleCenter.x < restoreIconBounds.center.x - 100f)
        assertTrue(movedBubbleCenter.y < restoreIconBounds.center.y - 60f)
        onNodeWithContentDescription("Restore window").performClick()
        mainClock.advanceTimeBy(300)
        waitForIdle()
        assertEquals(
            initialWindowBounds,
            onNodeWithTag("Floating window").fetchSemanticsNode().boundsInRoot,
        )
        onNodeWithContentDescription("Minimize").performClick()
        mainClock.advanceTimeBy(300)
        waitForIdle()
        val retainedBubbleCenter = onNodeWithContentDescription("Restore window")
            .fetchSemanticsNode().boundsInRoot.center
        assertEquals(movedBubbleCenter.x, retainedBubbleCenter.x, 1f)
        assertEquals(movedBubbleCenter.y, retainedBubbleCenter.y, 1f)

        onNodeWithContentDescription("Restore window").performClick()
        mainClock.advanceTimeBy(300)
        waitForIdle()
        val restoredWindowCenter = onNodeWithTag("Floating window")
            .fetchSemanticsNode().boundsInRoot.center
        onNodeWithText("Inspector").performTouchInput {
            swipe(center, center + Offset(-100f, -80f))
        }
        waitForIdle()
        val movedWindowCenter = onNodeWithTag("Floating window")
            .fetchSemanticsNode().boundsInRoot.center
        assertTrue(movedWindowCenter.x < restoredWindowCenter.x - 50f)
        assertTrue(movedWindowCenter.y < restoredWindowCenter.y - 40f)
        onNodeWithContentDescription("Minimize").performClick()
        mainClock.advanceTimeBy(300)
        waitForIdle()
        val parkedBubbleCenter = onNodeWithContentDescription("Restore window")
            .fetchSemanticsNode().boundsInRoot.center
        assertEquals(movedBubbleCenter.x, parkedBubbleCenter.x, 1f)
        assertEquals(movedBubbleCenter.y, parkedBubbleCenter.y, 1f)
        onNodeWithContentDescription("Restore window").performClick()
        mainClock.advanceTimeBy(300)
        waitForIdle()
        val restoredMovedWindowCenter = onNodeWithTag("Floating window")
            .fetchSemanticsNode().boundsInRoot.center
        assertEquals(movedWindowCenter.x, restoredMovedWindowCenter.x, 1f)
        assertEquals(movedWindowCenter.y, restoredMovedWindowCenter.y, 1f)
        onNodeWithText("Floating body").assertIsDisplayed()
        onNodeWithContentDescription("Close").performClick()
        runOnIdle { assertFalse(state.visible) }
    }

    @Test
    fun floatingWindowScrimMinimizesOnOutsideClickAndRestoresBackgroundInteraction() =
        runDesktopComposeUiTest(width = 1000, height = 800) {
            val state = dialogStateOf(true)
            var backgroundClicks = 0
            setContent {
                Theme {
                    Box(Modifier.fillMaxSize()) {
                        StyledButton(onClick = { backgroundClicks++ }) {
                            StyledText("Background action")
                        }
                        StyledDialog(
                            state = state,
                            style = StyledDialog.Style.FloatingWindow(
                                backgroundMode = StyledDialog.FloatingWindowBackgroundMode.Minimize(
                                    showScrim = true,
                                ),
                                icon = icon,
                            ),
                            title = { StyledText("Inspector") },
                        ) {
                            StyledText("Floating body")
                        }
                    }
                }
            }

            val backgroundCenter = onNodeWithText("Background action")
                .fetchSemanticsNode().boundsInRoot.center
            onRoot().performTouchInput { click(backgroundCenter) }
            mainClock.advanceTimeBy(300)
            waitForIdle()
            onNodeWithContentDescription("Restore window").assertIsDisplayed()
            runOnIdle {
                assertEquals(0, backgroundClicks)
                assertTrue(state.visible)
            }

            onRoot().performTouchInput { click(backgroundCenter) }
            runOnIdle { assertEquals(1, backgroundClicks) }
        }

    @Test
    fun floatingWindowOutsideClickCanBeBlockedOrDismissed() =
        runDesktopComposeUiTest(width = 1000, height = 800) {
            val state = dialogStateOf(true)
            var behavior by mutableStateOf<StyledDialog.FloatingWindowBackgroundMode>(
                StyledDialog.FloatingWindowBackgroundMode.Block(showScrim = true),
            )
            var backgroundClicks = 0
            setContent {
                Theme {
                    Box(Modifier.fillMaxSize()) {
                        StyledButton(onClick = { backgroundClicks++ }) {
                            StyledText("Background action")
                        }
                        StyledDialog(
                            state = state,
                            style = StyledDialog.Style.FloatingWindow(
                                backgroundMode = behavior,
                            ),
                        ) {
                            StyledText("Floating body")
                        }
                    }
                }
            }

            val backgroundCenter = onNodeWithText("Background action")
                .fetchSemanticsNode().boundsInRoot.center
            onRoot().performTouchInput { click(backgroundCenter) }
            runOnIdle {
                assertEquals(0, backgroundClicks)
                assertTrue(state.visible)
                behavior = StyledDialog.FloatingWindowBackgroundMode.Dismiss(showScrim = true)
                state.show()
            }
            waitForIdle()
            onRoot().performTouchInput { click(backgroundCenter) }
            runOnIdle {
                assertEquals(0, backgroundClicks)
                assertFalse(state.visible)
            }
        }

    @Test
    fun floatingWindowUsesItsNestedThemeColors() = runDesktopComposeUiTest(
        width = 1000, height = 800,
    ) {
        var expectedColor by mutableStateOf(Color.Unspecified)
        var expectedHeaderColor by mutableStateOf(Color.Unspecified)
        var expectedHeaderContentColor by mutableStateOf(Color.Unspecified)
        var expectedContentColor by mutableStateOf(Color.Unspecified)
        var titleContentColor by mutableStateOf(Color.Unspecified)
        var minimizeIconTint by mutableStateOf(Color.Unspecified)
        var bubble by mutableStateOf(StyledDialog.BubbleConfig())
        val state = dialogStateOf(true)
        val tintedIcon: IconComposable = { _, modifier, tint ->
            Box(modifier.background(tint))
        }
        setContent {
            StyledTheme(
                Platform.JvmWindows,
                rememberFluent2ThemeProvider(),
                themeAppearance = StyledThemeAppearance.Light,
            ) {
                Theme(
                    customization = {
                        register(RecordingStyledIcon { description, tint ->
                            if (description == "Minimize") minimizeIconTint = tint
                        })
                    },
                    appearance = StyledThemeAppearance.Dark,
                ) {
                    expectedColor = if (theme == 0) {
                        MaterialTheme.colorScheme.surfaceContainerHigh
                    } else {
                        StyledTheme.colorScheme.surface
                    }
                    expectedHeaderColor = StyledTheme.colorScheme.surfaceVariant
                    expectedHeaderContentColor = StyledTheme.contentColorFor(expectedHeaderColor)
                    expectedContentColor = StyledTheme.contentColorFor(expectedColor)
                    StyledDialog(
                        state = state,
                        modifier = Modifier.testTag("Floating window"),
                        style = StyledDialog.Style.FloatingWindow(
                            initialSize = DpSize(720.dp, 480.dp),
                            minSize = DpSize(320.dp, 240.dp),
                            shape = RoundedCornerShape(0.dp),
                            icon = tintedIcon,
                            bubble = bubble,
                        ),
                        title = {
                            val contentColor = LocalStyledContentColor.current
                            SideEffect { titleContentColor = contentColor }
                            StyledText("Inspector")
                        },
                    ) {}
                }
            }
        }

        waitForIdle()
        runOnIdle {
            assertEquals(expectedHeaderContentColor, titleContentColor)
            assertEquals(expectedHeaderContentColor, minimizeIconTint)
        }
        val image = onNodeWithTag("Floating window").captureToImage().toPixelMap()
        assertEquals(expectedColor, image[300, 150])
        onNodeWithContentDescription("Minimize").performClick()
        mainClock.advanceTimeBy(300)
        waitForIdle()
        val defaultBubble = onNodeWithTag("Floating window").captureToImage().toPixelMap()
        assertEquals(expectedHeaderColor, defaultBubble[4, defaultBubble.height / 2])
        assertEquals(expectedContentColor, defaultBubble[defaultBubble.width / 2, defaultBubble.height / 2])

        runOnIdle {
            bubble = StyledDialog.BubbleConfig(
                containerColor = Color.Red,
                contentColor = Color.Green,
                borderColor = Color.Blue,
                shape = RoundedCornerShape(50),
                shadowElevation = 20.dp,
            )
        }
        mainClock.advanceTimeBy(300)
        waitForIdle()
        val customBubble = onNodeWithTag("Floating window").captureToImage().toPixelMap()
        assertEquals(Color.Red, customBubble[4, customBubble.height / 2])
        assertEquals(Color.Green, customBubble[customBubble.width / 2, customBubble.height / 2])
        assertTrue(customBubble[0, 0] != Color.Red)
        assertTrue((0 until customBubble.width).any { x ->
            (0 until customBubble.height).any { y ->
                val pixel = customBubble[x, y]
                pixel.blue > 0.7f && pixel.red < 0.3f && pixel.green < 0.3f
            }
        })

        onNodeWithContentDescription("Restore window").performClick()
        mainClock.advanceTimeBy(300)
        waitForIdle()
        val restored = onNodeWithTag("Floating window").captureToImage().toPixelMap()
        assertEquals(expectedColor, restored[300, 150])
        assertEquals(expectedHeaderColor, restored[0, 0])

        mainClock.autoAdvance = false
        onNodeWithContentDescription("Minimize").performClick()
        mainClock.advanceTimeByFrame()
        mainClock.advanceTimeBy(112)
        waitForIdle()
        val transitioning = onNodeWithTag("Floating window").captureToImage().toPixelMap()
        val headerColor = transitioning[transitioning.width / 2, 8]
        assertTrue(headerColor != expectedHeaderColor && headerColor != Color.Red)
        val iconColor = transitioning[transitioning.width / 2, 24]
        assertTrue(iconColor != expectedContentColor && iconColor != Color.Green)
        mainClock.advanceTimeBy(300)
        waitForIdle()
        val minimizedAgain = onNodeWithTag("Floating window").captureToImage().toPixelMap()
        assertEquals(Color.Red, minimizedAgain[4, minimizedAgain.height / 2])
        assertEquals(Color.Green, minimizedAgain[minimizedAgain.width / 2, minimizedAgain.height / 2])
        mainClock.autoAdvance = true
    }

    @Test
    fun fullscreenToolbarIconsInheritTitleContentColor() = runDesktopComposeUiTest(
        width = 1000, height = 800,
    ) {
        assumeTrue("Fluent 2 keeps fullscreen actions in the footer", theme != 2)

        var titleContentColor by mutableStateOf(Color.Unspecified)
        val tintedIcon: IconComposable = { _, modifier, tint ->
            Box(modifier.background(tint).testTag("fullscreen toolbar icon"))
        }
        setContent {
            Theme {
                StyledDialog(
                    state = dialogStateOf(true),
                    style = StyledDialog.Style.Fullscreen(),
                    title = {
                        val contentColor = LocalStyledContentColor.current
                        SideEffect { titleContentColor = contentColor }
                        StyledText("Settings")
                    },
                    buttons = StyledDialog.Buttons(
                        positive = StyledDialog.Button(
                            {}, "Save", tintedIcon, StyledDialog.ButtonPresentation.Icon,
                        ),
                    ),
                ) { StyledText("Body") }
            }
        }

        val iconNode = onNodeWithTag("fullscreen toolbar icon", useUnmergedTree = true)
        iconNode.assertIsDisplayed()
        val icon = iconNode.captureToImage().toPixelMap()
        runOnIdle {
            assertEquals(
                titleContentColor,
                icon[icon.width / 2, icon.height / 2],
            )
        }
    }

    @Test
    fun expandableFullscreenTitleUsesHeaderContentColor() = runDesktopComposeUiTest(
        width = 1000, height = 800,
    ) {
        var titleStyleColor by mutableStateOf(Color.Unspecified)
        var titleContentColor by mutableStateOf(Color.Unspecified)
        var expectedContentColor by mutableStateOf(Color.Unspecified)
        setContent {
            val baseTypography = PlatformDefaults.typography(Platform.JvmWindows)
            val typography = baseTypography.copy(
                headlineMedium = baseTypography.headlineMedium.copy(color = Color.Red),
            )
            Theme(typography = typography) {
                val headerContentColor = StyledTheme.contentColorFor(StyledTheme.colorScheme.surface)
                SideEffect { expectedContentColor = headerContentColor }
                StyledDialog(
                    state = dialogStateOf(true),
                    style = StyledDialog.Style.Fullscreen(
                        expandableHeader = StyledDialog.Style.Fullscreen.ExpandableHeader(),
                    ),
                    title = {
                        val styleColor = LocalStyledTextStyle.current.color
                        val contentColor = LocalStyledContentColor.current
                        SideEffect {
                            titleStyleColor = styleColor
                            titleContentColor = contentColor
                        }
                        StyledText("Expandable title")
                    },
                ) { StyledText("Body") }
            }
        }

        onNodeWithText("Expandable title").assertIsDisplayed()
        runOnIdle {
            assertEquals(expectedContentColor, titleContentColor)
            assertEquals(expectedContentColor, titleStyleColor)
        }
    }

    @Test
    fun expandableFullscreenHeaderMatchesDialogContainerByDefault() = runDesktopComposeUiTest(
        width = 1000, height = 800,
    ) {
        var expectedContainerColor by mutableStateOf(Color.Unspecified)
        setContent {
            Theme {
                SideEffect { expectedContainerColor = StyledTheme.colorScheme.surface }
                StyledDialog(
                    state = dialogStateOf(true),
                    modifier = Modifier.testTag("fullscreen dialog"),
                    style = StyledDialog.Style.Fullscreen(
                        expandableHeader = StyledDialog.Style.Fullscreen.ExpandableHeader(),
                    ),
                    title = { StyledText("Expandable title") },
                ) {}
            }
        }

        val dialog = onNodeWithTag("fullscreen dialog").captureToImage().toPixelMap()
        runOnIdle {
            assertEquals(expectedContainerColor, dialog[dialog.width / 2, 16])
            assertEquals(expectedContainerColor, dialog[dialog.width / 2, dialog.height - 16])
        }
    }

    @Test
    fun explicitIconWorksInEveryDialogAndEmbeddedSheetWithRegistryContext() =
        runDesktopComposeUiTest(width = 1000, height = 800) {
            var style by mutableStateOf<StyledDialog.Style>(styles.first())
            var embedded by mutableStateOf(false)
            val state = dialogStateOf(true)
            val contexts = mutableListOf<StyledContext>()
            setContent {
                Theme(customization = {
                    register(RecordingIcons { contexts += it })
                }) {
                    Box(Modifier.fillMaxSize()) {
                        val buttons = StyledDialog.Buttons(
                            positive = StyledDialog.Button(
                                {}, "Save", icon, StyledDialog.ButtonPresentation.Icon,
                            ),
                            negative = StyledDialog.Button(
                                {}, "Cancel", icon, StyledDialog.ButtonPresentation.Icon,
                            ),
                            neutral = StyledDialog.Button(
                                {}, "Apply", icon, StyledDialog.ButtonPresentation.Icon,
                            ),
                        )
                        if (embedded) {
                            StyledBottomSheet(state = state, buttons = buttons) { StyledText("Body") }
                        } else {
                            StyledDialog(state = state, style = style, buttons = buttons) {
                                assertEquals(StyledContext.Default, LocalStyledContext.current)
                                StyledIconButton(
                                    onClick = {},
                                    variant = StyledButton.Variant.Filled,
                                    icon = icon,
                                    contentDescription = "Body action",
                                )
                            }
                        }
                    }
                }
            }
            for (nextStyle in styles) {
                runOnIdle {
                    if (nextStyle != styles.first()) contexts.clear()
                    style = nextStyle
                }
                for (label in listOf("Save", "Cancel", "Apply")) {
                    onNodeWithContentDescription(label).assertIsDisplayed()
                    onNodeWithText(label).assertDoesNotExist()
                }
                onNodeWithContentDescription("Body action").assertIsDisplayed()
                runOnIdle {
                    val expected = when (nextStyle) {
                        is StyledDialog.Style.Dialog -> StyledDialog.Context.DialogAction
                        is StyledDialog.Style.BottomSheet -> StyledDialog.Context.BottomSheetAction
                        is StyledDialog.Style.FloatingWindow -> StyledDialog.Context.DialogAction
                        else -> StyledDialog.Context.FullscreenAction
                    }
                    val actionContexts = contexts.filterIsInstance<StyledDialog.ActionContext>()
                    assertTrue(actionContexts.any { it.context == expected })
                    assertTrue(
                        contexts.all {
                            (it is StyledDialog.ActionContext && it.context == expected) ||
                                it == StyledContext.Default
                        },
                    )
                }
            }
            runOnIdle {
                contexts.clear()
                embedded = true
            }
            onNodeWithContentDescription("Save").assertIsDisplayed()
            runOnIdle {
                assertTrue(contexts.isNotEmpty())
                assertTrue(
                    contexts.all {
                        (it is StyledDialog.ActionContext &&
                            it.context == StyledDialog.Context.BottomSheetAction) ||
                            it == StyledContext.Default
                    },
                )
            }
        }

    @Test
    fun iconActionsExposeTheirLabelAsTooltipWithoutClicking() = runDesktopComposeUiTest {
        val state = dialogStateOf(true)
        var callbacks = 0
        setContent {
            Theme {
                StyledDialog(
                    state = state, style = StyledDialog.Style.Fullscreen(),
                    buttons = StyledDialog.Buttons(
                        positive = StyledDialog.Button(
                            { callbacks++ }, "Save", icon, StyledDialog.ButtonPresentation.Icon,
                        ),
                    ),
                ) { StyledText("Body") }
            }
        }
        onNodeWithContentDescription("Save").performMouseInput {
            enter(center)
            moveTo(center)
        }
        mainClock.advanceTimeBy(800)
        waitForIdle()
        onNodeWithText("Save").assertIsDisplayed()
        runOnIdle {
            assertEquals(0, callbacks)
            assertTrue(state.visible)
        }
    }

    @Test
    fun explicitTextIgnoresIconEvenWithIconPreferringToolbarMapping() = runDesktopComposeUiTest {
        setContent {
            Theme(customization = {
                if (theme == 0) register(Material3Dialog.create { StyledDialog.ButtonPresentation.Icon })
                if (theme == 1) register(CupertinoFlatDialog.create { StyledDialog.ButtonPresentation.Icon })
            }) {
                StyledDialog(
                    state = dialogStateOf(true), style = StyledDialog.Style.Fullscreen(),
                    buttons = StyledDialog.Buttons(
                        positive = StyledDialog.Button(
                            {}, "Save", icon, StyledDialog.ButtonPresentation.Text,
                        ),
                        negative = StyledDialog.Button(
                            {}, "Cancel", icon, StyledDialog.ButtonPresentation.Text,
                        ),
                    ),
                ) { StyledText("Body") }
            }
        }
        onNodeWithText("Save").assertIsDisplayed()
        onNodeWithText("Cancel").assertIsDisplayed()
        onNodeWithContentDescription("Save").assertDoesNotExist()
        onNodeWithContentDescription("Cancel").assertDoesNotExist()
    }

    @Test
    fun toolbarMappingCanOptIntoIconsWithoutChangingFooterAutoActions() = runDesktopComposeUiTest {
        setContent {
            Theme(customization = {
                if (theme == 0) register(Material3Dialog.create { StyledDialog.ButtonPresentation.Icon })
                if (theme == 1) register(CupertinoFlatDialog.create { StyledDialog.ButtonPresentation.Icon })
            }) {
                StyledDialog(
                    state = dialogStateOf(true), style = StyledDialog.Style.Fullscreen(),
                    buttons = StyledDialog.Buttons(
                        positive = StyledDialog.Button({}, "Save", icon),
                        negative = StyledDialog.Button({}, "Cancel", icon),
                        neutral = StyledDialog.Button({}, "Apply", icon),
                    ),
                ) { StyledText("Body") }
            }
        }
        if (theme != 2) {
            onNodeWithContentDescription("Save").assertIsDisplayed()
            onNodeWithContentDescription("Cancel").assertIsDisplayed()
        } else {
            onNodeWithText("Save").assertIsDisplayed()
            onNodeWithText("Cancel").assertIsDisplayed()
        }
        onNodeWithText("Apply").assertIsDisplayed()
    }

    @Test
    fun structuredIconsPreserveDisabledDataAndDismissalPolicies() = runDesktopComposeUiTest {
        val state = dialogStateOf("Document")
        var enabled by mutableStateOf(false)
        var callbacks = 0
        var dismissRequests = 0
        setContent {
            Theme {
                StyledDialog(
                    state = state, style = StyledDialog.Style.Fullscreen(),
                    onDismissRequest = {
                        dismissRequests++
                        state.dismiss()
                    },
                    buttons = StyledDialog.Buttons(
                        positive = StyledDialog.Button(
                            onClick = {
                                assertEquals("Document", state.requireData())
                                callbacks++
                            },
                            label = "Save", icon = icon,
                            presentation = StyledDialog.ButtonPresentation.Icon,
                            enabled = enabled,
                        ),
                        neutral = StyledDialog.Button(
                            onClick = { callbacks++ }, label = "Apply", icon = icon,
                            presentation = StyledDialog.ButtonPresentation.Icon,
                            dismissOnClick = false,
                        ),
                    ),
                ) { StyledText(state.requireData()) }
            }
        }
        onNodeWithContentDescription("Save").assertIsNotEnabled().performClick()
        runOnIdle {
            assertEquals(0, callbacks)
            assertTrue(state.visible)
        }
        onNodeWithContentDescription("Apply").performClick()
        runOnIdle {
            assertEquals(1, callbacks)
            assertEquals(0, dismissRequests)
            assertTrue(state.visible)
            enabled = true
        }
        onNodeWithContentDescription("Save").performClick()
        runOnIdle {
            assertEquals(2, callbacks)
            assertEquals(1, dismissRequests)
            assertFalse(state.visible)
            assertEquals(null, state.data)
        }
    }

    @Composable
    private fun Theme(
        customization: (@Composable StyledRegistry.Builder.() -> Unit)? = null,
        appearance: StyledThemeAppearance = StyledThemeAppearance.System,
        typography: StyledTypography? = null,
        content: @Composable () -> Unit,
    ) {
        val provider: StyledThemeProvider = when (theme) {
            0 -> rememberMaterial3ThemeProvider()
            1 -> rememberCupertinoFlatThemeProvider()
            else -> rememberFluent2ThemeProvider()
        }
        StyledTheme(
            Platform.JvmWindows,
            provider,
            themeAppearance = appearance,
            typography = typography ?: PlatformDefaults.typography(Platform.JvmWindows),
            customization = customization,
            content = content,
        )
    }

    private class RecordingStyledIcon(
        private val record: (String?, Color) -> Unit,
    ) : StyledIcon.Implementation {
        override val key
            get() = StyledIcon.Key

        @Composable
        override fun Render(
            icon: IconComposable,
            modifier: Modifier,
            contentDescription: String?,
            tint: Color,
        ) {
            SideEffect { record(contentDescription, tint) }
            com.michaelflisar.composestyled.classes.IconComposable(
                icon = icon,
                contentDescription = contentDescription,
                modifier = modifier,
                tint = tint,
            )
        }
    }

    @Composable
    private fun RecordingIcons(record: (StyledContext) -> Unit): StyledIconButton.Implementation =
        when (theme) {
            0 -> Material3IconButton.create { variant, context ->
                record(context)
                Material3IconButton.DefaultVariantMapping(variant, context)
            }
            1 -> CupertinoFlatIconButton.create { variant, context ->
                record(context)
                CupertinoFlatIconButton.DefaultVariantMapping(variant, context)
            }
            else -> Fluent2IconButton.create { variant, context ->
                record(context)
                Fluent2IconButton.DefaultVariantMapping(variant, context)
            }
        }
}
