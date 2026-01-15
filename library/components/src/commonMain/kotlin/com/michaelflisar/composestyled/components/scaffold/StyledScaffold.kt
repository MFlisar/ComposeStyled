package com.michaelflisar.composestyled.components.scaffold

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import com.michaelflisar.composestyled.core.runtime.LocalContentColor
import kotlin.math.roundToInt

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

enum class TopBarBehavior {
    Pinned,              // immer sichtbar, kein collapse
    EnterAlways,         // verschwindet beim hochscrollen, kommt sofort wieder
    ExitUntilCollapsed   // kollabiert bis minHeight, dann bleibt "collapsed" stehen
}

@Composable
fun rememberSystemBarsInsetsPx(enabled: Boolean): InsetsPx {
    if (!enabled)
        return InsetsPx(
            0,
            0,
            0,
            0
        )
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val top = WindowInsets.statusBars.getTop(density)
    val bottom = WindowInsets.navigationBars.getBottom(density)
    val left = WindowInsets.systemBars.getLeft(density, layoutDirection)
    val right = WindowInsets.systemBars.getRight(density, layoutDirection)
    return remember(top, bottom, left, right) {
        InsetsPx(
            left,
            top,
            right,
            bottom
        ).also {
            println("InsetsPx: $it")
        }
    }
}

/* -------- TopBar scroll behavior (Material3-like) -------- */

@Stable
class TopBarScrollState internal constructor(
    private val maxHeightPx: Float,
    private val minHeightPx: Float,
    private val behavior: TopBarBehavior,
) {
    init {
        require(maxHeightPx >= minHeightPx) { "maxHeightPx must be >= minHeightPx" }
    }

    // 0..(max-min) for ExitUntilCollapsed, 0..max for EnterAlways
    private val maxOffsetPx: Float =
        when (behavior) {
            TopBarBehavior.Pinned -> 0f
            TopBarBehavior.EnterAlways -> maxHeightPx
            TopBarBehavior.ExitUntilCollapsed -> (maxHeightPx - minHeightPx)
        }

    // heightOffset: 0 = fully shown, negative = moved/collapsed
    var heightOffsetPx by mutableFloatStateOf(0f)
        private set

    val collapsedFraction: Float
        get() {
            if (maxOffsetPx == 0f) return 0f
            val collapsed = (-heightOffsetPx / maxOffsetPx).coerceIn(0f, 1f)
            return collapsed
        }

    fun onPreScroll(dy: Float): Float {
        if (maxOffsetPx == 0f) return 0f
        // Material semantics: dy<0 = scroll up => collapse/hide more
        val old = heightOffsetPx
        val new = (heightOffsetPx + dy).coerceIn(-maxOffsetPx, 0f)
        heightOffsetPx = new
        // consumed should be the delta we took from available
        return new - old
    }

    fun onPostScroll(dy: Float) {
        if (maxOffsetPx == 0f) return
        // Expand on downward scroll (dy>0)
        if (dy > 0f) {
            heightOffsetPx = (heightOffsetPx + dy).coerceIn(-maxOffsetPx, 0f)
        }
    }

    suspend fun settle(velocityY: Float) {
        if (maxOffsetPx == 0f) return
        // Simple settle: depending on velocity or midpoint.
        // velocityY > 0 => user flinging down => show
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

    /**
     * Visible height of the top bar container (px) for padding purposes.
     * For EnterAlways: when hidden, visible height -> 0.
     * For ExitUntilCollapsed: visible height -> minHeight when fully collapsed.
     */
    fun visibleHeightPx(): Float {
        return when (behavior) {
            TopBarBehavior.Pinned -> maxHeightPx
            TopBarBehavior.EnterAlways -> (maxHeightPx + heightOffsetPx).coerceIn(0f, maxHeightPx)
            TopBarBehavior.ExitUntilCollapsed -> {
                // container always reserves at least minHeight
                val collapsed = -heightOffsetPx // 0..(max-min)
                (maxHeightPx - collapsed).coerceIn(minHeightPx, maxHeightPx)
            }
        }
    }
}

@Composable
fun rememberTopBarScrollState(
    maxHeight: Dp,
    minHeight: Dp,
    behavior: TopBarBehavior,
): TopBarScrollState {
    val density = LocalDensity.current
    val maxPx = with(density) { maxHeight.toPx() }
    val minPx = with(density) { minHeight.toPx() }
    return remember(maxPx, minPx, behavior) {
        TopBarScrollState(
            maxPx,
            minPx,
            behavior
        )
    }
}

/* -------- Scaffold -------- */

@Composable
fun StyledScaffold(
    modifier: Modifier = Modifier,

    containerColor: Color = StyledTheme.colors.surface,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),

    // Insets
    respectSystemBars: Boolean = true,

    // Top bar + collapsing
    topBarMaxHeight: Dp = 56.dp,
    topBarMinHeight: Dp = 56.dp, // for ExitUntilCollapsed set smaller
    topBarBehavior: TopBarBehavior = TopBarBehavior.Pinned,
    topBar: @Composable (collapsedFraction: Float) -> Unit = { _ -> },

    // StyledNavigation (Rail or BottomBar; rendering is decided internally)
    rail: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,

    // FAB
    fabPosition: FabPosition = FabPosition(),
    floatingActionButton: @Composable () -> Unit = {},

    // Snackbar
    snackbarHost: @Composable () -> Unit = {},

    content: @Composable (PaddingValuesDp) -> Unit,
) {
    val insetsPx =
        rememberSystemBarsInsetsPx(
            respectSystemBars
        )
    val density = LocalDensity.current
    val inLeftDp = with(density) { insetsPx.left.toDp() }
    val inTopDp = with(density) { insetsPx.top.toDp() }
    val inRightDp = with(density) { insetsPx.right.toDp() }
    val inBottomDp = with(density) { insetsPx.bottom.toDp() }

    val scrollState =
        rememberTopBarScrollState(
            maxHeight = topBarMaxHeight + inTopDp,
            minHeight = topBarMinHeight + inTopDp,
            behavior = topBarBehavior
        )

    val nestedScrollConn = remember(topBarBehavior, scrollState) {
        if (topBarBehavior == TopBarBehavior.Pinned) null else object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val consumedY = scrollState.onPreScroll(available.y)
                return Offset(0f, consumedY)
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                scrollState.onPostScroll(available.y)
                return Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                scrollState.settle(available.y)
                return Velocity.Zero
            }
        }
    }

    // FAB position animation: move animation work out of Layout/Measure.
    // We compute a target Y in layout and feed it into this Animatable via LaunchedEffect.
    val animatedFabY = remember { Animatable(0f) }
    var fabTargetY by remember { mutableIntStateOf(0) }
    var fabHasTarget by remember { mutableIntStateOf(0) } // 0=false, 1=true

    LaunchedEffect(fabTargetY, fabHasTarget) {
        if (fabHasTarget == 0) return@LaunchedEffect
        // Snap on first target, then animate.
        if (animatedFabY.value == 0f) {
            animatedFabY.snapTo(fabTargetY.toFloat())
        } else {
            animatedFabY.animateTo(
                fabTargetY.toFloat(),
                spring(stiffness = Spring.StiffnessMediumLow)
            )
        }
    }

    val snackbarLayoutInfo =
        rememberStyledSnackbarHostLayoutInfo()

    StyledSurface(
        modifier = modifier.then(
            if (nestedScrollConn != null) Modifier.nestedScroll(
                nestedScrollConn
            ) else Modifier
        ),
        backgroundColor = containerColor,
        contentColor = contentColor
    ) {
        Box {
            SubcomposeLayout(Modifier.fillMaxSize()) { constraints ->
                val loose = constraints.copy(minWidth = 0, minHeight = 0)

                // --- TOP ---
                val topPlaceables = subcompose("top") {
                    // container includes status bar inset padding so content doesn't go under it
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(topBarMaxHeight + inTopDp)
                            .padding(start = inLeftDp, top = inTopDp, end = inRightDp)
                    ) {
                        topBar(scrollState.collapsedFraction)
                    }
                }.map { it.measure(loose) }

                // visible height for padding (behavior-dependent)
                val topVisibleHpx = scrollState.visibleHeightPx().roundToInt()

                // --- FAB measurement (used by overlay placement) ---
                val fabPlaceables = subcompose("fab") {
                    floatingActionButton()
                }.map { it.measure(loose) }

                val fab = fabPlaceables.firstOrNull()
                val fabW = fab?.width ?: 0
                val fabH = fab?.height ?: 0

                // --- NAVIGATION ---
                // Measure both; show rail only if it has a width.
                val bottomNavPlaceables = subcompose("navigation-bottom") {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(start = inLeftDp, end = inRightDp, bottom = inBottomDp)
                    ) {
                        bottomBar()
                    }
                }.map { it.measure(loose) }
                val bottomNavH = bottomNavPlaceables.maxOfOrNull { it.height } ?: 0

                val railPlaceables = subcompose("navigation-rail") {
                    Column(
                        Modifier
                            .fillMaxHeight()
                            .wrapContentWidth()
                            .padding(
                                start = inLeftDp,
                                top = with(this@SubcomposeLayout) { topVisibleHpx.toDp() },
                                bottom = inBottomDp
                            )
                    ) {
                        rail()
                    }
                }.map { it.measure(loose) }
                val railW = railPlaceables.maxOfOrNull { it.width } ?: 0

                val isRailMode = railW > 0

                // --- CONTENT PADDING ---
                val contentX = railW
                val pad =
                    PaddingValuesDp(
                        start = inLeftDp, // rail offset is applied via placement
                        top = with(this@SubcomposeLayout) { topVisibleHpx.toDp() },
                        end = inRightDp,
                        bottom = with(this@SubcomposeLayout) { if (!isRailMode) bottomNavH.toDp() else 0.toDp() }
                    )

                val contentPlaceables = subcompose("content") {
                    CompositionLocalProvider(LocalContentColor provides contentColor) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .padding(pad.asPaddingValues())
                        ) {
                            content(pad)
                        }
                    }
                }
                    .map {
                        // IMPORTANT: When a rail is present, measure content only in the remaining width.
                        // Otherwise content can be measured too wide and overflow behind/under the rail.
                        val contentConstraints = if (railW > 0) {
                            constraints.copy(
                                minWidth = 0,
                                maxWidth = (constraints.maxWidth - railW).coerceAtLeast(0)
                            )
                        } else {
                            constraints
                        }
                        it.measure(contentConstraints)
                    }

                // --- SNACKBAR HOST ---
                val snackbarMarginHpx = 16.dp.roundToPx()
                val snackbarMarginVpx = 16.dp.roundToPx()
                val snackbarMaxWidthPx = 600.dp.roundToPx()
                val snackbarConstraints = constraints.copy(
                    minWidth = 0,
                    maxWidth = minOf(
                        constraints.maxWidth - snackbarMarginHpx * 2,
                        snackbarMaxWidthPx
                    )
                        .coerceAtLeast(0),
                    minHeight = 0
                )

                val snackbarPlaceables = subcompose("snackbar") {
                    snackbarHost()
                }.map { it.measure(snackbarConstraints) }

                val snackbar = snackbarPlaceables.firstOrNull()
                val snackbarW = snackbar?.width ?: 0
                val snackbarH = snackbar?.height ?: 0
                val isSnackbarMeasured = snackbar != null && snackbarW > 0 && snackbarH > 0

                layout(constraints.maxWidth, constraints.maxHeight) {
                    // content (shift right when rail is visible)
                    contentPlaceables.forEach { it.place(contentX, 0) }

                    // top: place with heightOffset (negative)
                    val topY = scrollState.heightOffsetPx.roundToInt()
                    topPlaceables.forEach { it.place(0, topY) }

                    // navigation placement
                    if (!isRailMode) {
                        bottomNavPlaceables.forEach { it.place(0, constraints.maxHeight - bottomNavH) }
                    } else {
                        railPlaceables.forEach { it.place(0, 0) }
                    }

                    // snackbar host: bottom-center with margins.
                    // Must always be above bottom bar + system inset.
                    val contentAreaLeft = if (railW > 0) contentX else 0
                    val contentAreaWidth = if (railW > 0) {
                        (constraints.maxWidth - contentAreaLeft).coerceAtLeast(0)
                    } else {
                        constraints.maxWidth
                    }

                    // Center snackbar within the content area (not across the rail).
                    val snackbarXRaw = contentAreaLeft + ((contentAreaWidth - snackbarW) / 2)

                    // Keep horizontal margins within the content area.
                    val snackbarMinX = contentAreaLeft + snackbarMarginHpx
                    val snackbarMaxX =
                        (contentAreaLeft + contentAreaWidth - snackbarW - snackbarMarginHpx)
                            .coerceAtLeast(snackbarMinX)

                    val snackbarX = snackbarXRaw.coerceIn(snackbarMinX, snackbarMaxX)

                    val snackbarYBase =
                        constraints.maxHeight - (if (!isRailMode) bottomNavH else 0) - insetsPx.bottom - snackbarMarginVpx
                    val snackbarY = (snackbarYBase - snackbarH).coerceAtLeast(0)
                    snackbar?.place(snackbarX, snackbarY)

                    // ---- FAB placement ----
                    // Goal: keep FAB above bottom bar and above snackbar.
                    if (fab != null) {
                        val m = fabPosition.insets.roundToPx()

                        val x = when (fabPosition.horizontal) {
                            FabHorizontalAlignment.Start -> insetsPx.left + m
                            FabHorizontalAlignment.End -> constraints.maxWidth - fabW - insetsPx.right - m
                        }

                        val occupiedBySnackbarPx = when {
                            snackbarLayoutInfo.snackbarHeightPx > 0 -> snackbarLayoutInfo.occupiedHeightPx.roundToInt()
                            isSnackbarMeasured -> snackbarH
                            else -> 0
                        }

                        val y = when (fabPosition.vertical) {
                            FabVerticalAlignment.Top -> topVisibleHpx + m
                            FabVerticalAlignment.Bottom -> {
                                val baseBottomLimit =
                                    constraints.maxHeight -
                                        (if (!isRailMode) bottomNavH else 0) -
                                        insetsPx.bottom -
                                        m

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