package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyledcomponents.registry.LocalStyledRegistry
import com.michaelflisar.composestyledcomponents.registry.StyledComponent
import com.michaelflisar.composestyledcomponents.registry.StyledKey
import com.michaelflisar.composestyledcomponents.ui.components.StyledButton.Customization

object StyledSegmentedButton {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledSegmentedButton")

    // -------------
    // 2) defaults
    // -------------

    @Stable
    class Defaults(
        val itemShape: Shape,
        val itemContentPadding: PaddingValues,
    )

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation> {
        override val key: StyledKey<Implementation>
            get() = Key

        val defaults: Defaults

        @Composable
        fun Render(
            selected: Boolean,
            multiSelect: Boolean,
            modifier: Modifier,
            enabled: Boolean,
            itemShape: Shape,
            itemContentPadding: PaddingValues,
            customization: Customization?,
            content: @Composable () -> Unit,
            onClick: () -> Unit,
        )
    }
}

// -------------------------
// defaults
// -------------------------

object StyledSegmentedButtonDefaults {

    val itemShape: Shape
        @Composable get() = LocalStyledRegistry.current[StyledSegmentedButton.Key].defaults.itemShape

    val itemContentPadding: PaddingValues
        @Composable get() = LocalStyledRegistry.current[StyledSegmentedButton.Key].defaults.itemContentPadding
}

// -------------------------
// public API
// -------------------------

@Composable
fun StyledSegmentedButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    multiSelect: Boolean = false,
    customization: Customization? = null,
    itemShape: Shape = StyledSegmentedButtonDefaults.itemShape,
    itemContentPadding: PaddingValues = StyledSegmentedButtonDefaults.itemContentPadding,
    content: @Composable () -> Unit,
) {
    LocalStyledRegistry.current[StyledSegmentedButton.Key].Render(
        selected = selected,
        multiSelect = multiSelect,
        modifier = modifier,
        enabled = enabled,
        itemShape = itemShape,
        itemContentPadding = itemContentPadding,
        customization = customization,
        content = content,
        onClick = onClick,
    )
}
