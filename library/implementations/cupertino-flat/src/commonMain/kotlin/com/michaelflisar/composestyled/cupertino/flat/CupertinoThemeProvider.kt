package com.michaelflisar.composestyled.cupertino.flat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyled.classes.StyledThemeDefinition
import com.michaelflisar.composestyled.classes.StyledThemeProvider
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatBadge
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatBottomNavigationBar
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatButton
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatCard
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatChip
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatCheckbox
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatDropdownMenu
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatFloatingActionButton
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatIcon
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatIconButton
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatInput
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatListItem
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatPopup
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatPopupMenu
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatProgressIndicator
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatRangeSlider
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatRadioButton
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatSegmentedButton
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatSegmentedButtonRow
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatSideNavigation
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatSlider
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatSnackbar
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatSnackbarHost
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatSurface
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatSwitch
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatTab
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatTabRow
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatTooltip
import com.michaelflisar.composestyled.cupertino.flat.components.CupertinoFlatTopBar
import com.michaelflisar.composestyled.ui.icons.sfsymbols.SFSymbolIconSet
import com.michaelflisar.composestyled.cupertino.flat.layout.CupertinoFlatBottomSheet
import com.michaelflisar.composestyled.cupertino.flat.layout.CupertinoFlatDialog
import com.michaelflisar.composestyled.cupertino.flat.layout.CupertinoFlatScaffold
import com.michaelflisar.composestyled.registry.StyledRegistry
import com.michaelflisar.composestyled.themes.cupertino.CupertinoThemes

@Composable
fun rememberCupertinoFlatThemeProvider(): CupertinoFlatThemeProvider =
    remember { createCupertinoFlatThemeProvider() }

fun createCupertinoFlatThemeProvider(): CupertinoFlatThemeProvider = CupertinoFlatThemeProvider()

class CupertinoFlatThemeProvider internal constructor() : StyledThemeProvider {

    override val id = "cupertino-flat"
    override val defaultThemeColors = CupertinoThemes.blue
    override val defaultIconSet = SFSymbolIconSet

    @Composable
    override fun Root(content: @Composable () -> Unit) = content()

    @Composable
    override fun definition(): StyledThemeDefinition = cupertinoFlatThemeDefinition()
}

@Composable
fun cupertinoFlatThemeDefinition(): StyledThemeDefinition {
    val registry = StyledRegistry.createComposable {
        registerCupertinoFlatComponents()
    }
    return remember(registry) {
        StyledThemeDefinition(name = "Cupertino Flat", registry = registry)
    }
}

@Composable
fun StyledRegistry.Builder.registerCupertinoFlatComponents() {
    register(CupertinoFlatBadge.create())
    register(CupertinoFlatButton.create())
    register(CupertinoFlatDialog.create())
    register(CupertinoFlatBottomSheet.create())
    register(CupertinoFlatChip.create())
    register(CupertinoFlatIconButton.create())
    register(CupertinoFlatPopup.create())
    register(CupertinoFlatPopupMenu.create())
    register(CupertinoFlatTooltip.create())
    register(CupertinoFlatDropdownMenu.create())
    register(CupertinoFlatCard.create())
    register(CupertinoFlatListItem.create())
    register(CupertinoFlatIcon.create())
    register(CupertinoFlatScaffold.create())
    register(CupertinoFlatTopBar.create())
    register(CupertinoFlatBottomNavigationBar.create())
    register(CupertinoFlatSnackbar.create())
    register(CupertinoFlatSnackbarHost.create())
    register(CupertinoFlatFloatingActionButton.create())
    register(CupertinoFlatSurface.create())
    register(CupertinoFlatSideNavigation.create())
    register(CupertinoFlatCheckbox.create())
    register(CupertinoFlatRadioButton.create())
    register(CupertinoFlatSwitch.create())
    register(CupertinoFlatInput.create())
    register(CupertinoFlatProgressIndicator.create())
    register(CupertinoFlatSlider.create())
    register(CupertinoFlatRangeSlider.create())
    register(CupertinoFlatTabRow.create())
    register(CupertinoFlatTab.create())
    register(CupertinoFlatSegmentedButton.create())
    register(CupertinoFlatSegmentedButtonRow.create())
}