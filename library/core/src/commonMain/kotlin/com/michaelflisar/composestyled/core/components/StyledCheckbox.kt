package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.theme.ThemeProperty
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.IVariant
import com.michaelflisar.composestyled.core.classes.TokenMap
import com.michaelflisar.composestyled.core.classes.colors.ColorRef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.icons.Check
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.renderer.StyledTokenComponents
import com.michaelflisar.composestyled.core.renderer.StyledTokenRenderer
import com.michaelflisar.composestyled.core.renderer.StyledWrapperComponents
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.LocalContentColor
import com.michaelflisar.composestyled.core.runtime.interaction.rememberStyledResolveState

object StyledCheckbox {

    // properties
    private val Property = ThemeProperty<ColorSet>("checkbox")

    // tokens
    internal val Tokens = TokenMap.create(Property, Variant.entries.toSet())

    // variants
    enum class Variant(
        override val id: String,
    ) : IVariant {
        Default("checkbox.default"),
    }

    @InternalComposeStyledApi
    @Composable
    fun registerVariantStyles(
        unchecked: StatefulBaseColorDef,
        checked: StatefulBaseColorDef,
    ) {
        Tokens.registerStyles(
            styles = mapOf(
                Variant.Default to ColorSet(unchecked = unchecked, checked = checked),
            )
        )
    }

    fun customize(
        unchecked: ColorRef? = null,
        checked: ColorRef? = null,
    ) = Customization(
        unchecked = unchecked,
        checked = checked,
    )

    data class ColorSet(
        val unchecked: StatefulBaseColorDef,
        val checked: StatefulBaseColorDef,
    )

    @Immutable
    class Customization internal constructor(
        val unchecked: ColorRef?,
        val checked: ColorRef?,
    )
}

// ----------------------
// Renderer
// ----------------------

interface StyledCheckboxTokenRenderer : StyledTokenRenderer {

    @Composable
    fun Render(
        checked: Boolean,
        modifier: Modifier,
        backgroundColor: Color,
        contentColor: Color,
        enabled: Boolean,
        onCheckedChange: ((Boolean) -> Unit)?,
        shape: Shape,
        borderColor: Color?,
        borderWidth: Dp,
        interactionSource: MutableInteractionSource,
        indication: Indication?,
        contentDescription: String?,
        checkIcon: @Composable () -> Unit,
    )
}

interface StyledCheckboxWrapperRenderer {

    @Composable
    fun Render(
        variant: StyledCheckbox.Variant,
        customization: StyledCheckbox.Customization?,
        checked: Boolean,
        onCheckedChange: ((Boolean) -> Unit)?,
        modifier: Modifier,
        enabled: Boolean,
        shape: Shape,
        borderWidth: Dp,
        interactionSource: MutableInteractionSource,
        indication: Indication?,
        contentDescription: String?,
        checkIcon: @Composable () -> Unit,
    )
}

// ----------------------
// Defaults
// ----------------------

object StyledCheckboxDefaults {

    val DefaultVariant = StyledCheckbox.Variant.Default

    val BorderWidth: Dp = 1.dp

    val shape: Shape
        @Composable @ReadOnlyComposable get() = StyledTheme.shapes.control

    @Composable
    fun CheckIcon(
        color: Color = LocalContentColor.current,
    ) {
        StyledIcon(
            imageVector = Check,
            contentDescription = null,
            tint = color,
        )
    }
}

// ----------------------
// Composable
// ----------------------

@Composable
fun StyledCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    variant: StyledCheckbox.Variant = StyledCheckboxDefaults.DefaultVariant,
    customization: StyledCheckbox.Customization? = null,
    enabled: Boolean = true,
    shape: Shape = StyledCheckboxDefaults.shape,
    borderWidth: Dp = StyledCheckboxDefaults.BorderWidth,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication? = null,
    contentDescription: String? = null,
    checkIcon: @Composable () -> Unit = { StyledCheckboxDefaults.CheckIcon() },
    isError: Boolean = false,
) {
    when (val components = LocalStyledComponents.current) {
        is StyledTokenComponents -> {
            val state = rememberStyledResolveState(
                interactionSource = interactionSource,
                enabled = enabled,
                isError = isError,
            )

            val set = StyledCheckbox.Tokens.resolveToken(variant)
            val def = if (checked) set.checked else set.unchecked
            val o = if (checked) customization?.checked else customization?.unchecked

            val defCustomised = def.customise(
                foreground = o
            )
            val resolved = defCustomised.resolve(state)

            components.checkbox.Render(
                checked = checked,
                modifier = modifier,
                enabled = enabled,
                shape = shape,
                backgroundColor = resolved.background,
                contentColor = resolved.foreground,
                borderColor = resolved.border,
                borderWidth = borderWidth,
                interactionSource = interactionSource,
                indication = indication,
                contentDescription = contentDescription,
                checkIcon = checkIcon,
                onCheckedChange = onCheckedChange,
            )
        }

        is StyledWrapperComponents -> {
            components.checkbox.Render(
                variant = variant,
                customization = customization,
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = modifier,
                enabled = enabled,
                shape = shape,
                borderWidth = borderWidth,
                interactionSource = interactionSource,
                indication = indication,
                contentDescription = contentDescription,
                checkIcon = checkIcon,
            )
        }
    }
}
