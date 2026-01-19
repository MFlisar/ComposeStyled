package com.michaelflisar.composestyled.components.scaffold

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.components.StyledSurface
import com.michaelflisar.composestyled.core.components.StyledText
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import com.michaelflisar.composestyled.components.scaffold.systemui.StyledUpdateStatusBar
import com.michaelflisar.composestyled.core.isDark
import androidx.compose.ui.draw.alpha

/* ---------- Pinned (Small) ---------- */
@Composable
fun StyledTopAppBarPinned(
    collapsedFraction: Float,
    title: String,
    containerColor: Color = StyledTheme.colors.surface,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    val heights = LocalTopBarHeights.current
    val collapsed = StyledTopAppBarDefaults.SmallHeight
    SideEffect {
        heights.collapsed = collapsed
        heights.expandableExtra = 0.dp
        heights.expanded = collapsed
    }

    StyledSurface(
        modifier = Modifier
            .fillMaxWidth()
            .clipToBounds(),
        backgroundColor = containerColor,
        contentColor = contentColor
    ) {
        Row(
            Modifier
                .windowInsetsPadding(WindowInsets.statusBars.only(WindowInsetsSides.Top))
                .padding(horizontal = 16.dp)
                .heightIn(min = collapsed),
            verticalAlignment = Alignment.CenterVertically
        ) {
            navigationIcon?.invoke()
            if (navigationIcon != null) Spacer(Modifier.size(8.dp))
            StyledText(
                text = title,
                style = StyledTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.weight(1f))
            actions()
        }
    }
}

/* ---------- EnterAlways (Small) ---------- */
@Composable
fun StyledTopAppBarEnterAlways(
    collapsedFraction: Float,
    title: String,
    containerColor: Color = StyledTheme.colors.surface,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    StyledTopAppBarPinned(
        collapsedFraction = collapsedFraction,
        title = title,
        containerColor = containerColor,
        contentColor = contentColor,
        navigationIcon = navigationIcon,
        actions = actions
    )
}

/* ---------- Collapsing (Large -> Small) ---------- */
@Composable
fun StyledTopAppBarCollapsing(
    collapsedFraction: Float,
    title: String,
    containerColor: Color = StyledTheme.colors.surface,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    val heights = LocalTopBarHeights.current
    val collapsed = StyledTopAppBarDefaults.SmallHeight
    SideEffect {
        heights.collapsed = collapsed
        heights.expandableExtra = null   // von Messung im Scaffold ableiten
        heights.expanded = null
    }

    val fraction = collapsedFraction.coerceIn(0f, 1f)

    StyledSurface(
        modifier = Modifier
            .fillMaxWidth()
            .clipToBounds(),
        backgroundColor = containerColor,
        contentColor = contentColor
    ) {
        Box(
            Modifier
                .windowInsetsPadding(WindowInsets.statusBars.only(WindowInsetsSides.Top))
                .padding(horizontal = 16.dp)
        ) {
            // „Large“-like Titel unten
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 8.dp)
                    .alpha(1f - fraction),
                verticalAlignment = Alignment.CenterVertically
            ) {
                navigationIcon?.invoke()
                if (navigationIcon != null) Spacer(Modifier.size(8.dp))
                StyledText(
                    text = title,
                    style = StyledTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.weight(1f))
                actions()
            }

            // „Small“-like Titel mittig
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .alpha(fraction),
                verticalAlignment = Alignment.CenterVertically
            ) {
                navigationIcon?.invoke()
                if (navigationIcon != null) Spacer(Modifier.size(8.dp))
                StyledText(
                    text = title,
                    style = StyledTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.weight(1f))
                actions()
            }
        }
    }
}

/* ---------- Expandable (Material3-like Large -> Small; no custom expanded content) ----------
 * Large title sits at the bottom when expanded and transitions to a small/centered title when collapsed.
 * Height is deterministic (SmallHeight + LargeExtra) so the Scaffold can consume it without measuring.
 */
@Composable
fun StyledTopAppBarExpandable(
    collapsedFraction: Float,
    title: String,
    containerColor: Color = StyledTheme.colors.surface,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    val heights = LocalTopBarHeights.current

    val collapsed = StyledTopAppBarDefaults.SmallHeight
    // Material3 LargeTopAppBar is ~152dp; with a 64dp collapsed height this leaves ~88dp extra.
    // Keep this deterministic so we don't need Subcompose measuring here.
    val expanded = StyledTopAppBarDefaults.LargeHeight
    val expandableExtra = (expanded - collapsed).coerceAtLeast(0.dp)

    SideEffect {
        heights.collapsed = collapsed
        heights.expandableExtra = expandableExtra
        heights.expanded = expanded
    }

    StyledUpdateStatusBar(
        color = Color.Transparent,
        darkIcons = contentColor.isDark
    )

    StyledSurface(
        modifier = Modifier
            .fillMaxWidth()
            .clipToBounds(),
        backgroundColor = containerColor,
        contentColor = contentColor
    ) {
        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars.only(WindowInsetsSides.Top))
        ) {
            // Top row: same as "small" app bar (navigation + title + actions)
            Row(
                Modifier
                    .padding(horizontal = 16.dp)
                    .heightIn(min = collapsed),
                verticalAlignment = Alignment.CenterVertically
            ) {
                navigationIcon?.invoke()
                if (navigationIcon != null) Spacer(Modifier.size(8.dp))

                // Small title fades in as we collapse
                StyledText(
                    modifier = Modifier.alpha(collapsedFraction.coerceIn(0f, 1f)),
                    text = title,
                    style = StyledTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.weight(1f))
                actions()
            }

            // Large title area: occupies the expandable space and slides/fades out while collapsing.
            // We keep it simple: pinned container, bottom-aligned title.
            Box(
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = expandableExtra)
                    .padding(horizontal = 16.dp)
            ) {
                val largeAlpha = (1f - collapsedFraction).coerceIn(0f, 1f)
                StyledText(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 12.dp)
                        .alpha(largeAlpha),
                    text = title,
                    // TODO: if you have a dedicated headline style in StyledTheme, use it here.
                    style = StyledTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}