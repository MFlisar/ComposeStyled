package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledComponentWithDefaults
import com.michaelflisar.composestyled.registry.StyledKey
import com.michaelflisar.composestyled.ui.internal.StyledToggleItem

object StyledRadioButton {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledRadioButton")
        val DefaultsKey = StyledKey<Defaults>("StyledRadioButtonDefaults")
    // -------------
    // 2) defaults
    // -------------

    @Stable
    data class Defaults(
        val clickBehavior: StyledToggleBehavior,
        val shape: Shape,
    )

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation>, StyledComponentWithDefaults<Defaults> {
        override val key: StyledKey<Implementation>
            get() = Key

        override val defaultsKey: StyledKey<Defaults>
            get() = DefaultsKey

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
// defaults
// -------------------------

object StyledRadioButtonDefaults {

    val clickBehavior: StyledToggleBehavior
        @Composable get() = LocalStyledRegistry.current[StyledRadioButton.DefaultsKey].clickBehavior

    val shape: Shape
        @Composable get() = LocalStyledRegistry.current[StyledRadioButton.DefaultsKey].shape
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
    clickBehavior: StyledToggleBehavior = StyledRadioButtonDefaults.clickBehavior,
    shape: Shape = StyledRadioButtonDefaults.shape,
    label: @Composable (() -> Unit)? = null,
) {
    val implementation = LocalStyledRegistry.current[StyledRadioButton.Key]
    StyledToggleItem(
        value = selected,
        onValueChange = onClick?.let { click -> { _ -> click() } },
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        behavior = clickBehavior,
        shape = shape,
        role = androidx.compose.ui.semantics.Role.RadioButton,
        label = label,
    ) { controlModifier, controlOnValueChange ->
        implementation.Render(
            selected = selected,
            onClick = if (controlOnValueChange == null) null else onClick,
            modifier = controlModifier,
            enabled = enabled,
            tint = tint,
            interactionSource = interactionSource,
        )
    }
}


