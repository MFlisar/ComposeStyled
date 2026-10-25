package com.michaelflisar.composestyledcomponents.ui.specials

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.layout.StyledRow
import com.michaelflisar.composestyledcomponents.ui.layout.StyledSpacedColumn
import com.michaelflisar.composestyledcomponents.ui.layout.StyledSpacedRow
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText

object StyledInformationRowDefaults {

    @Composable
    fun labelStyle(): TextStyle =
        StyledTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)

    @Composable
    fun infoStyle(): TextStyle =
        StyledTheme.typography.bodyMedium
}

@Composable
fun StyledInformationRow(
    label: String,
    info: String,
    color: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    backgroundShape: Shape? = null,
    labelStyle: TextStyle = StyledInformationRowDefaults.labelStyle(),
    infoStyle: TextStyle = StyledInformationRowDefaults.infoStyle(),
    modifier: Modifier = Modifier,
) {
    StyledInformationRow(
        label = label,
        backgroundColor = backgroundColor,
        backgroundShape = backgroundShape,
        labelStyle = labelStyle,
        modifier = modifier,
        content = { infoModifier ->
            StyledText(
                modifier = infoModifier,
                text = info,
                style = infoStyle,
                color = color,
            )

        }
    )
}

@Composable
fun StyledInformationRow(
    label: String,
    backgroundColor: Color = Color.Unspecified,
    backgroundShape: Shape? = null,
    labelStyle: TextStyle = StyledInformationRowDefaults.labelStyle(),
    modifier: Modifier = Modifier,
    content: @Composable (modifier: Modifier) -> Unit,
) {
    StyledSpacedColumn(
        modifier = modifier,
        itemSpacing = StyledTheme.spacing.medium,
    ) {
        InfoRowContent(
            label = label,
            labelStyle = labelStyle,
            backgroundColor = backgroundColor,
            backgroundShape = backgroundShape,
            content = content,
        )
    }
}

@Composable
fun StyledInformationRowHorizontal(
    label: String,
    info: String,
    modifier: Modifier = Modifier,
    labelWidth: Dp? = null,
    infoWidth: Dp? = null,
    color: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    backgroundShape: Shape? = null,
    labelStyle: TextStyle = StyledInformationRowDefaults.labelStyle(),
    infoStyle: TextStyle = StyledInformationRowDefaults.infoStyle(),
    endContent: @Composable RowScope.() -> Unit = {},
) {
    StyledInformationRowHorizontal(
        label = label,
        labelWidth = labelWidth,
        infoWidth = infoWidth,
        backgroundColor = backgroundColor,
        backgroundShape = backgroundShape,
        labelStyle = labelStyle,
        modifier = modifier,
    ) { infoModifier ->
        StyledRow(
            modifier = infoModifier,
            horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StyledText(
                modifier = Modifier.weight(1f, fill = false),
                text = info,
                style = infoStyle,
                color = color,
            )
            endContent()
        }
    }
}

@Composable
fun StyledInformationRowHorizontal(
    label: String,
    labelWidth: Dp? = null,
    infoWidth: Dp? = null,
    backgroundColor: Color = Color.Unspecified,
    backgroundShape: Shape? = null,
    labelStyle: TextStyle = StyledInformationRowDefaults.labelStyle(),
    modifier: Modifier = Modifier,
    content: @Composable (modifier: Modifier) -> Unit,
) {
    StyledSpacedRow(
        modifier = modifier,
        itemSpacing = StyledTheme.spacing.medium,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        InfoRowContent(
            label = label,
            labelStyle = labelStyle,
            labelWidth = labelWidth,
            infoWidth = infoWidth,
            backgroundColor = backgroundColor,
            backgroundShape = backgroundShape,
            content = content,
        )
    }
}

@Composable
private fun InfoRowContent(
    label: String,
    labelStyle: TextStyle,
    labelWidth: Dp? = null,
    infoWidth: Dp? = null,
    backgroundColor: Color = Color.Unspecified,
    backgroundShape: Shape? = null,
    content: @Composable (modifier: Modifier) -> Unit,
) {
    StyledText(
        modifier = Modifier.then(
            if (labelWidth != null) Modifier.width(labelWidth) else Modifier
        ),
        text = label,
        style = labelStyle,
    )

    content(
        Modifier
            .then(
                if (backgroundColor != Color.Unspecified) {
                    Modifier
                        .background(
                            color = backgroundColor,
                            shape = backgroundShape ?: RectangleShape,
                        )
                        .padding(StyledTheme.padding.small)
                } else {
                    Modifier
                }
            )
            .then(
                if (infoWidth != null) Modifier.width(infoWidth) else Modifier
            )
    )
}