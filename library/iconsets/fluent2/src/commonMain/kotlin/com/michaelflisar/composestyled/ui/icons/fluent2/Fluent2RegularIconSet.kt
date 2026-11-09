package com.michaelflisar.composestyled.ui.icons.fluent2

import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.classes.toIconComposable
import com.michaelflisar.composestyled.ui.icons.StyledIconSet
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsAlert
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsAdd
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsArrowLeft
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsArrowRight
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsArrowSort
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsArrowSync
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsCheckmark
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsChevronDown
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsChevronLeft
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsChevronRight
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsClipboardPaste
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsCopy
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsCut
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsDelete
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsDismiss
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsEdit
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsFilter
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsHeart
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsHome
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsInfo
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsLineHorizontal3
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsMoreHorizontal
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsPersonCircle
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsQuestionCircle
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsSave
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsSearch
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsSettings
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsShare
import com.michaelflisar.composestyled.ui.icons.fluent2.regular.FluentuiSystemIconsSubtract

/** Regular Fluent UI System icon mapping for the Core semantic icon set. */
object Fluent2RegularIconSet : StyledIconSet {

    private val icons = mapOf(
        StyledIcons.Name.Add to FluentuiSystemIconsAdd.toIconComposable(),
        StyledIcons.Name.NavigateBack to FluentuiSystemIconsArrowLeft.toIconComposable(),
        StyledIcons.Name.Cancel to FluentuiSystemIconsDismiss.toIconComposable(),
        StyledIcons.Name.Check to FluentuiSystemIconsCheckmark.toIconComposable(),
        StyledIcons.Name.Clear to FluentuiSystemIconsDismiss.toIconComposable(),
        StyledIcons.Name.Close to FluentuiSystemIconsDismiss.toIconComposable(),
        StyledIcons.Name.Delete to FluentuiSystemIconsDelete.toIconComposable(),
        StyledIcons.Name.Dropdown to FluentuiSystemIconsChevronDown.toIconComposable(),
        StyledIcons.Name.Edit to FluentuiSystemIconsEdit.toIconComposable(),
        StyledIcons.Name.Menu to FluentuiSystemIconsLineHorizontal3.toIconComposable(),
        StyledIcons.Name.More to FluentuiSystemIconsMoreHorizontal.toIconComposable(),
        StyledIcons.Name.Remove to FluentuiSystemIconsSubtract.toIconComposable(),
        StyledIcons.Name.Search to FluentuiSystemIconsSearch.toIconComposable(),
        StyledIcons.Name.NavigateForward to FluentuiSystemIconsArrowRight.toIconComposable(),
        StyledIcons.Name.Submenu to FluentuiSystemIconsChevronRight.toIconComposable(),
        StyledIcons.Name.SubmenuBack to FluentuiSystemIconsChevronLeft.toIconComposable(),
        StyledIcons.Name.Refresh to FluentuiSystemIconsArrowSync.toIconComposable(),
        StyledIcons.Name.Save to FluentuiSystemIconsSave.toIconComposable(),
        StyledIcons.Name.Home to FluentuiSystemIconsHome.toIconComposable(),
        StyledIcons.Name.Settings to FluentuiSystemIconsSettings.toIconComposable(),
        StyledIcons.Name.Account to FluentuiSystemIconsPersonCircle.toIconComposable(),
        StyledIcons.Name.Notifications to FluentuiSystemIconsAlert.toIconComposable(),
        StyledIcons.Name.Info to FluentuiSystemIconsInfo.toIconComposable(),
        StyledIcons.Name.Help to FluentuiSystemIconsQuestionCircle.toIconComposable(),
        StyledIcons.Name.Favorite to FluentuiSystemIconsHeart.toIconComposable(),
        StyledIcons.Name.Share to FluentuiSystemIconsShare.toIconComposable(),
        StyledIcons.Name.Filter to FluentuiSystemIconsFilter.toIconComposable(),
        StyledIcons.Name.Sort to FluentuiSystemIconsArrowSort.toIconComposable(),
        StyledIcons.Name.Copy to FluentuiSystemIconsCopy.toIconComposable(),
        StyledIcons.Name.Paste to FluentuiSystemIconsClipboardPaste.toIconComposable(),
        StyledIcons.Name.Cut to FluentuiSystemIconsCut.toIconComposable(),
    )

    override fun resolve(icon: StyledIcons.Name): IconComposable = icons.getValue(icon)
}