package com.michaelflisar.composestyled.foundation.layout

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals

class FloatingPositionFractionTest {

    @Test
    fun positionFractionTracksMovableAreaAcrossBoundsChanges() {
        val area = DpSize(600.dp, 400.dp)
        val item = DpSize(200.dp, 100.dp)
        val margin = 10.dp
        val original = DpOffset(110.dp, 60.dp)

        val fraction = original.toFraction(area, item, margin)
        val changedBounds = fraction.toPosition(DpSize(1000.dp, 800.dp), item, margin)
        val restored = fraction.toPosition(area, item, margin)

        assertEquals(0.25f, fraction.x)
        assertEquals(1f / 6f, fraction.y)
        assertEquals(210.dp, changedBounds.x)
        assertEquals(126.66667f, changedBounds.y.value, 0.001f)
        assertEquals(original.x, restored.x)
        assertEquals(original.y, restored.y)
    }

    @Test
    fun independentPositionsKeepTheirOwnFractions() {
        val area = DpSize(600.dp, 400.dp)
        val item = DpSize(200.dp, 100.dp)
        val margin = 10.dp
        val window = DpOffset(110.dp, 60.dp).toFraction(area, item, margin)
        val bubble = DpOffset(410.dp, 260.dp).toFraction(area, item, margin)

        assertEquals(DpOffset(110.dp, 60.dp), window.toPosition(area, item, margin))
        assertEquals(DpOffset(410.dp, 260.dp), bubble.toPosition(area, item, margin))
    }

    @Test
    fun zeroMovementRangeRetainsPreviousFraction() {
        val previous = FloatingPositionFraction(0.75f, 0.25f)
        val position = DpOffset(10.dp, 90.dp).toFraction(
            availableSpace = DpSize(100.dp, 200.dp),
            itemSize = DpSize(100.dp, 40.dp),
            margin = 10.dp,
            previous = previous,
        )

        assertEquals(0.75f, position.x)
        assertEquals(0.5f, position.y)
        assertEquals(
            DpOffset(10.dp, 90.dp),
            position.toPosition(DpSize(100.dp, 200.dp), DpSize(100.dp, 40.dp), 10.dp),
        )
    }

    @Test
    fun conversionConstrainsPositionsToMovableRange() {
        val position = DpOffset(1000.dp, -100.dp).toFraction(
            availableSpace = DpSize(600.dp, 400.dp),
            itemSize = DpSize(200.dp, 100.dp),
            margin = 10.dp,
        )

        assertEquals(FloatingPositionFraction(1f, 0f), position)
    }

    @Test
    fun minimizedBubbleSnapsToNearestHorizontalEdge() {
        val availableSpace = DpSize(600.dp, 400.dp)
        val bubbleSize = DpSize(48.dp, 48.dp)
        val margin = 12.dp

        assertEquals(
            DpOffset(12.dp, 140.dp),
            DpOffset(100.dp, 140.dp).snapToHorizontalEdge(availableSpace, bubbleSize, margin),
        )
        assertEquals(
            DpOffset(564.dp, 220.dp),
            DpOffset(500.dp, 220.dp).snapToHorizontalEdge(availableSpace, bubbleSize, margin),
        )
    }
}
