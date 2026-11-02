package com.michaelflisar.demo.demos.regions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledPrimaryScrollableTabRow
import com.michaelflisar.composestyled.ui.components.StyledPrimaryTabRow
import com.michaelflisar.composestyled.ui.components.StyledSecondaryScrollableTabRow
import com.michaelflisar.composestyled.ui.components.StyledTab
import com.michaelflisar.composestyled.ui.layout.StyledPagerDefaults
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoTabs(baseId: Int, expandState: ExpandState) {
    val tabs = listOf("Overview", "Details", "Settings")
    val selectedTabIndex1 = rememberSaveable { mutableIntStateOf(0) }
    val selectedTabIndex2 = rememberSaveable { mutableIntStateOf(0) }
    val manyTabs = listOf(
        "Overview", "Details", "Settings", "History", "Activity", "Members",
        "Permissions", "Notifications", "Integrations", "Billing", "Security",
        "Appearance", "Privacy", "Advanced", "About",
    )

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

        SubTitle("Styled Primary Tab Row on primary container")
        StyledPrimaryTabRow(
            selectedTabIndex = selectedTabIndex1.intValue,
            containerColor = StyledTheme.colorScheme.primary,
            contentColor = StyledTheme.colorScheme.onPrimary,
        ) {
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
    }
}
