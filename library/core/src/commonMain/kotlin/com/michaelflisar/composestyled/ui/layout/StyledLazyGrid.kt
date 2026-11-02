package com.michaelflisar.composestyled.ui.layout

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

object StyledLazyGridDefaults {
    val verticalArrangement: Arrangement.Vertical = Arrangement.Top
    val horizontalArrangement: Arrangement.Horizontal = Arrangement.Start
}

/** A lazy vertical grid with theme-aware spacing and item content. */
@Composable
@OptIn(InternalComposeStyledApi::class)
fun StyledLazyVerticalGrid(
    columns: GridCells,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = StyledLazyGridDefaults.verticalArrangement,
    horizontalArrangement: Arrangement.Horizontal = StyledLazyGridDefaults.horizontalArrangement,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyGridScope.() -> Unit,
) {
    StyledVerticalScrollbarLayout(
        modifier = modifier,
        state = state,
    ) { contentModifier ->
        LazyVerticalGrid(
            columns = columns,
            modifier = contentModifier,
            state = state,
            contentPadding = contentPadding,
            reverseLayout = reverseLayout,
            verticalArrangement = verticalArrangement,
            horizontalArrangement = horizontalArrangement,
            flingBehavior = flingBehavior,
            userScrollEnabled = userScrollEnabled,
            content = content,
        )
    }
}

/** A lazy vertical grid with separate gaps between rows and columns. */
@Composable
fun StyledLazyVerticalGrid(
    columns: GridCells,
    verticalItemSpacing: Dp,
    horizontalItemSpacing: Dp,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyGridScope.() -> Unit,
) {
    StyledLazyVerticalGrid(
        columns = columns,
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        reverseLayout = reverseLayout,
        verticalArrangement = Arrangement.spacedBy(verticalItemSpacing),
        horizontalArrangement = Arrangement.spacedBy(horizontalItemSpacing),
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        content = content,
    )
}

/** A lazy horizontal grid with theme-aware spacing and item content. */
@Composable
@OptIn(InternalComposeStyledApi::class)
fun StyledLazyHorizontalGrid(
    rows: GridCells,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = StyledLazyGridDefaults.verticalArrangement,
    horizontalArrangement: Arrangement.Horizontal = StyledLazyGridDefaults.horizontalArrangement,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyGridScope.() -> Unit,
) {
    StyledHorizontalScrollbarLayout(
        modifier = modifier,
        state = state,
    ) { contentModifier ->
        LazyHorizontalGrid(
            rows = rows,
            modifier = contentModifier,
            state = state,
            contentPadding = contentPadding,
            reverseLayout = reverseLayout,
            verticalArrangement = verticalArrangement,
            horizontalArrangement = horizontalArrangement,
            flingBehavior = flingBehavior,
            userScrollEnabled = userScrollEnabled,
            content = content,
        )
    }
}

/** A lazy horizontal grid with separate gaps between rows and columns. */
@Composable
fun StyledLazyHorizontalGrid(
    rows: GridCells,
    verticalItemSpacing: Dp,
    horizontalItemSpacing: Dp,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyGridScope.() -> Unit,
) {
    StyledLazyHorizontalGrid(
        rows = rows,
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        reverseLayout = reverseLayout,
        verticalArrangement = Arrangement.spacedBy(verticalItemSpacing),
        horizontalArrangement = Arrangement.spacedBy(horizontalItemSpacing),
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        content = content,
    )
}
