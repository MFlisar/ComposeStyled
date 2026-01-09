package com.michaelflisar.composestyled.demo.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.components.StyledCard
import com.michaelflisar.composestyled.core.components.StyledText

@Composable
fun CardsScreen() {
    Column(
        modifier = Modifier.Companion
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(StyledTheme.spacings.medium)
    ) {
        StyledText("Cards")
        StyledCard(
            modifier = Modifier.Companion.fillMaxWidth(),
            outlined = false
        ) {
            Column(
                modifier = Modifier.Companion
                    .padding(StyledTheme.paddings.medium),
                verticalArrangement = Arrangement.spacedBy(StyledTheme.spacings.medium)
            ) {
                StyledText("Filled Card")
                StyledText("This card uses StyledCard (Material3 adapter).")
                Row(
                    horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacings.medium)
                ) {
                    StyledButton(
                        onClick = { },
                        variant = StyledButton.Variant.FilledPrimary
                    ) { StyledText("Action") }
                    StyledButton(
                        onClick = { },
                        variant = StyledButton.Variant.Text
                    ) { StyledText("Secondary") }
                }
            }
        }
        StyledCard(
            modifier = Modifier.Companion.fillMaxWidth(),
            outlined = true
        ) {
            Column(
                modifier = Modifier.Companion.padding(StyledTheme.paddings.medium),
                verticalArrangement = Arrangement.spacedBy(StyledTheme.spacings.medium)
            ) {
                StyledText("Outlined Card")
                StyledText("Outlined variant for low emphasis surfaces.")
            }
        }
    }
}