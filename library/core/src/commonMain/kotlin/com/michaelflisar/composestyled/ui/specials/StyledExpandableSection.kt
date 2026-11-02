package com.michaelflisar.composestyled.ui.specials

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor
import com.michaelflisar.composestyled.ui.primitives.StyledExpandIcon
import com.michaelflisar.composestyled.utils.ExpandState
import kotlinx.coroutines.flow.first

/**
 * Styling options for a section with a clickable title and animated expandable content.
 * Create a style with [rememberStyledExpandableSectionStyle].
 */
object StyledExpandableSection {
    enum class IconPlacement {
        Left, Right, Hide
    }

    class Style(
        val iconPlacement: IconPlacement,
        val hideIconIfNotExpandable: Boolean,
        val contentPadding: PaddingValues,
        val shape: MutableState<Shape>,
        val borderColor: MutableState<Color>,
        val containerColor: MutableState<Color>,
        val contentColor: MutableState<Color>,
        val applyColorsToTitle: Boolean,
        val applyColorsToContent: Boolean,
    ) {
        internal fun getRootContainerColor(): Color? {
            return if (applyColorsToTitle && applyColorsToContent) {
                containerColor.value.takeIf { it != Color.Unspecified }
            } else {
                null
            }
        }

        internal fun getRootContentColor(): Color? {
            return if (applyColorsToTitle && applyColorsToContent) {
                contentColor.value.takeIf { it != Color.Unspecified }
            } else {
                null
            }
        }

        internal fun getTitleContainerColor(): Color? {
            return if (applyColorsToTitle) {
                containerColor.value.takeIf { it != Color.Unspecified }
            } else {
                null
            }
        }

        internal fun getContentContainerColor(): Color? {
            return if (applyColorsToContent) {
                containerColor.value.takeIf { it != Color.Unspecified }
            } else {
                null
            }
        }

        internal fun getTitleContentColor(): Color? {
            return if (applyColorsToTitle) {
                contentColor.value.takeIf { it != Color.Unspecified }
            } else {
                null
            }
        }

        internal fun getContentContentColor(): Color? {
            return if (applyColorsToContent) {
                contentColor.value.takeIf { it != Color.Unspecified }
            } else {
                null
            }
        }
    }
}

/**
 * Creates a section style with themed defaults and remembered shape/color state.
 * Controls icon placement, content padding, border and whether colors apply to title or content.
 */
@Composable
fun rememberStyledExpandableSectionStyle(
    iconPlacement: StyledExpandableSection.IconPlacement = StyledExpandableSection.IconPlacement.Left,
    hideIconIfNotExpandable: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(all = StyledTheme.padding.medium),
    shape: Shape = StyledTheme.shapes.small,
    containerColor: Color = StyledTheme.colorScheme.surfaceVariant,
    contentColor: Color = StyledTheme.colorScheme.onSurfaceVariant,
    borderColor: Color = StyledTheme.colorScheme.outline,
    applyColorsToTitle: Boolean = true,
    applyColorsToContent: Boolean = false,
): StyledExpandableSection.Style {
    return StyledExpandableSection.Style(
        iconPlacement = iconPlacement,
        hideIconIfNotExpandable = hideIconIfNotExpandable,
        contentPadding = contentPadding,
        shape = remember(shape) { mutableStateOf(shape) },
        borderColor = remember(borderColor) { mutableStateOf(borderColor) },
        containerColor = remember(containerColor) { mutableStateOf(containerColor) },
        contentColor = remember(contentColor) { mutableStateOf(contentColor) },
        applyColorsToTitle = applyColorsToTitle,
        applyColorsToContent = applyColorsToContent
    )
}

/**
 * Displays a section with a saveable expanded state when [expanded] is not supplied.
 * Customize [style] with [rememberStyledExpandableSectionStyle].
 */
@Composable
fun StyledExpandableSection(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    style: StyledExpandableSection.Style = rememberStyledExpandableSectionStyle(),
    expanded: MutableState<Boolean> = rememberSaveable { mutableStateOf(true) },
    canToggle: Boolean = true,
    info: (@Composable () -> Unit)? = null,
    onExpansionAnimationFinished: (expanded: Boolean) -> Unit = {},
    content: @Composable () -> Unit,
) {
    ExpandableSectionContent(
        title = title,
        modifier = modifier,
        style = style,
        expanded = expanded.value,
        canToggle = canToggle,
        info = info,
        onToggle = { expanded.value = !expanded.value },
        onExpansionAnimationFinished = onExpansionAnimationFinished,
        content = content
    )
}

/**
 * Controlled expandable section. Use [onExpansionAnimationFinished] to observe
 * the settled expanded state, including changes made outside [onToggle].
 * Customize [style] with [rememberStyledExpandableSectionStyle].
 * Optionally use [ExpandState.isExpanded] for [expanded] and [ExpandState.toggle] for [onToggle]
 * to share general expansion logic or enforce single expansion with [ExpandState.singleOnly].
 */
@Composable
fun StyledExpandableSection(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    style: StyledExpandableSection.Style = rememberStyledExpandableSectionStyle(),
    expanded: Boolean = true,
    canToggle: Boolean = true,
    info: (@Composable () -> Unit)? = null,
    onToggle: () -> Unit,
    onExpansionAnimationFinished: (expanded: Boolean) -> Unit = {},
    content: @Composable () -> Unit,
) {
    ExpandableSectionContent(
        title = title,
        modifier = modifier,
        style = style,
        expanded = expanded,
        canToggle = canToggle,
        info = info,
        onToggle = onToggle,
        onExpansionAnimationFinished = onExpansionAnimationFinished,
        content = content
    )
}

@OptIn(InternalComposeStyledApi::class)
@Composable
private fun ExpandableSectionContent(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    style: StyledExpandableSection.Style = rememberStyledExpandableSectionStyle(),
    expanded: Boolean = true,
    canToggle: Boolean = true,
    info: (@Composable () -> Unit)? = null,
    onToggle: () -> Unit,
    onExpansionAnimationFinished: (expanded: Boolean) -> Unit,
    content: @Composable () -> Unit,
) {
    val visibility = remember { MutableTransitionState(expanded) }
    visibility.targetState = expanded
    val lastReportedExpanded = remember { mutableStateOf(expanded) }
    val currentOnExpansionAnimationFinished by rememberUpdatedState(onExpansionAnimationFinished)

    LaunchedEffect(expanded) {
        val changed = expanded != lastReportedExpanded.value
        lastReportedExpanded.value = expanded
        if (changed) {
            snapshotFlow { visibility.isIdle && visibility.currentState == expanded }
                .first { it }
            withFrameNanos { }
            currentOnExpansionAnimationFinished(expanded)
        }
    }

    Column(
        modifier = modifier
            .then(
                if (style.borderColor.value != Color.Unspecified) {
                    Modifier.border(1.dp, style.borderColor.value, style.shape.value)
                } else {
                    Modifier
                }
            )
            .clip(style.shape.value)
            .then(
                style.getRootContainerColor()?.let {
                    Modifier.background(it)
                } ?: Modifier
            )
    ) {
        UpdateStyledThemeLocals(
            textStyle = StyledTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (style.borderColor.value != Color.Unspecified) {
                            Modifier
                        } else {
                            Modifier.clip(style.shape.value)
                        }
                    )
                    .then(
                        style.getTitleContainerColor()?.let {
                            Modifier.background(it)
                        } ?: Modifier
                    )
                    .clickable(canToggle) {
                        onToggle()
                    }
                    .padding(
                        start = 0.dp,
                        bottom = StyledTheme.padding.medium,
                        top = StyledTheme.padding.medium,
                        end = 0.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (style.iconPlacement == StyledExpandableSection.IconPlacement.Left && (canToggle || !style.hideIconIfNotExpandable)) {
                    StyledExpandIcon(
                        expanded = expanded,
                        modifier = Modifier,
                        rotationDirection = StyledExpandIcon.RotationDirection.CounterClockwise,
                        color = style.getTitleContentColor() ?: LocalStyledContentColor.current
                    )
                } else {
                    Spacer(modifier = Modifier.width(StyledTheme.padding.medium))
                }

                UpdateStyledThemeLocals(
                    contentColor = style.getTitleContentColor()
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        title()
                    }
                    if (info != null) {
                        info()
                    }
                }

                if (style.iconPlacement == StyledExpandableSection.IconPlacement.Right && (canToggle || !style.hideIconIfNotExpandable)) {
                    StyledExpandIcon(
                        expanded = expanded,
                        rotationDirection = StyledExpandIcon.RotationDirection.Clockwise,
                        color = style.getTitleContentColor() ?: LocalStyledContentColor.current
                    )
                } else {
                    Spacer(modifier = Modifier.width(StyledTheme.padding.medium))
                }
            }
        }
        AnimatedVisibility(
            visibleState = visibility,
            modifier = Modifier.fillMaxWidth()
                .then(
                    style.getContentContainerColor()?.let {
                        Modifier.background(it)
                    } ?: Modifier
                )
        ) {
            UpdateStyledThemeLocals(
                contentColor = style.getContentContentColor()
            ) {
                Column(
                    modifier = Modifier.padding(style.contentPadding)
                ) {
                    content()
                }

            }
        }
    }
}