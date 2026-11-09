package com.michaelflisar.demo.demos.showcase.areas

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.toIconComposable
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.specials.StyledSwipeAction
import com.michaelflisar.composestyled.ui.specials.StyledSwipeActionSide
import com.michaelflisar.composestyled.ui.specials.StyledSwipeOverscrollBehavior
import com.michaelflisar.composestyled.ui.specials.StyledSwipeRevealAction
import com.michaelflisar.composestyled.ui.specials.StyledSwipeRevealActionBox
import com.michaelflisar.composestyled.ui.specials.StyledSwipeToActionBox
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.demo.helper.RegionContainer

@Composable
internal fun DemoSwipeActions(baseId: Int, expandState: ExpandState) {
    val message = rememberSaveable { mutableStateOf("Swipe an item to try either pattern.") }

    RegionContainer(
        title = "Swipe Actions",
        id = baseId + 1,
        expanded = expandState,
    ) {
        StyledText(text = message.value)

        StyledColumn(itemSpacing = StyledTheme.spacing.medium) {
            StyledSwipeToActionBox(
                modifier = Modifier.fillMaxWidth(),
                shape = StyledTheme.shapes.medium,
                overscrollBehavior = StyledSwipeOverscrollBehavior.Sticky,
                backgroundColor = StyledTheme.colorScheme.surfaceVariant,
                contentColor = StyledTheme.colorScheme.onSurfaceVariant,
                revealBackgroundColor = Color.Transparent,
                revealContentColor = LocalStyledContentColor.current,
                contentPadding = PaddingValues(
                    horizontal = StyledTheme.padding.medium,
                    vertical = StyledTheme.padding.small,
                ),
                contentAlignment = Alignment.CenterStart,
                actionFromStart = StyledSwipeAction(
                    label = "Archive",
                    icon = Icons.Default.Archive.toIconComposable(),
                    backgroundColorProvider = { scope ->
                        if (scope.willTrigger) StyledTheme.colorScheme.success
                        else Color.Transparent
                    },
                    contentColorProvider = { scope ->
                        if (scope.willTrigger) StyledTheme.colorScheme.onSuccess
                        else LocalStyledContentColor.current
                    },
                ),
                actionFromEnd = StyledSwipeAction(
                    label = "Delete",
                    icon = Icons.Default.Delete.toIconComposable(),
                    backgroundColorProvider = { scope ->
                        val color = animateColorAsState(
                            targetValue = if (scope.willTrigger) StyledTheme.colorScheme.error
                            else Color.Transparent,
                            animationSpec = tween(1000)
                        )
                        color.value
                    },
                    contentColorProvider = { scope ->
                        val color = animateColorAsState(
                            targetValue = if (scope.willTrigger) StyledTheme.colorScheme.onError
                            else LocalStyledContentColor.current,
                        )
                        color.value
                    }
                ),
                onSwipe = { side ->
                    message.value = when (side) {
                        StyledSwipeActionSide.Start -> "Archived by a successful swipe."
                        StyledSwipeActionSide.End -> "Deleted by a successful swipe."
                    }
                }
            ) {
                StyledText(
                    text = "Swipe to trigger"
                )
            }

            StyledSwipeRevealActionBox(
                modifier = Modifier.fillMaxWidth(),
                shape = StyledTheme.shapes.medium,
                backgroundColor = StyledTheme.colorScheme.surfaceVariant,
                contentColor = StyledTheme.colorScheme.onSurfaceVariant,
                contentPadding = PaddingValues(
                    horizontal = StyledTheme.padding.medium,
                    vertical = StyledTheme.padding.small,
                ),
                contentAlignment = Alignment.CenterStart,
                overscrollBehavior = StyledSwipeOverscrollBehavior.Sticky,
                actionsFromStart = listOf(
                    StyledSwipeRevealAction(
                        label = "Archive",
                        backgroundColor = StyledTheme.colorScheme.success,
                        contentColor = StyledTheme.colorScheme.onSuccess,
                        onClick = { message.value = "Archived from the revealed actions." },
                    ),
                    StyledSwipeRevealAction(
                        label = "Delete",
                        backgroundColor = StyledTheme.colorScheme.error,
                        contentColor = StyledTheme.colorScheme.onError,
                        onClick = { message.value = "Deleted from the revealed actions." },
                    ),
                ),
                actionsFromEnd = listOf(
                    StyledSwipeRevealAction(
                        label = "Edit",
                        backgroundColor = StyledTheme.colorScheme.onSurfaceVariant,
                        contentColor = StyledTheme.colorScheme.surfaceVariant,
                        onClick = { context ->
                            message.value = "Selected ${context.side.name.lowercase()} action."
                        },
                    ),
                ),
            ) {
                StyledText("Swipe to reveal actions")
            }
        }
    }
}
