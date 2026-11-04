package com.michaelflisar.composestyled.ui.icons.material3

import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.classes.toIconComposable
import com.michaelflisar.composestyled.ui.icons.StyledIconSet
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsAccountCircle
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsAdd
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsArrowBack
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsArrowForward
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsCheck
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsChevronRight
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsClear
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsClose
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsContentCopy
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsContentCut
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsContentPaste
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsDelete
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsEdit
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsFavorite
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsFilterList
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsHelp
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsHome
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsInfo
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsKeyboardArrowDown
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsMenu
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsMoreVert
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsNotifications
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsRefresh
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsRemove
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsSave
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsSearch
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsSettings
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsShare
import com.michaelflisar.composestyled.ui.icons.material3.filled.MaterialIconsSort

/** Filled Material icon mapping for the Core semantic icon set. */
object Material3FilledIconSet : StyledIconSet {

    private val icons = mapOf(
        StyledIcons.Name.Add to MaterialIconsAdd.toIconComposable(),
        StyledIcons.Name.NavigateBack to MaterialIconsArrowBack.toIconComposable(),
        StyledIcons.Name.Cancel to MaterialIconsClose.toIconComposable(),
        StyledIcons.Name.Check to MaterialIconsCheck.toIconComposable(),
        StyledIcons.Name.Clear to MaterialIconsClear.toIconComposable(),
        StyledIcons.Name.Close to MaterialIconsClose.toIconComposable(),
        StyledIcons.Name.Delete to MaterialIconsDelete.toIconComposable(),
        StyledIcons.Name.Dropdown to MaterialIconsKeyboardArrowDown.toIconComposable(),
        StyledIcons.Name.Edit to MaterialIconsEdit.toIconComposable(),
        StyledIcons.Name.Menu to MaterialIconsMenu.toIconComposable(),
        StyledIcons.Name.More to MaterialIconsMoreVert.toIconComposable(),
        StyledIcons.Name.Remove to MaterialIconsRemove.toIconComposable(),
        StyledIcons.Name.Search to MaterialIconsSearch.toIconComposable(),
        StyledIcons.Name.NavigateForward to MaterialIconsArrowForward.toIconComposable(),
        StyledIcons.Name.Submenu to MaterialIconsChevronRight.toIconComposable(),
        StyledIcons.Name.Refresh to MaterialIconsRefresh.toIconComposable(),
        StyledIcons.Name.Save to MaterialIconsSave.toIconComposable(),
        StyledIcons.Name.Home to MaterialIconsHome.toIconComposable(),
        StyledIcons.Name.Settings to MaterialIconsSettings.toIconComposable(),
        StyledIcons.Name.Account to MaterialIconsAccountCircle.toIconComposable(),
        StyledIcons.Name.Notifications to MaterialIconsNotifications.toIconComposable(),
        StyledIcons.Name.Info to MaterialIconsInfo.toIconComposable(),
        StyledIcons.Name.Help to MaterialIconsHelp.toIconComposable(),
        StyledIcons.Name.Favorite to MaterialIconsFavorite.toIconComposable(),
        StyledIcons.Name.Share to MaterialIconsShare.toIconComposable(),
        StyledIcons.Name.Filter to MaterialIconsFilterList.toIconComposable(),
        StyledIcons.Name.Sort to MaterialIconsSort.toIconComposable(),
        StyledIcons.Name.Copy to MaterialIconsContentCopy.toIconComposable(),
        StyledIcons.Name.Paste to MaterialIconsContentPaste.toIconComposable(),
        StyledIcons.Name.Cut to MaterialIconsContentCut.toIconComposable(),
    )

    override fun resolve(icon: StyledIcons.Name): IconComposable = icons.getValue(icon)
}