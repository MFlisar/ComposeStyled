package com.michaelflisar.composestyled.cupertino.liquid.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.ui.components.StyledTopBar
import com.michaelflisar.composestyled.foundation.components.FoundationTopBar
import com.michaelflisar.composestyled.cupertino.liquid.liquidGlass
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

/** Compact, centered iOS-style navigation bar; not a native UIKit view. */
@OptIn(InternalComposeStyledApi::class)
class CupertinoLiquidTopBar internal constructor() : StyledTopBar.Implementation {

    private val renderer = FoundationTopBar(44.dp, centerTitle = true)

    @Composable
    override fun Render(
        variant: StyledTopBar.Variant,
        modifier: Modifier,
        containerColor: Color,
        contentColor: Color,
        windowInsets: WindowInsets,
        navigationIcon: @Composable () -> Unit,
        actions: @Composable RowScope.() -> Unit,
        title: @Composable () -> Unit,
    ) {
        renderer.Render(
            variant = variant,
            modifier = modifier.liquidGlass(
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
                tint = containerColor,
            ),
            containerColor = Color.Transparent,
            contentColor = contentColor,
            windowInsets = windowInsets,
            navigationIcon = navigationIcon,
            actions = actions,
            title = title,
        )
    }

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoLiquidTopBar() }
    }
}
