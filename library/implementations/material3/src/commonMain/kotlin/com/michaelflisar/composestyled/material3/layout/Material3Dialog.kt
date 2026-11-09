package com.michaelflisar.composestyled.material3.layout

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.michaelflisar.composestyled.foundation.layout.FoundationDialog
import com.michaelflisar.composestyled.foundation.layout.FoundationDialogBounds
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.internal.dialogInput

@OptIn(InternalComposeStyledApi::class)
class Material3Dialog internal constructor(
    override val presentationMapping: PresentationMapping,
) : FoundationDialog() {

    override val defaults: StyledDialog.Defaults
        @Composable get() = StyledDialog.Defaults(
            shape = MaterialTheme.shapes.extraLarge,
            sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            maxWidth = 560.dp,
            edgeMargin = if (StyledTheme.platform.isDesktopOrWeb()) 24.dp else 16.dp,
        )

    override val verticalAlertActions = false
    override val fullscreenActionsInToolbar = true
    override val alertPadding
        @Composable get() = 24.dp
    override val alertTitleSpacing
        @Composable get() = 16.dp
    override val alertActionSpacing
        @Composable get() = 24.dp
    override val actionSpacing
        @Composable get() = 8.dp
    override val actionRowSpacing
        @Composable get() = 12.dp
    override val stackPositiveActionFirst = true
    override val alertTitleStyle
        @Composable get() = MaterialTheme.typography.headlineSmall
    override val containerColor
        @Composable get() = MaterialTheme.colorScheme.surfaceContainerHigh

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(
            presentationMapping: PresentationMapping = DefaultPresentationMapping,
        ): Material3Dialog = remember(presentationMapping) { Material3Dialog(presentationMapping) }

        // -------------
        // 2) mapping
        // -------------

        /** Resolves Auto for actual toolbar actions only; missing icons fall back to text. */
        typealias PresentationMapping = (StyledDialog.ActionSlot) -> StyledDialog.ButtonPresentation

        val DefaultPresentationMapping: PresentationMapping = { slot ->
            if (slot == StyledDialog.ActionSlot.Negative) StyledDialog.ButtonPresentation.Icon
            else StyledDialog.ButtonPresentation.Text
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    override fun Modal(
        onDismissRequest: () -> Unit,
        modifier: Modifier,
        shape: Shape,
        maxWidth: Dp,
        maxHeightFraction: Float,
        edgeMargin: Dp,
        properties: StyledDialog.Properties,
        title: (@Composable () -> Unit)?,
        buttons: StyledDialog.Buttons,
        content: @Composable ColumnScope.(PaddingValues) -> Unit,
    ) {
        BasicAlertDialog(
            onDismissRequest = onDismissRequest,
            modifier = Modifier.fillMaxSize(),
            properties = DialogProperties(
                dismissOnBackPress = properties.dismissOnBackPress,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false,
            ),
        ) {
            FoundationDialogBounds(
                maxWidth, maxHeightFraction, edgeMargin, properties, onDismissRequest,
            ) { bounds ->
                Alert(
                    bounds.then(modifier).dialogInput(properties.dismissOnBackPress, onDismissRequest),
                    shape, title, buttons, content,
                )
            }
        }
    }
}
