package com.michaelflisar.composestyled.ui.layout

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi


/** A lazy horizontal list that uses Styled theme defaults for its content layout. */
@Composable
@OptIn(InternalComposeStyledApi::class)
fun StyledLazyRow(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    horizontalArrangement: Arrangement.Horizontal = StyledRowDefaults.horizontalArrangement,
    verticalAlignment: Alignment.Vertical = StyledRowDefaults.verticalAlignment,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyListScope.() -> Unit,
) {
    StyledHorizontalScrollbarLayout(
        modifier = modifier,
        state = state,
    ) { contentModifier ->
        LazyRow(
            modifier = contentModifier,
            state = state,
            contentPadding = contentPadding,
            reverseLayout = reverseLayout,
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
            flingBehavior = flingBehavior,
            userScrollEnabled = userScrollEnabled,
            content = content,
        )
    }
}

/** A lazy horizontal list with a fixed gap between items. */
@Composable
fun StyledLazyRow(
    itemSpacing: Dp,
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = StyledRowDefaults.verticalAlignment,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyListScope.() -> Unit,
) {
    StyledLazyRow(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        reverseLayout = reverseLayout,
        horizontalArrangement = Arrangement.spacedBy(itemSpacing),
        verticalAlignment = verticalAlignment,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        content = content,
    )
}