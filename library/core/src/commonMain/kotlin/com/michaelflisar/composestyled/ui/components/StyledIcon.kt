package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledKey
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.runtime.LocalStyledContext

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
// defaults
// -------------------------

object StyledIconDefaults {
    val size: Dp
        @OptIn(InternalComposeStyledApi::class)
        @Composable get() = when (LocalStyledContext.current) {
            StyledMenuBar.Context.MenuItem -> minOf(StyledTheme.sizes.iconSize, 16.dp)
            StyledBadge.Context.BadgeContent -> StyledBadgeDefaults.iconSize
            else -> StyledTheme.sizes.iconSize
        }
}

// -------------------------
// public API
// -------------------------

/** Renders an [IconComposable] using the active theme's sizing and current content color by default. */
@Composable
fun StyledIcon(
    icon: IconComposable,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    size: Dp? = StyledIconDefaults.size,
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