package com.michaelflisar.composestyled.ui.layout

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

/** A lazy vertical list that uses Styled theme defaults for its content layout. */
@Composable
@OptIn(InternalComposeStyledApi::class)
fun StyledLazyColumn(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = StyledColumnDefaults.verticalArrangement,
    horizontalAlignment: Alignment.Horizontal = StyledColumnDefaults.horizontalAlignment,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyListScope.() -> Unit,
) {
    StyledVerticalScrollbarLayout(
        modifier = modifier,
        state = state,
    ) { contentModifier ->
        LazyColumn(
            modifier = contentModifier,
            state = state,
            contentPadding = contentPadding,
            reverseLayout = reverseLayout,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            flingBehavior = flingBehavior,
            userScrollEnabled = userScrollEnabled,
            content = content,
        )
    }
}

/** A lazy vertical list with a fixed gap between items. */
@Composable
fun StyledLazyColumn(
    itemSpacing: Dp,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = StyledColumnDefaults.horizontalAlignment,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyListScope.() -> Unit,
) {
    StyledLazyColumn(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        reverseLayout = reverseLayout,
        verticalArrangement = Arrangement.spacedBy(itemSpacing),
        horizontalAlignment = horizontalAlignment,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        content = content,
    )
}
