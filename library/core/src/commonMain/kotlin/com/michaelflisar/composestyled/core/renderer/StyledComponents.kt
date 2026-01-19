package com.michaelflisar.composestyled.core.renderer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import com.michaelflisar.composestyled.core.components.StyledButtonRenderer
import com.michaelflisar.composestyled.core.components.StyledCardRenderer
import com.michaelflisar.composestyled.core.components.StyledCheckboxRenderer
import com.michaelflisar.composestyled.core.components.StyledIconRenderer
import com.michaelflisar.composestyled.core.components.StyledSeparatorRenderer
import com.michaelflisar.composestyled.core.components.StyledSurfaceRenderer
import com.michaelflisar.composestyled.core.components.StyledTextFieldRenderer
import com.michaelflisar.composestyled.core.components.StyledTextRenderer

sealed class StyledComponents {

    abstract val surface: StyledSurfaceRenderer
    abstract val button: StyledButtonRenderer
    abstract val card: StyledCardRenderer
    abstract val checkbox: StyledCheckboxRenderer
    abstract val icon: StyledIconRenderer
    abstract val separator: StyledSeparatorRenderer
    abstract val text: StyledTextRenderer
    abstract val textField: StyledTextFieldRenderer

    data class Components(
        override val surface: StyledSurfaceRenderer,
        override val button: StyledButtonRenderer,
        override val card: StyledCardRenderer,
        override val checkbox: StyledCheckboxRenderer,
        override val icon: StyledIconRenderer,
        override val separator: StyledSeparatorRenderer,
        override val text: StyledTextRenderer,
        override val textField: StyledTextFieldRenderer,
    ) : StyledComponents()

    data class WrapperComponents(
        val root: @Composable (content: @Composable () -> Unit) -> Unit,
        override val surface: StyledSurfaceRenderer,
        override val button: StyledButtonRenderer,
        override val card: StyledCardRenderer,
        override val checkbox: StyledCheckboxRenderer,
        override val icon: StyledIconRenderer,
        override val separator: StyledSeparatorRenderer,
        override val text: StyledTextRenderer,
        override val textField: StyledTextFieldRenderer,
    ) : StyledComponents()
}


internal val LocalStyledComponents =
    staticCompositionLocalOf<StyledComponents> { error("No StyledComponents provided!") }