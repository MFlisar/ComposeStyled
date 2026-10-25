package com.michaelflisar.demo.demos

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelflisar.composestyledcomponents.ui.components.CircularProgressIndicator
import com.michaelflisar.composestyledcomponents.ui.components.LinearProgressIndicator
import com.michaelflisar.composestyledcomponents.ui.layout.StyledSpacedColumn
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText
import com.michaelflisar.demo.helper.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoProgressIndicators(baseId: Int, expandState: ExpandState) {
    RegionContainer(
        title = "Progress Indicators",
        id = baseId + 1,
        expanded = expandState,
    ) {
        StyledSpacedColumn {
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
