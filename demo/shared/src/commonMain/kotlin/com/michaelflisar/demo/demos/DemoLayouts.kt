package com.michaelflisar.demo.demos

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.ui.layout.StyledSpacedColumn
import com.michaelflisar.composestyledcomponents.ui.layout.StyledSpacedFlowRow
import com.michaelflisar.composestyledcomponents.ui.layout.StyledSpacedRow
import com.michaelflisar.composestyledcomponents.ui.layout.StyledSpacedLazyColumn
import com.michaelflisar.composestyledcomponents.ui.layout.StyledSpacedLazyHorizontalGrid
import com.michaelflisar.composestyledcomponents.ui.layout.StyledSpacedLazyRow
import com.michaelflisar.composestyledcomponents.ui.layout.StyledSpacedLazyVerticalGrid
import com.michaelflisar.composestyledcomponents.ui.layout.StyledHorizontalScrollbarContainer
import com.michaelflisar.composestyledcomponents.ui.layout.StyledVerticalScrollbarContainer
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText
import com.michaelflisar.demo.helper.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoLayouts(baseId: Int, expandState: ExpandState) {
    val items = listOf("One", "Two", "Three", "Four")

    RegionContainer(
        title = "Layouts",
        id = baseId + 1,
        expanded = expandState,
    ) {
        SubTitle("Rows, columns and flow rows")
        StyledSpacedRow {
            StyledText("Row item one")
            StyledText("Row item two")
            StyledText("Row item three")
        }
        StyledSpacedColumn {
            StyledText("Column item one")
            StyledText("Column item two")
        }
        StyledSpacedFlowRow(maxItemsInEachRow = 2) {
            items.forEach { item ->
                StyledText(item)
            }
        }

        SubTitle("Vertical scrollbar")
        StyledVerticalScrollbarContainer(
            modifier = Modifier.fillMaxWidth().height(120.dp),
        ) {
            StyledSpacedColumn {
                (1..8).forEach { index ->
                    StyledText("Scrollable item $index")
                }
            }
        }

        SubTitle("Horizontal scrollbar")
        StyledHorizontalScrollbarContainer(
            modifier = Modifier.fillMaxWidth().height(48.dp),
        ) {
            StyledSpacedRow {
                (1..8).forEach { index ->
                    StyledText(
                        modifier = Modifier.width(96.dp),
                        text = "Item $index",
                    )
                }
            }
        }

        SubTitle("Lazy layouts")
        SubTitle("Lazy column")
        StyledSpacedLazyColumn(
            modifier = Modifier.fillMaxWidth().height(120.dp),
        ) {
            items(items) { item ->
                StyledText(item)
            }
        }

        SubTitle("Lazy row")
        StyledSpacedLazyRow(
            modifier = Modifier.fillMaxWidth().height(48.dp),
        ) {
            items(items) { item ->
                StyledText(item)
            }
        }

        SubTitle("Lazy vertical grid")
        StyledSpacedLazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth().height(120.dp),
        ) {
            items(items) { item ->
                StyledText(item)
            }
        }

        SubTitle("Lazy horizontal grid")
        StyledSpacedLazyHorizontalGrid(
            rows = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth().height(120.dp),
        ) {
            items(items) { item ->
                StyledText(item)
            }
        }
    }
}
