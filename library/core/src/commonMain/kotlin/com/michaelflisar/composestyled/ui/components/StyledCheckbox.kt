package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledComponentWithDefaults
import com.michaelflisar.composestyled.registry.StyledKey
import com.michaelflisar.composestyled.ui.internal.StyledToggleItem

object StyledCheckbox {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledCheckbox")
        val DefaultsKey = StyledKey<Defaults>("StyledCheckboxDefaults")
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
// defaults
// -------------------------

object StyledCheckboxDefaults {

    val clickBehavior: StyledToggleBehavior
        @Composable get() = LocalStyledRegistry.current[StyledCheckbox.DefaultsKey].clickBehavior

    val shape: Shape
        @Composable get() = LocalStyledRegistry.current[StyledCheckbox.DefaultsKey].shape
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
    clickBehavior: StyledToggleBehavior = StyledCheckboxDefaults.clickBehavior,
    shape: Shape = StyledCheckboxDefaults.shape,
    label: @Composable (() -> Unit)? = null,
) {
    StyledCheckbox(
        checked = checked.value,
        onCheckedChange = { checked.value = it },
        modifier = modifier,
        enabled = enabled,
        tint = tint,
        onTint = onTint,
        interactionSource = interactionSource,
        clickBehavior = clickBehavior,
        shape = shape,
        label = label,
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
    clickBehavior: StyledToggleBehavior = StyledCheckboxDefaults.clickBehavior,
    shape: Shape = StyledCheckboxDefaults.shape,
    label: @Composable (() -> Unit)? = null,
) {
    val implementation = LocalStyledRegistry.current[StyledCheckbox.Key]
    StyledToggleItem(
        value = checked,
        onValueChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        behavior = clickBehavior,
        shape = shape,
        role = androidx.compose.ui.semantics.Role.Checkbox,
        label = label,
    ) { controlModifier, controlOnValueChange ->
        implementation.Render(
            checked = checked,
            onCheckedChange = controlOnValueChange,
            modifier = controlModifier,
            enabled = enabled,
            tint = tint,
            onTint = onTint,
            interactionSource = interactionSource,
        )
    }
}


