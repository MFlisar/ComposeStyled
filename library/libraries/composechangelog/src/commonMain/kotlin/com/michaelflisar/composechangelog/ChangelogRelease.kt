package com.michaelflisar.composechangelog

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composechangelog.ui.ChangelogEntryText
import com.michaelflisar.composechangelog.ui.ChangelogHeaderCard
import com.michaelflisar.composestyled.classes.CustomColors
import com.michaelflisar.composestyled.tokens.StyledColorScheme

/** A release whose DSL groups are materialized the first time [groups] is accessed. */
data class ChangelogRelease(
    val version: ChangelogVersion,
    internal val groups: List<ChangelogGroup>,
    val date: String? = null,
)

private class DeferredList<T>(
    content: () -> List<T>,
) : AbstractList<T>() {
    private val values by lazy(content)

    override val size: Int
        get() = values.size

    override fun get(index: Int): T = values[index]
}

sealed interface ChangelogGroup {
    data class Entries(
        val category: ChangelogCategory,
        val items: List<String>,
    ) : ChangelogGroup

    data class Custom(
        val content: @Composable ColumnScope.() -> Unit,
    ) : ChangelogGroup
}

enum class ChangelogCategory(
    val label: String,
    val darkThemeColor: Color,
    val lightThemeColor: Color,
) {
    Added("Added", Color(0xFFA1D18A), Color(0xFF386A20)),
    Changed("Changed", Color(0xFFAAC7FF), Color(0xFF415F91)),
    Deprecated("Deprecated", Color(0xFFE9C349), Color(0xFF775A00)),
    Removed("Removed", Color(0xFFFFB4AB), Color(0xFF904A46)),
    Fixed("Fixed", Color(0xFF4CD7C5), Color(0xFF006A60)),
    Security("Security", Color(0xFFF2B8B5), Color(0xFFB3261E));

    fun color(colorScheme: StyledColorScheme): Color =
        if (colorScheme.isDarkTheme) darkThemeColor else lightThemeColor
}

@DslMarker
annotation class ChangelogDsl

@ChangelogDsl
class ChangelogReleasesBuilder internal constructor() {
    private val releases = mutableListOf<ChangelogRelease>()

    /** Adds a release using its display name and numeric version code. */
    fun release(
        name: String,
        code: Int,
        date: String? = null,
        content: ChangelogReleaseBuilder.() -> Unit,
    ) = release(ChangelogVersion(name, code), date, content)

    private fun release(
        version: ChangelogVersion,
        date: String?,
        content: ChangelogReleaseBuilder.() -> Unit,
    ) {
        releases += ChangelogRelease(
            version = version,
            groups = DeferredList {
                ChangelogReleaseBuilder().apply(content).build()
            },
            date = date,
        )
    }

    internal fun build(): List<ChangelogRelease> = releases.toList()
}

@ChangelogDsl
class ChangelogReleaseBuilder internal constructor() {
    private val groups = mutableListOf<ChangelogGroup>()

    fun added(content: ChangelogEntryGroupBuilder.() -> Unit) =
        addEntries(ChangelogCategory.Added, content)

    fun changed(content: ChangelogEntryGroupBuilder.() -> Unit) =
        addEntries(ChangelogCategory.Changed, content)

    fun deprecated(content: ChangelogEntryGroupBuilder.() -> Unit) =
        addEntries(ChangelogCategory.Deprecated, content)

    fun removed(content: ChangelogEntryGroupBuilder.() -> Unit) =
        addEntries(ChangelogCategory.Removed, content)

    fun fixed(content: ChangelogEntryGroupBuilder.() -> Unit) =
        addEntries(ChangelogCategory.Fixed, content)

    fun security(content: ChangelogEntryGroupBuilder.() -> Unit) =
        addEntries(ChangelogCategory.Security, content)

    fun custom(content: @Composable ColumnScope.() -> Unit) {
        groups += ChangelogGroup.Custom(content)
    }

    /** Adds a header card whose entries use the changelog text formatter. */
    fun headerCard(
        title: String,
        colors: CustomColors? = null,
        content: ChangelogEntryGroupBuilder.() -> Unit,
    ) {
        val items = ChangelogEntryGroupBuilder().apply(content).build()
        custom {
            ChangelogHeaderCard(title = title, colors = colors) {
                items.forEach { ChangelogEntryText(it) }
            }
        }
    }

    internal fun build(): List<ChangelogGroup> = groups.toList()

    private fun addEntries(
        category: ChangelogCategory,
        content: ChangelogEntryGroupBuilder.() -> Unit,
    ) {
        val builder = ChangelogEntryGroupBuilder().apply(content)
        val items = builder.build()
        if (items.isNotEmpty()) {
            groups += ChangelogGroup.Entries(category, items)
        }
    }
}

@ChangelogDsl
class ChangelogEntryGroupBuilder internal constructor() {
    private val items = mutableListOf<String>()

    fun item(text: String) {
        items += text
    }

    internal fun build(): List<String> = items.toList()
}

fun changelogReleases(
    content: ChangelogReleasesBuilder.() -> Unit,
): List<ChangelogRelease> = ChangelogReleasesBuilder()
    .apply(content)
    .build()
