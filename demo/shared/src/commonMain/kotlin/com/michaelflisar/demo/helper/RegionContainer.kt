package com.michaelflisar.demo.helper

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.michaelflisar.composestyledcomponents.ui.components.StyledCardDefaults
import com.michaelflisar.composestyledcomponents.ui.components.StyledExpandableSection
import com.michaelflisar.composestyledcomponents.ui.components.rememberStyledExpandableSectionStyle
import com.michaelflisar.composestyledcomponents.ui.layout.StyledSpacedColumn
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText

@Composable
internal fun RegionContainer(
    title: String,
    id: Int,
    expanded: ExpandState,
    content: @Composable () -> Unit,
) {
    //val id = remember(expanded) { expanded.getNextId() }

    StyledExpandableSection(
        title = { StyledText(title) },
        modifier = Modifier.fillMaxWidth(),
        expanded = expanded.isExpanded(id),
        onToggle = { expanded.toggle(id) }
    ) {
        StyledSpacedColumn(
            modifier = Modifier.fillMaxWidth().padding(StyledCardDefaults.contentPadding)
        ) {
            content()
        }
    }
}
