package com.michaelflisar.composestyled.ui.icons.sfsymbols

import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.ui.icons.StyledIconSet
import com.michaelflisar.composestyled.ui.icons.StyledIcons

/** Semantic icon mapping backed by SF Symbols on iOS and Material icons elsewhere. */
object SFSymbolIconSet : StyledIconSet {

    private val symbols = mapOf(
        StyledIcons.Name.Add to "plus",
        StyledIcons.Name.NavigateBack to "chevron.left",
        StyledIcons.Name.Cancel to "xmark.circle",
        StyledIcons.Name.Check to "checkmark",
        StyledIcons.Name.Clear to "xmark",
        StyledIcons.Name.Close to "xmark",
        StyledIcons.Name.Delete to "trash",
        StyledIcons.Name.Dropdown to "chevron.down",
        StyledIcons.Name.Edit to "pencil",
        StyledIcons.Name.Menu to "line.3.horizontal",
        StyledIcons.Name.More to "ellipsis",
        StyledIcons.Name.Remove to "minus",
        StyledIcons.Name.Search to "magnifyingglass",
        StyledIcons.Name.NavigateForward to "chevron.right",
        StyledIcons.Name.Refresh to "arrow.clockwise",
        StyledIcons.Name.Save to "square.and.arrow.down",
        StyledIcons.Name.Home to "house",
        StyledIcons.Name.Settings to "gear",
        StyledIcons.Name.Account to "person.crop.circle",
        StyledIcons.Name.Notifications to "bell",
        StyledIcons.Name.Info to "info.circle",
        StyledIcons.Name.Help to "questionmark.circle",
        StyledIcons.Name.Favorite to "heart",
        StyledIcons.Name.Share to "square.and.arrow.up",
        StyledIcons.Name.Filter to "line.3.horizontal.decrease",
        StyledIcons.Name.Sort to "arrow.up.arrow.down",
        StyledIcons.Name.Copy to "doc.on.doc",
        StyledIcons.Name.Paste to "doc.on.clipboard",
        StyledIcons.Name.Cut to "scissors",
    )

    override fun resolve(icon: StyledIcons.Name): IconComposable =
        platformIcon(icon, symbols.getValue(icon))
}

internal expect fun platformIcon(icon: StyledIcons.Name, symbol: String): IconComposable
