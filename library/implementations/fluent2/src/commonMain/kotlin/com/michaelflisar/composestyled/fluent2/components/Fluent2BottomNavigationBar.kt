package com.michaelflisar.composestyled.fluent2.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.ui.components.StyledBottomNavigationBar
import com.michaelflisar.composestyled.foundation.components.FoundationBottomNavigationBar
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class Fluent2BottomNavigationBar internal constructor() :
    StyledBottomNavigationBar.Implementation by FoundationBottomNavigationBar(
        minimumHeight = 48.dp,
        indicatorStyle = FoundationBottomNavigationBar.IndicatorStyle.Bottom,
    ) {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Fluent2BottomNavigationBar() }
    }
}
