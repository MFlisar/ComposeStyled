package com.michaelflisar.composestyled.demo.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.components.layout.ContentColumn
import com.michaelflisar.composestyled.components.layout.ContentRow
import com.michaelflisar.composestyled.core.ProvideStyledTheme
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.colors.BaseColorDef
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.components.StyledIcon
import com.michaelflisar.composestyled.core.components.StyledText
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.LocalBackgroundColor
import com.michaelflisar.composestyled.core.runtime.LocalContentColor
import com.michaelflisar.composestyled.core.runtime.ProvideStyledLocals
import com.michaelflisar.composestyled.core.tokens.StyledColors
import com.michaelflisar.composestyled.demo.DemoThemes

private object Shapes {
    val r12 = RoundedCornerShape(12.dp)
    val r16 = RoundedCornerShape(16.dp)
}

@Composable
fun PreviewScheme(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    ContentColumn(
        modifier = modifier
    ) {
        // Title
        StyledText(
            text = title,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = StyledTheme.typography.titleMedium
        )

        // Schemes side-by-side
        ContentRow(Modifier.fillMaxWidth()) {
            PreviewSchemeColumn(DemoThemes.light, Modifier.weight(1f)) { content() }
            PreviewSchemeColumn(DemoThemes.dark, Modifier.weight(1f)) { content() }
        }
    }
}

@OptIn(InternalComposeStyledApi::class)
@Composable
private fun PreviewSchemeColumn(
    c: StyledColors,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    ProvideStyledTheme(colors = c) {
        ProvideStyledLocals(
            contentColor = c.onBackground,
            backgroundColor = c.background
        ) {
            ContentColumn(modifier = modifier) {
                content()
            }
        }
    }
}

@Composable
fun PreviewSwatch(
    label: String,
    bg: Color,
    fg: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier
            .height(48.dp)
            .background(bg, Shapes.r12)
            .border(1.dp, fg.copy(alpha = 0.12f), Shapes.r12),
        contentAlignment = Alignment.Center
    ) {
        StyledText(
            label,
            color = fg,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PreviewStateChip(
    label: String,
    base: BaseColorDef,
    modifier: Modifier = Modifier,
) {
    val bg = base.background.resolve()
    val fg = base.foreground.resolve()
    val br = base.border?.resolve()

    Row(
        modifier = modifier
            .height(48.dp)
            .background(bg, shape = Shapes.r12)
            .then(if (br != null) Modifier.border(1.dp, br, Shapes.r12) else Modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        StyledText(label, color = fg)
    }
}

@Composable
fun PreviewRegionCard(
    title: String,
    subtitle: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(LocalBackgroundColor.current, Shapes.r16)
            .border(1.dp, LocalContentColor.current, Shapes.r16)
            .padding(StyledTheme.paddings.medium),
        verticalArrangement = Arrangement.spacedBy(StyledTheme.spacings.medium)
    ) {
        StyledText(title, style = StyledTheme.typography.titleMedium)
        if (subtitle != null) {
            StyledText(subtitle)
        }
        content()
    }
}

@Composable
fun PreviewExpandableContent(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    val expanded = remember { mutableStateOf(false) }
    StyledButton(
        onClick = { expanded.value = !expanded.value }
    ) {
        ContentRow {
            StyledText(title, modifier = Modifier.weight(1f))
            val rot = animateFloatAsState(targetValue = if (expanded.value) 180f else 0f)
            StyledIcon(Icons.Default.ArrowDropDown, null, Modifier.rotate(rot.value))
        }
    }

    AnimatedVisibility(
        visible = expanded.value,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        ContentColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            content()
        }
    }
}