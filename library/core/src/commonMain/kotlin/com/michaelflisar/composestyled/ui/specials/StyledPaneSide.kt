package com.michaelflisar.composestyled.ui.specials

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.runtime.LocalStyledContext
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledIconButton
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.primitives.StyledVerticalDivider

object StyledPaneSide {
    enum class Side {
        Left,
        Right,
    }
}

/**
 * Applies and consumes [windowInsets] around the header, expanded content and collapsed label.
 * The divider remains full-height, and inset padding counts toward the pane's width.
 */
@Composable
fun StyledPaneSide(
    modifier: Modifier = Modifier,
    side: StyledPaneSide.Side,
    label: String,
    expanded: MutableState<Boolean>,
    icon: @Composable () -> Unit = { DefaultPaneIcon(label) },
    divider: Boolean = true,
    showLabelWhenCollapsed: Boolean = true,
    collapsible: Boolean = true,
    windowInsets: WindowInsets = WindowInsets(0, 0, 0, 0),
    content: @Composable ColumnScope.() -> Unit,
) {
    val isLeft = side == StyledPaneSide.Side.Left
    val isExpanded = expanded.value

    Row(modifier = modifier) {
        if (!isLeft && divider) {
            StyledVerticalDivider()
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f, fill = false)
                .windowInsetsPadding(windowInsets),
            horizontalAlignment = if (isLeft) Alignment.Start else Alignment.End,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (isLeft) {
                    if (collapsible) {
                        ExpandIcon(
                            expanded = expanded,
                            icon = icon,
                        )
                    }
                    Title(label, isExpanded)
                } else {
                    Title(label, isExpanded)
                    if (collapsible) {
                        ExpandIcon(
                            expanded = expanded,
                            icon = icon,
                        )
                    }
                }
            }

            Box(
                modifier = Modifier.fillMaxHeight().align(Alignment.CenterHorizontally),
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    modifier = Modifier.align(if (isLeft) Alignment.CenterStart else Alignment.CenterEnd),
                    visible = isExpanded,
                    enter = fadeIn() + expandHorizontally(
                        expandFrom = if (isLeft) Alignment.End else Alignment.Start,
                    ),
                    exit = fadeOut() + shrinkHorizontally(
                        shrinkTowards = if (isLeft) Alignment.End else Alignment.Start,
                    ),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(StyledTheme.padding.medium),
                        verticalArrangement = Arrangement.spacedBy(StyledTheme.spacing.medium),
                    ) {
                        content()
                    }
                }

                if (showLabelWhenCollapsed) {
                    androidx.compose.animation.AnimatedVisibility(
                        modifier = Modifier.align(Alignment.Center).zIndex(1f),
                        visible = !isExpanded,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        VerticalTitle(side, label) {
                            expanded.value = !expanded.value
                        }
                    }
                }
            }
        }
        if (isLeft && divider) {
            StyledVerticalDivider()
        }
    }
}

@OptIn(InternalComposeStyledApi::class)
@Composable
private fun ExpandIcon(
    expanded: MutableState<Boolean>,
    icon: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalStyledContext provides StyledSidePane.Context.IconSlot,
    ) {
        StyledIconButton(
            onClick = { expanded.value = !expanded.value },
        ) {
            icon()
        }
    }
}

@Composable
internal fun DefaultPaneIcon(label: String) {
    StyledIcon(
        icon = StyledIcons.Menu,
        contentDescription = "Toggle $label pane",
    )
}

@Composable
private fun Title(
    label: String,
    expanded: Boolean,
) {
    Box(
        modifier = Modifier.height(StyledTheme.sizes.minimumInteractiveSize),
        contentAlignment = Alignment.CenterStart,
    ) {
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally(),
        ) {
            StyledText(
                modifier = Modifier.padding(horizontal = StyledTheme.padding.medium),
                text = label,
                style = StyledTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun VerticalTitle(
    side: StyledPaneSide.Side,
    label: String,
    onClick: () -> Unit,
) {
    val rotation = if (side == StyledPaneSide.Side.Left) -90f else 90f
    Row(
        modifier = Modifier
            .clip(StyledTheme.shapes.small)
            .clickable(role = Role.Button, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        StyledText(
            modifier = Modifier
                .padding(StyledTheme.padding.medium)
                .vertical()
                .rotate(rotation),
            text = label,
            style = StyledTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
    }
}

private fun Modifier.vertical() = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    layout(placeable.height, placeable.width) {
        placeable.place(
            x = -(placeable.width / 2 - placeable.height / 2),
            y = -(placeable.height / 2 - placeable.width / 2),
        )
    }
}
