package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.runtime.LocalStyledContext
import com.michaelflisar.composestyled.runtime.StyledContext
import com.michaelflisar.composestyled.ui.internal.LocalStyledToolbarMenuLevel
import com.michaelflisar.composestyled.ui.internal.StyledToolbarMenuLevel

/** Provides contextual button styling for controls composed in a toolbar. */
object StyledToolbar {

    /** Context for toolbar action buttons. */
    enum class Context : StyledContext {
        Action,
    }
}

/** A compact, theme-aware horizontal container for application toolbar actions and menus. */
@OptIn(InternalComposeStyledApi::class)
@Composable
fun StyledToolbar(
    modifier: Modifier = Modifier,
    containerColor: Color = StyledTheme.colorScheme.surface,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    contentPadding: PaddingValues = PaddingValues(
        horizontal = StyledTheme.padding.small,
        vertical = 2.dp,
    ),
    itemSpacing: Dp = StyledTheme.spacing.small,
    content: @Composable RowScope.() -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val menuLevel = remember(coroutineScope) {
        StyledToolbarMenuLevel(coroutineScope)
    }

    CompositionLocalProvider(
        LocalStyledToolbarMenuLevel provides menuLevel,
        LocalStyledContext provides StyledToolbar.Context.Action,
    ) {
        StyledSurface(
            modifier = modifier.fillMaxWidth(),
            shape = RectangleShape,
            color = containerColor,
            contentColor = contentColor,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 36.dp)
                    .padding(contentPadding),
                horizontalArrangement = Arrangement.spacedBy(itemSpacing),
                verticalAlignment = Alignment.CenterVertically,
                content = content,
            )
        }
    }
}
