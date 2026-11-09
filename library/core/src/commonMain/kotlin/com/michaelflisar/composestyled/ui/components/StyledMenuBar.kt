package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.runtime.LocalStyledContext
import com.michaelflisar.composestyled.runtime.StyledContext
import com.michaelflisar.composestyled.ui.internal.LocalStyledMenuBarLevel
import com.michaelflisar.composestyled.ui.internal.StyledMenuBarItem
import com.michaelflisar.composestyled.ui.internal.StyledMenuBarLevel
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.primitives.StyledVerticalDivider

/** Provides contextual button styling for items composed in a menu bar. */
object StyledMenuBar {

    /** Context for top-level menu items. */
    enum class Context : StyledContext {
        MenuItem,
    }
}

/** Content scope for menu-bar commands, menus, separators and custom entries. */
@Stable
class StyledMenuBarScope internal constructor(
    private val renderRowContent:
    @Composable (@Composable RowScope.() -> Unit) -> Unit,
) {

    /** Adds a button that opens a menu on hover or click. */
    @Composable
    fun menu(
        text: String,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        interactionSource: MutableInteractionSource? = null,
        variant: StyledButton.Variant = StyledButton.Variant.Auto,
        icon: IconComposable? = null,
        content: @Composable StyledPopupMenuScope.() -> Unit,
    ) {
        menu(
            triggerContent = { StyledText(text) },
            modifier = modifier,
            enabled = enabled,
            interactionSource = interactionSource,
            variant = variant,
            icon = icon,
            content = content,
        )
    }

    /** Adds a menu whose trigger can contain arbitrary composables. */
    @Composable
    fun menu(
        triggerContent: @Composable RowScope.() -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        interactionSource: MutableInteractionSource? = null,
        variant: StyledButton.Variant = StyledButton.Variant.Auto,
        icon: IconComposable? = null,
        content: @Composable StyledPopupMenuScope.() -> Unit,
    ) {
        StyledMenuBarItem(
            menu = content,
            modifier = modifier,
            enabled = enabled,
            interactionSource = interactionSource,
            variant = variant,
            icon = icon,
            content = triggerContent,
        )
    }

    /** Adds a regular command button. */
    @Composable
    fun item(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        interactionSource: MutableInteractionSource? = null,
        variant: StyledButton.Variant = StyledButton.Variant.Auto,
        icon: IconComposable? = null,
    ) {
        StyledButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            interactionSource = interactionSource,
            variant = variant,
            content = {
                icon?.let { StyledIcon(icon = it, contentDescription = null) }
                StyledText(text)
            },
        )
    }

    /** Adds a vertical separator between menu-bar groups. */
    @Composable
    fun divider(modifier: Modifier = Modifier) {
        StyledVerticalDivider(modifier = modifier)
    }

    /** Adds arbitrary content to the menu bar. */
    @Composable
    fun custom(content: @Composable RowScope.() -> Unit) {
        renderRowContent(content)
    }
}

/** A compact, theme-aware horizontal menu bar for application commands. */
@OptIn(InternalComposeStyledApi::class)
@Composable
fun StyledMenuBar(
    modifier: Modifier = Modifier,
    containerColor: Color = StyledTheme.colorScheme.surface,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    itemSpacing: Dp = 0.dp,
    content: @Composable StyledMenuBarScope.() -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val menuLevel = remember(coroutineScope) {
        StyledMenuBarLevel(coroutineScope)
    }

    CompositionLocalProvider(
        LocalStyledMenuBarLevel provides menuLevel,
        LocalStyledContext provides StyledMenuBar.Context.MenuItem,
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
                    //.heightIn(min = 36.dp)
                    .padding(contentPadding),
                horizontalArrangement = Arrangement.spacedBy(itemSpacing),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val menuBarRow = this
                content(StyledMenuBarScope { rowContent -> menuBarRow.rowContent() })
            }
        }
    }
}
