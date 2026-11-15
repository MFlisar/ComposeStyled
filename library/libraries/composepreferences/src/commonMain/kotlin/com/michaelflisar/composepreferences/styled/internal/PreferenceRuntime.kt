package com.michaelflisar.composepreferences.styled.internal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composepreferences.styled.PreferenceFilter
import com.michaelflisar.composepreferences.styled.PreferenceSectionContentSeparatorStyle
import com.michaelflisar.composepreferences.styled.PreferenceStyle
import com.michaelflisar.composepreferences.styled.layout.PreferenceSectionContentDivider
import com.michaelflisar.composepreferences.styled.layout.PreferenceScreenState

internal data class PreferenceAncestor(
    val title: String,
    val subtitle: String?,
    val filterTags: List<String>,
)

internal class PreferenceIdentityAllocator {
    private var nextId = 0
    private val entryOrders = mutableMapOf<String, Int>()

    fun next(key: String?, scope: List<String>): String {
        val generated = "auto-${nextId++}"
        val identity = key?.let { "key:${(scope + it).joinToString("/")}" } ?: generated
        if (identity !in entryOrders) entryOrders[identity] = entryOrders.size
        return identity
    }

    fun order(identity: String): Int =
        checkNotNull(entryOrders[identity]) { "Unknown preference identity: $identity" }
}

internal class PreferenceSectionSeparatorTracker {
    private data class Entry(
        val scope: List<String>,
        val order: Int,
        val isInsideSection: Boolean,
    )

    private val entries = mutableStateMapOf<String, Entry>()

    fun update(
        identity: String,
        scope: List<String>,
        order: Int,
        isVisible: Boolean,
        isInsideSection: Boolean,
    ) {
        if (isVisible) {
            val entry = Entry(
                scope = scope,
                order = order,
                isInsideSection = isInsideSection,
            )
            if (entries[identity] != entry) entries[identity] = entry
        } else {
            entries.remove(identity)
        }
    }

    fun remove(identity: String) {
        entries.remove(identity)
    }

    fun hasNext(identity: String, scope: List<String>): Boolean {
        val current = entries[identity] ?: return false
        if (!current.isInsideSection) return false
        val siblings = entries.values
            .filter { it.scope == scope }
            .sortedBy(Entry::order)
        val currentIndex = siblings.indexOf(current)
        return siblings.getOrNull(currentIndex + 1)?.isInsideSection == true
    }
}

internal data class PreferenceSectionShapeEntry(
    val order: Int,
    val bounds: Rect?,
)

internal class PreferenceSectionShapeTracker {
    private data class Entry(
        val scope: List<String>,
        val order: Int,
        val bounds: Rect?,
    )

    private val entries = mutableStateMapOf<String, Entry>()

    fun update(
        identity: String,
        scope: List<String>,
        order: Int,
        isVisible: Boolean,
    ) {
        if (isVisible) {
            val current = entries[identity]
            val bounds = current
                ?.takeIf { it.scope == scope && it.order == order }
                ?.bounds
            val entry = Entry(
                scope = scope,
                order = order,
                bounds = bounds,
            )
            if (current != entry) entries[identity] = entry
        } else {
            entries.remove(identity)
        }
    }

    fun updateBounds(identity: String, bounds: Rect) {
        val current = entries[identity] ?: return
        if (current.bounds != bounds) {
            entries[identity] = current.copy(bounds = bounds)
        }
    }

    fun remove(identity: String) {
        entries.remove(identity)
    }

    fun entries(scope: List<String>): List<PreferenceSectionShapeEntry> =
        entries.values
            .filter { it.scope == scope }
            .sortedBy(Entry::order)
            .map {
                PreferenceSectionShapeEntry(
                    order = it.order,
                    bounds = it.bounds,
                )
            }
}

internal enum class PreferenceListSpacingKind {
    Ordinary,
    InSectionContent,
    DetachedSection,
}

internal class PreferenceListSpacingTracker {
    private data class Entry(
        val scope: List<String>,
        val order: Int,
        val isTargetVisible: Boolean,
        val isTransitionVisible: Boolean,
        val isInsideSection: Boolean,
        val isDetachedSection: Boolean,
        val hasDividerAfter: Boolean,
    )

    private val entries = mutableStateMapOf<String, Entry>()

    fun update(
        identity: String,
        scope: List<String>,
        order: Int,
        isVisible: Boolean,
        isTransitionVisible: Boolean = isVisible,
        isInsideSection: Boolean = false,
        isDetachedSection: Boolean = false,
        hasDividerAfter: Boolean = false,
    ) {
        val entry = Entry(
            scope = scope,
            order = order,
            isTargetVisible = isVisible,
            isTransitionVisible = isTransitionVisible,
            isInsideSection = isInsideSection,
            isDetachedSection = isDetachedSection,
            hasDividerAfter = hasDividerAfter,
        )
        if (entries[identity] != entry) entries[identity] = entry
    }

    fun remove(identity: String) {
        entries.remove(identity)
    }

    fun hasVisibleEntries(scope: List<String>): Boolean =
        entries.values.any { it.scope == scope && it.isTargetVisible }

    fun hasPresentEntries(scope: List<String>): Boolean =
        entries.values.any { it.scope == scope && it.isTransitionVisible }

    fun spacingBefore(
        identity: String,
        scope: List<String>,
        isTransitionVisible: Boolean,
        itemSpacing: Dp,
        sectionSpacing: Dp,
        ordinaryItemSpacing: Dp,
        leadingItems: Int,
    ): Dp {
        if (!isTransitionVisible) return 0.dp
        val siblings = entries.entries
            .filter { it.value.scope == scope }
            .sortedBy { it.value.order }
        val currentIndex = siblings.indexOfFirst { it.key == identity }
        if (currentIndex < 0) {
            return if (leadingItems > 0) ordinaryItemSpacing else 0.dp
        }
        val current = siblings[currentIndex].value
        val previous = siblings
            .take(currentIndex)
            .lastOrNull { it.value.isTargetVisible }
            ?.value
        val previousKind = when {
            previous?.hasDividerAfter == true -> PreferenceListSpacingKind.InSectionContent
            previous != null -> previous.kind()
            leadingItems > 0 -> PreferenceListSpacingKind.Ordinary
            else -> return 0.dp
        }
        return spacingBetween(
            previous = previousKind,
            current = current.kind(),
            itemSpacing = itemSpacing,
            sectionSpacing = sectionSpacing,
            ordinaryItemSpacing = ordinaryItemSpacing,
        )
    }

    fun firstIsOrdinaryItem(scope: List<String>): Boolean =
        entries.values
            .filter { it.scope == scope && it.isTargetVisible }
            .minByOrNull(Entry::order)
            ?.let { !it.isInsideSection && !it.isDetachedSection }
            ?: false

    private fun Entry.kind(): PreferenceListSpacingKind = when {
        isDetachedSection -> PreferenceListSpacingKind.DetachedSection
        isInsideSection -> PreferenceListSpacingKind.InSectionContent
        else -> PreferenceListSpacingKind.Ordinary
    }

    private fun spacingBetween(
        previous: PreferenceListSpacingKind,
        current: PreferenceListSpacingKind,
        itemSpacing: Dp,
        sectionSpacing: Dp,
        ordinaryItemSpacing: Dp,
    ): Dp = when {
        current == PreferenceListSpacingKind.DetachedSection -> sectionSpacing
        previous == PreferenceListSpacingKind.InSectionContent &&
                current == PreferenceListSpacingKind.InSectionContent -> itemSpacing
        else -> ordinaryItemSpacing
    }
}

internal class PreferenceListSpacing(
    private val tracker: PreferenceListSpacingTracker,
    private val scope: List<String>,
    private val itemSpacing: Dp,
    private val sectionSpacing: Dp,
    private val ordinaryItemSpacing: Dp = itemSpacing,
    private val leadingItems: Int = 0,
    val animateChanges: Boolean = true,
) {
    fun spacingBefore(identity: String, isTransitionVisible: Boolean): Dp =
        tracker.spacingBefore(
            identity = identity,
            scope = scope,
            isTransitionVisible = isTransitionVisible,
            itemSpacing = itemSpacing,
            sectionSpacing = sectionSpacing,
            ordinaryItemSpacing = ordinaryItemSpacing,
            leadingItems = leadingItems,
        )

    fun firstIsOrdinaryItem(): Boolean = tracker.firstIsOrdinaryItem(scope)
}

@Composable
internal fun animatedPreferenceListSpacing(
    listSpacing: PreferenceListSpacing,
    identity: String,
    isTransitionVisible: Boolean,
): Dp {
    val targetSpacing = listSpacing.spacingBefore(identity, isTransitionVisible)
    return animatedPreferenceSpacing(
        targetSpacing = targetSpacing,
        animateChanges = listSpacing.animateChanges,
        label = "Preference item spacing",
    )
}

@Composable
internal fun animatedPreferenceSpacing(
    targetSpacing: Dp,
    animateChanges: Boolean,
    label: String,
): Dp {
    if (!animateChanges) return targetSpacing
    return animateDpAsState(
        targetValue = targetSpacing,
        label = label,
    ).value
}

internal data class PreferenceRuntime(
    val screenState: PreferenceScreenState,
    val saveableStateHolder: SaveableStateHolder,
    val identityAllocator: PreferenceIdentityAllocator,
    val style: PreferenceStyle,
    val filterResultTracker: PreferenceFilterResultTracker,
    val sectionSeparatorTracker: PreferenceSectionSeparatorTracker,
    val sectionShapeTracker: PreferenceSectionShapeTracker,
    val listSpacingTracker: PreferenceListSpacingTracker,
    val listSpacing: PreferenceListSpacing,
    val filterQuery: String,
    val includeHierarchyInFilter: Boolean,
    val filterScope: PreferenceFilter.Scope,
    val currentPath: List<String>,
    val navigationPaths: List<List<String>>,
    val treePath: List<String>,
    val identityScope: List<String>,
    val ancestors: List<PreferenceAncestor>,
    val parentEnabled: Boolean,
    val parentVisible: Boolean,
    val parentExpanded: Boolean,
    val insideSection: Boolean,
    val sectionShapeCoordinates: LayoutCoordinates?,
) {
    val isFiltering: Boolean
        get() = filterQuery.isNotBlank()

    fun currentRouteKey(path: List<String> = currentPath): String =
        if (path.isEmpty()) "root" else path.joinToString("/")

    fun isInFilterScope(path: List<String> = treePath): Boolean =
        when (filterScope) {
            PreferenceFilter.Scope.CurrentLevel -> path == currentPath
            PreferenceFilter.Scope.CurrentBranch ->
                path.size >= currentPath.size && path.take(currentPath.size) == currentPath
            PreferenceFilter.Scope.Global -> true
        }
}

internal val LocalPreferenceRuntime =
    staticCompositionLocalOf<PreferenceRuntime?> { null }

internal val LocalPreferenceStyle =
    staticCompositionLocalOf<PreferenceStyle?> { null }

@Composable
internal fun currentPreferenceRuntime(): PreferenceRuntime =
    checkNotNull(LocalPreferenceRuntime.current) {
        "Preference composables must be called within PreferenceScreen."
    }

internal data class PreferenceEntryVisuals(
    val enabled: Boolean,
    val breadcrumb: String?,
    val style: PreferenceStyle,
    val insideSection: Boolean,
)

/** Registers one preference entry in the active screen and renders its content when applicable. */
@Composable
internal fun PreferenceEntry(
    title: String?,
    subtitle: String?,
    filterTags: List<String>,
    key: String?,
    isEnabled: @Composable () -> Boolean,
    isVisible: @Composable () -> Boolean,
    renderInFilter: Boolean = true,
    trackSectionSeparator: Boolean = true,
    trackInListSpacing: Boolean = true,
    breaksSectionShape: Boolean = false,
    content: @Composable (PreferenceEntryVisuals) -> Unit,
) {
    val runtime = currentPreferenceRuntime()
    val identity = remember(runtime.identityAllocator, runtime.identityScope, key) {
        runtime.identityAllocator.next(key, runtime.identityScope)
    }
    val enabled = runtime.parentEnabled && isEnabled()
    val visible = runtime.parentVisible && isVisible()
    val queryTokens = runtime.filterQuery
        .trim()
        .split(Regex("\\s+"))
        .filter(String::isNotEmpty)
    val searchable = buildList {
        title?.let { add(it) }
        subtitle?.let(::add)
        addAll(filterTags)
        if (runtime.includeHierarchyInFilter) {
            runtime.ancestors.forEach { ancestor ->
                add(ancestor.title)
                ancestor.subtitle?.let(::add)
                addAll(ancestor.filterTags)
            }
        }
    }
    val matches = queryTokens.all { token ->
        searchable.any { it.contains(token, ignoreCase = true) }
    }
    val isAtCurrentLevel = runtime.currentPath == runtime.treePath &&
            runtime.parentExpanded
    val isFilterResult =
        visible && renderInFilter && runtime.isInFilterScope() && matches
    if (runtime.isFiltering) {
        DisposableEffect(runtime.filterResultTracker, identity) {
            onDispose {
                runtime.filterResultTracker.remove(identity)
            }
        }
        SideEffect {
            runtime.filterResultTracker.update(
                identity = identity,
                hierarchy = runtime.identityScope,
                order = runtime.identityAllocator.order(identity),
                isMatch = isFilterResult && runtime.includeHierarchyInFilter,
            )
        }
    }
    val shouldRender = if (runtime.isFiltering) {
        isFilterResult
    } else {
        visible && isAtCurrentLevel
    }
    val visibilityState = remember(identity) {
        MutableTransitionState(if (runtime.isFiltering) false else shouldRender)
    }
    visibilityState.targetState = shouldRender
    val animateVisibility = runtime.listSpacing.animateChanges
    val isTransitionVisible = visibilityState.currentState || visibilityState.targetState
    val sectionShapeCoordinates = runtime.sectionShapeCoordinates
    val insideSection = runtime.insideSection && !breaksSectionShape
    val tracksSectionShape = sectionShapeCoordinates != null &&
        breaksSectionShape
    if (tracksSectionShape) {
        DisposableEffect(runtime.sectionShapeTracker, identity) {
            onDispose {
                runtime.sectionShapeTracker.remove(identity)
            }
        }
        SideEffect {
            runtime.sectionShapeTracker.update(
                identity = identity,
                scope = runtime.identityScope,
                order = runtime.identityAllocator.order(identity),
                isVisible = isTransitionVisible,
            )
        }
    }
    val tracksSectionContentDivider = trackSectionSeparator &&
            !runtime.isFiltering &&
            runtime.insideSection &&
            runtime.style.spacing.sectionContentSeparator is PreferenceSectionContentSeparatorStyle.Divider
    if (tracksSectionContentDivider) {
        DisposableEffect(runtime.sectionSeparatorTracker, identity) {
            onDispose {
                runtime.sectionSeparatorTracker.remove(identity)
            }
        }
        SideEffect {
            runtime.sectionSeparatorTracker.update(
                identity = identity,
                scope = runtime.identityScope,
                order = runtime.identityAllocator.order(identity),
                isVisible = shouldRender,
                isInsideSection = insideSection,
            )
        }
    }
    val hasNextSectionContentEntry = tracksSectionContentDivider &&
            insideSection &&
            shouldRender &&
            runtime.sectionSeparatorTracker.hasNext(identity, runtime.identityScope)
    if (trackInListSpacing) {
        DisposableEffect(runtime.listSpacingTracker, identity) {
            onDispose {
                runtime.listSpacingTracker.remove(identity)
            }
        }
        SideEffect {
            runtime.listSpacingTracker.update(
                identity = identity,
                scope = runtime.identityScope,
                order = runtime.identityAllocator.order(identity),
                isVisible = shouldRender,
                isTransitionVisible = isTransitionVisible,
                isInsideSection = insideSection,
                hasDividerAfter = hasNextSectionContentEntry,
            )
        }
    }
    val spacingBefore = if (trackInListSpacing) {
        animatedPreferenceListSpacing(
            listSpacing = runtime.listSpacing,
            identity = identity,
            isTransitionVisible = isTransitionVisible,
        )
    } else {
        0.dp
    }

    val breadcrumb = runtime.ancestors
        .map { it.title }
        .filter(String::isNotBlank)
        .joinToString(" › ")
        .ifBlank { null }
    val listScope = if (runtime.isFiltering) {
        "filter:${runtime.filterScope.name}:${runtime.currentRouteKey()}"
    } else {
        runtime.currentRouteKey()
    }
    val entryScope = (runtime.identityScope + identity).joinToString("/")
    val entryContent: @Composable () -> Unit = {
        runtime.saveableStateHolder.SaveableStateProvider("preference:$listScope:$entryScope") {
            if (runtime.isFiltering &&
                runtime.includeHierarchyInFilter &&
                runtime.filterResultTracker.startsHierarchyBlock(identity)
            ) {
                FilterSectionInfo(runtime.ancestors, runtime.style.spacing.itemSpacing)
            }
            content(
                PreferenceEntryVisuals(
                    enabled = enabled,
                    breadcrumb = breadcrumb,
                    style = runtime.style,
                    insideSection = insideSection,
                )
            )
        }
    }
    val visibilityModifier = Modifier.fillMaxWidth().then(
        if (tracksSectionShape) {
            Modifier.onGloballyPositioned { coordinates ->
                val parentCoordinates = sectionShapeCoordinates
                if (
                    parentCoordinates != null &&
                    parentCoordinates.isAttached &&
                    coordinates.isAttached
                ) {
                    val position = parentCoordinates.localPositionOf(
                        sourceCoordinates = coordinates,
                        relativeToSource = Offset.Zero,
                    )
                    runtime.sectionShapeTracker.updateBounds(
                        identity = identity,
                        bounds = Rect(
                            left = position.x,
                            top = position.y,
                            right = position.x + coordinates.size.width,
                            bottom = position.y + coordinates.size.height,
                        ),
                    )
                }
            }
        } else {
            Modifier
        }
    )
    AnimatedVisibility(
        visibleState = visibilityState,
        modifier = visibilityModifier,
        enter = if (animateVisibility) {
            fadeIn() + expandVertically(
                expandFrom = Alignment.Top,
                clip = true,
            )
        } else {
            EnterTransition.None
        },
        exit = if (animateVisibility) {
            fadeOut() + shrinkVertically(
                shrinkTowards = Alignment.Top,
                clip = true,
            )
        } else {
            ExitTransition.None
        },
        label = if (runtime.isFiltering) "Preference filter result" else "Preference visibility",
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = spacingBefore)
                .then(
                    if (runtime.isFiltering) {
                        Modifier.padding(bottom = runtime.style.spacing.itemSpacing)
                    } else {
                        Modifier
                    },
                ),
        ) {
            entryContent()
            if (tracksSectionContentDivider &&
                runtime.listSpacing.animateChanges
            ) {
                AnimatedVisibility(
                    visible = hasNextSectionContentEntry,
                    enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                    exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top),
                    label = "Preference section divider",
                ) {
                    PreferenceSectionContentDivider(runtime.style)
                }
            } else if (tracksSectionContentDivider && hasNextSectionContentEntry) {
                PreferenceSectionContentDivider(runtime.style)
            }
        }
    }
}
