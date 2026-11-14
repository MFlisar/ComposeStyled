package com.michaelflisar.composepreferences.styled.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composepreferences.styled.PreferenceFilter
import com.michaelflisar.composepreferences.styled.PreferenceOpenGroupHeaderStyle
import com.michaelflisar.composepreferences.styled.internal.LocalPreferenceRuntime
import com.michaelflisar.composepreferences.styled.internal.PreferenceAncestor
import com.michaelflisar.composepreferences.styled.internal.PreferenceEntry
import com.michaelflisar.composepreferences.styled.internal.PreferenceListSpacing
import com.michaelflisar.composepreferences.styled.internal.animatedPreferenceListSpacing
import com.michaelflisar.composepreferences.styled.internal.currentPreferenceRuntime
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.icons.StyledIcons

/** A navigable preference group whose children are shown on a separate animated list level. */
@Composable
fun PreferenceGroup(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier.Companion,
    key: String? = null,
    icon: IconComposable? = null,
    filterTags: List<String> = emptyList(),
    isEnabled: @Composable () -> Boolean = { true },
    isVisible: @Composable () -> Boolean = { true },
    shape: Shape? = null,
    content: @Composable () -> Unit,
) {
    val parent =
        currentPreferenceRuntime()
    val identity = remember(parent.identityAllocator, parent.identityScope, key) {
        parent.identityAllocator.next(key, parent.identityScope)
    }
    val groupPath = parent.treePath + identity
    val enabled = parent.parentEnabled && isEnabled()
    val visible = parent.parentVisible && isVisible()
    val groupShape = shape.takeUnless { parent.insideSection }
    val showGroup = visible && parent.parentExpanded && if (parent.isFiltering) {
        parent.filterScope != PreferenceFilter.Scope.Global &&
                parent.isInFilterScope()
    } else {
        parent.currentPath == parent.treePath
    }
    val groupIsCurrentPage = !parent.isFiltering && parent.currentPath == groupPath
    val childRuntime = parent.copy(
        identityScope = parent.identityScope + identity,
        treePath = groupPath,
        ancestors = parent.ancestors + PreferenceAncestor(
            title,
            subtitle,
            filterTags
        ),
        parentEnabled = enabled,
        parentVisible = visible,
        parentExpanded = true,
        insideSection = false,
        sectionShapeCoordinates = null,
    )
    val hasVisibleContent = parent.listSpacingTracker.hasVisibleEntries(
        childRuntime.identityScope,
    )
    val hasPresentContent = parent.listSpacingTracker.hasPresentEntries(
        childRuntime.identityScope,
    )
    val groupIsVisible = visible && (showGroup || groupIsCurrentPage || hasVisibleContent)
    val groupIsPresent = groupIsVisible || hasPresentContent
    val groupSpacingBefore = animatedPreferenceListSpacing(
        parent.listSpacing,
        identity,
        groupIsVisible,
    )
    DisposableEffect(parent.listSpacingTracker, identity) {
        onDispose {
            parent.listSpacingTracker.remove(identity)
        }
    }
    SideEffect {
        parent.listSpacingTracker.update(
            identity = identity,
            scope = parent.identityScope,
            order = parent.identityAllocator.order(identity),
            isVisible = groupIsVisible,
            isTransitionVisible = groupIsPresent,
            isInsideSection = parent.insideSection,
        )
    }

    val groupListSpacing = PreferenceListSpacing(
        tracker = parent.listSpacingTracker,
        scope = childRuntime.identityScope,
        itemSpacing = parent.style.spacing.itemSpacing,
        sectionSpacing = parent.style.spacing.sectionSpacing,
        animateChanges = parent.listSpacing.animateChanges,
    )
    val scopedChildRuntime = childRuntime.copy(listSpacing = groupListSpacing)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = groupSpacingBefore),
        verticalArrangement = Arrangement.Top,
    ) {
        // Reserve the identity even when the row is hidden to keep navigation IDs stable.
        PreferenceEntry(
            title = title,
            subtitle = subtitle,
            filterTags = filterTags,
            key = "group-entry:$identity",
            isEnabled = { enabled },
            isVisible = { showGroup },
            renderInFilter = parent.filterScope != PreferenceFilter.Scope.Global,
            trackInListSpacing = false,
            breaksSectionShape = groupShape != null,
        ) {
            PreferenceRowContent(
                title = title,
                subtitle = subtitle,
                icon = icon,
                trailing = {
                    StyledIcon(
                        icon = StyledIcons.Submenu,
                        contentDescription = null,
                    )
                },
                enabled = enabled,
                clickable = true,
                onClick = {
                    parent.screenState.navigate(
                        PreferenceRoute(
                            id = identity,
                            title = title,
                        ),
                        parentDepth = parent.currentPath.size,
                    )
                },
                style = it.style,
                insideSection = it.insideSection,
                shape = groupShape,
            )
        }

        val groupContent: @Composable () -> Unit = {
            CompositionLocalProvider(LocalPreferenceRuntime provides scopedChildRuntime) {
                content()
            }
        }

        if (parent.isFiltering) {
            groupContent()
        } else if (groupIsCurrentPage && visible) {
            val groupTopSpacing = when (parent.style.openedGroupHeader) {
                PreferenceOpenGroupHeaderStyle.Disabled -> 0.dp
                is PreferenceOpenGroupHeaderStyle.Style -> parent.style.spacing.itemSpacing
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = groupTopSpacing)
                    .clip(parent.style.item.shape),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                ) {
                    groupContent()
                }
            }
        } else {
            groupContent()
        }
    }
}
