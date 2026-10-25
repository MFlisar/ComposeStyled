package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.registry.LocalStyledRegistry
import com.michaelflisar.composestyledcomponents.registry.StyledComponent
import com.michaelflisar.composestyledcomponents.registry.StyledKey

object StyledBottomNavigationBar {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledBottomNavigationBar")

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation> {
        override val key: StyledKey<Implementation>
            get() = Key

        @Composable
        fun Render(
            modifier: Modifier,
            containerColor: Color,
            contentColor: Color,
            windowInsets: WindowInsets,
            content: @Composable RowScope.() -> Unit,
        )

        @Composable
        fun RenderItem(
            rowScope: RowScope,
            selected: Boolean,
            onClick: () -> Unit,
            modifier: Modifier,
            enabled: Boolean,
            alwaysShowLabel: Boolean,
            selectedContentColor: Color,
            unselectedContentColor: Color,
            indicatorColor: Color,
            interactionSource: MutableInteractionSource?,
            icon: @Composable () -> Unit,
            label: @Composable (() -> Unit)?,
        )
    }
}

// -------------------------
// public API
// -------------------------

/** Bottom navigation containing equally weighted StyledBottomNavigationBarItem destinations. */
@Composable
fun StyledBottomNavigationBar(
    modifier: Modifier = Modifier,
    containerColor: Color = StyledTheme.colorScheme.surfaceVariant,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    windowInsets: WindowInsets = WindowInsets.safeDrawing.only(
        WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal
    ),
    content: @Composable RowScope.() -> Unit,
) {
    LocalStyledRegistry.current[StyledBottomNavigationBar.Key].Render(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        windowInsets = windowInsets,
        content = content,
    )
}

/** Renders shared destinations; a null selectedKey leaves all items unselected. */
@Composable
fun <K : Any> StyledBottomNavigationBar(
    items: List<StyledNavigationItem<K>>,
    selectedKey: K?,
    onItemClick: (StyledNavigationItem<K>) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = StyledTheme.colorScheme.surfaceVariant,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    windowInsets: WindowInsets = WindowInsets.safeDrawing.only(
        WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal
    ),
    alwaysShowLabel: Boolean = true,
) {
    StyledBottomNavigationBar(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        windowInsets = windowInsets,
    ) {
        items.forEach { item ->
            key(item.key) {
                StyledBottomNavigationBarItem(
                    selected = item.key == selectedKey,
                    onClick = { onItemClick(item) },
                    enabled = item.enabled,
                    alwaysShowLabel = alwaysShowLabel,
                    label = item.label,
                    icon = item.icon,
                )
            }
        }
    }
}

/** Selection and navigation are controlled by the caller. Use a label or an accessible icon. */
@Composable
fun RowScope.StyledBottomNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    alwaysShowLabel: Boolean = true,
    selectedContentColor: Color = StyledTheme.colorScheme.primary,
    unselectedContentColor: Color = StyledTheme.currentContentColor,
    indicatorColor: Color = StyledTheme.colorScheme.primarySurface,
    interactionSource: MutableInteractionSource? = null,
    label: @Composable (() -> Unit)? = null,
    icon: @Composable () -> Unit,
) {
    LocalStyledRegistry.current[StyledBottomNavigationBar.Key].RenderItem(
        rowScope = this,
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        alwaysShowLabel = alwaysShowLabel,
        selectedContentColor = selectedContentColor,
        unselectedContentColor = unselectedContentColor,
        indicatorColor = indicatorColor,
        interactionSource = interactionSource,
        icon = icon,
        label = label,
    )
}
