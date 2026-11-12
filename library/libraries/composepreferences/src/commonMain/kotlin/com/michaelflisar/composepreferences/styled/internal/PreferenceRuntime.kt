package com.michaelflisar.composepreferences.styled.internal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composepreferences.styled.PreferenceFilter
import com.michaelflisar.composepreferences.styled.PreferenceInGroupSeparatorStyle
import com.michaelflisar.composepreferences.styled.PreferenceStyle
import com.michaelflisar.composepreferences.styled.layout.PreferenceInGroupDivider
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

internal class PreferenceGroupSeparatorTracker {
    private data class Entry(
        val scope: List<String>,
        val order: Int,
    )

    private val entries = mutableStateMapOf<String, Entry>()

    fun update(identity: String, scope: List<String>, order: Int, isVisible: Boolean) {
        if (isVisible) {
            val entry = Entry(scope = scope, order = order)
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
        return entries.values.any { it.scope == scope && it.order > current.order }
    }
}

internal class PreferenceListSpacingTracker {
    private data class Entry(
        val scope: List<String>,
        val order: Int,
        val isDetachedSection: Boolean,
        val hasDividerAfter: Boolean,
    )

    private val entries = mutableStateMapOf<String, Entry>()

    fun update(
        identity: String,
        scope: List<String>,
        order: Int,
        isVisible: Boolean,
        isDetachedSection: Boolean = false,
        hasDividerAfter: Boolean = false,
    ) {
        if (isVisible) {
            val entry = Entry(
                scope = scope,
                order = order,
                isDetachedSection = isDetachedSection,
                hasDividerAfter = hasDividerAfter,
            )
            if (entries[identity] != entry) entries[identity] = entry
        } else {
            entries.remove(identity)
        }
    }

    fun remove(identity: String) {
        entries.remove(identity)
    }

    fun spacingKinds(scope: List<String>): List<Boolean> =
        entries.values
            .filter { it.scope == scope }
            .sortedBy(Entry::order)
            .flatMap { entry ->
                buildList {
                    add(entry.isDetachedSection)
                    if (entry.hasDividerAfter) add(false)
                }
            }
}

internal class PreferenceListSpacingArrangement(
    private val tracker: PreferenceListSpacingTracker,
    private val scope: List<String>,
    private val itemSpacing: Dp,
    private val sectionSpacing: Dp,
    private val leadingItems: Int = 0,
) : Arrangement.Vertical {
    override val spacing: Dp
        get() = if (tracker.spacingKinds(scope).isEmpty()) {
            itemSpacing
        } else {
            minOf(itemSpacing, sectionSpacing)
        }

    override fun Density.arrange(
        totalSize: Int,
        sizes: IntArray,
        outPositions: IntArray,
    ) {
        val spacingKinds = spacingKinds()
        if (spacingKinds.size != sizes.size) {
            arrangeWithSpacing(sizes, outPositions, itemSpacing.roundToPx())
            return
        }
        var position = 0
        sizes.indices.forEach { index ->
            if (index > 0) {
                val spacing = if (spacingKinds[index]) {
                    sectionSpacing
                } else {
                    itemSpacing
                }
                position += spacing.roundToPx()
            }
            outPositions[index] = position
            position += sizes[index]
        }
    }

    fun additionalSpacingPx(density: Density): Int {
        val baseline = with(density) { spacing.roundToPx() }
        return spacingKinds().drop(1).sumOf { isSection ->
            val spacing = if (isSection) sectionSpacing else itemSpacing
            (with(density) { spacing.roundToPx() } - baseline).coerceAtLeast(0)
        }
    }

    private fun spacingKinds(): List<Boolean> = buildList {
        repeat(leadingItems) { add(false) }
        addAll(tracker.spacingKinds(scope))
    }

    private fun arrangeWithSpacing(
        sizes: IntArray,
        outPositions: IntArray,
        spacing: Int,
    ) {
        var position = 0
        sizes.indices.forEach { index ->
            if (index > 0) position += spacing
            outPositions[index] = position
            position += sizes[index]
        }
    }
}

internal fun Modifier.accountForPreferenceListSpacing(
    arrangement: PreferenceListSpacingArrangement,
): Modifier = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    val height = placeable.height + arrangement.additionalSpacingPx(this)
    // The column places variable gaps itself; extend its measured bounds to include them.
    layout(placeable.width, height) {
        placeable.place(0, 0)
    }
}

internal data class PreferenceRuntime(
    val screenState: PreferenceScreenState,
    val saveableStateHolder: SaveableStateHolder,
    val identityAllocator: PreferenceIdentityAllocator,
    val style: PreferenceStyle,
    val filterResultTracker: PreferenceFilterResultTracker,
    val groupSeparatorTracker: PreferenceGroupSeparatorTracker,
    val listSpacingTracker: PreferenceListSpacingTracker,
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
    title: String,
    subtitle: String?,
    filterTags: List<String>,
    key: String?,
    isEnabled: @Composable () -> Boolean,
    isVisible: @Composable () -> Boolean,
    renderInFilter: Boolean = true,
    trackInGroupSeparator: Boolean = true,
    trackInListSpacing: Boolean = true,
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
        add(title)
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
    val usesInGroupDivider = trackInGroupSeparator &&
            !runtime.isFiltering &&
            runtime.insideSection &&
            runtime.style.spacing.inGroupSeparator is PreferenceInGroupSeparatorStyle.Divider
    if (usesInGroupDivider) {
        DisposableEffect(runtime.groupSeparatorTracker, identity) {
            onDispose {
                runtime.groupSeparatorTracker.remove(identity)
            }
        }
        SideEffect {
            runtime.groupSeparatorTracker.update(
                identity = identity,
                scope = runtime.identityScope,
                order = runtime.identityAllocator.order(identity),
                isVisible = shouldRender,
            )
        }
    }
    val hasNextInGroupEntry = usesInGroupDivider &&
            shouldRender &&
            runtime.groupSeparatorTracker.hasNext(identity, runtime.identityScope)
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
                isVisible = shouldRender || runtime.isFiltering,
                hasDividerAfter = hasNextInGroupEntry,
            )
        }
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
                    insideSection = runtime.insideSection,
                )
            )
        }
    }
    if (runtime.isFiltering) {
        val visibilityState = remember(identity) { MutableTransitionState(false) }
        visibilityState.targetState = shouldRender
        AnimatedVisibility(
            visibleState = visibilityState,
            modifier = Modifier.fillMaxWidth(),
            enter = fadeIn() + expandVertically(
                expandFrom = Alignment.Top,
                clip = true,
            ),
            exit = fadeOut() + shrinkVertically(
                shrinkTowards = Alignment.Top,
                clip = true,
            ),
            label = "Preference filter result",
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = runtime.style.spacing.itemSpacing),
            ) {
                entryContent()
            }
        }
    } else if (shouldRender) {
        val renderedContent: @Composable () -> Unit = {
            entryContent()
            if (hasNextInGroupEntry) {
                PreferenceInGroupDivider(runtime.style)
            }
        }
        renderedContent()
    }
}
