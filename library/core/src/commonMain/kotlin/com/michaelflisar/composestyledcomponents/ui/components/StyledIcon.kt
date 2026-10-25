package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.classes.IconComposable
import com.michaelflisar.composestyledcomponents.tokens.LocalStyledContentColor
import com.michaelflisar.composestyledcomponents.registry.LocalStyledRegistry
import com.michaelflisar.composestyledcomponents.registry.StyledComponent
import com.michaelflisar.composestyledcomponents.registry.StyledKey

object StyledIcon {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledIcon")

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation> {
        override val key: StyledKey<Implementation>
            get() = Key

        @Composable
        fun Render(
            icon: IconComposable,
            modifier: Modifier,
            contentDescription: String?,
            tint: Color,
        )
    }
}

// -------------------------
// public API
// -------------------------

@Composable
fun StyledIcon(
    icon: IconComposable,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    size: Dp? = StyledTheme.sizes.iconSize,
    tint: Color = LocalStyledContentColor.current,
) {
    LocalStyledRegistry.current[StyledIcon.Key].Render(
        icon = icon,
        modifier = modifier.then(
            if (size != null) Modifier.size(size) else Modifier
        ),
        contentDescription = contentDescription,
        tint = tint,
    )
}