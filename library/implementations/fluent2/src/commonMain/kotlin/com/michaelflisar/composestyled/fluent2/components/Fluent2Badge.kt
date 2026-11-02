package com.michaelflisar.composestyled.fluent2.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.foundation.components.FoundationBadge
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledBadge

@OptIn(InternalComposeStyledApi::class)
class Fluent2Badge internal constructor(
    renderer: StyledBadge.Implementation,
) : StyledBadge.Implementation by renderer {

    override val defaults: StyledBadge.Defaults
        @Composable get() = StyledBadge.Defaults(
            minSize = 16.dp,
            dotSize = 6.dp,
            textStyle = StyledTheme.typography.labelSmall.copy(fontSize = 11.sp, lineHeight = 16.sp),
            iconSize = 10.dp,
            contentPadding = PaddingValues(horizontal = 4.dp),
        )

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(): Fluent2Badge {
            val renderer = FoundationBadge.create()
            return remember(renderer) { Fluent2Badge(renderer) }
        }
    }
}
