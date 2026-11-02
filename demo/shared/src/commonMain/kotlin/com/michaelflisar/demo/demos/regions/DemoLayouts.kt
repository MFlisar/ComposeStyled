package com.michaelflisar.demo.demos.regions

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledDropdownIndex
import com.michaelflisar.composestyled.ui.layout.StyledCollapsibleHeader
import com.michaelflisar.composestyled.ui.layout.StyledCollapsibleHeaderScrollBehavior
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.layout.StyledFlowRow
import com.michaelflisar.composestyled.ui.layout.StyledGrid
import com.michaelflisar.composestyled.ui.layout.StyledHorizontalScrollbarContainer
import com.michaelflisar.composestyled.ui.layout.StyledLazyColumn
import com.michaelflisar.composestyled.ui.layout.StyledLazyHorizontalGrid
import com.michaelflisar.composestyled.ui.layout.StyledLazyRow
import com.michaelflisar.composestyled.ui.layout.StyledLazyVerticalGrid
import com.michaelflisar.composestyled.ui.layout.StyledPager
import com.michaelflisar.composestyled.ui.layout.StyledPagerDefaults
import com.michaelflisar.composestyled.ui.layout.StyledRow
import com.michaelflisar.composestyled.ui.layout.StyledSplitPanel
import com.michaelflisar.composestyled.ui.layout.StyledVerticalScrollbarContainer
import com.michaelflisar.composestyled.ui.layout.rememberStyledCollapsibleHeaderState
import com.michaelflisar.composestyled.ui.layout.rememberStyledSplitPanelState
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.state.rememberDialogState
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle
import kotlinx.coroutines.launch

@Composable
internal fun DemoLayouts(baseId: Int, expandState: ExpandState) {
    val items = listOf(
        "One", "Two", "Three", "Four", "Five", "Six",
        "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve",
    )

    RegionContainer(
        title = "Rows, columns, flow rows and static grid",
        id = baseId + 1,
        expanded = expandState,
    ) {
        StyledRow(itemSpacing = StyledTheme.spacing.medium) {
            StyledText("Row item one")
            StyledText("Row item two")
            StyledText("Row item three")
        }
        StyledColumn(itemSpacing = StyledTheme.spacing.medium) {
            StyledText("Column item one")
            StyledText("Column item two")
        }
        StyledFlowRow(spacing = StyledTheme.spacing.medium, maxItemsInEachRow = 2) {
            items.forEach { item ->
                StyledText(item)
            }
        }
        StyledGrid(
            columns = 3,
            rows = 2,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            spacing = StyledTheme.spacing.medium,
            contentPadding = PaddingValues(StyledTheme.spacing.small),
        ) { row, column ->
            StyledText("Cell $row, $column")
        }
    }

    RegionContainer(
        title = "Vertical scrollbar",
        id = baseId + 2,
        expanded = expandState,
    ) {
        StyledVerticalScrollbarContainer(
            modifier = Modifier.fillMaxWidth().height(120.dp),
        ) {
            StyledColumn(itemSpacing = StyledTheme.spacing.medium) {
                (1..8).forEach { index ->
                    StyledText("Scrollable item $index")
                }
            }
        }
    }

    RegionContainer(
        title = "Horizontal scrollbar",
        id = baseId + 3,
        expanded = expandState,
    ) {
        StyledHorizontalScrollbarContainer(
            modifier = Modifier.fillMaxWidth().height(48.dp),
        ) {
            StyledRow(
                itemSpacing = StyledTheme.spacing.medium,
                modifier = Modifier.fillMaxHeight()
            ) {
                (1..8).forEach { index ->
                    StyledText(
                        modifier = Modifier.width(96.dp),
                        text = "Item $index",
                    )
                }
            }
        }
    }

    RegionContainer(
        title = "Lazy layouts",
        id = baseId + 4,
        expanded = expandState,
    ) {
        SubTitle("Lazy column")
        StyledLazyColumn(
            itemSpacing = StyledTheme.spacing.medium,
            modifier = Modifier.fillMaxWidth().height(120.dp),
        ) {
            items(items) { item ->
                StyledText(item)
            }
        }

        SubTitle("Lazy row")
        StyledLazyRow(
            itemSpacing = StyledTheme.spacing.medium,
            modifier = Modifier.fillMaxWidth().height(48.dp),
        ) {
            items(items) { item ->
                StyledText(item)
            }
        }

        SubTitle("Lazy vertical grid")
        StyledLazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalItemSpacing = StyledTheme.spacing.medium,
            horizontalItemSpacing = StyledTheme.spacing.medium,
            modifier = Modifier.fillMaxWidth().height(120.dp),
        ) {
            items(items) { item ->
                StyledText(item)
            }
        }

        SubTitle("Lazy horizontal grid")
        StyledLazyHorizontalGrid(
            rows = GridCells.Fixed(2),
            verticalItemSpacing = StyledTheme.spacing.medium,
            horizontalItemSpacing = StyledTheme.spacing.medium,
            modifier = Modifier.fillMaxWidth().height(120.dp),
        ) {
            items(items) { item ->
                StyledText(item)
            }
        }
    }

    RegionContainer(
        title = "Styled pager",
        id = baseId + 5,
        expanded = expandState,
    ) {
        val pages = listOf("Overview", "Details", "Settings")
        val pagerState = rememberPagerState { pages.size }
        StyledPager(
            modifier = Modifier.fillMaxWidth().height(220.dp),
            pages = pages,
            pagerState = pagerState,
            tab = { _, page -> StyledPagerDefaults.Tab(page) },
            pageContent = { _, page ->
                StyledText(
                    modifier = Modifier.padding(StyledTheme.padding.content),
                    text = "Content for $page",
                )
            },
        )
        StyledText("Selected page: ${pages[pagerState.currentPage]}")
    }

    RegionContainer(
        title = "Resizable split panels",
        id = baseId + 6,
        expanded = expandState,
    ) {
        val horizontalSplitState = rememberStyledSplitPanelState(180.dp)
        StyledSplitPanel(
            modifier = Modifier.fillMaxWidth().height(160.dp),
            state = horizontalSplitState,
            firstPaneMinSize = 100.dp,
            firstPaneMaxSize = 280.dp,
            secondPaneMinSize = 100.dp,
            firstPaneContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(StyledTheme.colorScheme.primary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center,
                ) {
                    StyledText("First pane")
                }
            },
            secondPaneContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(StyledTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center,
                ) {
                    StyledText("Second pane")
                }
            },
        )

        val verticalSplitState = rememberStyledSplitPanelState(64.dp)
        StyledSplitPanel(
            modifier = Modifier.fillMaxWidth().height(240.dp),
            orientation = Orientation.Vertical,
            state = verticalSplitState,
            firstPaneMinSize = 48.dp,
            firstPaneMaxSize = 180.dp,
            secondPaneMinSize = 48.dp,
            firstPaneContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(StyledTheme.colorScheme.primary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center,
                ) {
                    StyledText("Top pane")
                }
            },
            secondPaneContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(StyledTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center,
                ) {
                    StyledText("Bottom pane")
                }
            },
        )
    }

    RegionContainer(
        title = "Collapsible header",
        id = baseId + 7,
        expanded = expandState,
    ) {
        val dialogState = rememberDialogState()
        val headerState = rememberStyledCollapsibleHeaderState()
        val listState = rememberLazyListState()
        val scope = rememberCoroutineScope()
        val scrollModes = listOf(
            "Enter always" to StyledCollapsibleHeaderScrollBehavior.EnterAlways,
            "Exit until collapsed" to StyledCollapsibleHeaderScrollBehavior.ExitUntilCollapsed,
        )
        val selectedScrollMode = rememberSaveable { mutableStateOf(1) }
        val scrollBehavior = scrollModes[selectedScrollMode.value].second
        val contentModes = listOf("Scrollable list", "Static content")
        val selectedContentMode = rememberSaveable { mutableStateOf(0) }

        StyledText(
            "Open the isolated scroll demo. The header collapses as its list scrolls; the sticky row stays visible.",
        )
        StyledButton(onClick = dialogState::show) {
            StyledText("Show collapsible header")
        }
        StyledDialog(
            state = dialogState,
            title = { StyledText("Collapsible header") },
            buttons = StyledDialog.Buttons(
                positive = StyledDialog.Button(
                    label = "Close",
                    onClick = { }
                )
            )
        ) { contentPadding ->
            StyledColumn(
                itemSpacing = StyledTheme.spacing.medium,
                modifier = Modifier.padding(contentPadding),
            ) {
                StyledText(
                    "Scroll the list to collapse the header. Choose whether it expands immediately or only after the list reaches its start.",
                )
                StyledDropdownIndex(
                    modifier = Modifier.fillMaxWidth(),
                    items = scrollModes.map { it.first },
                    selectedIndex = selectedScrollMode,
                    title = "Scroll behavior",
                )
                StyledDropdownIndex(
                    modifier = Modifier.fillMaxWidth(),
                    items = contentModes,
                    selectedIndex = selectedContentMode,
                    title = "Content type",
                )
                StyledRow(itemSpacing = StyledTheme.spacing.medium) {
                    StyledButton(onClick = { scope.launch { headerState.expand() } }) {
                        StyledText("Expand")
                    }
                    StyledButton(onClick = { scope.launch { headerState.collapse() } }) {
                        StyledText("Collapse")
                    }
                    StyledText("Collapsed: ${(headerState.collapsedFraction * 100).toInt()}%")
                }
                StyledCollapsibleHeader(
                    modifier = Modifier.fillMaxWidth().height(220.dp),
                    state = headerState,
                    scrollBehavior = scrollBehavior,
                    header = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(96.dp)
                                .background(StyledTheme.colorScheme.primary.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center,
                        ) {
                            StyledText("Collapsible header")
                        }
                    },
                    stickyHeader = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .background(StyledTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center,
                        ) {
                            StyledText("Sticky header")
                        }
                    },
                    content = {
                        if (selectedContentMode.value == 0) {
                            StyledLazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                state = listState,
                                itemSpacing = StyledTheme.spacing.small,
                            ) {
                                items((1..100).toList()) { index ->
                                    StyledText("Scrollable content $index")
                                }
                            }
                        } else {
                            StyledText("Static small content")
                        }
                    },
                )
            }
        }
    }

}
