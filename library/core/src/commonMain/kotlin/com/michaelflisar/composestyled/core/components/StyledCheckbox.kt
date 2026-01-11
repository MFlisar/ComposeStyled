package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.theme.ThemeProperty
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.IVariantId
import com.michaelflisar.composestyled.core.classes.TokenMap
import com.michaelflisar.composestyled.core.classes.Variant
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.classes.customDataOrNull
import com.michaelflisar.composestyled.core.icons.Check
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.renderer.StyledTokenCompontents
import com.michaelflisar.composestyled.core.renderer.StyledTokenRenderer
import com.michaelflisar.composestyled.core.renderer.StyledWrapperComponents
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.LocalContentColor
import com.michaelflisar.composestyled.core.runtime.interaction.rememberStyledResolveState

typealias StyledCheckboxVariant = Variant<StyledCheckbox.VariantId, StyledCheckbox.ColorSet>

object StyledCheckbox {

    // ----------------------
    // Properties
    // ----------------------

    private val Property =
        ThemeProperty<ColorSet>("checkbox")

    // ----------------------
    // VariantId
    // ----------------------

    enum class VariantId(
        override val id: String,
    ) : IVariantId {
        Default("checkbox.default"),
    }

    // ----------------------
    // Variants
    // ----------------------

    object Variants {
        val Default: StyledCheckboxVariant =
            Variant.Token(VariantId.Default)

        fun custom(
            unchecked: StatefulBaseColorDef,
            checked: StatefulBaseColorDef,
        ) = Variant.Custom(
            VariantId.Default,
            ColorSet(unchecked, checked)
        )
    }

    // ----------------------
    // Tokens
    // ----------------------

    internal val Tokens =
        TokenMap.create<VariantId, ColorSet>(Property)

    @InternalComposeStyledApi
    @Composable
    fun registerVariantStyles(
        unchecked: StatefulBaseColorDef,
        checked: StatefulBaseColorDef,
    ) {
        Tokens.registerStyles(
            styles = mapOf(
                VariantId.Default to ColorSet(unchecked, checked)
            )
        )
    }

    data class ColorSet(
        val unchecked: StatefulBaseColorDef,
        val checked: StatefulBaseColorDef,
    )
}

// ----------------------
// Renderer
// ----------------------

interface StyledCheckboxTokenRenderer: StyledTokenRenderer {

    @Composable
    fun Render(
        checked: Boolean,
        modifier: Modifier,
        backgroundColor: Color,
        contentColor: Color,
        enabled: Boolean,
        onCheckedChange: ((Boolean) -> Unit)?,
        shape: Shape,
        borderColor: Color,
        borderWidth: Dp,
        interactionSource: MutableInteractionSource?,
        indication: Indication?,
        contentDescription: String?,
        checkIcon: @Composable () -> Unit,
    )
}

interface StyledCheckboxWrapperRenderer {

    @Composable
    fun Render(
        request: Request,
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

    data class Request(
        val variant: StyledCheckbox.VariantId,
        val customColors: StyledCheckbox.ColorSet?,
    )
}

// ----------------------
// Defaults
// ----------------------

object StyledCheckboxDefaults {

    val BorderWidth: Dp = 1.dp

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
    variant: StyledCheckboxVariant = StyledCheckbox.Variants.Default,
    enabled: Boolean = true,
    shape: Shape = StyledTheme.shapes.checkbox,
    borderWidth: Dp = StyledCheckboxDefaults.BorderWidth,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication? = null,
    contentDescription: String? = null,
    checkIcon: @Composable () -> Unit = { StyledCheckboxDefaults.CheckIcon() },
    isError: Boolean = false,
) {
    when (val components = LocalStyledComponents.current) {
        is StyledTokenCompontents -> {
            val state = rememberStyledResolveState(
                interactionSource = interactionSource,
                enabled = enabled,
                isError = isError,
            )

            val def = StyledCheckbox.Tokens.resolveVariantData(variant)
            val colors = if (checked) def.checked else def.unchecked
            val resolved = colors.resolve(state)

            components.checkbox.Render(
                checked = checked,
                modifier = modifier,
                enabled = enabled,
                shape = shape,
                backgroundColor = resolved.background,
                contentColor = resolved.foreground,
                borderColor = resolved.border ?: Color.Unspecified,
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
                request = StyledCheckboxWrapperRenderer.Request(
                    variant = variant.variantId,
                    customColors = variant.customDataOrNull(),
                ),
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