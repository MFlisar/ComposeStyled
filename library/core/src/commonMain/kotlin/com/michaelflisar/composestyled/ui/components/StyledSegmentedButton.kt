package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledComponentWithDefaults
import com.michaelflisar.composestyled.registry.StyledKey
import com.michaelflisar.composestyled.ui.components.StyledButton.Customization

object StyledSegmentedButton {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledSegmentedButton")
    val DefaultsKey = StyledKey<Defaults>("StyledSegmentedButtonDefaults")

        // -------------
        // 2) defaults
        // -------------

        @Stable
        data class Defaults(
            val itemShape: Shape,
            val itemContentPadding: PaddingValues,
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
            selected: Boolean,
            multiSelect: Boolean,
            showSelectedIcon: Boolean,
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
        @Composable get() = LocalStyledRegistry.current[StyledSegmentedButton.DefaultsKey].itemShape

    val itemContentPadding: PaddingValues
        @Composable get() = LocalStyledRegistry.current[StyledSegmentedButton.DefaultsKey].itemContentPadding
}

// -------------------------
// public API
// -------------------------

/** A selectable button intended to be placed inside a segmented-button row. */
@Composable
fun StyledSegmentedButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    multiSelect: Boolean = false,
    showSelectedIcon: Boolean = true,
    customization: Customization? = null,
    itemShape: Shape = StyledSegmentedButtonDefaults.itemShape,
    itemContentPadding: PaddingValues = StyledSegmentedButtonDefaults.itemContentPadding,
    content: @Composable () -> Unit,
) {
    LocalStyledRegistry.current[StyledSegmentedButton.Key].Render(
        selected = selected,
        multiSelect = multiSelect,
        showSelectedIcon = showSelectedIcon,
        modifier = modifier,
        enabled = enabled,
        itemShape = itemShape,
        itemContentPadding = itemContentPadding,
        customization = customization,
        content = content,
        onClick = onClick,
    )
}
