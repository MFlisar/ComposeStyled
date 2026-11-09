package com.michaelflisar.composestyled.ui.icons.material3

import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.classes.toIconComposable
import com.michaelflisar.composestyled.ui.icons.StyledIconSet
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsAccountCircle
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsAdd
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsArrowBack
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsArrowForward
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsCheck
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsChevronLeft
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsChevronRight
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsClear
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsClose
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsContentCopy
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsContentCut
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsContentPaste
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsDelete
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsEdit
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsFavorite
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsFilterList
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsHelp
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsHome
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsInfo
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsKeyboardArrowDown
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsMenu
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsMoreVert
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsNotifications
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsRefresh
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsRemove
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsSave
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsSearch
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsSettings
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsShare
import com.michaelflisar.composestyled.ui.icons.material3.outlined.MaterialIconsSort

/** Outlined Material icon mapping for the Core semantic icon set. */
object Material3OutlinedIconSet : StyledIconSet {

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
        StyledIcons.Name.SubmenuBack to MaterialIconsChevronLeft.toIconComposable(),
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