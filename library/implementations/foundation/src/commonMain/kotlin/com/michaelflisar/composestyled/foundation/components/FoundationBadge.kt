package com.michaelflisar.composestyled.foundation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledBadge

@InternalComposeStyledApi
class FoundationBadge : StyledBadge.Implementation {

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
        fun create() = remember { FoundationBadge() }
    }

    @Composable
    override fun Render(
        badge: (@Composable () -> Unit)?,
        content: @Composable () -> Unit,
        alignment: Alignment,
        minSize: Dp,
        dotSize: Dp,
        textStyle: TextStyle,
        contentPadding: PaddingValues,
        modifier: Modifier,
        offset: DpOffset,
    ) {
        val size = if (badge == null) dotSize else minSize
        FoundationBadgeLayout(
            modifier = modifier,
            alignment = alignment,
            offset = offset,
            content = content,
            badge = {
                Box(
                    modifier = Modifier
                        .defaultMinSize(minWidth = size, minHeight = size)
                        .background(StyledTheme.colorScheme.error, CircleShape)
                        .then(if (badge == null) Modifier else Modifier.padding(contentPadding)),
                    contentAlignment = Alignment.Center,
                ) {
                    if (badge != null) {
                        UpdateStyledThemeLocals(
                            contentColor = StyledTheme.colorScheme.onError,
                            textStyle = textStyle,
                            content = badge,
                        )
                    }
                }
            },
        )
    }
}
