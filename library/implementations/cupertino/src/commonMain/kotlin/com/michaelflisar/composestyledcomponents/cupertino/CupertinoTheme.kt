package com.michaelflisar.composestyledcomponents.cupertino

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyledcomponents.classes.StyledThemeDefinition
import com.michaelflisar.composestyledcomponents.classes.StyledThemeProvider
import com.michaelflisar.composestyledcomponents.cupertino.components.CupertinoBottomNavigationBar
import com.michaelflisar.composestyledcomponents.cupertino.components.CupertinoButton
import com.michaelflisar.composestyledcomponents.cupertino.components.CupertinoCard
import com.michaelflisar.composestyledcomponents.cupertino.components.CupertinoChip
import com.michaelflisar.composestyledcomponents.cupertino.components.CupertinoCheckbox
import com.michaelflisar.composestyledcomponents.cupertino.components.CupertinoDropdownMenuItem
import com.michaelflisar.composestyledcomponents.cupertino.components.CupertinoFloatingActionButton
import com.michaelflisar.composestyledcomponents.cupertino.components.CupertinoIcon
import com.michaelflisar.composestyledcomponents.cupertino.components.CupertinoIconButton
import com.michaelflisar.composestyledcomponents.cupertino.components.CupertinoInput
import com.michaelflisar.composestyledcomponents.cupertino.components.CupertinoNavigationRail
import com.michaelflisar.composestyledcomponents.cupertino.components.CupertinoPopup
import com.michaelflisar.composestyledcomponents.cupertino.components.CupertinoPopupMenu
import com.michaelflisar.composestyledcomponents.cupertino.components.CupertinoProgressIndicator
import com.michaelflisar.composestyledcomponents.cupertino.components.CupertinoRadioButton
import com.michaelflisar.composestyledcomponents.cupertino.components.CupertinoScaffold
import com.michaelflisar.composestyledcomponents.cupertino.components.CupertinoTabRow
import com.michaelflisar.composestyledcomponents.cupertino.components.CupertinoSegmentedButton
import com.michaelflisar.composestyledcomponents.cupertino.components.CupertinoSegmentedButtonRow
import com.michaelflisar.composestyledcomponents.cupertino.components.CupertinoSnackbar
import com.michaelflisar.composestyledcomponents.cupertino.components.CupertinoSnackbarHost
import com.michaelflisar.composestyledcomponents.cupertino.components.CupertinoSurface
import com.michaelflisar.composestyledcomponents.cupertino.components.CupertinoSwitch
import com.michaelflisar.composestyledcomponents.cupertino.components.CupertinoTab
import com.michaelflisar.composestyledcomponents.cupertino.components.CupertinoTooltip
import com.michaelflisar.composestyledcomponents.cupertino.components.CupertinoTopBar
import com.michaelflisar.composestyledcomponents.registry.StyledRegistry

@Composable
fun rememberCupertinoTheme(
    registerDefaultComponents: @Composable StyledRegistry.Builder.() -> Unit = { registerCupertinoComponents() },
    registerCustomComponents: (@Composable StyledRegistry.Builder.() -> Unit)? = null,
): CupertinoThemeProvider = remember(
    registerDefaultComponents,
    registerCustomComponents,
) {
    CupertinoThemeProvider(registerDefaultComponents, registerCustomComponents)
}

class CupertinoThemeProvider internal constructor(
    private val registerDefaultComponents: @Composable StyledRegistry.Builder.() -> Unit,
    private val registerCustomComponents: (@Composable StyledRegistry.Builder.() -> Unit)? = null,
) : StyledThemeProvider {
    @Composable
    override fun Root(content: @Composable () -> Unit) = content()

    @Composable
    override fun definition(): StyledThemeDefinition = cupertinoThemeDefinition(
        registerDefaultComponents = registerDefaultComponents,
        registerCustomComponents = registerCustomComponents,
    )
}

@Composable
fun cupertinoThemeDefinition(
    registerDefaultComponents: @Composable StyledRegistry.Builder.() -> Unit = {
        registerCupertinoComponents()
    },
    registerCustomComponents: (@Composable StyledRegistry.Builder.() -> Unit)? = null,
): StyledThemeDefinition {
    val registry = StyledRegistry.createComposable {
        registerDefaultComponents()
        registerCustomComponents?.invoke(this)
    }
    return remember(registry) {
        StyledThemeDefinition(name = "Cupertino", registry = registry)
    }
}

@Composable
fun StyledRegistry.Builder.registerCupertinoComponents() {
    register(CupertinoButton.create())
    register(CupertinoChip.create())
    register(CupertinoIconButton.create())
    register(CupertinoPopup.create())
    register(CupertinoPopupMenu.create())
    register(CupertinoTooltip.create())
    register(CupertinoDropdownMenuItem.create())
    register(CupertinoCard.create())
    register(CupertinoIcon.create())
    register(CupertinoScaffold.create())
    register(CupertinoTopBar.create())
    register(CupertinoBottomNavigationBar.create())
    register(CupertinoSnackbar.create())
    register(CupertinoSnackbarHost.create())
    register(CupertinoFloatingActionButton.create())
    register(CupertinoSurface.create())
    register(CupertinoNavigationRail.create())
    register(CupertinoCheckbox.create())
    register(CupertinoRadioButton.create())
    register(CupertinoSwitch.create())
    register(CupertinoInput.create())
    register(CupertinoProgressIndicator.create())
    register(CupertinoTabRow.create())
    register(CupertinoTab.create())
    register(CupertinoSegmentedButton.create())
    register(CupertinoSegmentedButtonRow.create())
}