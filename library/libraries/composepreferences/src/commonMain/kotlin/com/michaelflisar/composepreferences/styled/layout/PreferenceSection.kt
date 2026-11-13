package com.michaelflisar.composepreferences.styled.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composepreferences.styled.internal.LocalPreferenceRuntime
import com.michaelflisar.composepreferences.styled.internal.PreferenceAncestor
import com.michaelflisar.composepreferences.styled.internal.PreferenceEntry
import com.michaelflisar.composepreferences.styled.internal.PreferenceListSpacingArrangement
import com.michaelflisar.composepreferences.styled.internal.PreferenceSectionShapeEntry
import com.michaelflisar.composepreferences.styled.internal.accountForPreferenceListSpacing
import com.michaelflisar.composepreferences.styled.internal.currentPreferenceRuntime
import com.michaelflisar.composestyled.ui.components.StyledIconDefaults
import com.michaelflisar.composestyled.ui.components.StyledListItem
import com.michaelflisar.composestyled.ui.primitives.StyledExpandIcon
import com.michaelflisar.composestyled.ui.primitives.StyledHorizontalDivider
import kotlin.collections.plus

/** A group of related preferences that can optionally be expanded. */
@Composable
fun PreferenceSection(
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
fun PreferenceSection(
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
    val canExpand = isExpandable ?: (expanded != null || parent.style.section.isExpandable)
    val sectionIsExpanded = !canExpand || expandedState.value
    val enabled = parent.parentEnabled && isEnabled()
    val visible = parent.parentVisible && isVisible()
    val isCurrentLevel = parent.currentPath == parent.treePath
    val showHeader = !parent.isFiltering && isCurrentLevel &&
        parent.parentExpanded && visible
    val sectionStyle = parent.style.section
    val clipsSectionShape = !parent.isFiltering &&
        sectionIsExpanded &&
        (!sectionStyle.headerIsPartOfShape || (isCurrentLevel && visible))
    val sectionShapeCoordinates = remember(identity) {
        mutableStateOf<LayoutCoordinates?>(null)
    }
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
            isVisible = true,
            isInsideSection = parent.insideSection && sectionStyle.headerIsPartOfShape,
            isDetachedSection = showHeader && !sectionStyle.headerIsPartOfShape,
        )
    }
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
        sectionShapeCoordinates = sectionShapeCoordinates.value.takeIf { clipsSectionShape },
    )
    val sectionShape = PreferenceSectionClipShape(
        shape = sectionStyle.shape,
        entries = parent.sectionShapeTracker.entries(current.identityScope),
        breakSpacing = parent.style.spacing.itemSpacing,
    )
    val firstItemIsOrdinary = parent.listSpacingTracker
        .firstIsOrdinaryItem(current.identityScope)
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

    val sectionListSpacingArrangement = PreferenceListSpacingArrangement(
        tracker = parent.listSpacingTracker,
        scope = current.identityScope,
        itemSpacing = if (parent.isFiltering) {
            0.dp
        } else {
            parent.style.spacing.inGroupSeparator.spacingOrZero
        },
        sectionSpacing = parent.style.spacing.sectionSpacing,
        ordinaryItemSpacing = if (parent.isFiltering) {
            0.dp
        } else {
            parent.style.spacing.itemSpacing
        },
    )
    val sectionChildren: @Composable () -> Unit = {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (!parent.isFiltering &&
                        !sectionStyle.headerIsPartOfShape &&
                        sectionIsExpanded
                    ) {
                        Modifier
                            .onGloballyPositioned {
                                sectionShapeCoordinates.value = it
                            }
                            .clip(sectionShape)
                    } else {
                        Modifier
                    }
                )
                .accountForPreferenceListSpacing(sectionListSpacingArrangement),
            verticalArrangement = sectionListSpacingArrangement,
        ) {
            pageContent()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (!parent.isFiltering &&
                    isCurrentLevel &&
                    visible &&
                    sectionStyle.headerIsPartOfShape &&
                    sectionIsExpanded
                ) {
                    Modifier
                        .onGloballyPositioned {
                            sectionShapeCoordinates.value = it
                        }
                        .clip(sectionShape)
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
            trackInListSpacing = false,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (!sectionStyle.headerIsPartOfShape) {
                            Modifier.padding(
                                bottom = if (firstItemIsOrdinary) {
                                    parent.style.spacing.itemSpacing
                                } else {
                                    parent.style.spacing.sectionSpacing
                                },
                            )
                        } else {
                            Modifier
                        }
                    ),
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
                    minHeight = if (canExpand) {
                        sectionStyle.minimumExpandableItemHeight
                    } else {
                        sectionStyle.minimumItemHeight
                    },
                )
            }
        }
        if (parent.isFiltering || !isCurrentLevel) {
            sectionChildren()
        } else if (!canExpand) {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (showHeader && parent.parentExpanded && visible) {
                    PreferenceSectionHeaderSeparator(
                        parent.style,
                        sectionStyle,
                        firstItemIsOrdinary,
                    )
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
                        PreferenceSectionHeaderSeparator(
                            parent.style,
                            sectionStyle,
                            firstItemIsOrdinary,
                        )
                    }
                    sectionChildren()
                }
            }
        }
    }
}

private class PreferenceSectionClipShape(
    private val shape: Shape,
    private val entries: List<PreferenceSectionShapeEntry>,
    private val breakSpacing: Dp,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: androidx.compose.ui.unit.LayoutDirection,
        density: androidx.compose.ui.unit.Density,
    ): Outline {
        if (entries.isEmpty() || entries.any { it.bounds == null }) {
            return shape.createOutline(size, layoutDirection, density)
        }

        val path = Path()
        val breakSpacingPx = with(density) { breakSpacing.toPx() }
        var segmentStart = 0f

        fun addSectionSegment(top: Float, bottom: Float) {
            val clippedTop = top.coerceIn(0f, size.height)
            val clippedBottom = bottom.coerceIn(0f, size.height)
            if (clippedBottom <= clippedTop) return
            val segmentOutline = shape.createOutline(
                Size(size.width, clippedBottom - clippedTop),
                layoutDirection,
                density,
            )
            path.addOutline(segmentOutline, Offset(0f, clippedTop))
        }

        entries.forEach { entry ->
            val bounds = checkNotNull(entry.bounds)
            val breakTop = bounds.top.coerceIn(0f, size.height)
            val breakBottom = bounds.bottom.coerceIn(0f, size.height)
            val segmentTop = (breakTop - breakSpacingPx).coerceIn(0f, size.height)
            val segmentBottom = (breakBottom + breakSpacingPx).coerceIn(0f, size.height)
            addSectionSegment(segmentStart, segmentTop)

            val left = bounds.left.coerceIn(0f, size.width)
            val right = bounds.right.coerceIn(0f, size.width)
            if (right > left && breakBottom > breakTop) {
                path.addRect(Rect(left, breakTop, right, breakBottom))
            }
            segmentStart = segmentBottom
        }

        addSectionSegment(segmentStart, size.height)
        return Outline.Generic(path)
    }

    private fun Path.addOutline(outline: Outline, offset: Offset) {
        val outlinePath = Path()
        when (outline) {
            is Outline.Rectangle -> outlinePath.addRect(outline.rect)
            is Outline.Rounded -> outlinePath.addRoundRect(outline.roundRect)
            is Outline.Generic -> outlinePath.addPath(outline.path)
        }
        addPath(outlinePath, offset)
    }
}


@Composable
private fun PreferenceSectionHeaderSeparator(
    style: com.michaelflisar.composepreferences.styled.PreferenceStyle,
    sectionStyle: com.michaelflisar.composepreferences.styled.PreferenceSectionStyle,
    firstItemIsOrdinary: Boolean,
) {
    if (sectionStyle.headerIsPartOfShape) {
        if (firstItemIsOrdinary) {
            Spacer(Modifier.height(style.spacing.itemSpacing))
        } else {
            when (val separator = style.spacing.inGroupSeparator) {
                is com.michaelflisar.composepreferences.styled.PreferenceInGroupSeparatorStyle.Spacing ->
                    Spacer(Modifier.height(separator.spacing))

                is com.michaelflisar.composepreferences.styled.PreferenceInGroupSeparatorStyle.Divider ->
                    PreferenceInGroupDivider(style)
            }
        }
    }
}

@Composable
internal fun PreferenceInGroupDivider(
    style: com.michaelflisar.composepreferences.styled.PreferenceStyle,
) {
    val verticalPadding =
        (style.spacing.inGroupSeparator as? com.michaelflisar.composepreferences.styled.PreferenceInGroupSeparatorStyle.Divider)?.verticalPadding
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
