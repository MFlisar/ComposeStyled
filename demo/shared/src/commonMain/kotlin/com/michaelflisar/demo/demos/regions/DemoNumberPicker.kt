package com.michaelflisar.demo.demos.regions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledAlternativeNumberPicker
import com.michaelflisar.composestyled.ui.components.StyledNumberPicker
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.utils.number.ValidationResult
import com.michaelflisar.composestyled.utils.number.rememberNumberParser
import com.michaelflisar.composestyled.utils.number.rememberNumberValidator
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoNumberPicker(baseId: Int, expandState: ExpandState) {
    val quantity = rememberSaveable { mutableStateOf(5) }
    val buttonOnlyQuantity = rememberSaveable { mutableStateOf(5) }
    val decimal = rememberSaveable { mutableStateOf(2.5) }
    val integerParser = rememberNumberParser(sample = 0, stepSize = 1)
    val decimalParser = rememberNumberParser(sample = 0.0, stepSize = 0.25, commas = 2)
    val quantityValidation = remember {
        { candidate: Int? ->
            if (candidate == null || candidate !in 0..10) {
                ValidationResult.Invalid("Enter a value between 0 and 10")
            } else {
                ValidationResult.Valid
            }
        }
    }
    val decimalValidation = remember {
        { candidate: Double? ->
            if (candidate == null || candidate !in 0.0..5.0) {
                ValidationResult.Invalid("Enter a value between 0 and 5")
            } else {
                ValidationResult.Valid
            }
        }
    }
    val quantityValidator = rememberNumberValidator(quantity.value, quantityValidation)
    val buttonOnlyQuantityValidator = rememberNumberValidator(buttonOnlyQuantity.value, quantityValidation)
    val decimalValidator = rememberNumberValidator(decimal.value, decimalValidation)

    val modifier = Modifier

    RegionContainer(
        title = "Number Picker",
        id = baseId + 1,
        expanded = expandState,
    ) {
        StyledColumn(itemSpacing = StyledTheme.spacing.medium) {
            SubTitle("Integer 0–10, step 1; buttons wrap at bounds, manual input enabled")
            StyledNumberPicker(
                value = quantity,
                validator = quantityValidator,
                parser = integerParser,
                modifier = modifier,
                label = "Quantity",
                overrun = 0 to 10,
            )
            StyledText("Current value: ${quantity.value}")

            SubTitle("Integer 0–10, step 1; buttons wrap at bounds, manual input disabled")
            StyledNumberPicker(
                value = buttonOnlyQuantity,
                validator = buttonOnlyQuantityValidator,
                parser = integerParser,
                modifier = modifier,
                label = "Quantity (buttons only)",
                overrun = 0 to 10,
                allowManualInput = false,
            )
            StyledText("Current value: ${buttonOnlyQuantity.value}")

            SubTitle("Decimal 0–5, step 0.25; manual input enabled")
            StyledAlternativeNumberPicker(
                value = decimal,
                validator = decimalValidator,
                parser = decimalParser,
                modifier = modifier,
                label = "Amount",
            )
            StyledText("Current value: ${decimal.value}")

            SubTitle("Disabled")
            StyledNumberPicker(
                value = quantity,
                validator = quantityValidator,
                parser = integerParser,
                modifier = modifier,
                label = "Disabled",
                enabled = false,
            )
        }
    }
}
