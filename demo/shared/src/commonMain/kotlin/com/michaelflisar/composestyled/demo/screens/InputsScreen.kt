package com.michaelflisar.composestyled.demo.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.core.components.StyledText
import com.michaelflisar.composestyled.core.components.StyledTextField

@Composable
fun InputsScreen() {
    var text by remember { mutableStateOf("") }
    var text2 by remember { mutableStateOf("") }
    var roText by remember { mutableStateOf("Read-only value") }

    Column(
        modifier = Modifier.Companion
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StyledText("Inputs")
        StyledTextField(
            value = text,
            onValueChange = { text = it },
            variant = StyledTextField.Variant.Filled,
            placeholder = { StyledText("Filled") }
        )
        StyledTextField(
            value = text2,
            onValueChange = { text2 = it },
            variant = StyledTextField.Variant.Outlined,
            placeholder = { StyledText("Outlined") }
        )
        StyledTextField(
            value = "Error text",
            onValueChange = { },
            isError = true,
            variant = StyledTextField.Variant.Outlined,
            placeholder = { StyledText("Error") }
        )
        StyledTextField(
            value = roText,
            onValueChange = { roText = it },
            readOnly = true,
            variant = StyledTextField.Variant.Filled,
            placeholder = { StyledText("Read-only") }
        )
        StyledTextField(
            value = "Disabled",
            onValueChange = { },
            enabled = false,
            variant = StyledTextField.Variant.Filled,
            placeholder = { StyledText("Disabled") }
        )
    }
}