package com.michaelflisar.demo.demos.regions

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.CircularProgressIndicator
import com.michaelflisar.composestyled.ui.components.LinearProgressIndicator
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoProgressIndicators(baseId: Int, expandState: ExpandState) {
    RegionContainer(
        title = "Progress Indicators",
        id = baseId + 1,
        expanded = expandState,
    ) {
        StyledColumn(itemSpacing = StyledTheme.spacing.medium) {
            SubTitle("Circular")
            CircularProgressIndicator()
            CircularProgressIndicator(progress = 0.65f)

            SubTitle("Linear")
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            LinearProgressIndicator(
                progress = 0.65f,
                modifier = Modifier.fillMaxWidth(),
            )
            StyledText("Progress can be null (indeterminate) or between 0 and 1.")
        }
    }
}
