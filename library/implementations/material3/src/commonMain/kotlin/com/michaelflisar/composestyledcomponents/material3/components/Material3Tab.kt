package com.michaelflisar.composestyledcomponents.material3.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyledcomponents.ui.components.StyledTab

class Material3Tab internal constructor() : StyledTab.Implementation {

    companion object {
        @Composable
        fun create() = remember { Material3Tab() }
    }

    @Composable
    override fun Render(
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        selectedContentColor: Color,
        unselectedContentColor: Color,
        interactionSource: MutableInteractionSource?,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        Tab(
            selected = selected,
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            selectedContentColor = selectedContentColor,
            unselectedContentColor = unselectedContentColor,
            interactionSource = interactionSource,
            content = content,
        )
    }
}
