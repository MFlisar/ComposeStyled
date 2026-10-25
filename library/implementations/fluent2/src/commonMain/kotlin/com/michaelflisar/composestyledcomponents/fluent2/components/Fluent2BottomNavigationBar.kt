package com.michaelflisar.composestyledcomponents.fluent2.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.ui.components.StyledBottomNavigationBar
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationBottomNavigationBar
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class Fluent2BottomNavigationBar internal constructor() :
    StyledBottomNavigationBar.Implementation by FoundationBottomNavigationBar(48.dp) {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Fluent2BottomNavigationBar() }
    }
}
