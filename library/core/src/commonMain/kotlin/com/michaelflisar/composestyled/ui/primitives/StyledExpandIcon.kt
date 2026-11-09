package com.michaelflisar.composestyled.ui.primitives

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.icons.StyledIcons

object StyledExpandIcon {
    /** Direction of the expanded content, not the icon's placement. */
    enum class ExpandDirection {
        Down, Up, Start, End
    }

    enum class RotationDirection {
        Clockwise,
        CounterClockwise;

        companion object {
            fun forStartPlacement() = CounterClockwise
            fun forEndPlacement() = Clockwise
        }
    }

    @Stable
    sealed interface IconMode {
        /** [icon] is assumed to point down before direction and state are applied. */
        @Immutable
        class Rotating(
            val icon: IconComposable = StyledIcons.Dropdown,
        ) : IconMode

        /** Icons are used unchanged; [crossfade] optionally animates their replacement. */
        @Immutable
        class Replacing(
            val collapsedIcon: IconComposable,
            val expandedIcon: IconComposable,
            val crossfade: Boolean = false,
        ) : IconMode
    }
}

// -------------------------
// public API
// -------------------------

/**
 * Displays an automatically animated rotating icon or replaces state-specific icons.
 * A rotating icon points in [expandDirection] when collapsed and in the opposite direction
 * when expanded. Start and End follow the current layout direction.
 * [expandDirection] and [rotationDirection] have no effect on replacing icons.
 */
@Composable
fun StyledExpandIcon(
    expanded: Boolean,
    modifier: Modifier = Modifier,
    expandDirection: StyledExpandIcon.ExpandDirection = StyledExpandIcon.ExpandDirection.Down,
    rotationDirection: StyledExpandIcon.RotationDirection = StyledExpandIcon.RotationDirection.CounterClockwise,
    color: Color = LocalStyledContentColor.current,
    iconMode: StyledExpandIcon.IconMode = StyledExpandIcon.IconMode.Rotating(),
    size: Dp? = StyledTheme.sizes.iconSize,
) {
    when (iconMode) {
        is StyledExpandIcon.IconMode.Rotating -> {
            val progress by animateFloatAsState(
                targetValue = if (expanded) 1f else 0f,
                animationSpec = tween(),
                label = "StyledExpandIcon",
            )
            StyledExpandIcon(
                expansionProgress = progress,
                modifier = modifier,
                expandDirection = expandDirection,
                rotationDirection = rotationDirection,
                color = color,
                iconMode = iconMode,
                size = size,
            )
        }

        is StyledExpandIcon.IconMode.Replacing -> {
            if (iconMode.crossfade) {
                Crossfade(
                    targetState = expanded,
                    modifier = modifier.semantics { contentDescription = "expand" },
                    label = "StyledExpandIcon",
                ) { targetExpanded ->
                    StyledIcon(
                        icon = if (targetExpanded) iconMode.expandedIcon else iconMode.collapsedIcon,
                        tint = color,
                        contentDescription = null,
                        size = size,
                    )
                }
            } else {
                StyledIcon(
                    icon = if (expanded) iconMode.expandedIcon else iconMode.collapsedIcon,
                    modifier = modifier,
                    tint = color,
                    contentDescription = "expand",
                    size = size,
                )
            }
        }
    }
}

/**
 * Displays a rotating icon at [expansionProgress], from collapsed (0) to expanded (1).
 * No internal animation is applied. [iconMode] only accepts rotating icons.
 * The icon points in [expandDirection] at 0 and in the opposite direction at 1.
 * Start and End follow the current layout direction.
 *
 * @throws IllegalArgumentException if [expansionProgress] is not finite or outside 0..1.
 */
@Composable
fun StyledExpandIcon(
    expansionProgress: Float,
    modifier: Modifier = Modifier,
    expandDirection: StyledExpandIcon.ExpandDirection = StyledExpandIcon.ExpandDirection.Down,
    rotationDirection: StyledExpandIcon.RotationDirection = StyledExpandIcon.RotationDirection.CounterClockwise,
    color: Color = LocalStyledContentColor.current,
    iconMode: StyledExpandIcon.IconMode.Rotating = StyledExpandIcon.IconMode.Rotating(),
    size: Dp? = StyledTheme.sizes.iconSize,
) {
    require(expansionProgress.isFinite() && expansionProgress in 0f..1f) {
        "expansionProgress must be finite and in 0f..1f, but was $expansionProgress"
    }
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val baseRotation = when (expandDirection) {
        StyledExpandIcon.ExpandDirection.Down -> 0f
        StyledExpandIcon.ExpandDirection.Up -> 180f
        StyledExpandIcon.ExpandDirection.Start -> if (isRtl) -90f else 90f
        StyledExpandIcon.ExpandDirection.End -> if (isRtl) 90f else -90f
    }
    val expandedRotation = when (rotationDirection) {
        StyledExpandIcon.RotationDirection.Clockwise -> 180f
        StyledExpandIcon.RotationDirection.CounterClockwise -> -180f
    }
    StyledIcon(
        modifier = modifier.rotate(baseRotation + expansionProgress * expandedRotation),
        icon = iconMode.icon,
        tint = color,
        contentDescription = "expand",
        size = size,
    )
}