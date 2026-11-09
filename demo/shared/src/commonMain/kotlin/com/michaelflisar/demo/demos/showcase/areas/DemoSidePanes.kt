package com.michaelflisar.demo.demos.showcase.areas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.specials.StyledPaneContainer
import com.michaelflisar.composestyled.ui.specials.StyledTranslatableContent
import com.michaelflisar.composestyled.ui.specials.rememberStyledSidePane
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.demo.helper.RegionContainer

@Composable
internal fun DemoSidePanes(baseId: Int, expandState: ExpandState) {
    RegionContainer(
        title = "Side panes and translatable content",
        id = baseId + 1,
        expanded = expandState,
    ) {
        StyledPaneContainer(
            modifier = Modifier.fillMaxWidth().height(180.dp),
            left = rememberStyledSidePane("Navigation") {
                StyledText("Navigation pane content")
            },
            right = rememberStyledSidePane(
                label = "Details",
                icon = {
                    StyledIcon(
                        icon = StyledIcons.Search,
                        contentDescription = "Search details",
                    )
                },
            ) {
                StyledText("Details pane content")
            },
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                StyledText("Main content")
            }
        }

        StyledTranslatableContent(
            modifier = Modifier.fillMaxWidth().height(48.dp),
            side = StyledTranslatableContent.Side.Left,
            translation = 0.65f,
        ) {
            StyledText(
                modifier = Modifier
                    .stretch(true)
                    .background(StyledTheme.colorScheme.primary.copy(alpha = 0.12f)),
                text = "Partially revealed content",
            )
        }
    }
}
