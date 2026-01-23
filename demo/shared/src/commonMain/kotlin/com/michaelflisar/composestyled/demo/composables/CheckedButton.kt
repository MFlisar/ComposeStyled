package com.michaelflisar.composestyled.demo.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.components.StyledButtonDefaults
import com.michaelflisar.composestyled.core.components.StyledCheckbox
import com.michaelflisar.composestyled.core.components.StyledIcon
import com.michaelflisar.composestyled.core.components.StyledText

@Composable
fun CheckedImageButton(
    imageVector: ImageVector,
    checked: Boolean,
    onClick: () -> Unit,
) {
    StyledButton(
        onClick = onClick,
        variant = if (checked)  StyledButton.Variant.Prominent else StyledButton.Variant.Normal
    ) {
        StyledIcon(
            imageVector = imageVector,
            contentDescription = null
        )
    }
}

@Composable
fun CheckedTextButton(
    text: String,
    checked: Boolean,
    onClick: () -> Unit,
) {
    StyledButton(
        onClick = onClick,
        variant = if (checked)  StyledButton.Variant.Prominent else StyledButton.Variant.Normal
    ) {
        StyledText(text = text)
    }
}

@Composable
fun CheckedButton(
    text: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    variant: StyledButton.Variant = StyledButton.Variant.Text,
    onClick: () -> Unit,
) {
    StyledButton(
        modifier = modifier,
        variant = variant,
        //enabled = mode.value != theme,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.heightIn(min = StyledButtonDefaults.minimumHeight()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StyledText(
                text = text,
                modifier = Modifier.weight(1f)
            )
            AnimatedVisibility(
                visible = checked,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                StyledCheckbox(
                    checked = true,
                    onCheckedChange = { }
                )
            }
        }
    }
}