package com.michaelflisar.composestyled.material3.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.ui.components.StyledClickable

class Material3Clickable internal constructor() : StyledClickable.Implementation {

    override val defaults: StyledClickable.Defaults
        @Composable get() = StyledClickable.Defaults(
            shape = RoundedCornerShape(percent = 50),
            contentPadding = ButtonDefaults.TextButtonContentPadding,
        )

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember {
            Material3Clickable()
        }
    }

    @Composable
    override fun Render(
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        interactionSource: MutableInteractionSource?,
        shape: Shape,
        contentPadding: PaddingValues,
        content: @Composable RowScope.() -> Unit,
    ) {
        Row(
            modifier = modifier
                .heightIn(min = ButtonDefaults.MinHeight)
                .clip(shape)
                .clickable(
                    interactionSource = interactionSource,
                    enabled = enabled,
                    role = Role.Button,
                    onClick = onClick,
                )
                .padding(contentPadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}
