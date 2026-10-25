package com.michaelflisar.demo.demos

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.classes.toIconComposable
import com.michaelflisar.composestyledcomponents.classes.CustomColors
import com.michaelflisar.composestyledcomponents.ui.components.StyledIcon
import com.michaelflisar.composestyledcomponents.ui.components.StyledInput
import com.michaelflisar.composestyledcomponents.ui.components.StyledInputDefaults
import com.michaelflisar.composestyledcomponents.ui.components.StyledAlternativeInput
import com.michaelflisar.composestyledcomponents.ui.components.StyledOptionalInput
import com.michaelflisar.composestyledcomponents.ui.components.StyledTertiaryIconButton
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle
import com.michaelflisar.demo.helper.ExpandState

@Composable
internal fun DemoInputs(baseId: Int, expandState: ExpandState) {
    val name = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val notes = remember { mutableStateOf("") }
    val custom = remember { mutableStateOf("") }
    val age = remember { mutableStateOf(28) }
    val price = remember { mutableStateOf(12.5) }
    val optionalRating = remember { mutableStateOf<Double?>(null) }
    val optionalQuantity = remember { mutableStateOf<Int?>(null) }
    val numericName = remember { mutableStateOf(42) }
    
    val modifier = Modifier
    
    RegionContainer(
        title = "Inputs",
        id = baseId + 1,
        expanded = expandState
    ) {
        SubTitle("Default Inputs")
        listOf(false, true).forEach { alternative ->
            val value = remember { mutableStateOf("") }
            if (alternative) {
                StyledAlternativeInput(
                    value = value.value,
                    onValueChange = { value.value = it },
                    modifier = modifier,
                    label = { StyledText("Alternative") },
                    placeholder = { StyledText("Enter text") },
                )
            } else {
                StyledInput(
                    value = value.value,
                    onValueChange = { value.value = it },
                    modifier = modifier,
                    label = { StyledText("Default") },
                    placeholder = { StyledText("Enter text") },
                )
            }
        }
        SubTitle("Input with action buttons")
        StyledInput(
            value = name.value,
            onValueChange = { name.value = it },
            modifier = modifier,
            leadingIcon = {
                StyledTertiaryIconButton(onClick = {}) {
                    StyledIcon(icon = Icons.Default.Check.toIconComposable(), contentDescription = "Leading action")
                }
            },
            trailingIcon = {
                StyledTertiaryIconButton(onClick = {}) {
                    StyledIcon(icon = Icons.Default.Check.toIconComposable(), contentDescription = "Trailing action")
                }
            },
        )
        SubTitle("Disabled Inputs")
        listOf(false, true).forEach { alternative ->
            if (alternative) {
                StyledAlternativeInput(
                    value = "Disabled input",
                    onValueChange = {},
                    modifier = modifier,
                    enabled = false,
                    label = { StyledText("Alternative") },
                )
            } else {
                StyledInput(
                    value = "Disabled input",
                    onValueChange = {},
                    modifier = modifier,
                    enabled = false,
                    label = { StyledText("Default") },
                )
            }
        }
        SubTitle("Inputs with Customization")
        StyledAlternativeInput(
            value = custom.value,
            onValueChange = { custom.value = it },
            modifier = modifier,
            shape = StyledTheme.shapes.large,
            customization = StyledInputDefaults.customize(CustomColors.PrimarySurface),
            label = { StyledText("Custom shape and colors") },
            trailingIcon = { StyledIcon(icon = Icons.Default.Check.toIconComposable(), contentDescription = null) },
        )
        SubTitle("Input Features")
        val invalidName = name.value.isNotEmpty() && name.value.length < 3
        StyledAlternativeInput(
            value = name.value,
            onValueChange = { name.value = it },
            modifier = modifier,
            label = { StyledText("Name") },
            placeholder = { StyledText("At least 3 characters") },
            leadingIcon = { StyledIcon(icon = Icons.Default.Check.toIconComposable(), contentDescription = null) },
            isError = invalidName,
            errorMessage = if (invalidName) "Enter at least 3 characters" else null,
            supportingText = {
                StyledText(if (invalidName) "Enter at least 3 characters" else "Your display name")
            },
        )
        StyledInput(
            value = password.value,
            onValueChange = { password.value = it },
            modifier = modifier,
            label = { StyledText("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        )
        StyledInput(
            value = notes.value,
            onValueChange = { notes.value = it },
            modifier = modifier,
            label = { StyledText("Notes") },
            singleLine = false,
            minLines = 3,
            maxLines = 5,
        )
        SubTitle("Numeric Inputs")
        StyledInput(
            value = numericName.value,
            onValueChange = { numericName.value = it ?: 0 },
            modifier = modifier,
            label = { StyledText("Controlled integer") },
            supportingText = { StyledText("Current value: ${numericName.value}") },
        )
        StyledInput(
            value = age,
            modifier = modifier,
            label = { StyledText("Integer state") },
            supportingText = { StyledText("Current value: ${age.value}") },
        )
        StyledInput(
            value = price.value,
            onValueChange = { price.value = it ?: 0.0 },
            modifier = modifier,
            label = { StyledText("Controlled decimal") },
            supportingText = { StyledText("Current value: ${price.value}") },
        )
        StyledOptionalInput(
            value = optionalRating.value,
            sample = 0.0,
            onValueChange = { optionalRating.value = it },
            modifier = modifier,
            label = { StyledText("Nullable decimal") },
            placeholder = { StyledText("Optional rating") },
            supportingText = { StyledText("Current value: ${optionalRating.value ?: "not set"}") },
        )
        StyledOptionalInput(
            value = optionalQuantity,
            sample = 0,
            modifier = modifier,
            label = { StyledText("Nullable integer state") },
            placeholder = { StyledText("Optional quantity") },
            supportingText = { StyledText("Current value: ${optionalQuantity.value ?: "not set"}") },
        )
    }
}
