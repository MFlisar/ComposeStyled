package com.michaelflisar.composestyled.components.scaffold

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.colors.ColorRef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.asColorRef
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.components.StyledVerticalSeparator
import com.michaelflisar.composestyled.core.renderer.ExtensionKey
import com.michaelflisar.composestyled.core.renderer.ExtensionRenderer
import com.michaelflisar.composestyled.core.renderer.getStyledExtensionRenderer
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.LocalBackgroundColor
import com.michaelflisar.composestyled.core.runtime.LocalContentColor
import com.michaelflisar.composestyled.core.runtime.ProvideStyledLocals
import com.michaelflisar.composestyled.core.runtime.interaction.rememberStyledResolveState
import com.michaelflisar.composestyled.theme.cupertino.CupertinoUtil
import com.michaelflisar.composestyled.theme.fluent2.Fluent2Util
import com.michaelflisar.composestyled.theme.material3.Material3Util

// ----------------------
// Key + Interface
// ----------------------

object StyledRailKey : ExtensionKey<StyledRailRenderer>

interface StyledRailRenderer : ExtensionRenderer<StyledRailRenderer> {

    override val key: StyledRailKey
        get() = StyledRailKey

    @Composable
    fun render(
        items: List<StyledNavigationItem>,
        modifier: Modifier,
        header: @Composable (() -> Unit)?,
        footer: @Composable (() -> Unit)?,
        customization: StyledRail.Customization?,
    )

}

// ----------------------
// Composable
// ----------------------

@OptIn(InternalComposeStyledApi::class)
@Composable
fun StyledRail(
    items: List<StyledNavigationItem>,
    modifier: Modifier = Modifier,
    header: @Composable (() -> Unit)? = null,
    footer: @Composable (() -> Unit)? = null,
    customization: StyledRail.Customization? = null,
) {
    val renderer = getStyledExtensionRenderer(StyledRailKey)
    renderer.render(items, modifier, header, footer, customization)
}

// ----------------------
// Classes
// ----------------------

// --

// ----------------------
// StyledRail
// ----------------------

object StyledRail {

    // variants
    enum class Variant {
        Default
    }

    val width: Dp = 150.dp

    // ----------------------
    // Customization
    // ----------------------

    fun customize(
        background: ColorRef? = null,
        content: ColorRef? = null,
    ) = Customization(background, content)

    fun customize(
        background: Color? = null,
        content: Color? = null,
    ) = customize(background?.asColorRef(), content?.asColorRef())

    @Immutable
    class Customization internal constructor(
        val background: ColorRef?,
        val content: ColorRef?,
    )

    // ----------------------
    // Renderers
    // ----------------------

    object Material3 : StyledRailRenderer {

        @Composable
        override fun render(
            items: List<StyledNavigationItem>,
            modifier: Modifier,
            header: @Composable (() -> Unit)?,
            footer: @Composable (() -> Unit)?,
            customization: Customization?,
        ) {
            // TODO: just an example
            val contentColor =
                customization?.content?.resolve() ?: StyledTheme.colors.onSurfaceVariant
            val containerColor =
                customization?.background?.resolve() ?: StyledTheme.colors.surfaceVariant
            Content(items, modifier, header, footer, containerColor, contentColor, ::RenderItem)
        }

        @Composable
        private fun RenderItem(
            item: StyledNavigationItem,
        ) {
            val foreground =
                if (item.selected) StyledTheme.colors.onPrimary else StyledTheme.colors.secondary
            val background =
                if (item.selected) StyledTheme.colors.primary else LocalBackgroundColor.current
            val colors = if (item.selected) {
                Material3Util.resolveButtonColors(
                    StyledButton.Variant.Primary,
                    StyledButton.customize(background, foreground, null)
                )
            } else {
                Material3Util.resolveButtonColors(
                    StyledButton.Variant.Text,
                    StyledButton.customize(background, foreground, null)
                )
            }
            ItemContent(item, colors, false)

        }
    }

    object Cupertino : StyledRailRenderer {

        @Composable
        override fun render(
            items: List<StyledNavigationItem>,
            modifier: Modifier,
            header: @Composable (() -> Unit)?,
            footer: @Composable (() -> Unit)?,
            customization: Customization?,
        ) {
            // TODO: just an example
            val contentColor =
                customization?.content?.resolve() ?: StyledTheme.colors.onSurfaceVariant
            val containerColor =
                customization?.background?.resolve() ?: StyledTheme.colors.surfaceVariant
            Content(items, modifier, header, footer, containerColor, contentColor, ::RenderItem)
        }

        @Composable
        private fun RenderItem(
            item: StyledNavigationItem,
        ) {
            val foreground =
                if (item.selected) StyledTheme.colors.primary else LocalContentColor.current
            val border = if (item.selected) StyledTheme.colors.primary else null
            val colors = CupertinoUtil.resolveButtonColors(
                StyledButton.Variant.Text,
                StyledButton.customize(content = foreground, border = border)
            )
            //val colors = remember(item.selected, foreground) { CupertinoUtil.buttonColors(foreground, border) }
            ItemContent(item, colors, false)
        }
    }

    object Fluent2 : StyledRailRenderer {

        @Composable
        override fun render(
            items: List<StyledNavigationItem>,
            modifier: Modifier,
            header: @Composable (() -> Unit)?,
            footer: @Composable (() -> Unit)?,
            customization: Customization?,
        ) {
            // TODO: just an example
            val contentColor = customization?.content?.resolve() ?: StyledTheme.colors.onBackground
            val containerColor =
                customization?.background?.resolve() ?: StyledTheme.colors.background
            Content(items, modifier, header, footer, containerColor, contentColor, ::RenderItem)
        }

        @Composable
        private fun RenderItem(
            item: StyledNavigationItem,
        ) {
            val foreground = LocalContentColor.current
            val colors = Fluent2Util.resolveButtonColors(
                StyledButton.Variant.Text,
                StyledButton.customize(content = foreground)
            )
            ItemContent(item, colors, true)
        }
    }

}

// ----------------------
// private composables
// ----------------------

@OptIn(InternalComposeStyledApi::class)
@Composable
private fun Content(
    items: List<StyledNavigationItem>,
    modifier: Modifier,
    header: @Composable (() -> Unit)?,
    footer: @Composable (() -> Unit)?,
    containerColor: Color,
    contentColor: Color,
    itemRenderer: @Composable (StyledNavigationItem) -> Unit,
) {
    ProvideStyledLocals(
        backgroundColor = containerColor,
        contentColor = contentColor
    ) {
        Column(
            modifier = modifier
                .width(StyledRail.width)
                .fillMaxHeight()
                .background(containerColor)
                .padding(
                    horizontal = StyledTheme.paddings.medium,
                    vertical = StyledTheme.paddings.medium
                ),
            verticalArrangement = Arrangement.spacedBy(StyledTheme.spacings.small),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            header?.invoke()
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(StyledTheme.spacings.small),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items.forEach { item -> itemRenderer(item) }
            }
            footer?.invoke()
        }
    }
}

@OptIn(InternalComposeStyledApi::class)
@Composable
private fun ItemContent(
    item: StyledNavigationItem,
    colors: StatefulBaseColorDef,
    showLineIndicator: Boolean,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val state = rememberStyledResolveState(
        interactionSource = interactionSource,
        enabled = item.enabled,
        isError = false,
    )
    val colors = colors.resolve(state)

    val modifier = Modifier
        .fillMaxWidth()
        .defaultMinSize(
            minHeight = StyledTheme.sizes.minimumInteractiveSize,
            minWidth = StyledTheme.sizes.minimumInteractiveSize
        )
        .clip(StyledTheme.shapes.control)
        .then(if (colors.background != Color.Transparent) Modifier.background(colors.background) else Modifier)
        .then(
            if (colors.border != null) Modifier.border(
                1.dp,
                colors.border!!,
                StyledTheme.shapes.control
            ) else Modifier
        )
        .hoverable(
            interactionSource = interactionSource,
            enabled = item.enabled
        )
        .clickable(
            enabled = item.enabled,
            role = Role.Tab,
            onClick = item.onClick
        )
        .padding(horizontal = StyledTheme.paddings.medium, vertical = StyledTheme.paddings.medium)

    ProvideStyledLocals(
        contentColor = colors.foreground,
        backgroundColor = colors.background
    ) {
        Row(
            modifier = modifier.height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacings.small),
        ) {
            if (showLineIndicator) {
                Box(modifier = Modifier.fillMaxHeight().width(4.dp)) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = item.selected,
                        modifier = Modifier.fillMaxSize(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        StyledVerticalSeparator(
                            modifier = Modifier.clip(StyledTheme.shapes.indicator),
                            color = StyledTheme.colors.primary,
                            thickness = 4.dp,
                        )
                    }
                }
            }
            item.icon?.invoke()
            item.label?.invoke()
        }
    }
}