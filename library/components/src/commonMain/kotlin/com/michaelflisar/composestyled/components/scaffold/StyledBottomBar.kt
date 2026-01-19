package com.michaelflisar.composestyled.components.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.components.icons.IconMoreVert
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.components.StyledIcon
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.ProvideStyledLocals

@OptIn(InternalComposeStyledApi::class)
@Composable
fun StyledBottomBar(
    items: List<StyledNavigationItem>,
    modifier: Modifier = Modifier.fillMaxWidth(),
    maxItemsInBottomNavigation: Int = 5,
    containerColor: Color = StyledTheme.colors.surfaceVariant,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
) {
    val density = LocalDensity.current
    val navBarBottomDp = with(density) { WindowInsets.navigationBars.getBottom(this).toDp() }

    ProvideStyledLocals(
        backgroundColor = containerColor,
        contentColor = contentColor
    ) {
        // Background should draw to the very bottom.
        // Only the *content* should be inset above the navigation bar.
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(containerColor)
                // Content padding includes the nav-bar inset; background does not.
                .padding(bottom = navBarBottomDp)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val displayedItems = if (items.size > maxItemsInBottomNavigation) {
                items.take(maxItemsInBottomNavigation - 1)
            } else {
                items
            }
            val itemMore = if (items.size > maxItemsInBottomNavigation) {
                // TODO popup
                StyledNavigationItem(
                    selected = false,
                    onClick = {
                        // popup menu anzeigen
                    },
                    icon = {
                        StyledIcon(
                            imageVector = IconMoreVert,
                            contentDescription = null
                        )
                    },
                    label = null
                )
            } else {
                null
            }
            val allItems = if (itemMore != null) {
                displayedItems + itemMore
            } else {
                displayedItems
            }

            allItems.forEach { item ->
                ItemContent(
                    item = item,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ItemContent(
    item: StyledNavigationItem,
    modifier: Modifier,
) {
    val modifier = modifier
        .defaultMinSize(
            minHeight = StyledTheme.sizes.minimumInteractiveSize,
            minWidth = StyledTheme.sizes.minimumInteractiveSize
        )
        .clip(StyledTheme.shapes.control)
        .clickable(
            enabled = item.enabled,
            role = Role.Tab,
            onClick = item.onClick
        )
        .padding(horizontal = StyledTheme.paddings.medium, vertical = StyledTheme.paddings.medium)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        item.icon?.invoke()
        item.label?.let {
            if (item.icon != null) {
                Spacer(Modifier.height(StyledTheme.spacings.small))
            }
            it()
        }
    }
}