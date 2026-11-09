package com.michaelflisar.composepreferences.styled

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

internal val LocalStyledPreferenceStrings =
    staticCompositionLocalOf { StyledPreferenceStrings.ENGLISH }

@Immutable
data class StyledPreferenceStrings(
    val settings: String,
    val changelog: String,
    val proVersion: String,
    val close: String,
    val preferences: String,
    val save: String,
    val cancel: String,
    val filterOptions: String,
    val edit: String,
    val apply: String,
    val searchingEverywhere: String,
    val filtered: String,
) {
    companion object {
        val ENGLISH = StyledPreferenceStrings(
            settings = "Settings",
            changelog = "Changelog",
            proVersion = "Pro Version",
            close = "Close",
            preferences = "Preferences",
            save = "Save",
            cancel = "Cancel",
            filterOptions = "Filter options",
            edit = "Edit",
            apply = "Apply",
            searchingEverywhere = "Search everywhere",
            filtered = "Filtered",
        )

        val GERMAN = StyledPreferenceStrings(
            settings = "Einstellungen",
            changelog = "Änderungsprotokoll",
            proVersion = "Pro-Version",
            close = "Schließen",
            preferences = "Einstellungen",
            save = "Speichern",
            cancel = "Abbrechen",
            filterOptions = "Optionen filtern",
            edit = "Bearbeiten",
            apply = "Übernehmen",
            searchingEverywhere = "Überall suchen",
            filtered = "Gefiltert",
        )
    }
}