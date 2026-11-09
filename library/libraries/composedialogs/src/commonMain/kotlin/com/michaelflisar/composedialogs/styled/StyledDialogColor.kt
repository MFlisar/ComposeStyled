package com.michaelflisar.composedialogs.styled

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.ExperimentalComposeUiApi
import com.michaelflisar.composedialogs.dialogs.color.ColorPicker
import com.michaelflisar.composedialogs.dialogs.color.ColorPickerDefaults
import com.michaelflisar.composedialogs.dialogs.color.ColorPickerLabelStyle
import com.michaelflisar.composedialogs.dialogs.color.ColorPickerTexts
import com.michaelflisar.composedialogs.styled.provider.StyledColorUIProvider
import com.michaelflisar.composedialogs.styled.shared.composeDialog
import com.michaelflisar.composestyled.isLandscape
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.layout.StyledDialogDefaults
import com.michaelflisar.composestyled.ui.state.DialogState

/** Shows the ComposeDialogs color picker in a Styled dialog. */
@Composable
fun StyledDialogColor(
    state: DialogState,
    color: MutableState<Color>,
    modifier: Modifier = Modifier,
    texts: ColorPickerTexts = ColorPickerDefaults.texts(),
    alphaSupported: Boolean = true,
    directEditSupported: Boolean = true,
    shape: Shape = StyledTheme.shapes.small,
    gridSize: Int = if (isLandscape) 6 else 4,
    labelStyle: ColorPickerLabelStyle = ColorPickerLabelStyle.Value,
    style: StyledDialog.Style = StyledDialogDefaults.style,
    properties: StyledDialog.Properties = StyledDialog.Properties(),
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    buttons: StyledDialog.Buttons = StyledDialog.Buttons(),
    onDismissRequest: () -> Unit = state::dismiss,
) {
    val landscape = isLandscape
    val selectedSubColor = remember { mutableStateOf<Color?>(null) }
    val selectedPresetsLevel = rememberSaveable { mutableIntStateOf(0) }
    val selectedMainColor = remember(color.value) {
        mutableIntStateOf(nearestPresetGroup(color.value))
    }
    val selectedAlpha = rememberSaveable { mutableFloatStateOf(color.value.alpha) }
    val customPage = rememberSaveable { mutableStateOf(false) }
    val updateSelectedPresetColors = {
        selectedMainColor.intValue = nearestPresetGroup(color.value)
        selectedSubColor.value = null
        selectedPresetsLevel.intValue = 0
    }

    composeDialog(
        state = state,
        modifier = modifier,
        style = style,
        properties = properties,
        title = title,
        icon = icon,
        buttons = buttons,
        onDismissRequest = onDismissRequest,
    ) {
        if (state.visible) {
            ColorPicker(
                uiProvider = StyledColorUIProvider,
                color = color,
                texts = texts,
                alphaSupported = alphaSupported,
                directEditSupported = directEditSupported,
                shape = shape,
                gridSize = gridSize,
                labelStyle = labelStyle,
                landscape = landscape,
                customPage = customPage.value,
                selectedSubColor = selectedSubColor,
                selectedPresetsLevel = selectedPresetsLevel,
                selectedAlpha = selectedAlpha,
                mainColors = defaultPresetGroups.map { it[5] },
                mainLabels = null,
                subColors = defaultPresetGroups[selectedMainColor.intValue],
                subLabels = null,
                onMainColorSelected = {
                    selectedMainColor.intValue = it
                    selectedPresetsLevel.intValue = 1
                },
                onSubColorSelected = { index ->
                    selectedSubColor.value = defaultPresetGroups[selectedMainColor.intValue][index]
                    color.value = selectedSubColor.value!!.copy(alpha = selectedAlpha.floatValue)
                },
                onPresetsClick = { customPage.value = false },
                onCustomClick = { customPage.value = true },
                updateSelectedPresetColors = updateSelectedPresetColors,
            )
        }
    }
}

private val defaultPresetGroups = listOf(
    0xFFF44336, 0xFFE91E63, 0xFF9C27B0, 0xFF673AB7, 0xFF3F51B5,
    0xFF2196F3, 0xFF03A9F4, 0xFF00BCD4, 0xFF009688, 0xFF4CAF50,
    0xFF8BC34A, 0xFFCDDC39, 0xFFFFEB3B, 0xFFFFC107, 0xFFFF9800,
    0xFFFF5722, 0xFF795548, 0xFF9E9E9E, 0xFF607D8B, 0xFF424242,
).map { colorShades(Color(it)) }

private fun colorShades(color: Color): List<Color> = (0..10).map { index ->
    when {
        index < 5 -> blend(Color.White, color, index / 5f)
        index > 5 -> blend(color, Color.Black, (index - 5) / 5f)
        else -> color
    }
}

private fun blend(start: Color, end: Color, fraction: Float) = Color(
    red = start.red + (end.red - start.red) * fraction,
    green = start.green + (end.green - start.green) * fraction,
    blue = start.blue + (end.blue - start.blue) * fraction,
    alpha = 1f,
)

private fun nearestPresetGroup(color: Color): Int {
    val opaqueColor = color.copy(alpha = 1f)
    return defaultPresetGroups.indices.minByOrNull { index ->
        val candidate = defaultPresetGroups[index][5]
        kotlin.math.abs(opaqueColor.red - candidate.red) +
            kotlin.math.abs(opaqueColor.green - candidate.green) +
            kotlin.math.abs(opaqueColor.blue - candidate.blue)
    } ?: 0
}
