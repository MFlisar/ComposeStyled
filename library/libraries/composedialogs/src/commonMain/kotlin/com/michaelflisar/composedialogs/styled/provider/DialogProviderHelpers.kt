package com.michaelflisar.composedialogs.styled.provider

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.runtime.LocalStyledContext
import com.michaelflisar.composestyled.ui.components.StyledCard
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.primitives.StyledText

@Composable
internal fun LabeledDialogField(
    label: String?,
    modifier: Modifier,
    errorMessage: String?,
    labelTextAlign: TextAlign = TextAlign.Center,
    content: @Composable () -> Unit,
) {
    val fieldContent: @Composable (Modifier) -> Unit = { fieldModifier ->
        Column(
            modifier = fieldModifier
                .fillMaxWidth()
                .padding(
                    horizontal = StyledTheme.padding.medium,
                    vertical = StyledTheme.padding.small,
                ),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            StyledText(
                text = label ?: " ",
                modifier = Modifier.fillMaxWidth(),
                style = StyledTheme.typography.labelSmall,
                color = if (label == null) Color.Transparent else StyledTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                textAlign = labelTextAlign,
            )
            content()
            errorMessage?.let {
                StyledText(
                    text = it,
                    style = StyledTheme.typography.labelSmall,
                    color = StyledTheme.colorScheme.error,
                    maxLines = 2,
                )
            }
        }
    }

    if (label == null) {
        fieldContent(modifier)
    } else {
        StyledCard(
            modifier = modifier,
            applyContentPadding = false,
        ) {
            fieldContent(Modifier)
        }
    }
}

@OptIn(InternalComposeStyledApi::class)
@Composable
internal fun ProvideDialogActionContext(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalStyledContext provides StyledDialog.Context.DialogAction,
        content = content,
    )
}
