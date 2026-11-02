package com.michaelflisar.demo.helper

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledCardDefaults
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.specials.StyledExpandableSection
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.composestyled.utils.registerScrollToTopTarget
import com.michaelflisar.composestyled.utils.rememberScrollToTopTarget
import kotlinx.coroutines.launch

@Composable
internal fun RegionContainer(
    title: String,
    id: Int,
    expanded: ExpandState,
    content: @Composable () -> Unit,
) {
    //val id = remember(expanded) { expanded.getNextId() }

    val stateHolder = rememberSaveableStateHolder()
    val scrollToTopRequester = LocalShowcaseScrollToTopRequester.current
    val scrollToTopTarget = rememberScrollToTopTarget(scrollToTopRequester)
    val scope = rememberCoroutineScope()
    val isExpanded = expanded.isExpanded(id)
    StyledExpandableSection(
        title = { StyledText(title) },
        modifier = Modifier.fillMaxWidth().registerScrollToTopTarget(scrollToTopTarget),
        expanded = isExpanded,
        onToggle = { expanded.toggle(id) },
        onExpansionAnimationFinished = { isExpanded ->
            if (isExpanded) {
                scope.launch { scrollToTopTarget.request() }
            }
        },
    ) {
        stateHolder.SaveableStateProvider(id.toString()) {
            StyledColumn(
                itemSpacing = StyledTheme.spacing.medium,
                modifier = Modifier.fillMaxWidth().padding(StyledCardDefaults.contentPadding)
            ) {
                content()
            }
        }
    }
}
