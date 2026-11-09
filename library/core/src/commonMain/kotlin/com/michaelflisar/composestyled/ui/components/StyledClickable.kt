package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledComponentWithDefaults
import com.michaelflisar.composestyled.registry.StyledKey

object StyledClickable {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledClickable")
    val DefaultsKey = StyledKey<Defaults>("StyledClickableDefaults")

    // -------------
    // 2) defaults
    // -------------

    @Stable
    data class Defaults(
        val shape: Shape,
        val contentPadding: PaddingValues,
    )

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation>, StyledComponentWithDefaults<Defaults> {
        override val key: StyledKey<Implementation>
            get() = Key

        override val defaultsKey: StyledKey<Defaults>
            get() = DefaultsKey

        @Composable
        fun Render(
            onClick: () -> Unit,
            modifier: Modifier,
            enabled: Boolean,
            interactionSource: MutableInteractionSource?,
            shape: Shape,
            contentPadding: PaddingValues,
            content: @Composable RowScope.() -> Unit,
        )
    }
}

// -------------------------
// defaults
// -------------------------

object StyledClickableDefaults {

    val shape: Shape
        @Composable get() = LocalStyledRegistry.current[StyledClickable.DefaultsKey].shape

    val contentPadding: PaddingValues
        @Composable get() = LocalStyledRegistry.current[StyledClickable.DefaultsKey].contentPadding
}

// -------------------------
// public API
// -------------------------

/** A theme-rendered clickable row for custom content, without a button's minimum width. */
@Composable
fun StyledClickable(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    // defaults / customisation
    shape: Shape = StyledClickableDefaults.shape,
    contentPadding: PaddingValues = StyledClickableDefaults.contentPadding,
    // content
    content: @Composable RowScope.() -> Unit,
) {
    LocalStyledRegistry.current[StyledClickable.Key].Render(
        onClick = onClick,
        modifier = modifier.withStyledInputPointerIcon(),
        enabled = enabled,
        interactionSource = interactionSource,
        shape = shape,
        contentPadding = contentPadding,
        content = content,
    )
}
