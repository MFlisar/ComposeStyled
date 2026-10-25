package com.michaelflisar.composestyledcomponents.ui.specials

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.components.StyledSecondaryTabRow
import com.michaelflisar.composestyledcomponents.ui.components.StyledTab
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText
import kotlinx.coroutines.launch

object StyledPagerDefaults {

    @Composable
    fun Tab(text: String) {
        StyledText(
            text = text,
            modifier = Modifier.padding(StyledTheme.padding.medium)
        )
    }

}

@Composable
fun <T> StyledPager(
    pages: List<T>,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState { pages.size },
    tab: @Composable (index: Int, page: T) -> Unit,
    animatePageChange: Boolean = true,
    pageContent: @Composable PagerScope.(index: Int, page: T) -> Unit,
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier,
    ) {
        StyledSecondaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth(),
        ) {
            pages.forEachIndexed { index, page ->
                StyledTab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            if (animatePageChange) {
                                pagerState.animateScrollToPage(index)
                            } else {
                                pagerState.scrollToPage(index)
                            }
                        }
                    },
                ) {
                    tab(index, page)
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) { index ->

            pageContent(
                index,
                pages[index],
            )
        }
    }
}