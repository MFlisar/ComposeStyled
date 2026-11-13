package com.michaelflisar.composedialogs.styled.provider

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import com.michaelflisar.composedialogs.dialogs.color.ColorPickerIcon
import com.michaelflisar.composedialogs.dialogs.color.ColorTextStyle
import com.michaelflisar.composedialogs.dialogs.color.ColorUIProvider
import com.michaelflisar.composedialogs.styled.DialogInput
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor
import com.michaelflisar.composestyled.ui.components.StyledCard
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledSlider
import com.michaelflisar.composestyled.ui.components.StyledTextButton
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.state.rememberDialogState

internal object ColorUIProviderImpl : ColorUIProvider {

    @Composable
    override fun Text(
        text: String,
        modifier: Modifier,
        style: ColorTextStyle,
        color: Color?,
        fontWeight: FontWeight?,
    ) {
        StyledText(
            text = text,
            modifier = modifier,
            style = when (style) {
                ColorTextStyle.TitleSmall -> StyledTheme.typography.titleSmall
                ColorTextStyle.BodySmall -> StyledTheme.typography.bodySmall
                ColorTextStyle.BodyMedium -> StyledTheme.typography.bodyMedium
            },
            color = color ?: Color.Unspecified,
            fontWeight = fontWeight,
        )
    }

    @Composable
    override fun Slider(
        modifier: Modifier,
        value: Float,
        onValueChange: (Float) -> Unit,
    ) {
        StyledSlider(
            modifier = modifier,
            value = value,
            onValueChange = onValueChange,
        )
    }

    @Composable
    override fun Card(modifier: Modifier, content: @Composable () -> Unit) {
        StyledCard(modifier = modifier, content = content)
    }

    @Composable
    override fun Input(
        modifier: Modifier,
        value: String,
        onValueChange: (String) -> Unit,
        isError: Boolean,
    ) {
        com.michaelflisar.composestyled.ui.components.StyledInput(
            modifier = modifier,
            value = value,
            onValueChange = onValueChange,
            isError = isError,
            variant = com.michaelflisar.composestyled.ui.components.StyledInput.Variant.Outlined,
        )
    }

    @Composable
    override fun TabButton(
        modifier: Modifier,
        onClick: () -> Unit,
        content: @Composable RowScope.() -> Unit,
    ) {
        ProvideDialogActionContext {
            StyledTextButton(onClick = onClick, modifier = modifier, content = content)
        }
    }

    @Composable
    override fun Icon(icon: ColorPickerIcon, contentDescription: String?, tint: Color) {
        StyledIcon(
            icon = when (icon) {
                ColorPickerIcon.Check -> StyledIcons.Check
                ColorPickerIcon.Edit -> StyledIcons.Edit
            },
            contentDescription = contentDescription,
            tint = tint.takeUnless { it == Color.Unspecified } ?: LocalStyledContentColor.current,
        )
    }

    @Composable
    override fun colorPresetShape(): Shape = StyledTheme.shapes.small

    @Composable
    override fun InputDialog(
        visible: Boolean,
        title: String,
        value: MutableState<String>,
        isInputValid: (String) -> Boolean,
        invalidInputMessage: String,
        confirmText: String,
        dismissText: String,
        onConfirm: () -> Unit,
        onDismissRequest: () -> Unit,
    ) {
        val state = rememberDialogState()
        LaunchedEffect(visible) {
            if (visible) state.show() else state.dismiss()
        }
        DialogInput(
            state = state,
            value = value.value,
            onValueChange = { value.value = it },
            isError = !isInputValid(value.value),
            errorMessage = invalidInputMessage.takeIf { !isInputValid(value.value) },
            leadingIcon = {
                com.michaelflisar.composestyled.ui.primitives.StyledText("#")
            },
            title = { com.michaelflisar.composestyled.ui.primitives.StyledText(title) },
            buttons = StyledDialog.Buttons(
                positive = StyledDialog.Button(
                    onClick = {
                        if (isInputValid(value.value)) {
                            onConfirm()
                            state.dismiss()
                        }
                    },
                    label = confirmText,
                    enabled = isInputValid(value.value),
                ),
                negative = StyledDialog.Button(
                    onClick = {
                        state.dismiss()
                        onDismissRequest()
                    },
                    label = dismissText,
                ),
            ),
            onDismissRequest = {
                state.dismiss()
                onDismissRequest()
            },
        )
    }
}
