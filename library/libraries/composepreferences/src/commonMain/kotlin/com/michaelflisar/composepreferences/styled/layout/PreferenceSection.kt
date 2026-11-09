package com.michaelflisar.composepreferences.styled.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composepreferences.styled.internal.LocalPreferenceRuntime
import com.michaelflisar.composepreferences.styled.internal.PreferenceAncestor
import com.michaelflisar.composepreferences.styled.internal.PreferenceEntry
import com.michaelflisar.composepreferences.styled.internal.currentPreferenceRuntime
import com.michaelflisar.composestyled.ui.components.StyledIconDefaults
import com.michaelflisar.composestyled.ui.components.StyledListItem
import com.michaelflisar.composestyled.ui.primitives.StyledExpandIcon
import com.michaelflisar.composestyled.ui.primitives.StyledHorizontalDivider
import kotlin.collections.plus

/** An expandable group of related preferences. */
@Composable
fun StyledPreferenceSection(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    key: String? = null,
    isExpandable: Boolean? = null,
    filterTags: List<String> = emptyList(),
    isEnabled: @Composable () -> Boolean = { true },
    isVisible: @Composable () -> Boolean = { true },
    isInitiallyExpanded: Boolean = true,
    content: @Composable () -> Unit,
) {
    PreferenceSectionImpl(
        title = title,
        subtitle = subtitle,
        modifier = modifier,
        key = key,
        expanded = null,
        isExpandable = isExpandable,
        filterTags = filterTags,
        isEnabled = isEnabled,
        isVisible = isVisible,
        isInitiallyExpanded = isInitiallyExpanded,
        content = content,
    )
}

/** A preference section whose expanded state is controlled by the caller. */
@Composable
fun StyledPreferenceSection(
    title: String,
    expanded: MutableState<Boolean>,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    key: String? = null,
    isExpandable: Boolean? = null,
    filterTags: List<String> = emptyList(),
    isEnabled: @Composable () -> Boolean = { true },
    isVisible: @Composable () -> Boolean = { true },
    content: @Composable () -> Unit,
) {
    PreferenceSectionImpl(
        title = title,
        subtitle = subtitle,
        modifier = modifier,
        key = key,
        expanded = expanded,
        isExpandable = isExpandable,
        filterTags = filterTags,
        isEnabled = isEnabled,
        isVisible = isVisible,
        isInitiallyExpanded = true,
        content = content,
    )
}

@Composable
private fun PreferenceSectionImpl(
    title: String,
    subtitle: String?,
    modifier: Modifier,
    key: String?,
    expanded: MutableState<Boolean>?,
    isExpandable: Boolean?,
    filterTags: List<String>,
    isEnabled: @Composable () -> Boolean,
    isVisible: @Composable () -> Boolean,
    isInitiallyExpanded: Boolean,
    content: @Composable () -> Unit,
) {
    val parent =
        currentPreferenceRuntime()
    val identity = remember(parent.identityAllocator, parent.identityScope, key) {
        parent.identityAllocator.next(key, parent.identityScope)
    }
    val internalExpanded = rememberSaveable(identity) {
        mutableStateOf(isInitiallyExpanded)
    }
    val pageIsInNavigationStack = parent.treePath.isEmpty() ||
        parent.navigationPaths.any { routePath ->
            routePath.take(parent.treePath.size) == parent.treePath
        }
    if (!pageIsInNavigationStack && expanded == null && !internalExpanded.value) {
        SideEffect {
            internalExpanded.value = isInitiallyExpanded
        }
    }
    val expandedState = expanded ?: internalExpanded
    val canExpand = isExpandable ?: parent.style.section.isExpandable
    val sectionIsExpanded = !canExpand || expandedState.value
    val enabled = parent.parentEnabled && isEnabled()
    val visible = parent.parentVisible && isVisible()
    val isCurrentLevel = parent.currentPath == parent.treePath
    val showHeader = !parent.isFiltering && isCurrentLevel &&
        parent.parentExpanded && visible
    val sectionSpacingTracker = parent.sectionSpacingTracker
    DisposableEffect(sectionSpacingTracker, identity) {
        onDispose {
            sectionSpacingTracker.remove(identity)
        }
    }
    SideEffect {
        sectionSpacingTracker.update(
            identity = identity,
            scope = parent.identityScope,
            order = parent.identityAllocator.order(identity),
            isVisible = showHeader,
        )
    }
    val hasPreviousSection = sectionSpacingTracker.hasPrevious(identity, parent.identityScope)
    val sectionStyle = parent.style.section
    val current = parent.copy(
        identityScope = parent.identityScope + identity,
        ancestors = parent.ancestors + PreferenceAncestor(
            title,
            subtitle,
            filterTags
        ),
        parentEnabled = enabled,
        parentVisible = visible,
        parentExpanded = parent.parentExpanded,
        insideSection = if (parent.isFiltering) false else true,
    )
    val pageContent = @Composable {
        CompositionLocalProvider(LocalPreferenceRuntime provides current) {
            content()
        }
    }

    val headerBottomCorner by animateDpAsState(
        targetValue = if (sectionIsExpanded) 0.dp else parent.style.section.groupCornerSize,
        label = "Preference section header corners",
    )
    val headerShape = if (sectionStyle.headerIsPartOfShape) {
        RoundedCornerShape(
            topStart = parent.style.section.groupCornerSize,
            topEnd = parent.style.section.groupCornerSize,
            bottomStart = headerBottomCorner,
            bottomEnd = headerBottomCorner,
        )
    } else {
        sectionStyle.shape
    }

    val sectionChildren: @Composable () -> Unit = {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (!parent.isFiltering &&
                        !sectionStyle.headerIsPartOfShape &&
                        sectionIsExpanded
                    ) {
                        Modifier.clip(sectionStyle.shape)
                    } else {
                        Modifier
                    }
                ),
            verticalArrangement = Arrangement.spacedBy(
                    if (parent.isFiltering) {
                        0.dp
                    } else {
                        parent.style.inGroupSeparator.spacingOrZero
                    },
                ),
        ) {
            pageContent()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (showHeader && parent.insideSection && hasPreviousSection) {
                    Modifier.padding(top = parent.style.itemSpacing)
                } else {
                    Modifier
                }
            )
            .then(
                if (!parent.isFiltering &&
                    isCurrentLevel &&
                    visible &&
                    sectionStyle.headerIsPartOfShape &&
                    sectionIsExpanded
                ) {
                    Modifier.clip(sectionStyle.shape)
                } else {
                    Modifier
                }
            ),
    ) {
        // Reserve the identity even when the header is hidden to keep navigation IDs stable.
        PreferenceEntry(
            title = title,
            subtitle = subtitle,
            filterTags = filterTags,
            key = "section-header:$identity",
            isEnabled = { enabled },
            isVisible = { showHeader },
            renderInFilter = false,
            trackInGroupSeparator = false,
        ) {
            PreferenceRowContent(
                title = title,
                subtitle = subtitle,
                trailing = if (canExpand) {
                    {
                        StyledExpandIcon(
                            expanded = expandedState.value,
                            rotationDirection = StyledExpandIcon.RotationDirection.forEndPlacement()
                        )
                    }
                } else {
                    null
                },
                modifier = Modifier.zIndex(1f),
                enabled = enabled,
                clickable = canExpand,
                onClick = if (canExpand) {
                    { expandedState.value = !expandedState.value }
                } else {
                    null
                },
                style = it.style,
                insideSection = true,
                titleStyle = sectionStyle.titleTextStyle,
                subtitleStyle = sectionStyle.subtitleTextStyle,
                colors = sectionStyle.colors,
                shape = headerShape,
                contentPadding = sectionStyle.contentPadding,
                minHeight = sectionStyle.minimumItemHeight,
            )
        }
        if (parent.isFiltering || !isCurrentLevel) {
            sectionChildren()
        } else if (!canExpand) {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (showHeader && parent.parentExpanded && visible) {
                    PreferenceSectionHeaderSeparator(parent.style, sectionStyle)
                }
                sectionChildren()
            }
        } else {
            AnimatedVisibility(
                visible = expandedState.value,
                enter = expandVertically(
                    expandFrom = Alignment.Top,
                    clip = true,
                ),
                exit = shrinkVertically(
                    shrinkTowards = Alignment.Top,
                    clip = true,
                ),
                label = "Preference section content",
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (showHeader && parent.parentExpanded && visible) {
                        PreferenceSectionHeaderSeparator(parent.style, sectionStyle)
                    }
                    sectionChildren()
                }
            }
        }
    }
}


@Composable
private fun PreferenceSectionHeaderSeparator(
    style: com.michaelflisar.composepreferences.styled.PreferenceStyle,
    sectionStyle: com.michaelflisar.composepreferences.styled.PreferenceSectionStyle,
) {
    when (val separator = style.inGroupSeparator) {
        is com.michaelflisar.composepreferences.styled.PreferenceInGroupSeparatorStyle.Spacing -> Spacer(Modifier.height(separator.spacing))
        is com.michaelflisar.composepreferences.styled.PreferenceInGroupSeparatorStyle.Divider -> {
            if (sectionStyle.headerIsPartOfShape) {
                PreferenceInGroupDivider(style)
            } else {
                Spacer(Modifier.height(separator.headerSpacing))
            }
        }
    }
}

@Composable
internal fun PreferenceInGroupDivider(
    style: com.michaelflisar.composepreferences.styled.PreferenceStyle,
) {
    val verticalPadding =
        (style.inGroupSeparator as? com.michaelflisar.composepreferences.styled.PreferenceInGroupSeparatorStyle.Divider)?.verticalPadding
            ?: 0.dp
    val layoutDirection = androidx.compose.ui.platform.LocalLayoutDirection.current
    val itemPadding = style.item.contentPadding
    StyledListItem(
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        minHeight = 0.dp,
        contentPadding = PaddingValues(
            start = itemPadding.calculateStartPadding(layoutDirection),
            top = 0.dp,
            end = itemPadding.calculateEndPadding(layoutDirection),
            bottom = 0.dp,
        ),
        containerColor = style.item.colors.containerColor,
        leading = if (style.reserveLeadingIconSpace) {
            {
                Spacer(Modifier.width(StyledIconDefaults.size).height(0.dp))
            }
        } else {
            null
        },
    ) {
        StyledHorizontalDivider(Modifier.padding(vertical = verticalPadding))
    }
}

/** Renders the shared visual shell used by preference rows. */
@Composable
internal fun PreferenceRowContent(
    title: String,
    subtitle: String?,
    modifier: Modifier = Modifier,
    icon: IconComposable? = null,
    trailing: (@Composable RowScope.(textStyle: TextStyle) -> Unit)? = null,
    enabled: Boolean,
    clickable: Boolean,
    onClick: (() -> Unit)?,
    style: com.michaelflisar.composepreferences.styled.PreferenceStyle,
    insideSection: Boolean,
    colors: com.michaelflisar.composepreferences.styled.PreferenceRowColors = style.item.colors,
    titleStyle: TextStyle = style.item.titleTextStyle,
    subtitleStyle: TextStyle = style.item.subtitleTextStyle,
    shape: androidx.compose.ui.graphics.Shape? = null,
    contentPadding: androidx.compose.foundation.layout.PaddingValues? = null,
    minHeight: Dp? = null,
) {
    PreferenceStyledRow(
        title = title,
        subtitle = subtitle,
        icon = icon,
        trailing = trailing?.let { content ->
            {
                content(titleStyle)
            }
        },
        enabled = enabled,
        onClick = if (clickable) onClick else null,
        modifier = modifier.fillMaxWidth(),
        shape = shape ?: if (insideSection) RectangleShape else style.item.shape,
        titleStyle = titleStyle,
        subtitleStyle = subtitleStyle,
        style = style,
        insideSection = insideSection,
        colors = colors,
        contentPadding = contentPadding,
        minHeight = minHeight,
    )
}
