package com.michaelflisar.composestyledcomponents.fluent2

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyledcomponents.classes.StyledThemeDefinition
import com.michaelflisar.composestyledcomponents.classes.StyledThemeProvider
import com.michaelflisar.composestyledcomponents.fluent2.components.Fluent2BottomNavigationBar
import com.michaelflisar.composestyledcomponents.fluent2.components.Fluent2Button
import com.michaelflisar.composestyledcomponents.fluent2.components.Fluent2Card
import com.michaelflisar.composestyledcomponents.fluent2.components.Fluent2Chip
import com.michaelflisar.composestyledcomponents.fluent2.components.Fluent2Checkbox
import com.michaelflisar.composestyledcomponents.fluent2.components.Fluent2DropdownMenuItem
import com.michaelflisar.composestyledcomponents.fluent2.components.Fluent2FloatingActionButton
import com.michaelflisar.composestyledcomponents.fluent2.components.Fluent2Icon
import com.michaelflisar.composestyledcomponents.fluent2.components.Fluent2IconButton
import com.michaelflisar.composestyledcomponents.fluent2.components.Fluent2Input
import com.michaelflisar.composestyledcomponents.fluent2.components.Fluent2NavigationRail
import com.michaelflisar.composestyledcomponents.fluent2.components.Fluent2Popup
import com.michaelflisar.composestyledcomponents.fluent2.components.Fluent2PopupMenu
import com.michaelflisar.composestyledcomponents.fluent2.components.Fluent2ProgressIndicator
import com.michaelflisar.composestyledcomponents.fluent2.components.Fluent2RadioButton
import com.michaelflisar.composestyledcomponents.fluent2.components.Fluent2Scaffold
import com.michaelflisar.composestyledcomponents.fluent2.components.Fluent2TabRow
import com.michaelflisar.composestyledcomponents.fluent2.components.Fluent2SegmentedButton
import com.michaelflisar.composestyledcomponents.fluent2.components.Fluent2SegmentedButtonRow
import com.michaelflisar.composestyledcomponents.fluent2.components.Fluent2Snackbar
import com.michaelflisar.composestyledcomponents.fluent2.components.Fluent2SnackbarHost
import com.michaelflisar.composestyledcomponents.fluent2.components.Fluent2Surface
import com.michaelflisar.composestyledcomponents.fluent2.components.Fluent2Switch
import com.michaelflisar.composestyledcomponents.fluent2.components.Fluent2Tab
import com.michaelflisar.composestyledcomponents.fluent2.components.Fluent2Tooltip
import com.michaelflisar.composestyledcomponents.fluent2.components.Fluent2TopBar
import com.michaelflisar.composestyledcomponents.registry.StyledRegistry

@Composable
fun rememberFluent2Theme(
    registerDefaultComponents: @Composable StyledRegistry.Builder.() -> Unit = { registerFluent2Components() },
    registerCustomComponents: (@Composable StyledRegistry.Builder.() -> Unit)? = null,
): Fluent2ThemeProvider = remember(
    registerDefaultComponents,
    registerCustomComponents,
) {
    Fluent2ThemeProvider(registerDefaultComponents, registerCustomComponents)
}

class Fluent2ThemeProvider internal constructor(
    private val registerDefaultComponents: @Composable StyledRegistry.Builder.() -> Unit,
    private val registerCustomComponents: (@Composable StyledRegistry.Builder.() -> Unit)? = null,
) : StyledThemeProvider {
    @Composable
    override fun Root(content: @Composable () -> Unit) = content()

    @Composable
    override fun definition(): StyledThemeDefinition = fluent2ThemeDefinition(
        registerDefaultComponents = registerDefaultComponents,
        registerCustomComponents = registerCustomComponents,
    )
}

/**
 * Fluent 2 theme with hover, press and keyboard focus states for buttons.
 * Brand colors and typography follow the styled tokens.
 */
@Composable
fun fluent2ThemeDefinition(
    registerDefaultComponents: @Composable StyledRegistry.Builder.() -> Unit = {
        registerFluent2Components()
    },
    registerCustomComponents: (@Composable StyledRegistry.Builder.() -> Unit)? = null,
): StyledThemeDefinition {
    val registry = StyledRegistry.createComposable {
        registerDefaultComponents()
        registerCustomComponents?.invoke(this)
    }
    return remember(registry) {
        StyledThemeDefinition(name = "Fluent2", registry = registry)
    }
}

@Composable
fun StyledRegistry.Builder.registerFluent2Components() {
    register(Fluent2Button.create())
    register(Fluent2Chip.create())
    register(Fluent2IconButton.create())
    register(Fluent2Popup.create())
    register(Fluent2PopupMenu.create())
    register(Fluent2Tooltip.create())
    register(Fluent2DropdownMenuItem.create())
    register(Fluent2Card.create())
    register(Fluent2Icon.create())
    register(Fluent2Scaffold.create())
    register(Fluent2TopBar.create())
    register(Fluent2BottomNavigationBar.create())
    register(Fluent2Snackbar.create())
    register(Fluent2SnackbarHost.create())
    register(Fluent2FloatingActionButton.create())
    register(Fluent2Surface.create())
    register(Fluent2NavigationRail.create())
    register(Fluent2Checkbox.create())
    register(Fluent2RadioButton.create())
    register(Fluent2Switch.create())
    register(Fluent2Input.create())
    register(Fluent2ProgressIndicator.create())
    register(Fluent2TabRow.create())
    register(Fluent2Tab.create())
    register(Fluent2SegmentedButton.create())
    register(Fluent2SegmentedButtonRow.create())
}