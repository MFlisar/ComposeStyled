package com.michaelflisar.composestyledcomponents.ui.specials

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.runtime.LocalStyledContext
import com.michaelflisar.composestyledcomponents.runtime.StyledContext
import com.michaelflisar.composestyledcomponents.tokens.LocalStyledTextStyle
import com.michaelflisar.composestyledcomponents.tokens.minimumInteractiveSize
import com.michaelflisar.composestyledcomponents.ui.components.StyledInputDefaults
import com.michaelflisar.composestyledcomponents.ui.components.StyledTertiaryButton
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText

object StyledGroupBox {
    enum class Context : StyledContext {
        Clickable,
    }
}

@Composable
@OptIn(InternalComposeStyledApi::class)
fun StyledGroupBox(
    modifier: Modifier = Modifier,
    label: String = "",
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = StyledInputDefaults.shape,
    border: BorderStroke = BorderStroke(1.dp, StyledTheme.colorScheme.outline),
    containerColor: Color = StyledTheme.colorScheme.surface,
    contentPadding: PaddingValues = PaddingValues(
        all = StyledTheme.padding.medium
    ),
    contentAlignment: Alignment = Alignment.CenterStart,
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current

    var labelSize by remember { mutableStateOf(IntSize.Zero) }

    val labelHorizontalPadding = 16.dp

    val topPadding = with(density) {
        (labelSize.height / 2).toDp()
    }

    val borderCutout = remember(
        label,
        labelSize,
        density
    ) {
        if (label.isEmpty()) {
            null
        } else {
            val cutoutPaddingPx = with(density) { 4.dp.toPx() }
            val labelStartPx = with(density) { labelHorizontalPadding.toPx() }

            Rect(
                left = labelStartPx - cutoutPaddingPx,
                top = -cutoutPaddingPx,
                right = labelStartPx + labelSize.width + cutoutPaddingPx,
                bottom = labelSize.height / 2f + cutoutPaddingPx
            )
        }
    }

    val parentContext = LocalStyledContext.current

    BoxWithConstraints(modifier = modifier.alpha(if (enabled || onClick != null) 1f else 0.38f)) {
        val fillAvailableWidth = constraints.minWidth == constraints.maxWidth

        Column {
            Box(
                modifier = Modifier
                    .padding(top = topPadding)
                    .height(IntrinsicSize.Min)
                    .background(
                        color = containerColor,
                        shape = shape
                    )
                    .clip(shape)
                    .drawWithContent {
                        drawContent()

                        if (borderCutout != null) {
                            clipRect(
                                left = borderCutout.left,
                                top = borderCutout.top,
                                right = borderCutout.right,
                                bottom = borderCutout.bottom,
                                clipOp = ClipOp.Difference
                            ) {
                                drawOutlineBorder(
                                    shape = shape,
                                    border = border
                                )
                            }
                        } else {
                            drawOutlineBorder(
                                shape = shape,
                                border = border
                            )
                        }
                    }
            ) {
                if (onClick == null) {
                    Box(
                        Modifier
                            .then(if (fillAvailableWidth) Modifier.fillMaxWidth() else Modifier)
                            .minimumInteractiveSize()
                            .padding(contentPadding),
                        contentAlignment = contentAlignment
                    ) {
                        content()
                    }
                } else {
                    CompositionLocalProvider(
                        LocalStyledContext provides StyledGroupBox.Context.Clickable,
                    ) {
                        val textStyle = LocalStyledTextStyle.current
                        StyledTertiaryButton(
                            onClick = onClick,
                            modifier = if (fillAvailableWidth) {
                                Modifier.fillMaxWidth()
                            } else {
                                Modifier
                            },
                            enabled = enabled,
                            shape = shape,
                            interactionSource = interactionSource,
                        ) {
                            CompositionLocalProvider(
                                LocalStyledContext provides parentContext,
                                LocalStyledTextStyle provides textStyle
                            ) {
                                Box(
                                    Modifier
                                        .then(if (fillAvailableWidth) Modifier.fillMaxWidth() else Modifier)
                                        .minimumInteractiveSize()
                                        .padding(contentPadding),
                                    contentAlignment = contentAlignment
                                ) {
                                    content()
                                }
                            }
                        }
                    }
                }
            }
        }

        if (label.isNotEmpty()) {
            StyledText(
                text = label,
                maxLines = 1,
                style = StyledTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .wrapContentWidth()
                    .padding(horizontal = labelHorizontalPadding)
                    //.background(containerColor)

                    .onGloballyPositioned {
                        labelSize = it.size
                    }
            )
        }
    }
}

private fun DrawScope.drawOutlineBorder(
    shape: Shape,
    border: BorderStroke,
) {
    val outline = shape.createOutline(
        size = size,
        layoutDirection = layoutDirection,
        density = this
    )

    drawOutline(
        outline = outline,
        brush = border.brush,
        style = Stroke(
            width = border.width.toPx()
        )
    )
}