package com.michaelflisar.composestyled.adaptive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.StyledNavigationMode
import com.michaelflisar.composestyled.platform.Platform
import com.michaelflisar.composestyled.platform.PlatformDefaults
import com.michaelflisar.composestyled.ui.components.StyledBottomNavigationBar
import com.michaelflisar.composestyled.ui.components.StyledNavigationItem
import com.michaelflisar.composestyled.ui.components.StyledSideNavigation
import com.michaelflisar.composestyled.ui.components.StyledSideNavigationDefaults
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.layout.StyledScaffold
import com.michaelflisar.composestyled.ui.layout.StyledScaffoldDefaults

/** Layout or host-window dimensions used by adaptive decisions. */
@Stable
data class StyledAdaptiveSize(
    val width: Dp,
    val height: Dp,
) {
    init {
        require(!width.value.isNaN() && width >= 0.dp) { "width must be specified and non-negative" }
        require(!height.value.isNaN() && height >= 0.dp) { "height must be specified and non-negative" }
    }

    val widthClass: WidthClass
        get() = when {
            width == Dp.Infinity -> WidthClass.Compact
            width < 600.dp -> WidthClass.Compact
            width < 840.dp -> WidthClass.Medium
            else -> WidthClass.Expanded
        }

    /** Whether the width meets [threshold]; infinite width is treated as unknown. */
    fun isWidthAtLeast(threshold: Dp): Boolean =
        width != Dp.Infinity && width >= threshold

    /** Width breakpoints used by adaptive layout policies. */
    enum class WidthClass {
        Compact,
        Medium,
        Expanded,
    }
}

/** Platform, local layout bounds and host-window bounds supplied to an adaptive policy. */
@Stable
data class StyledAdaptiveContext(
    val platform: Platform,
    val layoutSize: StyledAdaptiveSize,
    val windowSize: StyledAdaptiveSize,
)

/** Resolved navigation, master-detail and dialog decisions for an adaptive context. */
@Stable
data class StyledAdaptiveInfo(
    val context: StyledAdaptiveContext,
    val navigationMode: StyledNavigationMode,
    val masterDetailMode: StyledAdaptiveMasterDetailMode,
    val dialogStyle: StyledDialog.Style,
)

/** Whether master and detail content occupy one pane or appear side by side. */
enum class StyledAdaptiveMasterDetailMode {
    SinglePane,
    SideBySide,
}

/** Visibility decisions for master/detail panes without changing selection state. */
@Stable
data class StyledAdaptiveMasterDetailPanes(
    val showMaster: Boolean,
    val showDetail: Boolean,
    val showEmptyDetail: Boolean,
)

/**
 * Adaptive breakpoints. Replace only the decisions that differ from the defaults.
 */
class StyledAdaptivePolicy(
    val navigationMode: (Platform, StyledAdaptiveSize) -> StyledNavigationMode =
        StyledAdaptive::defaultNavigationMode,
    val masterDetailMode: (Platform, StyledAdaptiveSize) -> StyledAdaptiveMasterDetailMode =
        StyledAdaptive::defaultMasterDetailMode,
    val dialogStyle: (Platform, StyledAdaptiveSize) -> StyledDialog.Style =
        StyledAdaptive::defaultDialogStyle,
)

object StyledAdaptive {

    val DefaultPolicy = StyledAdaptivePolicy()

    /**
     * Resolves all adaptive decisions for explicit layout and host-window sizes.
     */
    fun resolve(
        context: StyledAdaptiveContext,
        policy: StyledAdaptivePolicy = DefaultPolicy,
    ): StyledAdaptiveInfo = StyledAdaptiveInfo(
        context = context,
        navigationMode = policy.navigationMode(context.platform, context.layoutSize),
        masterDetailMode = policy.masterDetailMode(context.platform, context.layoutSize),
        dialogStyle = policy.dialogStyle(context.platform, context.windowSize),
    )

    /**
     * Observes the current host window by default, or an explicit local layout-size state.
     */
    @Composable
    fun info(
        platform: Platform = StyledTheme.platform,
        layoutSize: State<StyledAdaptiveSize>? = null,
        policy: StyledAdaptivePolicy = DefaultPolicy,
    ): State<StyledAdaptiveInfo> {
        val density = LocalDensity.current
        val windowSize = LocalWindowInfo.current.containerSize
        val hostSize = with(density) {
            StyledAdaptiveSize(windowSize.width.toDp(), windowSize.height.toDp())
        }
        val context = StyledAdaptiveContext(
            platform = platform,
            layoutSize = layoutSize?.value ?: hostSize,
            windowSize = hostSize,
        )
        val contextState = rememberUpdatedState(context)
        val policyState = rememberUpdatedState(policy)
        return remember {
            derivedStateOf { resolve(contextState.value, policyState.value) }
        }
    }

    /**
     * Returns an observable navigation decision, using the host window unless [layoutSize] is supplied.
     */
    @Composable
    fun navigation(
        platform: Platform = StyledTheme.platform,
        layoutSize: State<StyledAdaptiveSize>? = null,
        policy: StyledAdaptivePolicy = DefaultPolicy,
    ): State<StyledNavigationMode> {
        val adaptiveInfo = info(platform, layoutSize, policy)
        return navigation(adaptiveInfo)
    }

    /**
     * Derives an observable navigation decision from an already observed adaptive info state.
     */
    @Composable
    fun navigation(adaptiveInfo: State<StyledAdaptiveInfo>): State<StyledNavigationMode> =
        remember(adaptiveInfo) {
            derivedStateOf { adaptiveInfo.value.navigationMode }
        }

    /**
     * Returns an observable master-detail layout decision from an already observed info state.
     */
    @Composable
    fun masterDetail(adaptiveInfo: State<StyledAdaptiveInfo>): State<StyledAdaptiveMasterDetailMode> =
        remember(adaptiveInfo) {
            derivedStateOf { adaptiveInfo.value.masterDetailMode }
        }

    /**
     * Returns an observable dialog style from an already observed adaptive info state.
     */
    @Composable
    fun dialog(adaptiveInfo: State<StyledAdaptiveInfo>): State<StyledDialog.Style> =
        remember(adaptiveInfo) {
            derivedStateOf { adaptiveInfo.value.dialogStyle }
        }

    /**
     * Returns an observable master-detail layout decision, using the host window unless [layoutSize] is supplied.
     */
    @Composable
    fun masterDetail(
        platform: Platform = StyledTheme.platform,
        layoutSize: State<StyledAdaptiveSize>? = null,
        policy: StyledAdaptivePolicy = DefaultPolicy,
    ): State<StyledAdaptiveMasterDetailMode> {
        val adaptiveInfo = info(platform, layoutSize, policy)
        return masterDetail(adaptiveInfo)
    }

    /**
     * Returns an observable dialog style based on the host window size.
     */
    @Composable
    fun dialog(
        platform: Platform = StyledTheme.platform,
        policy: StyledAdaptivePolicy = DefaultPolicy,
    ): State<StyledDialog.Style> {
        val adaptiveInfo = info(platform, policy = policy)
        return dialog(adaptiveInfo)
    }

    /**
     * Resolves which panes are visible without changing selection or navigation state.
     */
    fun masterDetailPanes(
        mode: StyledAdaptiveMasterDetailMode,
        hasDetail: Boolean,
    ): StyledAdaptiveMasterDetailPanes = when (mode) {
        StyledAdaptiveMasterDetailMode.SinglePane -> StyledAdaptiveMasterDetailPanes(
            showMaster = !hasDetail,
            showDetail = hasDetail,
            showEmptyDetail = false,
        )

        StyledAdaptiveMasterDetailMode.SideBySide -> StyledAdaptiveMasterDetailPanes(
            showMaster = true,
            showDetail = hasDetail,
            showEmptyDetail = !hasDetail,
        )
    }

    /**
     * Whether scrollbars should be shown by default for the given platform.
     */
    fun showScrollbars(platform: Platform): Boolean = PlatformDefaults.showScrollbars(platform)

    internal fun defaultNavigationMode(
        platform: Platform,
        size: StyledAdaptiveSize,
    ): StyledNavigationMode = when {
        size.width == Dp.Infinity || size.width < 600.dp -> StyledNavigationMode.Bottom
        size.width < 840.dp ->
            StyledNavigationMode.Side(StyledSideNavigation.Mode.Compact)

        size.width < 1200.dp && platform.isDesktopOrWeb() ->
            StyledNavigationMode.Side(StyledSideNavigation.Mode.Expandable)

        else -> StyledNavigationMode.Side(StyledSideNavigation.Mode.Expanded)
    }

    internal fun defaultMasterDetailMode(
        platform: Platform,
        size: StyledAdaptiveSize,
    ): StyledAdaptiveMasterDetailMode {
        val breakpoint = if (platform.isDesktopOrWeb()) 720.dp else 840.dp
        return if (size.isWidthAtLeast(breakpoint)) {
            StyledAdaptiveMasterDetailMode.SideBySide
        } else {
            StyledAdaptiveMasterDetailMode.SinglePane
        }
    }

    internal fun defaultDialogStyle(
        @Suppress("UNUSED_PARAMETER") platform: Platform,
        size: StyledAdaptiveSize,
    ): StyledDialog.Style =
        if (size.width < 600.dp || size.height < 480.dp) {
            StyledDialog.Style.Fullscreen()
        } else {
            StyledDialog.Style.ToolbarDialog()
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
    BoxWithConstraints(modifier) {
        val density = LocalDensity.current
        val windowSize = LocalWindowInfo.current.containerSize
        val hostSize = with(density) {
            StyledAdaptiveSize(windowSize.width.toDp(), windowSize.height.toDp())
        }
        val layoutSize = StyledAdaptiveSize(maxWidth, maxHeight)
        val navigationMode = navigationMode(platform, layoutSize)
        val scaffoldInfo = StyledAdaptive.resolve(
            StyledAdaptiveContext(platform, layoutSize, hostSize),
            policy,
        ).copy(navigationMode = navigationMode)
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
            BoxWithConstraints(Modifier.fillMaxSize()) {
                val contentInfo = StyledAdaptive.resolve(
                    StyledAdaptiveContext(
                        platform = platform,
                        layoutSize = StyledAdaptiveSize(maxWidth, maxHeight),
                        windowSize = hostSize,
                    ),
                    policy,
                ).copy(navigationMode = navigationMode)
                val contentInfoState = rememberUpdatedState(contentInfo)
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
    BoxWithConstraints(modifier) {
        val density = LocalDensity.current
        val windowSize = LocalWindowInfo.current.containerSize
        val hostSize = with(density) {
            StyledAdaptiveSize(windowSize.width.toDp(), windowSize.height.toDp())
        }
        val layoutSize = StyledAdaptiveSize(maxWidth, maxHeight)
        val navigationMode = navigationMode(platform, layoutSize)
        val scaffoldInfo = StyledAdaptive.resolve(
            StyledAdaptiveContext(platform, layoutSize, hostSize),
            policy,
        ).copy(navigationMode = navigationMode)
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
            BoxWithConstraints(Modifier.fillMaxSize()) {
                val contentInfo = StyledAdaptive.resolve(
                    StyledAdaptiveContext(
                        platform = platform,
                        layoutSize = StyledAdaptiveSize(maxWidth, maxHeight),
                        windowSize = hostSize,
                    ),
                    policy,
                ).copy(navigationMode = navigationMode)
                val contentInfoState = rememberUpdatedState(contentInfo)
                content(paddingValues, contentInfoState)
            }
        }
    }
}

/**
 * Arranges a master and optional detail pane without owning selection or navigation state.
 */
@Composable
fun StyledAdaptiveMasterDetail(
    modifier: Modifier = Modifier,
    platform: Platform = StyledTheme.platform,
    masterDetailMode: State<StyledAdaptiveMasterDetailMode>? = null,
    policy: StyledAdaptivePolicy = StyledAdaptive.DefaultPolicy,
    masterFraction: Float = 0.4f,
    paneSpacing: Dp = StyledTheme.spacing.medium,
    master: @Composable (StyledAdaptiveInfo) -> Unit,
    detail: (@Composable (StyledAdaptiveInfo) -> Unit)? = null,
    emptyDetail: (@Composable (StyledAdaptiveInfo) -> Unit)? = null,
) {
    require(masterFraction > 0f && masterFraction < 1f) {
        "masterFraction must be greater than 0 and less than 1"
    }
    require(paneSpacing >= 0.dp && paneSpacing != Dp.Infinity) {
        "paneSpacing must be finite and non-negative"
    }
    BoxWithConstraints(modifier.fillMaxSize()) {
        val layoutSizeState = rememberUpdatedState(StyledAdaptiveSize(maxWidth, maxHeight))
        val localInfo = StyledAdaptive.info(
            platform = platform,
            layoutSize = layoutSizeState,
            policy = policy,
        ).value
        val currentInfo = masterDetailMode?.value?.let {
            localInfo.copy(masterDetailMode = it)
        } ?: localInfo
        val panes = StyledAdaptive.masterDetailPanes(
            mode = currentInfo.masterDetailMode,
            hasDetail = detail != null,
        )
        when (currentInfo.masterDetailMode) {
            StyledAdaptiveMasterDetailMode.SinglePane -> {
                if (panes.showDetail) {
                    detail?.invoke(currentInfo)
                } else {
                    master(currentInfo)
                }
            }

            StyledAdaptiveMasterDetailMode.SideBySide -> {
                Row(modifier = Modifier.fillMaxSize()) {
                    if (panes.showMaster) {
                        Box(Modifier.weight(masterFraction).fillMaxSize()) {
                            master(currentInfo)
                        }
                    }
                    Spacer(Modifier.width(paneSpacing))
                    Box(Modifier.weight(1f - masterFraction).fillMaxSize()) {
                        when {
                            panes.showDetail -> detail?.invoke(currentInfo)
                            panes.showEmptyDetail -> emptyDetail?.invoke(currentInfo)
                        }
                    }
                }
            }
        }
    }
}
