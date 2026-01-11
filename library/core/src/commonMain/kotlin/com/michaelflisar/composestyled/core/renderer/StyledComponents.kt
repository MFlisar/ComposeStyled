package com.michaelflisar.composestyled.core.renderer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import com.michaelflisar.composestyled.core.components.StyledButtonTokenRenderer
import com.michaelflisar.composestyled.core.components.StyledButtonWrapperRenderer
import com.michaelflisar.composestyled.core.components.StyledCardTokenRenderer
import com.michaelflisar.composestyled.core.components.StyledCardWrapperRenderer
import com.michaelflisar.composestyled.core.components.StyledCheckboxTokenRenderer
import com.michaelflisar.composestyled.core.components.StyledCheckboxWrapperRenderer
import com.michaelflisar.composestyled.core.components.StyledIconTokenRenderer
import com.michaelflisar.composestyled.core.components.StyledIconWrapperRenderer
import com.michaelflisar.composestyled.core.components.StyledSeparatorTokenRenderer
import com.michaelflisar.composestyled.core.components.StyledSeparatorWrapperRenderer
import com.michaelflisar.composestyled.core.components.StyledSurfaceTokenRenderer
import com.michaelflisar.composestyled.core.components.StyledSurfaceWrapperRenderer
import com.michaelflisar.composestyled.core.components.StyledTextFieldTokenRenderer
import com.michaelflisar.composestyled.core.components.StyledTextFieldWrapperRenderer
import com.michaelflisar.composestyled.core.components.StyledTextTokenRenderer
import com.michaelflisar.composestyled.core.components.StyledTextWrapperRenderer
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi

sealed interface StyledComponents

interface StyledTokenRenderer {

    @InternalComposeStyledApi
    @Composable
    fun registerVariantStyles()

}

data class StyledTokenCompontents(
    val surface: StyledSurfaceTokenRenderer,
    val button: StyledButtonTokenRenderer,
    val card: StyledCardTokenRenderer,
    val checkbox: StyledCheckboxTokenRenderer,
    val icon: StyledIconTokenRenderer,
    val separator: StyledSeparatorTokenRenderer,
    val text: StyledTextTokenRenderer,
    val textField: StyledTextFieldTokenRenderer,
) : StyledComponents {

    @InternalComposeStyledApi
    @Composable
    fun registerAllComponents() {
        listOf(
            surface,
            button,
            card,
            checkbox,
            icon,
            separator,
            text,
            textField,
        ).forEach {
            it.registerVariantStyles()
        }
    }
}

data class StyledWrapperComponents(
    val root: @Composable (content: @Composable () -> Unit) -> Unit,
    val surface: StyledSurfaceWrapperRenderer,
    val button: StyledButtonWrapperRenderer,
    val card: StyledCardWrapperRenderer,
    val checkbox: StyledCheckboxWrapperRenderer,
    val icon: StyledIconWrapperRenderer,
    val separator: StyledSeparatorWrapperRenderer,
    val text: StyledTextWrapperRenderer,
    val textField: StyledTextFieldWrapperRenderer,
) : StyledComponents


internal val LocalStyledComponents =
    staticCompositionLocalOf<StyledComponents> { error("No StyledComponents provided!") }