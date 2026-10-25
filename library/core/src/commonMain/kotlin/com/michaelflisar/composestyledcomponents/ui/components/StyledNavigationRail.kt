package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.registry.LocalStyledRegistry
import com.michaelflisar.composestyledcomponents.registry.StyledComponent
import com.michaelflisar.composestyledcomponents.registry.StyledKey

object StyledNavigationRail {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledNavigationRail")

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
            header: @Composable (() -> Unit)?,
            content: @Composable ColumnScope.() -> Unit,
        )

        @Composable
        fun RenderItem(
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

/** Start-side navigation. Scaffold handles placement and safe areas around the rail. */
@Composable
fun StyledNavigationRail(
    modifier: Modifier = Modifier,
    containerColor: Color = StyledTheme.colorScheme.surfaceVariant,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    header: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    LocalStyledRegistry.current[StyledNavigationRail.Key].Render(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        header = header,
        content = content,
    )
}


@Composable
fun <K : Any> StyledNavigationRail(
    items: List<StyledNavigationItem<K>>,
    selectedKey: K?,
    onItemClick: (StyledNavigationItem<K>) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = StyledTheme.colorScheme.surfaceVariant,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    header: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
) {
    StyledNavigationRail(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        header = header,
    ) {
        items.forEach { item ->
            key(item.key) {
                StyledNavigationRailItem(
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


@Composable
fun StyledNavigationRailItem(
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
    LocalStyledRegistry.current[StyledNavigationRail.Key].RenderItem(
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
