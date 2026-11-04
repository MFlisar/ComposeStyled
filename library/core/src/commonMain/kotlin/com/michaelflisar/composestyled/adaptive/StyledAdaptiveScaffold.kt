package com.michaelflisar.composestyled.adaptive

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.StyledNavigationMode
import com.michaelflisar.composestyled.platform.Platform
import com.michaelflisar.composestyled.ui.components.StyledBottomNavigationBar
import com.michaelflisar.composestyled.ui.components.StyledNavigationItem
import com.michaelflisar.composestyled.ui.components.StyledSideNavigation
import com.michaelflisar.composestyled.ui.components.StyledSideNavigationDefaults
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.layout.StyledScaffold
import com.michaelflisar.composestyled.ui.layout.StyledScaffoldDefaults

/**
 * Read-only adaptive information reported by one StyledAdaptiveScaffold.
 * Create one instance per scaffold with `remember { StyledAdaptiveScaffoldState() }`.
 */
@Stable
class StyledAdaptiveScaffoldState {
    /** Last committed decisions for this scaffold, or null before its first layout. */
    var info: StyledAdaptiveInfo? by mutableStateOf(null)
        private set

    /** The last committed navigation mode selected from the scaffold's constraints. */
    val navigationMode: StyledNavigationMode?
        get() = info?.navigationMode

    /** The last committed master-detail mode for the scaffold's overall constraints. */
    val masterDetailMode: StyledAdaptiveMasterDetailMode?
        get() = info?.masterDetailMode

    /** The last committed dialog style selected from the host window size. */
    val dialogStyle: StyledDialog.Style?
        get() = info?.dialogStyle

    internal fun update(info: StyledAdaptiveInfo) {
        this.info = info
    }
}


/**
 * Arranges caller-owned navigation and renders scaffold content with its current adaptive information.
 */
@Composable
fun <T> StyledAdaptiveScaffold(
    platform: Platform,
    navigationItems: List<StyledNavigationItem<T>>,
    selectedKey: MutableState<T> = remember { mutableStateOf(navigationItems.first().key) },
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: StyledScaffold.FloatingActionButtonPosition = StyledScaffold.FloatingActionButtonPosition.End,
    containerColor: Color = StyledTheme.colorScheme.surface,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = StyledScaffoldDefaults.contentWindowInsets,
    policy: StyledAdaptivePolicy = StyledAdaptive.DefaultPolicy,
    navigationMode: (platform: Platform, layoutSize: StyledAdaptiveSize) -> StyledNavigationMode =
        { currentPlatform, layoutSize ->
            policy.navigationMode(currentPlatform, layoutSize)
        },
    navigationContainerColor: Color = StyledTheme.colorScheme.surfaceVariant,
    navigationContentColor: Color = StyledTheme.contentColorFor(containerColor),
    sideNavigationExpanded: MutableState<Boolean> = remember { mutableStateOf(false) },
    state: StyledAdaptiveScaffoldState = remember { StyledAdaptiveScaffoldState() },
    sideNavigationToggleIcon: @Composable (StyledSideNavigation.ToggleState) -> Unit = {
        StyledSideNavigationDefaults.ToggleIcon(it)
    },
    collapseSideNavigationOnModeChange: Boolean = true,
    content: @Composable (PaddingValues, State<StyledAdaptiveInfo>) -> Unit,
) {
    val sideNavigationScrollState = rememberScrollState()
    StyledAdaptiveContainer(
        modifier = modifier,
        platform = platform,
        policy = policy,
    ) { adaptiveInfo ->
        val info = adaptiveInfo.value
        val navigationMode = navigationMode(platform, info.context.layoutSize)
        val scaffoldInfo = info.copy(navigationMode = navigationMode)
        SideEffect {
            if (
                collapseSideNavigationOnModeChange &&
                state.navigationMode != null &&
                state.navigationMode != navigationMode &&
                navigationMode == StyledNavigationMode.Side(StyledSideNavigation.Mode.Expandable)
            ) {
                sideNavigationExpanded.value = false
            }
            state.update(scaffoldInfo)
        }
        StyledScaffold(
            modifier = Modifier,
            topBar = topBar,
            bottomBar = {
                if (navigationMode.isBottom) {
                    StyledBottomNavigationBar(
                        items = navigationItems,
                        selectedKey = selectedKey.value,
                        onItemClick = { selectedKey.value = it.key },
                        containerColor = navigationContainerColor,
                        contentColor = navigationContentColor,
                    )
                }
            },
            sideNavigation = {
                if (navigationMode.isSide) {
                    StyledSideNavigation(
                        items = navigationItems,
                        selectedKey = selectedKey.value,
                        onItemClick = { selectedKey.value = it.key },
                        containerColor = navigationContainerColor,
                        contentColor = navigationContentColor,
                        mode = when (navigationMode) {
                            StyledNavigationMode.Bottom -> error("Bottom navigation has no side mode")
                            is StyledNavigationMode.Side -> navigationMode.mode
                        },
                        expanded = sideNavigationExpanded,
                        toggleIcon = sideNavigationToggleIcon,
                        scrollState = sideNavigationScrollState,
                    )
                }
            },
            snackbarHost = snackbarHost,
            floatingActionButton = floatingActionButton,
            floatingActionButtonPosition = floatingActionButtonPosition,
            containerColor = containerColor,
            contentColor = contentColor,
            contentWindowInsets = contentWindowInsets,
        ) { paddingValues ->
            StyledAdaptiveContainer(
                modifier = Modifier.fillMaxSize(),
                platform = platform,
                policy = policy,
            ) { contentAdaptiveInfo ->
                val contentInfoState = rememberUpdatedState(
                    contentAdaptiveInfo.value.copy(navigationMode = navigationMode),
                )
                content(paddingValues, contentInfoState)
            }
        }
    }
}

/**
 * Arranges caller-owned navigation and renders scaffold content with its current adaptive information.
 */
@Composable
fun StyledAdaptiveScaffold(
    platform: Platform,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: StyledScaffold.FloatingActionButtonPosition = StyledScaffold.FloatingActionButtonPosition.End,
    containerColor: Color = StyledTheme.colorScheme.surface,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = StyledScaffoldDefaults.contentWindowInsets,
    policy: StyledAdaptivePolicy = StyledAdaptive.DefaultPolicy,
    navigationMode: (platform: Platform, layoutSize: StyledAdaptiveSize) -> StyledNavigationMode =
        { currentPlatform, layoutSize ->
            policy.navigationMode(currentPlatform, layoutSize)
        },
    state: StyledAdaptiveScaffoldState = remember { StyledAdaptiveScaffoldState() },
    sideNavigationExpanded: MutableState<Boolean> = remember { mutableStateOf(false) },
    collapseSideNavigationOnModeChange: Boolean = true,
    navigation: @Composable (navigationMode: StyledNavigationMode) -> Unit,
    content: @Composable (PaddingValues, State<StyledAdaptiveInfo>) -> Unit,
) {
    StyledAdaptiveContainer(
        modifier = modifier,
        platform = platform,
        policy = policy,
    ) { adaptiveInfo ->
        val info = adaptiveInfo.value
        val navigationMode = navigationMode(platform, info.context.layoutSize)
        val scaffoldInfo = info.copy(navigationMode = navigationMode)
        SideEffect {
            if (
                collapseSideNavigationOnModeChange &&
                state.navigationMode != null &&
                state.navigationMode != navigationMode &&
                navigationMode == StyledNavigationMode.Side(StyledSideNavigation.Mode.Expandable)
            ) {
                sideNavigationExpanded.value = false
            }
            state.update(scaffoldInfo)
        }
        StyledScaffold(
            modifier = Modifier,
            topBar = topBar,
            bottomBar = {
                if (navigationMode.isBottom) {
                    navigation(navigationMode)
                }
            },
            sideNavigation = {
                if (navigationMode.isSide) {
                    navigation(navigationMode)
                }
            },
            snackbarHost = snackbarHost,
            floatingActionButton = floatingActionButton,
            floatingActionButtonPosition = floatingActionButtonPosition,
            containerColor = containerColor,
            contentColor = contentColor,
            contentWindowInsets = contentWindowInsets,
        ) { paddingValues ->
            StyledAdaptiveContainer(
                modifier = Modifier.fillMaxSize(),
                platform = platform,
                policy = policy,
            ) { contentAdaptiveInfo ->
                val contentInfoState = rememberUpdatedState(
                    contentAdaptiveInfo.value.copy(navigationMode = navigationMode),
                )
                content(paddingValues, contentInfoState)
            }
        }
    }
}
