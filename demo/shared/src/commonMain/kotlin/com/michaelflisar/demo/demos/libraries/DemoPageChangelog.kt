package com.michaelflisar.demo.demos.libraries

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.michaelflisar.composechangelog.ChangelogDefaults
import com.michaelflisar.composechangelog.changelogReleases
import com.michaelflisar.composechangelog.rememberChangelogState
import com.michaelflisar.composechangelog.ui.ChangelogDialog
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.CustomColors
import com.michaelflisar.composestyled.platform.SystemBarStyleEffectIfNeeded
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.layout.StyledScaffold
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.state.rememberDialogState
import com.michaelflisar.demo.helper.DemoTopBar

@Composable
internal fun DemoPageChangelog(onBack: () -> Unit) {
    val setup = remember {
        ChangelogDefaults.setup(
            textFormatter = ChangelogDefaults.textFormatter(
                color = mapOf(
                    "primary" to @Composable { StyledTheme.colorScheme.primary }
                )
            ),
            releases = changelogReleases {
                release(name = "1.2.0", code = 3, date = "2024-06-01") {
                    headerCard(
                        title = "IMPORTANT INFO",
                        colors = CustomColors.Primary,
                    ) {
                        item("Some info for you")
                        item("And some more info...")
                    }
                    added {
                        item(
                            "Added the <b>changelog screen</b> with <i>italic</i> and <u>underlined</u> text.",
                        )
                    }
                    changed {
                        item(
                            "Release <primary>categories</primary> and <color=primary>entries</color> follow their declaration order.",
                        )
                    }
                    security {
                        item("Security updates get their own category.")
                    }
                }
                release(name = "1.1.0", code = 2) {
                    fixed {
                        item("Corrected a layout issue.")
                    }
                    deprecated {
                        item("Marked an older setting as deprecated.")
                    }
                    removed {
                        item("Removed an obsolete option.")
                    }
                }
                release(name = "1.0.0", code = 1) {
                    added {
                        item("Introduced the first version.")
                    }
                }
            },
        )
    }

    val lastShownVersion = rememberSaveable { mutableIntStateOf(2) }
    val automaticState = rememberChangelogState(
        setup = setup,
        lastShownVersion = lastShownVersion.intValue,
        saveLastShownVersion = { lastShownVersion.intValue = it },
    )
    val manualState = rememberChangelogState()

    val showingAutomaticChangelog = remember { mutableStateOf(false) }

    val scaffoldColor = StyledTheme.colorScheme.surface
    val topBarColor = StyledTheme.colorScheme.primary
    SystemBarStyleEffectIfNeeded(
        statusBarColor = topBarColor,
        navigationBarColor = scaffoldColor,
        backgroundColor = scaffoldColor,
    )

    StyledScaffold(
        containerColor = scaffoldColor,
        topBar = {
            DemoTopBar(
                containerColor = topBarColor,
                title = "Changelog",
                onBack = onBack,
            )
        },
    ) { paddingValues ->
        StyledColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            itemSpacing = StyledTheme.spacing.medium,
        ) {
            StyledColumn(
                modifier = Modifier.padding(StyledTheme.padding.content),
                itemSpacing = StyledTheme.spacing.medium,
            ) {
                StyledText("Manual state")
                StyledButton(onClick = {
                    showingAutomaticChangelog.value = false
                    manualState.show()
                }) {
                    StyledText("Show all releases")
                }
                StyledButton(onClick = {
                    showingAutomaticChangelog.value = false
                    manualState.show(2)
                }) {
                    StyledText("Show from version ${setup.versionName(2)}")
                }
                StyledText("Automatic update state")
                StyledText(
                    if (automaticState.visible) "A new release is available."
                    else "No new release is available."
                )
                StyledButton(onClick = {
                    showingAutomaticChangelog.value = true
                }) {
                    StyledText("Show automatic changelog")
                }
                StyledText("Releases: ${setup.releases.joinToString { it.version.name }}")
                StyledText("lastShownVersion: ${lastShownVersion.value}")
                StyledText("setup.appVersion: ${setup.appVersion}")
            }
        }
    }


    ChangelogDialog(
        state = automaticState,
        setup = setup,
        title = { StyledText("Automatic changelog") },
        buttons = StyledDialog.Buttons(
            positive = StyledDialog.Button(
                label = "Close",
                onClick = {},
            )
        ),
    )
    ChangelogDialog(
        state = manualState,
        setup = setup,
        title = { StyledText("Manual changelog") },
        buttons = StyledDialog.Buttons(
            positive = StyledDialog.Button(
                label = "Close"
            )
        )
    )
}
