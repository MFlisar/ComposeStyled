package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledKey
import com.michaelflisar.composestyled.tokens.minimumInteractiveSize

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
            text: @Composable ColumnScope.() -> Unit,
        )
    }
}

// -------------------------
// public API
// -------------------------

/** A selectable tab item intended to be placed inside a tab row. */
@Composable
fun StyledTab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedContentColor: Color = Color.Unspecified,
    unselectedContentColor: Color = Color.Unspecified,
    interactionSource: MutableInteractionSource? = null,
    text: @Composable ColumnScope.() -> Unit,
) {
    LocalStyledRegistry.current[StyledTab.Key].Render(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        selectedContentColor = selectedContentColor,
        unselectedContentColor = unselectedContentColor,
        interactionSource = interactionSource,
        text = text,
    )
}
