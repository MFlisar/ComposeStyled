package com.michaelflisar.composepreferences.styled.layout

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.michaelflisar.composepreferences.styled.LocalPreferenceStrings
import com.michaelflisar.composepreferences.styled.PreferenceFilter
import com.michaelflisar.composepreferences.styled.PreferenceOpenGroupHeaderStyle
import com.michaelflisar.composepreferences.styled.PreferenceStyle
import com.michaelflisar.composepreferences.styled.PreferenceStrings
import com.michaelflisar.composepreferences.styled.internal.LocalPreferenceRuntime
import com.michaelflisar.composepreferences.styled.internal.LocalPreferenceStyle
import com.michaelflisar.composepreferences.styled.internal.PreferenceFilterResultTracker
import com.michaelflisar.composepreferences.styled.internal.PreferenceGroupSeparatorTracker
import com.michaelflisar.composepreferences.styled.internal.PreferenceIdentityAllocator
import com.michaelflisar.composepreferences.styled.internal.PreferenceListSpacingArrangement
import com.michaelflisar.composepreferences.styled.internal.PreferenceListSpacingTracker
import com.michaelflisar.composepreferences.styled.internal.PreferenceRuntime
import com.michaelflisar.composepreferences.styled.rememberPreferenceStyle
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledListItem
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.primitives.StyledText

@Stable
class PreferenceScreenState internal constructor(
    routes: List<PreferenceRoute> = emptyList(),
) {
    internal var routes by mutableStateOf(routes)
        private set

    internal fun navigate(route: PreferenceRoute, parentDepth: Int) {
        routes = routes.take(parentDepth) + route
    }

    internal fun navigateBack() {
        if (routes.isNotEmpty()) routes = routes.dropLast(1)
    }

    internal fun navigateToDepth(depth: Int) {
        routes = routes.take(depth)
    }
}

internal data class PreferenceRoute(
    val id: String,
    val title: String,
)

private val PreferenceScreenStateSaver: Saver<PreferenceScreenState, List<Any>> = Saver(
    save = { state -> state.routes.flatMap { listOf(it.id, it.title) } },
    restore = { saved ->
        PreferenceScreenState(
            saved.chunked(2).map { PreferenceRoute(it[0] as String, it[1] as String) },
        )
    },
)

/** Remembers navigation state for a [PreferenceScreen]. */
@Composable
fun rememberPreferenceScreenState(): PreferenceScreenState =
    rememberSaveable(saver = PreferenceScreenStateSaver) { PreferenceScreenState() }

/** Hosts nested preference lists, filtering and navigation state. */
@Composable
fun PreferenceScreen(
    modifier: Modifier = Modifier,
    state: PreferenceScreenState = rememberPreferenceScreenState(),
    filterQuery: String? = null,
    includeHierarchyInFilter: Boolean = true,
    style: PreferenceStyle = rememberPreferenceStyle(),
    strings: PreferenceStrings = PreferenceStrings.ENGLISH,
    content: @Composable () -> Unit,
) {
    PreferenceScreenImpl(
        modifier = modifier,
        state = state,
        filterQuery = filterQuery.orEmpty(),
        filterScope = PreferenceFilter.Scope.Global,
        includeHierarchyInFilter = includeHierarchyInFilter,
        style = style,
        strings = strings,
        content = content,
    )
}

/** Hosts nested preferences using a saveable filter state. */
@Composable
fun PreferenceScreen(
    filter: PreferenceFilter,
    modifier: Modifier = Modifier,
    state: PreferenceScreenState = rememberPreferenceScreenState(),
    style: PreferenceStyle = rememberPreferenceStyle(),
    strings: PreferenceStrings = PreferenceStrings.ENGLISH,
    content: @Composable () -> Unit,
) {
    PreferenceScreenImpl(
        modifier = modifier,
        state = state,
        filterQuery = filter.query,
        filterScope = filter.scope,
        includeHierarchyInFilter = filter.includeHierarchyInFilter,
        style = style,
        strings = strings,
        content = content,
    )
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun PreferenceScreenImpl(
    modifier: Modifier,
    state: PreferenceScreenState,
    filterQuery: String,
    filterScope: PreferenceFilter.Scope,
    includeHierarchyInFilter: Boolean,
    style: PreferenceStyle,
    strings: PreferenceStrings,
    content: @Composable () -> Unit,
) {
    val saveableStateHolder = rememberSaveableStateHolder()
    val query = filterQuery.trim()
    val direction = LocalLayoutDirection.current
    val shouldHandleBack = query.isEmpty() && state.routes.isNotEmpty()
    BackHandler(enabled = shouldHandleBack) {
        state.navigateBack()
    }

    CompositionLocalProvider(
        LocalPreferenceStyle provides style,
        LocalPreferenceStrings provides strings,
    ) {
        val lastNonEmptyQuery = remember { mutableStateOf(query) }
        if (query.isNotEmpty()) {
            SideEffect {
                lastNonEmptyQuery.value = query
            }
        }
        val filteringTransition = updateTransition(
            targetState = query.isNotEmpty(),
            label = "Preference filtering",
        )
        filteringTransition.AnimatedContent(
            modifier = modifier,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
        ) { isFiltering ->
            if (isFiltering) {
                val currentPath = when (filterScope) {
                    PreferenceFilter.Scope.CurrentLevel,
                    PreferenceFilter.Scope.CurrentBranch -> state.routes.map { it.id }
                    PreferenceFilter.Scope.Global -> emptyList()
                }
                val filterKey = buildString {
                    append("preference:filter:")
                    append(filterScope.name)
                    if (currentPath.isNotEmpty()) {
                        append(":")
                        append(currentPath.joinToString("/"))
                    }
                }
                saveableStateHolder.SaveableStateProvider(filterKey) {
                    PreferencePage(
                        modifier = Modifier.fillMaxSize(),
                        state = state,
                        saveableStateHolder = saveableStateHolder,
                        style = style,
                        query = query.ifEmpty { lastNonEmptyQuery.value },
                        includeHierarchyInFilter = includeHierarchyInFilter,
                        filterScope = filterScope,
                        currentPath = currentPath,
                        pageTitle = null,
                        navigationPaths = listOf(state.routes.map { it.id }),
                        content = content,
                    )
                }
            } else {
                val navigationTransition = updateTransition(
                    targetState = state.routes,
                    label = "Preference navigation",
                )
                navigationTransition.AnimatedContent(
                    modifier = Modifier.fillMaxSize(),
                    transitionSpec = {
                        val goingDeeper = targetState.size > initialState.size
                        val forward = direction == LayoutDirection.Ltr
                        val enterOffset = if (goingDeeper == forward) 1 else -1
                        val exitOffset = -enterOffset
                        slideInHorizontally { it * enterOffset } togetherWith
                                slideOutHorizontally { it * exitOffset }
                    },
                ) { routes ->
                    val currentPath = routes.map { it.id }
                    val navigationPaths = listOf(
                        navigationTransition.currentState.map { it.id },
                        navigationTransition.targetState.map { it.id },
                    ).distinct()
                    val pageTitle = routes.lastOrNull()?.title
                    val pageKey = if (currentPath.isEmpty()) {
                        "preference:root"
                    } else {
                        "preference:${currentPath.joinToString("/")}"
                    }
                    saveableStateHolder.SaveableStateProvider(pageKey) {
                        PreferencePage(
                            modifier = Modifier.fillMaxSize(),
                            state = state,
                            saveableStateHolder = saveableStateHolder,
                            style = style,
                            query = "",
                            includeHierarchyInFilter = includeHierarchyInFilter,
                            filterScope = filterScope,
                            currentPath = currentPath,
                            pageTitle = pageTitle,
                            navigationPaths = navigationPaths,
                            content = content,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PreferencePage(
    modifier: Modifier,
    state: PreferenceScreenState,
    saveableStateHolder: SaveableStateHolder,
    style: PreferenceStyle,
    query: String,
    includeHierarchyInFilter: Boolean,
    filterScope: PreferenceFilter.Scope,
    currentPath: List<String>,
    pageTitle: String?,
    navigationPaths: List<List<String>>,
    content: @Composable () -> Unit,
) {
    val scrollState = if (currentPath.isEmpty()) {
        rememberScrollState()
    } else {
        remember { ScrollState(0) }
    }
    val identityAllocator = remember { PreferenceIdentityAllocator() }
    val filterResultTracker = remember(
        includeHierarchyInFilter,
        filterScope,
        currentPath,
    ) {
        PreferenceFilterResultTracker()
    }
    val groupSeparatorTracker = remember { PreferenceGroupSeparatorTracker() }
    val listSpacingTracker = remember { PreferenceListSpacingTracker() }
    val groupHeader = when (val openGroupHeaderStyle = style.openedGroupHeader) {
        PreferenceOpenGroupHeaderStyle.Disabled -> null
        is PreferenceOpenGroupHeaderStyle.Style ->
            pageTitle?.takeIf { query.isEmpty() }?.let { it to openGroupHeaderStyle.value }
    }
    val pageSpacing = if (query.isNotEmpty() || currentPath.isNotEmpty()) {
        0.dp
    } else {
        style.spacing.itemSpacing
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(style.outerPadding),
        verticalArrangement = PreferenceListSpacingArrangement(
            tracker = listSpacingTracker,
            scope = emptyList(),
            itemSpacing = pageSpacing,
            sectionSpacing = style.spacing.sectionSpacing,
        ),
    ) {
        if (groupHeader != null) {
            val (groupHeaderTitle, groupHeaderStyle) = groupHeader
            StyledListItem(
                headline = {
                    StyledText(groupHeaderTitle, style = groupHeaderStyle.titleTextStyle)
                },
                leading = {
                    StyledIcon(
                        icon = StyledIcons.SubmenuBack,
                        contentDescription = null,
                    )
                },
                onClick = state::navigateBack,
                shape = groupHeaderStyle.shape,
                minHeight = groupHeaderStyle.minimumItemHeight,
                contentPadding = groupHeaderStyle.contentPadding,
                containerColor = groupHeaderStyle.containerColor,
                contentColor = groupHeaderStyle.contentColor,
            )
        }
        CompositionLocalProvider(
            LocalPreferenceRuntime provides PreferenceRuntime(
                screenState = state,
                saveableStateHolder = saveableStateHolder,
                identityAllocator = identityAllocator,
                style = style,
                filterResultTracker = filterResultTracker,
                groupSeparatorTracker = groupSeparatorTracker,
                listSpacingTracker = listSpacingTracker,
                filterQuery = query,
                includeHierarchyInFilter = includeHierarchyInFilter,
                filterScope = filterScope,
                currentPath = currentPath,
                navigationPaths = navigationPaths,
                treePath = emptyList(),
                identityScope = emptyList(),
                ancestors = emptyList(),
                parentEnabled = true,
                parentVisible = true,
                parentExpanded = true,
                insideSection = false,
            ),
        ) {
            content()
        }
    }
}
