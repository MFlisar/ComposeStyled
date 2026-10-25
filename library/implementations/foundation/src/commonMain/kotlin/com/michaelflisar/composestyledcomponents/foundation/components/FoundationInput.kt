package com.michaelflisar.composestyledcomponents.foundation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.ProvideCustomStyledContentLocals
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.runtime.LocalStyledContext
import com.michaelflisar.composestyledcomponents.ui.components.StyledInput

@InternalComposeStyledApi
class FoundationInput(
    override val defaults: StyledInput.Defaults,
) : StyledInput.Implementation {

    @Composable
    override fun topInsetToBorder(): Dp = 0.dp

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun Render(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier,
        variant: StyledInput.Variant,
        enabled: Boolean,
        readOnly: Boolean,
        interactionEnabled: Boolean,
        isError: Boolean,
        errorMessage: String?,
        singleLine: Boolean,
        minLines: Int,
        maxLines: Int,
        shape: Shape,
        customization: StyledInput.Customization?,
        textStyle: TextStyle,
        keyboardOptions: KeyboardOptions,
        keyboardActions: KeyboardActions,
        visualTransformation: VisualTransformation,
        interactionSource: MutableInteractionSource?,
        label: @Composable (() -> Unit)?,
        placeholder: @Composable (() -> Unit)?,
        leadingIcon: @Composable (() -> Unit)?,
        trailingIcon: @Composable (() -> Unit)?,
        supportingText: @Composable (() -> Unit)?,
    ) {
        val source = interactionSource ?: remember { MutableInteractionSource() }
        val focused by source.collectIsFocusedAsState()
        val colors = StyledTheme.colorScheme
        val centerLabel = LocalStyledContext.current == StyledInput.Context.NumberPicker
        val alpha = if (enabled) 1f else colors.disableFactors.content
        val customForeground = if (variant == StyledInput.Variant.Alternative) {
            customization?.colors?.filledForeground?.resolve()
        } else {
            customization?.colors?.textForeground?.resolve()
        }?.takeIf { it != Color.Unspecified }
        val contentColor = customForeground ?: colors.onSurface
        val foreground = contentColor.copy(alpha = contentColor.alpha * alpha)
        val customBorder = customization?.border?.resolve()?.takeIf { it != Color.Unspecified }
        val accent = when {
            isError -> colors.error
            focused -> colors.primary
            else -> customBorder ?: colors.outline
        }.let { it.copy(alpha = it.alpha * alpha) }
        val containerColor = if (variant == StyledInput.Variant.Alternative) {
            customization?.colors?.filledBackground?.resolve()
                ?.takeIf { it != Color.Unspecified } ?: colors.surfaceVariant
        } else {
            Color.Transparent
        }
        val indicatorWidth = if (focused) 2.dp else 1.dp
        val indicator = when (variant) {
            StyledInput.Variant.Default -> Modifier.border(indicatorWidth, accent, shape)
            StyledInput.Variant.Alternative -> Modifier.drawBehind {
                val stroke = indicatorWidth.toPx()
                drawLine(
                    color = accent,
                    start = Offset(0f, size.height - stroke / 2),
                    end = Offset(size.width, size.height - stroke / 2),
                    strokeWidth = stroke,
                )
            }
        }
        ProvideCustomStyledContentLocals(contentColor = foreground, textStyle = textStyle) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = modifier.defaultMinSize(minWidth = 240.dp)
                    .semantics {
                        if (isError && errorMessage != null) error(errorMessage)
                    },
                enabled = enabled && interactionEnabled,
                readOnly = readOnly,
                textStyle = textStyle.copy(color = foreground),
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                visualTransformation = visualTransformation,
                singleLine = singleLine,
                minLines = minLines,
                maxLines = maxLines,
                interactionSource = source,
                cursorBrush = SolidColor(accent),
                decorationBox = { innerTextField ->
                    Column(verticalArrangement = Arrangement.spacedBy(StyledTheme.spacing.small)) {
                        Box {
                            Row(
                                modifier = Modifier.clip(shape)
                                    .background(containerColor)
                                    .then(indicator)
                                    .defaultMinSize(
                                        minWidth = 240.dp,
                                        minHeight = StyledTheme.sizes.minimumInteractiveSize,
                                    )
                                    .padding(StyledTheme.padding.medium),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                if (centerLabel) {
                                    leadingIcon?.invoke()
                                    Column {
                                        if (label != null) {
                                            val labelStyle = StyledTheme.typography.labelMedium
                                            val labelHeight = with(LocalDensity.current) {
                                                (
                                                    if (labelStyle.lineHeight.isSp) {
                                                        labelStyle.lineHeight
                                                    } else {
                                                        labelStyle.fontSize
                                                    }
                                                ).toDp()
                                            }
                                            Spacer(Modifier.height(labelHeight))
                                        }
                                        Box {
                                            if (value.isEmpty() && placeholder != null) {
                                                ProvideCustomStyledContentLocals(
                                                    contentColor = colors.onSurfaceVariant.copy(
                                                        alpha = colors.onSurfaceVariant.alpha * alpha
                                                    ),
                                                    content = placeholder,
                                                )
                                            }
                                            innerTextField()
                                        }
                                    }
                                    trailingIcon?.invoke()
                                } else {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacing.small),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        leadingIcon?.invoke()
                                        Column(
                                            verticalArrangement = Arrangement.spacedBy(StyledTheme.spacing.small),
                                        ) {
                                            if (label != null) {
                                                ProvideCustomStyledContentLocals(
                                                    contentColor = accent,
                                                    textStyle = StyledTheme.typography.labelMedium,
                                                ) {
                                                    label()
                                                }
                                            }
                                            Box {
                                                if (value.isEmpty() && placeholder != null) {
                                                    ProvideCustomStyledContentLocals(
                                                        contentColor = colors.onSurfaceVariant.copy(
                                                            alpha = colors.onSurfaceVariant.alpha * alpha
                                                        ),
                                                        content = placeholder,
                                                    )
                                                }
                                                innerTextField()
                                            }
                                        }
                                    }
                                    if (trailingIcon != null) {
                                        Box(
                                            modifier = Modifier.padding(start = StyledTheme.spacing.small),
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            trailingIcon()
                                        }
                                    }
                                }
                            }
                            if (centerLabel && label != null) {
                                Box(
                                    modifier = Modifier.matchParentSize()
                                        .padding(top = StyledTheme.padding.medium),
                                    contentAlignment = Alignment.TopCenter,
                                ) {
                                    ProvideCustomStyledContentLocals(
                                        contentColor = accent,
                                        textStyle = StyledTheme.typography.labelMedium,
                                        content = label,
                                    )
                                }
                            }
                        }
                        if (supportingText != null) {
                            ProvideCustomStyledContentLocals(
                                contentColor = if (isError) accent else foreground,
                                textStyle = StyledTheme.typography.bodySmall,
                                content = supportingText,
                            )
                        }
                    }
                },
            )
        }
    }
}
