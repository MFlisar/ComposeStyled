package com.michaelflisar.composestyled.foundation.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledListItem

/** Shared list-item layout; nativeContent may replace only the main area's layout. */
@InternalComposeStyledApi
@Composable
fun FoundationListItem(
    onClick: (() -> Unit)?,
    modifier: Modifier,
    enabled: Boolean,
    interactionSource: MutableInteractionSource?,
    shape: Shape,
    minHeight: Dp,
    twoLineMinHeight: Dp,
    threeLineMinHeight: Dp,
    contentPadding: PaddingValues,
    spacing: Dp,
    containerColor: Color,
    contentColor: Color,
    supportingColor: Color,
    borderColor: Color,
    leading: (@Composable () -> Unit)?,
    actions: (@Composable RowScope.() -> Unit)?,
    content: StyledListItem.Content,
    defaultContainerColor: Color,
    defaultContentColor: Color,
    defaultSupportingColor: Color,
    headlineStyle: TextStyle,
    supportingStyle: TextStyle,
    overlineStyle: TextStyle,
    nativeContent: (@Composable (Modifier, Color, Color) -> Unit)? = null,
) {
    val background = containerColor.takeUnless { it == Color.Unspecified } ?: defaultContainerColor
    val foreground = contentColor.takeUnless { it == Color.Unspecified } ?: defaultContentColor
    val secondary = supportingColor.takeUnless { it == Color.Unspecified }
        ?: contentColor.takeUnless { it == Color.Unspecified }
        ?: defaultSupportingColor
    val border = borderColor.takeUnless { it == Color.Unspecified }
    val factor = if (enabled) 1f else StyledTheme.colorScheme.disableFactors.content
    val mainColor = foreground.copy(alpha = foreground.alpha * factor)
    val secondaryColor = secondary.copy(alpha = secondary.alpha * factor)
    val semantic = content as? StyledListItem.Content.Semantic
    val threeLine = semantic?.overline != null && semantic.supporting != null
    val minimum = when {
        threeLine -> maxOf(minHeight, threeLineMinHeight)
        semantic?.overline != null || semantic?.supporting != null ->
            maxOf(minHeight, twoLineMinHeight)
        else -> minHeight
    }
    val layoutDirection = LocalLayoutDirection.current
    val mainPadding = PaddingValues(
        start = contentPadding.calculateStartPadding(layoutDirection),
        top = contentPadding.calculateTopPadding(),
        end = if (actions != null) spacing else contentPadding.calculateEndPadding(layoutDirection),
        bottom = contentPadding.calculateBottomPadding(),
    )

    UpdateStyledThemeLocals(contentColor = foreground, textStyle = headlineStyle) {
        Layout(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = minimum)
                .background(background, shape)
                .then(if (border != null) Modifier.border(1.dp, border, shape) else Modifier)
                .clip(shape)
                .semantics(mergeDescendants = true) {
                    if (!enabled) disabled()
                }
                .then(
                    if (onClick != null) Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = LocalIndication.current,
                        enabled = enabled,
                        onClick = onClick,
                    ) else Modifier
                ),
            content = {
                Box(
                    contentAlignment = Alignment.CenterStart,
                    propagateMinConstraints = true,
                ) {
                    UpdateStyledThemeLocals(contentColor = mainColor) {
                        if (nativeContent != null) {
                            nativeContent(Modifier.fillMaxWidth(), mainColor, secondaryColor)
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(mainPadding),
                                horizontalArrangement = Arrangement.spacedBy(spacing),
                                verticalAlignment = if (threeLine) Alignment.Top
                                    else Alignment.CenterVertically,
                            ) {
                                leading?.invoke()
                                Column(
                                    modifier = Modifier.weight(1f),
                                ) {
                                    when (content) {
                                        is StyledListItem.Content.Custom -> content.content(this)
                                        is StyledListItem.Content.Semantic -> {
                                            content.overline?.let {
                                                UpdateStyledThemeLocals(
                                                    contentColor = secondaryColor,
                                                    textStyle = overlineStyle,
                                                    content = it,
                                                )
                                            }
                                            UpdateStyledThemeLocals(
                                                textStyle = headlineStyle,
                                                content = content.headline,
                                            )
                                            content.supporting?.let {
                                                UpdateStyledThemeLocals(
                                                    contentColor = secondaryColor,
                                                    textStyle = supportingStyle,
                                                    content = it,
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (actions != null) {
                    Row(
                        modifier = Modifier.padding(
                            top = contentPadding.calculateTopPadding(),
                            bottom = contentPadding.calculateBottomPadding(),
                            end = contentPadding.calculateEndPadding(layoutDirection),
                        ),
                        horizontalArrangement = Arrangement.spacedBy(spacing),
                        verticalAlignment = Alignment.CenterVertically,
                        content = actions,
                    )
                }
            },
        ) { measurables, constraints ->
            val action = measurables.getOrNull(1)?.measure(
                constraints.copy(minWidth = 0, minHeight = 0),
            )
            val actionWidth = action?.width ?: 0
            val mainConstraints = constraints.offset(horizontal = -actionWidth).copy(
                minHeight = maxOf(constraints.minHeight, action?.height ?: 0),
            )
            val main = measurables.first().measure(mainConstraints)
            val width = constraints.constrainWidth(main.width + actionWidth)
            val height = constraints.constrainHeight(main.height)
            layout(width, height) {
                main.placeRelative(0, 0)
                action?.placeRelative(
                    main.width,
                    if (threeLine) 0 else (height - action.height) / 2,
                )
            }
        }
    }
}
