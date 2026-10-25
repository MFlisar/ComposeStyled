package com.michaelflisar.composestyledcomponents.fluent2.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationRadioButton
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.ui.components.StyledRadioButton

@OptIn(InternalComposeStyledApi::class)
class Fluent2RadioButton internal constructor() : StyledRadioButton.Implementation by FoundationRadioButton() {
    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Fluent2RadioButton() }
    }
}
