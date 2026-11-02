package com.michaelflisar.composestyled.fluent2

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyled.classes.StyledThemeDefinition
import com.michaelflisar.composestyled.classes.StyledThemeProvider
import com.michaelflisar.composestyled.themes.fluent2.Fluent2Themes
import com.michaelflisar.composestyled.fluent2.components.Fluent2Badge
import com.michaelflisar.composestyled.fluent2.components.Fluent2BottomNavigationBar
import com.michaelflisar.composestyled.fluent2.components.Fluent2Button
import com.michaelflisar.composestyled.fluent2.components.Fluent2Card
import com.michaelflisar.composestyled.fluent2.components.Fluent2Chip
import com.michaelflisar.composestyled.fluent2.layout.Fluent2Dialog
import com.michaelflisar.composestyled.fluent2.layout.Fluent2BottomSheet
import com.michaelflisar.composestyled.fluent2.components.Fluent2Checkbox
import com.michaelflisar.composestyled.fluent2.components.Fluent2DropdownMenu
import com.michaelflisar.composestyled.fluent2.components.Fluent2FloatingActionButton
import com.michaelflisar.composestyled.fluent2.components.Fluent2Icon
import com.michaelflisar.composestyled.fluent2.components.Fluent2IconButton
import com.michaelflisar.composestyled.fluent2.components.Fluent2Input
import com.michaelflisar.composestyled.fluent2.components.Fluent2ListItem
import com.michaelflisar.composestyled.fluent2.components.Fluent2SideNavigation
import com.michaelflisar.composestyled.fluent2.components.Fluent2Popup
import com.michaelflisar.composestyled.fluent2.components.Fluent2PopupMenu
import com.michaelflisar.composestyled.fluent2.components.Fluent2ProgressIndicator
import com.michaelflisar.composestyled.fluent2.components.Fluent2RangeSlider
import com.michaelflisar.composestyled.fluent2.components.Fluent2RadioButton
import com.michaelflisar.composestyled.fluent2.layout.Fluent2Scaffold
import com.michaelflisar.composestyled.fluent2.components.Fluent2TabRow
import com.michaelflisar.composestyled.fluent2.components.Fluent2SegmentedButton
import com.michaelflisar.composestyled.fluent2.components.Fluent2SegmentedButtonRow
import com.michaelflisar.composestyled.fluent2.components.Fluent2Snackbar
import com.michaelflisar.composestyled.fluent2.components.Fluent2SnackbarHost
import com.michaelflisar.composestyled.fluent2.components.Fluent2Surface
import com.michaelflisar.composestyled.fluent2.components.Fluent2Switch
import com.michaelflisar.composestyled.fluent2.components.Fluent2Slider
import com.michaelflisar.composestyled.fluent2.components.Fluent2Tab
import com.michaelflisar.composestyled.fluent2.components.Fluent2Tooltip
import com.michaelflisar.composestyled.fluent2.components.Fluent2TopBar
import com.michaelflisar.composestyled.registry.StyledRegistry
import com.michaelflisar.composestyled.ui.icons.fluent2.Fluent2RegularIconSet

@Composable
fun rememberFluent2ThemeProvider(): Fluent2ThemeProvider = remember { createFluent2ThemeProvider() }

fun createFluent2ThemeProvider(): Fluent2ThemeProvider = Fluent2ThemeProvider()

class Fluent2ThemeProvider internal constructor() : StyledThemeProvider {
    override val id = "fluent2"
    override val defaultThemeColors = Fluent2Themes.blue
    override val defaultIconSet = Fluent2RegularIconSet

    @Composable
    override fun Root(content: @Composable () -> Unit) = content()

    @Composable
    override fun definition(): StyledThemeDefinition = fluent2ThemeDefinition()
}

/**
 * Fluent 2 theme with hover, press and keyboard focus states for buttons.
 * Brand colors and typography follow the styled tokens.
 */
@Composable
fun fluent2ThemeDefinition(): StyledThemeDefinition {
    val registry = StyledRegistry.createComposable {
        registerFluent2Components()
    }
    return remember(registry) {
        StyledThemeDefinition(name = "Fluent2", registry = registry)
    }
}

@Composable
fun StyledRegistry.Builder.registerFluent2Components() {
    register(Fluent2Badge.create())
    register(Fluent2Button.create())
    register(Fluent2Dialog.create())
    register(Fluent2BottomSheet.create())
    register(Fluent2Chip.create())
    register(Fluent2IconButton.create())
    register(Fluent2Popup.create())
    register(Fluent2PopupMenu.create())
    register(Fluent2Tooltip.create())
    register(Fluent2DropdownMenu.create())
    register(Fluent2Card.create())
    register(Fluent2ListItem.create())
    register(Fluent2Icon.create())
    register(Fluent2Scaffold.create())
    register(Fluent2TopBar.create())
    register(Fluent2BottomNavigationBar.create())
    register(Fluent2Snackbar.create())
    register(Fluent2SnackbarHost.create())
    register(Fluent2FloatingActionButton.create())
    register(Fluent2Surface.create())
    register(Fluent2SideNavigation.create())
    register(Fluent2Checkbox.create())
    register(Fluent2RadioButton.create())
    register(Fluent2Switch.create())
    register(Fluent2Input.create())
    register(Fluent2ProgressIndicator.create())
    register(Fluent2Slider.create())
    register(Fluent2RangeSlider.create())
    register(Fluent2TabRow.create())
    register(Fluent2Tab.create())
    register(Fluent2SegmentedButton.create())
    register(Fluent2SegmentedButtonRow.create())
}