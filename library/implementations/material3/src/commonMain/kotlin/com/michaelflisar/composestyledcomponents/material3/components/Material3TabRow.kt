package com.michaelflisar.composestyledcomponents.material3.components

import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyledcomponents.ui.components.StyledTabRow

class Material3TabRow internal constructor() : StyledTabRow.Implementation {

    companion object {
        @Composable
        fun create() = remember { Material3TabRow() }
    }

    @Composable
    override fun Render(
        variant: StyledTabRow.Variant,
        scrollable: Boolean,
        selectedTabIndex: Int,
        modifier: Modifier,
        containerColor: Color,
        contentColor: Color,
        content: @Composable () -> Unit,
    ) {
        when (variant) {
            StyledTabRow.Variant.Primary -> {
                if (scrollable) {
                    PrimaryScrollableTabRow(
                        selectedTabIndex = selectedTabIndex,
                        modifier = modifier,
                        containerColor = containerColor,
                        contentColor = contentColor,
                        tabs = content,
                    )
                } else {
                    PrimaryTabRow(
                        selectedTabIndex = selectedTabIndex,
                        modifier = modifier,
                        containerColor = containerColor,
                        contentColor = contentColor,
                        tabs = content,
                    )
                }
            }

            StyledTabRow.Variant.Secondary -> {
                if (scrollable) {
                    SecondaryScrollableTabRow(
                        selectedTabIndex = selectedTabIndex,
                        modifier = modifier,
                        containerColor = containerColor,
                        contentColor = contentColor,
                        tabs = content,
                    )
                } else {
                    SecondaryTabRow(
                        selectedTabIndex = selectedTabIndex,
                        modifier = modifier,
                        containerColor = containerColor,
                        contentColor = contentColor,
                        tabs = content,
                    )
                }
            }
        }
    }
}
