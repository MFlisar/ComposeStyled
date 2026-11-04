package com.michaelflisar.composestyled.ui.icons

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.classes.toIconComposable
import com.michaelflisar.composestyled.registry.LocalStyledRegistry

/**
 * Semantic icons resolved through the active theme's [StyledIconSet].
 *
 * Applications can replace the active set through
 * `StyledTheme(customization = { register(MyIconSet()) })`.
 *
 * MyIconSet must use [StyledIconSet.Key] as key and implement [StyledIconSet] to replace the default set.
 */
object StyledIcons {

    enum class Name {
        Add,
        NavigateBack,
        Cancel,
        Check,
        Clear,
        Close,
        Delete,
        Dropdown,
        Edit,
        Menu,
        More,
        Remove,
        Search,
        NavigateForward,
        Submenu,
        Refresh,
        Save,
        Home,
        Settings,
        Account,
        Notifications,
        Info,
        Help,
        Favorite,
        Share,
        Filter,
        Sort,
        Copy,
        Paste,
        Cut,
    }

    /** Renders a blank 24x24 icon. */
    val Empty: IconComposable = ImageVector.Builder(
        name = "Empty",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).build().toIconComposable()

    /** Create or add a new item. */
    val Add: IconComposable = icon(Name.Add)

    /** Navigate to the previous screen or location. */
    val NavigateBack: IconComposable = icon(Name.NavigateBack)

    /** Cancel an ongoing action. */
    val Cancel: IconComposable = icon(Name.Cancel)

    /** Confirm or accept an action. */
    val Check: IconComposable = icon(Name.Check)

    /** Clear entered content or a selection. */
    val Clear: IconComposable = icon(Name.Clear)

    /** Close or dismiss a dialog, popup, sheet, or overlay. */
    val Close: IconComposable = icon(Name.Close)

    /** Permanently delete an item. */
    val Delete: IconComposable = icon(Name.Delete)

    /** Expand a menu, section, accordion, or dropdown. */
    val Dropdown: IconComposable = icon(Name.Dropdown)

    /** Modify existing content. */
    val Edit: IconComposable = icon(Name.Edit)

    /** Open the primary navigation menu. */
    val Menu: IconComposable = icon(Name.Menu)

    /** Show additional actions that are not directly visible. */
    val More: IconComposable = icon(Name.More)

    /** Remove an item from a collection without necessarily deleting it. */
    val Remove: IconComposable = icon(Name.Remove)

    /** Search for content, items, or actions. */
    val Search: IconComposable = icon(Name.Search)

    /** Navigate to the next screen or location. */
    val NavigateForward: IconComposable = icon(Name.NavigateForward)

    /** Indicate that a menu item opens a submenu. */
    val Submenu: IconComposable = icon(Name.Submenu)

    /** Reload or update content. */
    val Refresh: IconComposable = icon(Name.Refresh)

    /** Persist changes. */
    val Save: IconComposable = icon(Name.Save)

    /** Navigate to the application's main screen. */
    val Home: IconComposable = icon(Name.Home)

    /** Open application or feature settings. */
    val Settings: IconComposable = icon(Name.Settings)

    /** Access the current user's profile or account. */
    val Account: IconComposable = icon(Name.Account)

    /** Display or access notifications. */
    val Notifications: IconComposable = icon(Name.Notifications)

    /** Display informational details. */
    val Info: IconComposable = icon(Name.Info)

    /** Show contextual help or support. */
    val Help: IconComposable = icon(Name.Help)

    /** Mark or unmark an item as a favorite. */
    val Favorite: IconComposable = icon(Name.Favorite)

    /** Share content with another app or user. */
    val Share: IconComposable = icon(Name.Share)

    /** Restrict content using filtering criteria. */
    val Filter: IconComposable = icon(Name.Filter)

    /** Change the ordering of content. */
    val Sort: IconComposable = icon(Name.Sort)

    /** Copy content to the clipboard. */
    val Copy: IconComposable = icon(Name.Copy)

    /** Insert clipboard content into the current context. */
    val Paste: IconComposable = icon(Name.Paste)

    /** Remove selected content and place it on the clipboard. */
    val Cut: IconComposable = icon(Name.Cut)

    private fun icon(name: Name): IconComposable =
        { contentDescription, modifier, tint ->
            LocalStyledRegistry.current[StyledIconSet.Key]
                .resolve(name)
                .invoke(contentDescription, modifier, tint)
        }
}
