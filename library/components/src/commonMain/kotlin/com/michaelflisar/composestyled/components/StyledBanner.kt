package com.michaelflisar.composestyled.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.components.layout.ContentColumn
import com.michaelflisar.composestyled.components.layout.ContentRow
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.colors.ColorRef
import com.michaelflisar.composestyled.core.classes.colors.asColorRef
import com.michaelflisar.composestyled.core.components.StyledCard
import com.michaelflisar.composestyled.core.components.StyledIcon
import com.michaelflisar.composestyled.core.components.StyledText
import com.michaelflisar.composestyled.core.renderer.ExtensionKey
import com.michaelflisar.composestyled.core.renderer.ExtensionRenderer
import com.michaelflisar.composestyled.core.renderer.getStyledExtensionRenderer
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi

// ----------------------
// Key + Interface
// ----------------------

object StyledBannerKey : ExtensionKey<StyledBannerRenderer>

interface StyledBannerRenderer : ExtensionRenderer<StyledBannerRenderer> {

    override val key: StyledBannerKey
        get() = StyledBannerKey

    @Composable
    fun Render(
        text: String,
        imageVector: ImageVector?,
        modifier: Modifier,
        title: String?,
        customization: StyledBanner.Customization?,
    )

}

// ----------------------
// Composable
// ----------------------

@OptIn(InternalComposeStyledApi::class)
@Composable
fun StyledBanner(
    text: String,
    imageVector: ImageVector?,
    modifier: Modifier = Modifier,
    title: String? = null,
    customization: StyledBanner.Customization? = null,
) {
    val renderer = getStyledExtensionRenderer(StyledBannerKey)
    renderer.Render(text, imageVector, modifier, title, customization)
}

// ----------------------
// Classes
// ----------------------

// --

// ----------------------
// StyledBanner
// ----------------------

object StyledBanner {

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

    object Material3 : StyledBannerRenderer {

        @InternalComposeStyledApi
        @Composable
        override fun registerVariantStyles() {
            // TODO: register styles if needed
        }

        @Composable
        override fun Render(
            text: String,
            imageVector: ImageVector?,
            modifier: Modifier,
            title: String?,
            customization: Customization?,
        ) {
            // TODO: just an example
            val contentColor = customization?.content?.resolve() ?: StyledTheme.colors.surfaceVariant
            val containerColor = customization?.background?.resolve() ?: StyledTheme.colors.onSurfaceVariant
            Content(text, imageVector, modifier, title, containerColor, contentColor)
        }
    }

    object Cupertino : StyledBannerRenderer {

        @InternalComposeStyledApi
        @Composable
        override fun registerVariantStyles() {
            // TODO: register styles if needed
        }

        @Composable
        override fun Render(
            text: String,
            imageVector: ImageVector?,
            modifier: Modifier,
            title: String?,
            customization: Customization?,
        ) {
            // TODO: just an example
            val contentColor = customization?.content?.resolve() ?: StyledTheme.colors.secondary
            val containerColor = customization?.background?.resolve() ?: StyledTheme.colors.onSecondary
            Content(text, imageVector, modifier, title, containerColor, contentColor)
        }
    }

    object Fluent2 : StyledBannerRenderer {

        @InternalComposeStyledApi
        @Composable
        override fun registerVariantStyles() {
            // TODO: register styles if needed
        }

        @Composable
        override fun Render(
            text: String,
            imageVector: ImageVector?,
            modifier: Modifier,
            title: String?,
            customization: Customization?,
        ) {
            // TODO: just an example
            val contentColor = customization?.content?.resolve() ?: StyledTheme.colors.background
            val containerColor = customization?.background?.resolve() ?: StyledTheme.colors.onBackground
            Content(text, imageVector, modifier, title, containerColor, contentColor)
        }
    }

}

// ----------------------
// private composables
// ----------------------

@Composable
private fun Content(
    text: String,
    imageVector: ImageVector?,
    modifier: Modifier,
    title: String?,
    containerColor: Color,
    contentColor: Color,
) {
    StyledCard(
        modifier = modifier,
        customization = StyledCard.customize(
            background = containerColor.asColorRef(),
            content = contentColor.asColorRef()
        )
    ) {
        ContentRow(
            padding = StyledTheme.paddings.medium,
            spacing = StyledTheme.spacings.medium,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (imageVector != null) {
                StyledIcon(
                    imageVector = imageVector,
                    contentDescription = null
                )
            }
            ContentColumn(
                padding = 0.dp,
                spacing = StyledTheme.spacings.small,
            ) {
                if (title != null) {
                    StyledText(
                        text = title,
                        style = StyledTheme.typography.titleMedium,
                    )
                }
                StyledText(text, style = StyledTheme.typography.bodySmall)
            }
        }
    }
}