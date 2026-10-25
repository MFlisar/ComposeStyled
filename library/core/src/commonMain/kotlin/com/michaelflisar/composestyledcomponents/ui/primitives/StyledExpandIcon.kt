package com.michaelflisar.composestyledcomponents.ui.primitives

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.classes.IconComposable
import com.michaelflisar.composestyledcomponents.classes.toIconComposable
import com.michaelflisar.composestyledcomponents.tokens.LocalStyledContentColor
import com.michaelflisar.composestyledcomponents.ui.components.StyledIcon
import com.michaelflisar.composestyledcomponents.ui.icons.MaterialIconsArrowDropDown

@Composable
fun StyledExpandIcon(
    expanded: Boolean,
    modifier: Modifier = Modifier,
    sideIsLeft: Boolean = true,
    color: Color = LocalStyledContentColor.current,
    icon: IconComposable = MaterialIconsArrowDropDown.toIconComposable(),
    size: Dp? = StyledTheme.sizes.iconSize,
) {
    val rotation by animateFloatAsState(if (expanded) (if (sideIsLeft) -180f else 180f) else 0f)
    StyledIcon(
        modifier = modifier.rotate(rotation),
        icon = icon,
        tint = color,
        contentDescription = "expand",
        size = size
    )
}