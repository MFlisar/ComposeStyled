package com.michaelflisar.demo.demos

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.classes.toIconComposable
import com.michaelflisar.composestyledcomponents.ui.components.StyledButton
import com.michaelflisar.composestyledcomponents.ui.components.StyledIcon
import com.michaelflisar.composestyledcomponents.ui.components.StyledPopup
import com.michaelflisar.composestyledcomponents.ui.layout.StyledSpacedColumn
import com.michaelflisar.composestyledcomponents.ui.overlay.StyledPopupContainer
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText
import com.michaelflisar.demo.helper.ExpandState
import com.michaelflisar.demo.helper.RegionContainer

@Composable
internal fun DemoPopup(baseId: Int, expandState: ExpandState) {

    RegionContainer(
        title = "Info Popup",
        id = baseId + 1,
        expanded = expandState,
    ) {
        val expanded = remember { mutableStateOf(false) }
        StyledPopupContainer(
            popup = {
                StyledPopup(
                    visible = expanded,
                    content = {
                        StyledSpacedColumn(
                            modifier = Modifier.padding(StyledTheme.padding.medium),
                        ) {
                            StyledText("Additional information", style = StyledTheme.typography.labelSmall)
                            StyledText(
                                text = "This popup provides a short explanation without leaving the current screen.",
                                style = StyledTheme.typography.bodyMedium,
                            )
                        }
                    },
                )
            },
            content = {
                StyledButton(onClick = { expanded.value = true }) {
                    StyledText("About this option")
                }
            }
        )
    }
    RegionContainer(
        title = "Popup",
        id = baseId + 2,
        expanded = expandState,
    ) {
        val expanded1 = remember { mutableStateOf(false) }
        val expanded2 = remember { mutableStateOf(false) }

        StyledPopupContainer(
            popup = {
                StyledPopup(
                    visible = expanded1,
                    content = {
                        StyledSpacedColumn(
                            modifier = Modifier.padding(StyledTheme.padding.medium),
                        ) {
                            StyledText("Some text")
                            StyledIcon(
                                Icons.Default.Info.toIconComposable()
                            )
                            StyledPopupContainer(
                                popup = {
                                    StyledPopup(
                                        visible = expanded2,
                                        content = {
                                            StyledText(
                                                modifier = Modifier.padding(StyledTheme.padding.medium),
                                                text = "Nested popup"
                                            )
                                        },
                                    )
                                },
                                content = {
                                    StyledButton(
                                        onClick = { expanded2.value = true }
                                    ) {
                                        StyledText("Open nested popup")
                                    }
                                }
                            )
                        }
                    },
                )
            },
            content = {
                StyledButton(onClick = { expanded1.value = true }) {
                    StyledText("About this option")
                }
            }
        )
    }
}
