package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.registry.LocalStyledRegistry
import com.michaelflisar.composestyledcomponents.registry.StyledComponent
import com.michaelflisar.composestyledcomponents.registry.StyledKey

object StyledCheckbox {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledCheckbox")

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation> {
        override val key: StyledKey<Implementation>
            get() = Key

        @Composable
        fun Render(
            checked: Boolean,
            onCheckedChange: ((Boolean) -> Unit)?,
            modifier: Modifier,
            enabled: Boolean,
            tint: Color,
            onTint: Color,
            interactionSource: MutableInteractionSource?,
        )
    }
}

// -------------------------
// public API
// -------------------------

@Composable
fun StyledCheckbox(
    checked: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tint: Color = StyledTheme.colorScheme.primary,
    onTint: Color = StyledTheme.contentColorFor(tint),
    interactionSource: MutableInteractionSource? = null,
) {
    StyledCheckbox(
        checked = checked.value,
        onCheckedChange = { checked.value = it },
        modifier = modifier,
        enabled = enabled,
        tint = tint,
        onTint = onTint,
        interactionSource = interactionSource,
    )
}

/** With a null callback, the parent owns interaction and accessibility semantics. */
@Composable
fun StyledCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tint: Color = StyledTheme.colorScheme.primary,
    onTint: Color = StyledTheme.contentColorFor(tint),
    interactionSource: MutableInteractionSource? = null,
) {
    LocalStyledRegistry.current[StyledCheckbox.Key].Render(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        tint = tint,
        onTint = onTint,
        interactionSource = interactionSource,
    )
}
