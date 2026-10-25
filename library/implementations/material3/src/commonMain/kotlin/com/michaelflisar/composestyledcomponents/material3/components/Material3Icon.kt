package com.michaelflisar.composestyledcomponents.material3.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyledcomponents.classes.IconComposable
import com.michaelflisar.composestyledcomponents.ui.components.StyledIcon

class Material3Icon internal constructor() : StyledIcon.Implementation {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Material3Icon() }
    }

    @Composable
    override fun Render(
        icon: IconComposable,
        modifier: Modifier,
        contentDescription: String?,
        tint: Color,
    ) {
        com.michaelflisar.composestyledcomponents.classes.IconComposable(
            icon = icon,
            contentDescription = contentDescription,
            modifier = modifier,
            tint = tint,
        )
    }
}
