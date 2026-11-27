package com.michaelflisar.composestyled.ui.specials

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.ui.components.StyledClickable
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.layout.StyledHorizontalScrollContainer
import com.michaelflisar.composestyled.ui.layout.StyledHorizontalScrollbarLayout
import com.michaelflisar.composestyled.ui.primitives.StyledText

/** One text and/or icon segment in a [StyledBreadcrumb]. */
@Immutable
data class StyledBreadcrumbItem(
    val label: String? = null,
    val onClick: (() -> Unit)? = null,
    val icon: IconComposable? = null,
    val iconContentDescription: String? = null,
)

/** Colors, typography and spacing used by [StyledBreadcrumb]. */
@Immutable
data class StyledBreadcrumbStyle(
    val itemColor: Color,
    val currentItemColor: Color,
    val separatorColor: Color,
    val itemTextStyle: TextStyle,
    val currentItemTextStyle: TextStyle,
    val itemSpacing: Dp,
    val separatorSize: Dp,
)

/** Creates theme-based styling for [StyledBreadcrumb]. */
@Composable
fun rememberStyledBreadcrumbStyle(
    itemColor: Color = StyledTheme.colorScheme.onSurfaceVariant,
    currentItemColor: Color = StyledTheme.colorScheme.onSurface,
    separatorColor: Color = StyledTheme.supportingContentColor(
        contentColor = StyledTheme.contentColorFor(
            backgroundColor = StyledTheme.colorScheme.surfaceVariant,
            fallback = StyledTheme.ContentColorFallback.BlackWhite,
        ),
        containerColor = StyledTheme.colorScheme.surfaceVariant,
    ),
    itemTextStyle: TextStyle = StyledTheme.typography.bodyMedium,
    currentItemTextStyle: TextStyle = itemTextStyle.copy(fontWeight = FontWeight.Medium),
    itemSpacing: Dp = StyledTheme.spacing.small,
    separatorSize: Dp = StyledTheme.sizes.iconSize,
): StyledBreadcrumbStyle = remember(
    itemColor,
    currentItemColor,
    separatorColor,
    itemTextStyle,
    currentItemTextStyle,
    itemSpacing,
    separatorSize,
) {
    StyledBreadcrumbStyle(
        itemColor = itemColor,
        currentItemColor = currentItemColor,
        separatorColor = separatorColor,
        itemTextStyle = itemTextStyle,
        currentItemTextStyle = currentItemTextStyle,
        itemSpacing = itemSpacing,
        separatorSize = separatorSize,
    )
}

/**
 * Displays a horizontally scrollable path. Items before the last are clickable when they have an
 * [StyledBreadcrumbItem.onClick] action; the last item is always treated as the current location.
 * Set [showRootItem] to `false` to hide the first item.
 * When [scrollToEndOnItemsAdded] is enabled, adding items animates the scroll to the end.
 */
@Composable
fun StyledBreadcrumb(
    items: List<StyledBreadcrumbItem>,
    modifier: Modifier = Modifier,
    style: StyledBreadcrumbStyle = rememberStyledBreadcrumbStyle(),
    separatorIcon: IconComposable = StyledIcons.NavigateForward,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    scrollToEndOnItemsAdded: Boolean = true,
    showRootItem: Boolean = true,
) {
    val scrollState = rememberScrollState()
    val previousItemCount = remember { mutableIntStateOf(items.size) }

    LaunchedEffect(items.size, scrollToEndOnItemsAdded) {
        val itemCount = items.size
        if (scrollToEndOnItemsAdded && itemCount > previousItemCount.intValue) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
        previousItemCount.intValue = itemCount
    }

    val firstVisibleIndex = if (showRootItem) 0 else 1
    if (items.size <= firstVisibleIndex) return

    Row(
        horizontalArrangement = Arrangement.spacedBy(style.itemSpacing),
        modifier = modifier.horizontalScroll(scrollState),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEachIndexed { index, item ->
            if (index < firstVisibleIndex) return@forEachIndexed

            val isCurrent = index == items.lastIndex
            if (index > firstVisibleIndex) {
                StyledIcon(
                    icon = separatorIcon,
                    contentDescription = null,
                    size = style.separatorSize,
                    tint = style.separatorColor,
                )
            }

            val onClick = item.onClick.takeUnless { isCurrent }
            val itemColor = if (isCurrent) style.currentItemColor else style.itemColor
            val itemTextStyle = if (isCurrent) style.currentItemTextStyle else style.itemTextStyle

            StyledClickable(
                onClick = { onClick?.invoke() },
                enabled = onClick != null,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(style.itemSpacing),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    item.icon?.let {
                        StyledIcon(
                            icon = it,
                            contentDescription = item.iconContentDescription,
                            tint = itemColor,
                        )
                    }
                    item.label?.let {
                        StyledText(
                            text = it,
                            color = itemColor,
                            style = itemTextStyle,
                            maxLines = maxLines,
                            overflow = overflow,
                        )
                    }
                }
            }
        }
    }
}
