package com.michaelflisar.composechangelog

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString

class ChangelogSetup internal constructor(
    releases: List<ChangelogRelease>,
    internal val filter: (ChangelogRelease) -> Boolean,
    val textFormatter: @Composable (String) -> AnnotatedString,
) {

    private val sourceReleases = releases.toList().also { source ->
        require(source.isNotEmpty()) {
            "At least one release is required to determine the current app version."
        }
        require(source.map { it.version.code }.distinct().size == source.size) {
            "Release version codes must be unique."
        }
    }

    val releases: List<ChangelogRelease> = sourceReleases

    val appVersion: ChangelogVersion = sourceReleases.maxBy { it.version.code }.version

    private val versionNames = sourceReleases.associate { it.version.code to it.version.name }

    /** Returns the display name of a release with [versionCode]. */
    fun versionName(versionCode: Int): String {
        require(versionCode >= 0) { "Version code must not be negative." }
        return requireNotNull(versionNames[versionCode]) {
            "No changelog release exists for version code $versionCode."
        }
    }

    val releaseCodes: Set<Int> by lazy {
        sourceReleases.filter(filter).map { it.version.code }.toSet()
    }
}

object ChangelogDefaults {

    /** Formats supported text tags and resolves named color tags from [color]. */
    fun textFormatter(
        color: Map<String, @Composable () -> Color> = emptyMap(),
    ): @Composable (String) -> AnnotatedString = { text ->
        val resolvedColors = mutableMapOf<String, Color>()
        for ((name, colorProvider) in color) {
            resolvedColors[name] = colorProvider()
        }
        formatChangelogText(text, resolvedColors)
    }

    /**
     * Stores releases and prepares their filtered, sorted content when displayed.
     *
     * @param releases Kotlin-defined releases
     * @param filter excludes releases when visible content is prepared
     * @param textFormatter formats entry text as an annotated string
     */
    fun setup(
        releases: List<ChangelogRelease>,
        filter: (ChangelogRelease) -> Boolean = { true },
        textFormatter: @Composable (String) -> AnnotatedString = textFormatter(),
    ): ChangelogSetup {
        return ChangelogSetup(
            releases = releases,
            filter = filter,
            textFormatter = textFormatter,
        )
    }
}
