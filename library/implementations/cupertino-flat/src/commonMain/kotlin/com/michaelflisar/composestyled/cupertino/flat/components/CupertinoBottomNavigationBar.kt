package com.michaelflisar.composestyled.cupertino.flat.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.ui.components.StyledBottomNavigationBar
import com.michaelflisar.composestyled.foundation.components.FoundationBottomNavigationBar
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class CupertinoFlatBottomNavigationBar internal constructor() :
    StyledBottomNavigationBar.Implementation by FoundationBottomNavigationBar(
        minimumHeight = 49.dp,
        showIndicator = false,
        addVerticalContentPadding = false,
    ) {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoFlatBottomNavigationBar() }
    }
}
