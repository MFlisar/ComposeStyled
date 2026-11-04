package com.michaelflisar.composestyled.adaptive

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.StyledNavigationMode
import com.michaelflisar.composestyled.platform.Platform
import com.michaelflisar.composestyled.platform.PlatformDefaults
import com.michaelflisar.composestyled.ui.components.StyledSideNavigation
import com.michaelflisar.composestyled.ui.layout.StyledDialog

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