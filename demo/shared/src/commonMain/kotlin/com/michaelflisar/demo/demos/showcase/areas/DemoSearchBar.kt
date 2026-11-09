package com.michaelflisar.demo.demos.showcase.areas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledPopupMenu
import com.michaelflisar.composestyled.ui.components.StyledSearchBar
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.utils.search.SearchUtil
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoSearchBar(baseId: Int, expandState: ExpandState) {
    val query = rememberSaveable { mutableStateOf("") }
    val submittedQuery = rememberSaveable { mutableStateOf("") }
    val suggestions = remember {
        listOf(
            "Buttons",
            "Dropdowns",
            "Inputs",
            "Search Bar",
            "Input Actions",
            "Numeric Inputs",
            "Sliders",
            "Switches",
            "Checkboxes",
            "Radio Buttons",
            "Dialogs",
            "Bottom Sheets",
            "Menus",
            "Tabs",
            "Toolbars",
            "App Bars",
            "Cards",
            "Lists",
            "Grids",
            "Images",
            "Text"
        )
    }
    val matches = remember(query.value) {
        SearchUtil.search(suggestions, query.value, text = { it })
    }
    val rankedQuery = rememberSaveable { mutableStateOf("") }
    val rankedMatches = remember(rankedQuery.value) {
        SearchUtil.search(suggestions, rankedQuery.value) { item, searchQuery ->
            when {
                item.equals(searchQuery, ignoreCase = true) -> 100
                item.startsWith(searchQuery, ignoreCase = true) -> 50
                item.contains(searchQuery, ignoreCase = true) -> 10
                item == "Inputs" && searchQuery.equals("text", ignoreCase = true) -> 5
                else -> null
            }
        }
    }

    RegionContainer(
        title = "Search Bar",
        id = baseId + 1,
        expanded = expandState,
    ) {
        SubTitle("Search with suggestions")
        StyledText("Select \"Inputs\": suggestions close; click the input or edit the query to open them.")
        StyledText("Typing filters while the popup stays open. Clear, outside clicks and Escape close it.")
        StyledSearchBar(
            value = query,
            placeholder = { StyledText("Search components") },
            onSearch = { submittedQuery.value = query.value },
            onClear = { submittedQuery.value = "" },
            suggestions = { selectSuggestion ->

                // header
                custom {
                    StyledText("${matches.size} matches")
                }

                // suggestions
                matches.forEach { suggestion ->
                    item(suggestion, onClick = { selectSuggestion(suggestion) })
                }

                // footer
                custom {
                    StyledText("Custom footer content", style = StyledTheme.typography.bodySmall)
                }

            },
        )
        if (submittedQuery.value.isNotEmpty()) {
            StyledText("Submitted: ${submittedQuery.value}")
        }
        SubTitle("Custom ranking and synonyms")
        StyledText("Search for \"text\" to match \"Inputs\" through a custom rule.")
        StyledSearchBar(
            value = rankedQuery,
            placeholder = { StyledText("Search with custom scoring") },
            popupHorizontalAlignment = StyledPopupMenu.HorizontalAlignment.End,
            suggestions = { selectSuggestion ->
                rankedMatches.forEach { suggestion ->
                    item(suggestion, onClick = { selectSuggestion(suggestion) })
                }
                header("Infos")
                custom(
                    content = {
                        StyledText(
                            "${rankedMatches.size} matches",
                            style = StyledTheme.typography.bodySmall
                        )
                    },
                )
            },
        )
    }
}
