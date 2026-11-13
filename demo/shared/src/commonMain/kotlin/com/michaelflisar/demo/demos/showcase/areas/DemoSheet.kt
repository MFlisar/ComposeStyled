package com.michaelflisar.demo.demos.showcase.areas

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledSurface
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.layout.StyledRow
import com.michaelflisar.composestyled.ui.layout.StyledSheet
import com.michaelflisar.composestyled.ui.layout.rememberStyledSheetState
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoSheet(baseId: Int, expandState: ExpandState) {
    RegionContainer(title = "Sheet", id = baseId + 1, expanded = expandState) {
        val sheetState = rememberStyledSheetState()
        var edge by remember { mutableStateOf(StyledSheet.Edge.Start) }
        var edgeTapCount by remember { mutableStateOf(0) }

        SubTitle("Choose an edge, tap through it, or swipe inward to open the sheet")
        StyledRow(itemSpacing = 8.dp) {
            StyledButton(onClick = { edge = StyledSheet.Edge.Start }) {
                StyledText("Start")
            }
            StyledButton(onClick = { edge = StyledSheet.Edge.End }) {
                StyledText("End")
            }
        }
        StyledRow(itemSpacing = 8.dp) {
            StyledButton(onClick = { edge = StyledSheet.Edge.Top }) {
                StyledText("Top")
            }
            StyledButton(onClick = { edge = StyledSheet.Edge.Bottom }) {
                StyledText("Bottom")
            }
        }
        StyledText("Tap through the selected edge or drag inward to open the sheet.")
        StyledButton(onClick = { sheetState.expand() }) {
            StyledText("Open sheet")
        }
        StyledText("Progress: ${(sheetState.progress * 100).toInt()}%")
        StyledSurface(
            color = StyledTheme.colorScheme.primarySurface,
            contentColor = StyledTheme.colorScheme.onPrimarySurface
        ) {
            Box(
                Modifier.fillMaxWidth().height(280.dp)
            ) {
                StyledRow {
                    StyledText(
                        text = "The sheet overlays this content. Tap the scrim or press Back to close it.",
                        modifier = Modifier.padding(16.dp),
                    )
                }
                val edgeButtonAlignment = when (edge) {
                    StyledSheet.Edge.Start -> Alignment.CenterStart
                    StyledSheet.Edge.End -> Alignment.CenterEnd
                    StyledSheet.Edge.Top -> Alignment.TopCenter
                    StyledSheet.Edge.Bottom -> Alignment.BottomCenter
                }
                StyledButton(
                    modifier = Modifier.align(edgeButtonAlignment),
                    onClick = { edgeTapCount++ },
                ) {
                    StyledText("Tap through ($edgeTapCount)")
                }
                StyledSheet(
                    state = sheetState,
                    edge = edge,
                    expandedSize = StyledSheet.Size.Fraction(0.82f),
                    triggerSize = 28.dp,
                    containerColor = StyledTheme.colorScheme.secondarySurface,
                    contentColor = StyledTheme.colorScheme.onSecondarySurface,
                ) {
                    StyledColumn(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        itemSpacing = 12.dp,
                    ) {
                        StyledText("Sheet content")
                        StyledText("Its size is 82% of the available width or height.")
                        StyledButton(onClick = { sheetState.collapse() }) {
                            StyledText("Close")
                        }
                    }
                }
            }
        }
    }
}
