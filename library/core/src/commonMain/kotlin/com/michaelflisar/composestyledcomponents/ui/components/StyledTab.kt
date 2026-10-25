package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.registry.LocalStyledRegistry
import com.michaelflisar.composestyledcomponents.registry.StyledComponent
import com.michaelflisar.composestyledcomponents.registry.StyledKey

object StyledTab {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledTab")

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation> {
        override val key: StyledKey<Implementation>
            get() = Key

        @Composable
        fun Render(
            selected: Boolean,
            onClick: () -> Unit,
            modifier: Modifier,
            enabled: Boolean,
            selectedContentColor: Color,
            unselectedContentColor: Color,
            interactionSource: MutableInteractionSource?,
            content: @Composable ColumnScope.() -> Unit,
        )
    }
}

// -------------------------
// public API
// -------------------------

@Composable
fun StyledTab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedContentColor: Color = StyledTheme.colorScheme.primary,
    unselectedContentColor: Color = StyledTheme.currentContentColor,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    LocalStyledRegistry.current[StyledTab.Key].Render(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        selectedContentColor = selectedContentColor,
        unselectedContentColor = unselectedContentColor,
        interactionSource = interactionSource,
        content = content,
    )
}
