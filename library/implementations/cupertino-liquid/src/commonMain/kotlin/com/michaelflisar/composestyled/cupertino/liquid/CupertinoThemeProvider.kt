package com.michaelflisar.composestyled.cupertino.liquid

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyled.classes.StyledThemeDefinition
import com.michaelflisar.composestyled.classes.StyledThemeProvider
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidBadge
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidBottomNavigationBar
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidButton
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidCard
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidChip
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidCheckbox
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidDropdownMenu
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidFloatingActionButton
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidIcon
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidIconButton
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidInput
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidListItem
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidPopup
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidPopupMenu
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidProgressIndicator
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidRangeSlider
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidRadioButton
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidSegmentedButton
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidSegmentedButtonRow
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidSideNavigation
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidSlider
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidSnackbar
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidSnackbarHost
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidSurface
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidSwitch
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidTab
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidTabRow
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidTooltip
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidTopBar
import com.michaelflisar.composestyled.ui.icons.sfsymbols.SFSymbolIconSet
import com.michaelflisar.composestyled.cupertino.liquid.layout.CupertinoLiquidBottomSheet
import com.michaelflisar.composestyled.cupertino.liquid.layout.CupertinoLiquidDialog
import com.michaelflisar.composestyled.cupertino.liquid.layout.CupertinoLiquidScaffold
import com.michaelflisar.composestyled.registry.StyledRegistry
import com.michaelflisar.composestyled.themes.cupertino.CupertinoThemes

@Composable
fun rememberCupertinoLiquidThemeProvider(): CupertinoLiquidThemeProvider =
    remember { CupertinoLiquidThemeProvider() }

class CupertinoLiquidThemeProvider internal constructor() : StyledThemeProvider {
    override val defaultThemeColors = CupertinoThemes.blue
    override val defaultIconSet = SFSymbolIconSet

    @Composable
    override fun Root(content: @Composable () -> Unit) = CupertinoLiquidContent(content)

    @Composable
    override fun definition(): StyledThemeDefinition = cupertinoLiquidThemeDefinition()
}

@Composable
fun cupertinoLiquidThemeDefinition(): StyledThemeDefinition {
    val registry = StyledRegistry.createComposable {
        registerCupertinoLiquidComponents()
    }
    return remember(registry) {
        StyledThemeDefinition(name = "Cupertino Liquid", registry = registry)
    }
}

@Composable
fun StyledRegistry.Builder.registerCupertinoLiquidComponents() {
    register(CupertinoLiquidBadge.create())
    register(CupertinoLiquidButton.create())
    register(CupertinoLiquidDialog.create())
    register(CupertinoLiquidBottomSheet.create())
    register(CupertinoLiquidChip.create())
    register(CupertinoLiquidIconButton.create())
    register(CupertinoLiquidPopup.create())
    register(CupertinoLiquidPopupMenu.create())
    register(CupertinoLiquidTooltip.create())
    register(CupertinoLiquidDropdownMenu.create())
    register(CupertinoLiquidCard.create())
    register(CupertinoLiquidListItem.create())
    register(CupertinoLiquidIcon.create())
    register(CupertinoLiquidScaffold.create())
    register(CupertinoLiquidTopBar.create())
    register(CupertinoLiquidBottomNavigationBar.create())
    register(CupertinoLiquidSnackbar.create())
    register(CupertinoLiquidSnackbarHost.create())
    register(CupertinoLiquidFloatingActionButton.create())
    register(CupertinoLiquidSurface.create())
    register(CupertinoLiquidSideNavigation.create())
    register(CupertinoLiquidCheckbox.create())
    register(CupertinoLiquidRadioButton.create())
    register(CupertinoLiquidSwitch.create())
    register(CupertinoLiquidInput.create())
    register(CupertinoLiquidProgressIndicator.create())
    register(CupertinoLiquidSlider.create())
    register(CupertinoLiquidRangeSlider.create())
    register(CupertinoLiquidTabRow.create())
    register(CupertinoLiquidTab.create())
    register(CupertinoLiquidSegmentedButton.create())
    register(CupertinoLiquidSegmentedButtonRow.create())
}