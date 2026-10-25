package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.registry.LocalStyledRegistry
import com.michaelflisar.composestyledcomponents.registry.StyledComponent
import com.michaelflisar.composestyledcomponents.registry.StyledKey

object StyledRadioButton {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledRadioButton")

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation> {
        override val key: StyledKey<Implementation>
            get() = Key

        @Composable
        fun Render(
            selected: Boolean,
            onClick: (() -> Unit)?,
            modifier: Modifier,
            enabled: Boolean,
            tint: Color,
            interactionSource: MutableInteractionSource?,
        )
    }
}

// -------------------------
// public API
// -------------------------

/** With a null callback, the parent owns interaction and accessibility semantics. */
@Composable
fun StyledRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tint: Color = StyledTheme.colorScheme.primary,
    interactionSource: MutableInteractionSource? = null,
) {
    LocalStyledRegistry.current[StyledRadioButton.Key].Render(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        tint = tint,
        interactionSource = interactionSource,
    )
}
