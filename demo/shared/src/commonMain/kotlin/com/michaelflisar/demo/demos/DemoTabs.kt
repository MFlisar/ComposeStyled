package com.michaelflisar.demo.demos

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.components.StyledPrimaryScrollableTabRow
import com.michaelflisar.composestyledcomponents.ui.components.StyledPrimaryTabRow
import com.michaelflisar.composestyledcomponents.ui.components.StyledSecondaryScrollableTabRow
import com.michaelflisar.composestyledcomponents.ui.components.StyledSecondaryTabRow
import com.michaelflisar.composestyledcomponents.ui.components.StyledTab
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText
import com.michaelflisar.composestyledcomponents.ui.specials.StyledPager
import com.michaelflisar.composestyledcomponents.ui.specials.StyledPagerDefaults
import com.michaelflisar.demo.helper.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoTabs(baseId: Int, expandState: ExpandState) {
    val tabs = listOf("Overview", "Details", "Settings")
    val pagerState = rememberPagerState { tabs.size }
    val selectedTabIndex1 = remember { mutableIntStateOf(0) }
    val selectedTabIndex2 = remember { mutableIntStateOf(0) }
    val manyTabs = listOf("Overview", "Details", "Settings", "History", "Activity", "Members")

    RegionContainer(
        title = "Tabs",
        id = baseId + 1,
        expanded = expandState,
    ) {
        SubTitle("Styled Primary Tab Row")
        StyledPrimaryTabRow(selectedTabIndex = selectedTabIndex1.intValue) {
            tabs.forEachIndexed { index, title ->
                StyledTab(
                    selected = selectedTabIndex1.intValue == index,
                    onClick = { selectedTabIndex1.intValue = index },
                ) {
                    StyledPagerDefaults.Tab(title)
                }
            }
        }

        SubTitle("Styled Primary Scrollable Tab Row")
        StyledPrimaryScrollableTabRow(selectedTabIndex = selectedTabIndex2.intValue) {
            manyTabs.forEachIndexed { index, title ->
                StyledTab(
                    selected = selectedTabIndex2.intValue == index,
                    onClick = { selectedTabIndex2.intValue = index },
                ) {
                    StyledPagerDefaults.Tab(title)
                }
            }
        }

        SubTitle("Styled Secondary Scrollable Tab Row")
        StyledSecondaryScrollableTabRow(selectedTabIndex = selectedTabIndex2.intValue) {
            manyTabs.forEachIndexed { index, title ->
                StyledTab(
                    selected = selectedTabIndex2.intValue == index,
                    onClick = { selectedTabIndex2.intValue = index },
                ) {
                    StyledPagerDefaults.Tab(title)
                }
            }
        }

        SubTitle("Styled Secondary Tab Row in StyledPager")
        StyledPager(
            pages = tabs,
            pagerState = pagerState,
            tab = { _, page -> StyledPagerDefaults.Tab(page) },
            pageContent = { _, page ->
                StyledText(
                    modifier = Modifier.padding(StyledTheme.padding.content),
                    text = "Content for $page"
                )
            }
        )
        StyledText("Selected tab: ${tabs[pagerState.currentPage]}")
    }
}
