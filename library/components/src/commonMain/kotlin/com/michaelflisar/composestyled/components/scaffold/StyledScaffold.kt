package com.michaelflisar.composestyled.components.scaffold

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.components.classes.InsetsPx
import com.michaelflisar.composestyled.components.classes.PaddingValuesDp
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.components.StyledSurface
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.ProvideStyledLocals
import kotlin.math.roundToInt

/* ---------- Public API ---------- */

@Stable
enum class FabVerticalAlignment { Top, Bottom }

@Stable
enum class FabHorizontalAlignment { Start, End }

@Stable
data class FabPosition(
    val insets: Dp = 16.dp,
    val vertical: FabVerticalAlignment = FabVerticalAlignment.Bottom,
    val horizontal: FabHorizontalAlignment = FabHorizontalAlignment.End,
)

@Stable
enum class RailTopPlacement {
    /** Rail beginnt oben (unter Statusbar) und läuft neben der TopBar. */
    BesideTopBar,

    /** Rail verhält sich wie Content: startet unterhalb der sichtbaren TopBar (inkl. Expand) und inkl. Statusbar. */
    BelowTopBar,
}

@Stable
enum class RailBottomPlacement {
    /** Rail läuft bis ganz unten (unter NavigationBar-Padding) und läuft neben der BottomBar. */
    BesideBottomBar,

    /** Rail endet oberhalb der BottomBar (inkl. BottomBar-Höhe) und inkl. NavigationBar. */
    AboveBottomBar,
}

object StyledTopAppBarDefaults {
    val SmallHeight = 56.dp
    val MediumHeight = 64.dp
    val LargeHeight = 96.dp
}

@Stable
class TopBarHeights {
    var expanded: Dp? by mutableStateOf(null)        // gesamte expanded-Höhe (Header + Extra)
    var collapsed: Dp? by mutableStateOf(null)       // Header-Höhe (Small)
    var expandableExtra: Dp? by mutableStateOf(null) // NUR die Extra-Höhe (wrap-content)
}

val LocalTopBarHeights = compositionLocalOf { TopBarHeights() }

/** M3-ähnliches ScrollBehavior (optional) */
@Stable
class StyledTopBarScrollBehavior internal constructor(
    internal val kind: Kind,
    internal val state: TopBarScrollState,
) {
    enum class Kind { Pinned, EnterAlways, ExitUntilCollapsed }
}

@Composable
fun styledPinnedScrollBehavior() = remember {
    StyledTopBarScrollBehavior(
        StyledTopBarScrollBehavior.Kind.Pinned,
        TopBarScrollState(56f, 56f, TopBarScrollState.Kind.Pinned)
    )
}

@Composable
fun styledEnterAlwaysScrollBehavior() = remember {
    StyledTopBarScrollBehavior(
        StyledTopBarScrollBehavior.Kind.EnterAlways,
        TopBarScrollState(56f, 0f, TopBarScrollState.Kind.EnterAlways)
    )
}

@Composable
fun styledExitUntilCollapsedScrollBehavior() = remember {
    StyledTopBarScrollBehavior(
        StyledTopBarScrollBehavior.Kind.ExitUntilCollapsed,
        TopBarScrollState(96f, 56f, TopBarScrollState.Kind.ExitUntilCollapsed)
    )
}

/* ---------- Internals ---------- */

@Composable
internal fun rememberSystemBarsInsetsPx(
    enabled: Boolean,
    contentWindowInsets: WindowInsets = WindowInsets.systemBars,
): InsetsPx {
    if (!enabled) return InsetsPx(0, 0, 0, 0)
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val top = WindowInsets.statusBars.getTop(density)
    val bottom = WindowInsets.navigationBars.getBottom(density)
    val left = contentWindowInsets.getLeft(density, layoutDirection)
    val right = contentWindowInsets.getRight(density, layoutDirection)
    return remember(top, bottom, left, right) { InsetsPx(left, top, right, bottom) }
}

@Stable
class TopBarScrollState internal constructor(
    private var maxHeightPx: Float,
    private var minHeightPx: Float,
    private val behavior: Kind,
) {
    enum class Kind { Pinned, EnterAlways, ExitUntilCollapsed }

    init {
        require(maxHeightPx >= minHeightPx)
    }

    private val maxOffsetPx: Float
        get() = when (behavior) {
            Kind.Pinned -> 0f
            Kind.EnterAlways -> maxHeightPx
            Kind.ExitUntilCollapsed -> (maxHeightPx - minHeightPx)
        }

    var heightOffsetPx by mutableStateOf(0f) // 0 = sichtbar, negativ = kollabiert
        private set

    val collapsedFraction: Float
        get() = if (maxOffsetPx == 0f) 0f else (-heightOffsetPx / maxOffsetPx).coerceIn(0f, 1f)

    fun updateHeights(newMaxPx: Float, newMinPx: Float) {
        require(newMaxPx >= newMinPx)

        // optional: jitter verhindern
        if (newMaxPx == maxHeightPx && newMinPx == minHeightPx) return

        maxHeightPx = newMaxPx
        minHeightPx = newMinPx

        val newMaxOffset = when (behavior) {
            Kind.Pinned -> 0f
            Kind.EnterAlways -> maxHeightPx
            Kind.ExitUntilCollapsed -> (maxHeightPx - minHeightPx)
        }

        // WICHTIG: Offset NICHT umrechnen, nur clampen
        heightOffsetPx = heightOffsetPx.coerceIn(-newMaxOffset, 0f)
    }

    fun onPreScroll(dy: Float): Float {
        if (maxOffsetPx == 0f || dy >= 0f) return 0f
        val old = heightOffsetPx
        val new = (heightOffsetPx + dy).coerceIn(-maxOffsetPx, 0f)
        heightOffsetPx = new
        return new - old
    }

    fun onPostScroll(dy: Float): Float {
        if (maxOffsetPx == 0f || dy <= 0f) return 0f
        val old = heightOffsetPx
        val new = (heightOffsetPx + dy).coerceIn(-maxOffsetPx, 0f)
        heightOffsetPx = new
        return new - old
    }

    suspend fun settle(velocityY: Float) {
        if (maxOffsetPx == 0f) return
        val target = when {
            velocityY > 1200f -> 0f
            velocityY < -1200f -> -maxOffsetPx
            collapsedFraction >= 0.5f -> -maxOffsetPx
            else -> 0f
        }
        val anim = Animatable(heightOffsetPx)
        anim.animateTo(target, tween(220))
        heightOffsetPx = anim.value
    }
}

@Composable
private fun rememberTopBarScrollState(kind: StyledTopBarScrollBehavior.Kind): TopBarScrollState {
    val init = when (kind) {
        StyledTopBarScrollBehavior.Kind.Pinned -> TopBarScrollState.Kind.Pinned
        StyledTopBarScrollBehavior.Kind.EnterAlways -> TopBarScrollState.Kind.EnterAlways
        StyledTopBarScrollBehavior.Kind.ExitUntilCollapsed -> TopBarScrollState.Kind.ExitUntilCollapsed
    }
    return remember(kind) { TopBarScrollState(56f, 56f, init) } // wird nach 1. Messung ersetzt
}

/* ---------- Scaffold ---------- */

@OptIn(InternalComposeStyledApi::class)
@Composable
fun StyledScaffold(
    modifier: Modifier = Modifier,

    containerColor: Color = StyledTheme.colors.surface,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),

    respectSystemBars: Boolean = true,
    contentWindowInsets: WindowInsets = WindowInsets.systemBars,
    consumeWindowInsets: Boolean = true,

    scrollBehavior: StyledTopBarScrollBehavior? = null, // null => pinned
    topBar: @Composable (collapsedFraction: Float) -> Unit = { _ -> },

    rail: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    railTopPlacement: RailTopPlacement = RailTopPlacement.BelowTopBar,
    railBottomPlacement: RailBottomPlacement = RailBottomPlacement.BesideBottomBar,

    fabPosition: FabPosition = FabPosition(),
    floatingActionButton: @Composable () -> Unit = {},

    snackbarHost: @Composable () -> Unit = {},

    content: @Composable (PaddingValues) -> Unit,
) {
    // 1) scroll behavior + state + nested scroll connection
    val effective = scrollBehavior ?: styledPinnedScrollBehavior()
    val kind = effective.kind
    val scrollState = rememberTopBarScrollState(kind)
    val nestedScrollConn = remember(kind, scrollState) {
        if (kind == StyledTopBarScrollBehavior.Kind.Pinned)
            null
        else
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    val consumedY = scrollState.onPreScroll(available.y)
                    return if (consumedY != 0f) Offset(0f, consumedY) else Offset.Zero
                }

                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource,
                ): Offset {
                    // Hybrid: verticalScroll => available.y > 0; Lazy* => consumed.y > 0
                    val dy = when {
                        available.y > 0f -> available.y
                        consumed.y > 0f -> consumed.y
                        else -> 0f
                    }
                    if (dy > 0f) scrollState.onPostScroll(dy)
                    return Offset.Zero
                }

                override suspend fun onPreFling(available: Velocity): Velocity {
                    scrollState.settle(available.y); return Velocity.Zero
                }

                override suspend fun onPostFling(
                    consumed: Velocity,
                    available: Velocity,
                ): Velocity {
                    scrollState.settle(available.y); return Velocity.Zero
                }
            }
    }

    val insetsPx = rememberSystemBarsInsetsPx(
        enabled = respectSystemBars,
        contentWindowInsets = contentWindowInsets
    )
    val density = LocalDensity.current
    val inLeftDp = with(density) { insetsPx.left.toDp() }
    val inRightDp = with(density) { insetsPx.right.toDp() }
    val inBottomDp = with(density) { insetsPx.bottom.toDp() }
    val inTopDp = with(density) { insetsPx.top.toDp() }

    // Root: nur Horizontal + Bottom konsumieren (Top bleibt für TopBar‑Inhalte)
    val rootConsumedInsets =
        contentWindowInsets.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)


    // FAB-Animation
    val animatedFabY = remember { Animatable(0f) }
    var fabTargetY by remember { mutableStateOf(0) }
    var fabHasTarget by remember { mutableStateOf(0) }
    LaunchedEffect(fabTargetY, fabHasTarget) {
        if (fabHasTarget == 0) return@LaunchedEffect
        if (animatedFabY.value == 0f) animatedFabY.snapTo(fabTargetY.toFloat())
        else animatedFabY.animateTo(
            fabTargetY.toFloat(),
            spring(stiffness = Spring.StiffnessMediumLow)
        )
    }

    val topBarHeights = remember { TopBarHeights() }

    StyledSurface(
        modifier = (
                if (consumeWindowInsets && respectSystemBars)
                    modifier.consumeWindowInsets(rootConsumedInsets)
                else modifier
                ).then(if (nestedScrollConn != null) Modifier.nestedScroll(nestedScrollConn) else Modifier),
        backgroundColor = containerColor,
        contentColor = contentColor
    ) {
        Box {
            SubcomposeLayout(Modifier.fillMaxSize()) { constraints ->

                val loose = constraints.copy(minWidth = 0, minHeight = 0)


                /* -------- PHASE 1: ermittele expanded/collapsed stabil -------- */

                // Wenn die TopBar bereits gesetzt hat, nutze das; sonst einmalig messen bei cf=0f
                val needFallbackMeasure =
                    topBarHeights.expanded == null || topBarHeights.expandableExtra == null

                if (needFallbackMeasure) {
                    val measureExpanded = subcompose("top-measure-expanded") {
                        CompositionLocalProvider(LocalTopBarHeights provides topBarHeights) {
                            Box(Modifier.fillMaxWidth()) { topBar(0f) } // expanded
                        }
                    }.map { it.measure(loose) }
                    val expPx = (measureExpanded.maxOfOrNull { it.height } ?: 0).toFloat()
                    val expDp = with(this) { expPx.toDp() }
                    val collapsedDp = topBarHeights.collapsed ?: StyledTopAppBarDefaults.SmallHeight
                    val measuredMinusInset = (expDp - inTopDp).coerceAtLeast(0.dp)
                    val extraDp = (measuredMinusInset - collapsedDp).coerceAtLeast(0.dp)

                    if (topBarHeights.expandableExtra == null) topBarHeights.expandableExtra =
                        extraDp
                    if (topBarHeights.expanded == null) topBarHeights.expanded =
                        collapsedDp + extraDp
                }

                val collapsedDp = topBarHeights.collapsed ?: StyledTopAppBarDefaults.SmallHeight
                val extraDp = topBarHeights.expandableExtra ?: 0.dp
                val expandedDp = topBarHeights.expanded ?: (collapsedDp + extraDp)

                // Scroll-Heights aktualisieren
                with(this) {
                    val maxPx = expandedDp.toPx()
                    val minPx = when (kind) {
                        StyledTopBarScrollBehavior.Kind.Pinned -> expandedDp.toPx()
                        StyledTopBarScrollBehavior.Kind.EnterAlways -> 0f
                        StyledTopBarScrollBehavior.Kind.ExitUntilCollapsed -> collapsedDp.toPx()
                    }.coerceAtMost(maxPx)
                    scrollState.updateHeights(maxPx, minPx)
                }

                /* -------- sichtbare Höhe -------- */

                val cf = scrollState.collapsedFraction // 0..1
                val visibleTopDp =
                    collapsedDp + extraDp * (1f - cf)        // ohne Statusbar
                val visibleTopHostDp =
                    visibleTopDp + inTopDp               // mit Statusbar

                /* -------- TOP: Host (clip sichtbar) -------- */
                val topHostPlaceables = subcompose("top-host") {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(visibleTopHostDp)
                            .clipToBounds()
                    ) {
                        CompositionLocalProvider(LocalTopBarHeights provides topBarHeights) {
                            Box(Modifier.fillMaxWidth()) {
                                topBar(cf)
                            }
                        }
                    }
                }.map { it.measure(loose) }

                /* -------- FAB/Navigation/Content/Snackbar -------- */

                // FAB
                val fabPlaceables =
                    subcompose("fab") { floatingActionButton() }.map { it.measure(loose) }
                val fab = fabPlaceables.firstOrNull()
                val fabW = fab?.width ?: 0
                val fabH = fab?.height ?: 0

                // Bottom bar: place to bottom (y = height - measuredHeight). Insets are handled INSIDE StyledBottomBar.
                val bottomNavPlaceables = subcompose("navigation-bottom") {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            // left/right insets always; bottom inset padding must be handled inside the bottomBar itself
                            .padding(start = inLeftDp, end = inRightDp)
                    ) {
                        bottomBar()
                    }
                }.map { it.measure(loose) }
                val bottomNavH = bottomNavPlaceables.maxOfOrNull { it.height } ?: 0

                // Rail
                val railPlaceables = subcompose("navigation-rail") {
                    val topPadding = when (railTopPlacement) {
                        RailTopPlacement.BesideTopBar -> if (respectSystemBars) inTopDp else 0.dp
                        RailTopPlacement.BelowTopBar -> visibleTopHostDp
                    }

                    val bottomPadding = when (railBottomPlacement) {
                        // Rail should reach the bottom. Any navigation-bar inset handling must be done INSIDE the rail
                        // because it draws its own background.
                        RailBottomPlacement.BesideBottomBar -> 0.dp

                        // Rail ends above the whole bottom bar area (which already includes its internal nav-bar padding).
                        RailBottomPlacement.AboveBottomBar -> with(this@SubcomposeLayout) { bottomNavH.toDp() }
                    }

                    Column(
                        Modifier
                            .fillMaxHeight()
                            .wrapContentWidth()
                            .padding(
                                start = inLeftDp,
                                top = topPadding,
                                bottom = bottomPadding
                            )
                    ) {
                        rail()
                    }
                }.map { it.measure(loose) }
                val railW = railPlaceables.maxOfOrNull { it.width } ?: 0
                val isRailMode = railW > 0

                // Content Padding
                // - top: visibleTopDp is WITHOUT status bar, so add inTopDp when we want system insets respected
                // - bottom: if a bottom bar is present, it must already include its own nav-bar padding;
                //           otherwise we must add inBottomDp manually.
                val contentX = railW
                val contentTop = visibleTopDp + (if (respectSystemBars) inTopDp else 0.dp)
                val contentBottom = with(this@SubcomposeLayout) {
                    if (!isRailMode) {
                        bottomNavH.toDp()
                    } else {
                        if (respectSystemBars) inBottomDp else 0.dp
                    }
                }
                val pad = PaddingValuesDp(
                    start = inLeftDp,
                    top = contentTop,
                    end = inRightDp,
                    bottom = contentBottom
                )

                val contentPlaceables = subcompose("content") {
                    ProvideStyledLocals(
                        contentColor = contentColor,
                    ) {
                        Box(Modifier.fillMaxSize()) { content(pad.asPaddingValues()) }
                    }
                }.map {
                    val contentConstraints = if (railW > 0) {
                        constraints.copy(
                            minWidth = 0,
                            maxWidth = (constraints.maxWidth - railW).coerceAtLeast(0)
                        )
                    } else constraints
                    it.measure(contentConstraints)
                }

                // Snackbar
                val snackbarMarginHpx = 16.dp.roundToPx()
                val snackbarMarginVpx = 16.dp.roundToPx()
                val snackbarMaxWidthPx = 600.dp.roundToPx()
                val snackbarConstraints = constraints.copy(
                    minWidth = 0,
                    maxWidth = minOf(
                        constraints.maxWidth - snackbarMarginHpx * 2,
                        snackbarMaxWidthPx
                    ).coerceAtLeast(0),
                    minHeight = 0
                )
                val snackbarPlaceables =
                    subcompose("snackbar") { snackbarHost() }.map { it.measure(snackbarConstraints) }
                val snackbar = snackbarPlaceables.firstOrNull()
                val snackbarW = snackbar?.width ?: 0
                val snackbarH = snackbar?.height ?: 0
                val isSnackbarMeasured = snackbar != null && snackbarW > 0 && snackbarH > 0

                layout(constraints.maxWidth, constraints.maxHeight) {
                    // Content
                    contentPlaceables.forEach { it.place(contentX, 0) }

                    // Top (oben fix)
                    topHostPlaceables.forEach { it.place(0, 0) }

                    // Navigation
                    if (!isRailMode) {
                        // BottomBar goes to the very bottom. Its content is already padded by inBottomDp.
                        bottomNavPlaceables.forEach {
                            it.place(
                                0,
                                constraints.maxHeight - bottomNavH
                            )
                        }
                    } else {
                        railPlaceables.forEach { it.place(0, 0) }
                    }

                    // Snackbar host
                    val contentAreaLeft = if (railW > 0) contentX else 0
                    val contentAreaWidth =
                        if (railW > 0) (constraints.maxWidth - contentAreaLeft).coerceAtLeast(0) else constraints.maxWidth
                    val snackbarXRaw = contentAreaLeft + ((contentAreaWidth - snackbarW) / 2)
                    val snackbarMinX = contentAreaLeft + snackbarMarginHpx
                    val snackbarMaxX =
                        (contentAreaLeft + contentAreaWidth - snackbarW - snackbarMarginHpx).coerceAtLeast(
                            snackbarMinX
                        )
                    val snackbarX = snackbarXRaw.coerceIn(snackbarMinX, snackbarMaxX)
                    val snackbarYBase =
                        constraints.maxHeight - (if (!isRailMode) bottomNavH else 0) - insetsPx.bottom - snackbarMarginVpx
                    val snackbarY = (snackbarYBase - snackbarH).coerceAtLeast(0)
                    snackbar?.place(snackbarX, snackbarY)

                    // FAB
                    if (fab != null) {
                        val m = fabPosition.insets.roundToPx()
                        val x = when (fabPosition.horizontal) {
                            FabHorizontalAlignment.Start -> insetsPx.left + m
                            FabHorizontalAlignment.End -> constraints.maxWidth - fabW - insetsPx.right - m
                        }
                        val yTop = with(this) { visibleTopDp.toPx() }.roundToInt() + m
                        val y = when (fabPosition.vertical) {
                            FabVerticalAlignment.Top -> yTop
                            FabVerticalAlignment.Bottom -> {
                                val baseBottomLimit =
                                    constraints.maxHeight -
                                            (if (!isRailMode) bottomNavH else 0) -
                                            insetsPx.bottom - m
                                val occupiedBySnackbarPx =
                                    if (isSnackbarMeasured) snackbarH else 0
                                val bottomLimit =
                                    baseBottomLimit - occupiedBySnackbarPx - if (occupiedBySnackbarPx > 0) m else 0
                                (bottomLimit - fabH).coerceAtLeast(0)
                            }
                        }
                        if (fabPosition.vertical == FabVerticalAlignment.Bottom) {
                            fabTargetY = y
                            fabHasTarget = 1
                            fab.place(x, animatedFabY.value.roundToInt())
                        } else {
                            fab.place(x, y)
                        }
                    }
                }
            }
        }
    }
}
