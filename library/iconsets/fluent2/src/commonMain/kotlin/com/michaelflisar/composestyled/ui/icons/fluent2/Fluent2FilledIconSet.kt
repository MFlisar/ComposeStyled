package com.michaelflisar.composestyled.ui.icons.fluent2

import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.classes.toIconComposable
import com.michaelflisar.composestyled.ui.icons.StyledIconSet
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsAlert
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsAdd
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsArrowLeft
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsArrowRight
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsArrowSort
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsArrowSync
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsCheckmark
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsChevronDown
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsChevronRight
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsClipboardPaste
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsCopy
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsCut
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsDelete
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsDismiss
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsEdit
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsFilter
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsHeart
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsHome
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsInfo
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsLineHorizontal3
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsMoreHorizontal
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsPersonCircle
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsQuestionCircle
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsSave
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsSearch
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsSettings
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsShare
import com.michaelflisar.composestyled.ui.icons.fluent2.filled.FluentuiSystemIconsSubtract

/** Filled Fluent UI System icon mapping for the Core semantic icon set. */
object Fluent2FilledIconSet : StyledIconSet {

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