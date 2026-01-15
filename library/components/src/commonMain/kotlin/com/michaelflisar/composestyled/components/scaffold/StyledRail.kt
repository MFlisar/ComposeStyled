package com.michaelflisar.composestyled.components.scaffold

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.heightIn
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
import com.composeunstyled.theme.ThemeProperty
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.IVariant
import com.michaelflisar.composestyled.core.classes.TokenMap
import com.michaelflisar.composestyled.core.classes.colors.BaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.ColorLocalToken
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.components.StyledButtonDefaults
import com.michaelflisar.composestyled.core.components.StyledVerticalSeparator
import com.michaelflisar.composestyled.core.renderer.ExtensionKey
import com.michaelflisar.composestyled.core.renderer.ExtensionRenderer
import com.michaelflisar.composestyled.core.renderer.getStyledExtensionRenderer
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.ProvideStyledLocals
import com.michaelflisar.composestyled.core.runtime.interaction.rememberStyledResolveState

// ----------------------
// Key + Interface
// ----------------------

object StyledRailKey : ExtensionKey<StyledRailRenderer>

interface StyledRailRenderer : ExtensionRenderer<StyledRailRenderer> {

    override val key: StyledRailKey
        get() = StyledRailKey

    @Composable
    fun Render(
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
    renderer.Render(items, modifier, header, footer, customization)
}

// ----------------------
// Classes
// ----------------------

// --

// ----------------------
// StyledRail
// ----------------------

object StyledRail {

    // properties
    private val Property = ThemeProperty<StatefulBaseColorDef>("rail")

    // tokens
    internal val Tokens = TokenMap.create(Property, Variant.entries.toSet())

    // variants
    enum class Variant(
        override val id: String,
    ) : IVariant {
        Default("rail.default")
    }

    val width: Dp = 200.dp

    // ----------------------
    // Customization
    // ----------------------

    fun customize(
        background: Color? = null,
        content: Color? = null,
    ) = Customization(background, content)

    @Immutable
    class Customization internal constructor(
        val background: Color?,
        val content: Color?,
    )

    // ----------------------
    // Renderers
    // ----------------------

    object Material3 : StyledRailRenderer {

        @InternalComposeStyledApi
        @Composable
        override fun registerVariantStyles() {
            // ...
        }

        @Composable
        override fun Render(
            items: List<StyledNavigationItem>,
            modifier: Modifier,
            header: @Composable (() -> Unit)?,
            footer: @Composable (() -> Unit)?,
            customization: Customization?,
        ) {
            // TODO: just an example
            val contentColor = customization?.content ?: StyledTheme.colors.onSurfaceVariant
            val containerColor = customization?.background ?: StyledTheme.colors.surfaceVariant
            Content(items, modifier, header, footer, containerColor, contentColor, ::RenderItem)
        }

        @Composable
        private fun RenderItem(
            item: StyledNavigationItem,
        ) {
            val foreground =
                if (item.selected) StyledTheme.colors.onPrimary else StyledTheme.colors.secondary
            val background = if (item.selected) StyledTheme.colors.primary else Color.Unspecified
            ItemContent(item, foreground, background, false)
        }
    }

    object Cupertino : StyledRailRenderer {

        @InternalComposeStyledApi
        @Composable
        override fun registerVariantStyles() {
            // TODO: register styles if needed
        }

        @Composable
        override fun Render(
            items: List<StyledNavigationItem>,
            modifier: Modifier,
            header: @Composable (() -> Unit)?,
            footer: @Composable (() -> Unit)?,
            customization: Customization?,
        ) {
            // TODO: just an example
            val contentColor = customization?.content ?: StyledTheme.colors.onSecondary
            val containerColor = customization?.background ?: StyledTheme.colors.secondary
            Content(items, modifier, header, footer, containerColor, contentColor, ::RenderItem)
        }

        @Composable
        private fun RenderItem(
            item: StyledNavigationItem,
        ) {
            val foreground =
                if (item.selected) StyledTheme.colors.onPrimary else StyledTheme.colors.secondary
            val background = if (item.selected) StyledTheme.colors.primary else Color.Unspecified
            ItemContent(item, foreground, background, false)
        }
    }

    object Fluent2 : StyledRailRenderer {

        @InternalComposeStyledApi
        @Composable
        override fun registerVariantStyles() {
            // TODO: register styles if needed
        }

        @Composable
        override fun Render(
            items: List<StyledNavigationItem>,
            modifier: Modifier,
            header: @Composable (() -> Unit)?,
            footer: @Composable (() -> Unit)?,
            customization: Customization?,
        ) {
            // TODO: just an example
            val contentColor = customization?.content ?: StyledTheme.colors.onBackground
            val containerColor = customization?.background ?: StyledTheme.colors.background
            Content(items, modifier, header, footer, containerColor, contentColor, ::RenderItem)
        }

        @Composable
        private fun RenderItem(
            item: StyledNavigationItem,
        ) {
            val foreground: Color? = null
            val background: Color? = null
            ItemContent(item, foreground, background, true)
        }
    }

}

// ----------------------
// private composables
// ----------------------

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

@Composable
private fun ItemContent(
    item: StyledNavigationItem,
    foreground: Color?,
    background: Color?,
    showLineIndicator: Boolean,
) {
    val interactionSource = remember { MutableInteractionSource() }

    /*
    val state = rememberStyledResolveState(
        interactionSource = interactionSource,
        enabled = item.enabled,
        isError = false,
    )
    val def = StyledButton.Tokens.resolveToken(variant).customise(
        background = customization?.background,
        foreground = customization?.content,
        border = customization?.border,
    )
    val resolved = def.resolve(state)
*/

    val modifier = Modifier
        .fillMaxWidth()
        .defaultMinSize(
            minHeight = StyledTheme.sizes.minimumInteractiveSize,
            minWidth = StyledTheme.sizes.minimumInteractiveSize
        )
        .clip(StyledTheme.shapes.control)
        .then(if (background != null) Modifier.background(background) else Modifier)
        .hoverable(
            interactionSource = interactionSource,
            enabled =  item.enabled
        )
        .clickable(
            enabled = item.enabled,
            role = Role.Tab,
            onClick = item.onClick
        )
        .padding(horizontal = StyledTheme.paddings.medium, vertical = StyledTheme.paddings.medium)

    ProvideStyledLocals(
        contentColor = foreground,
        backgroundColor = background
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