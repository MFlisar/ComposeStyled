package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.runtime.LocalStyledContext
import com.michaelflisar.composestyledcomponents.runtime.StyledContext
import com.michaelflisar.composestyledcomponents.registry.LocalStyledRegistry
import com.michaelflisar.composestyledcomponents.registry.StyledComponent
import com.michaelflisar.composestyledcomponents.registry.StyledKey

object StyledSnackbar {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledSnackbar")

    // -------------
    // 2) defaults
    // -------------

    @Stable
    class Defaults(
        val shape: Shape,
        val contentPadding: PaddingValues = PaddingValues(),
    )

    // -------------
    // 3) roles / variants / context
    // -------------

    enum class Context : StyledContext { SnackbarAction }

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation> {
        override val key: StyledKey<Implementation>
            get() = Key

        val defaults: Defaults

        @Composable
        fun Render(
            modifier: Modifier,
            shape: Shape,
            containerColor: Color,
            contentColor: Color,
            actionOnNewLine: Boolean,
            action: @Composable (() -> Unit)?,
            dismissAction: @Composable (() -> Unit)?,
            content: @Composable () -> Unit,
        )
    }
}

// -------------------------
// defaults
// -------------------------

object StyledSnackbarDefaults {
    val shape: Shape
        @Composable get() = LocalStyledRegistry.current[StyledSnackbar.Key].defaults.shape
}

// -------------------------
// public API
// -------------------------

@Composable
@OptIn(InternalComposeStyledApi::class)
fun StyledSnackbar(
    modifier: Modifier = Modifier,
    shape: Shape = StyledSnackbarDefaults.shape,
    containerColor: Color = StyledTheme.colorScheme.onSurface,
    contentColor: Color = StyledTheme.contentColorFor(containerColor, checkInversely = true),
    actionOnNewLine: Boolean = false,
    action: @Composable (() -> Unit)? = null,
    dismissAction: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val scopedAction: (@Composable () -> Unit)? = action?.let { content ->
        {
            CompositionLocalProvider(
                LocalStyledContext provides StyledSnackbar.Context.SnackbarAction,
                content = content,
            )
        }
    }
    LocalStyledRegistry.current[StyledSnackbar.Key].Render(
        modifier = modifier,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        actionOnNewLine = actionOnNewLine,
        action = scopedAction,
        dismissAction = dismissAction,
        content = content,
    )
}
