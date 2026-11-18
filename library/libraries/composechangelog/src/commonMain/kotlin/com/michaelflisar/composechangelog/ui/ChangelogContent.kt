package com.michaelflisar.composechangelog.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composechangelog.ChangelogGroup
import com.michaelflisar.composechangelog.ChangelogRelease
import com.michaelflisar.composechangelog.ChangelogSetup
import com.michaelflisar.composechangelog.ChangelogState
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledOutlinedCard
import com.michaelflisar.composestyled.ui.layout.StyledLazyColumn
import com.michaelflisar.composestyled.ui.primitives.StyledText

/** Renders visible changelog releases in a lazy, scrollable Styled list. */
@Composable
fun ChangelogContent(
    state: ChangelogState,
    setup: ChangelogSetup,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    val minimumVersion = state.minimumVisibleReleaseVersion
    val preparedReleases = remember(setup, minimumVersion) {
        lazy(LazyThreadSafetyMode.NONE) {
            setup.releases.asSequence()
                .filter { minimumVersion == null || it.version.code >= minimumVersion }
                .onEach { it.groups.size }
                .filter(setup.filter)
                .sortedByDescending { it.version.code }
                .toList()
        }
    }
    if (!state.visible)
        return
    val releases = preparedReleases.value
    val categoryTextStyle = StyledTheme.typography.labelSmall
    val categoryDotSize = with(LocalDensity.current) {
        categoryTextStyle.fontSize.toDp() * 0.55f
    }

    CompositionLocalProvider(LocalChangelogSetup provides setup) {
        StyledLazyColumn(
            modifier = modifier,
            itemSpacing = StyledTheme.spacing.medium,
            contentPadding = contentPadding
        ) {
            items(
                items = releases,
                key = { it.version.code },
            ) { release ->
                StyledOutlinedCard {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(StyledTheme.spacing.medium),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        // header mit farb kreisen + tags
                        ChangelogReleaseHeaderTags(release, categoryDotSize, categoryTextStyle)

                        // release header
                        ChangelogReleaseHeader(release)

                        // release gruppen
                        release.groups.forEach { group ->
                            when (group) {
                                is ChangelogGroup.Entries -> {
                                    ChangelogGroupHeader(group)
                                    group.items.forEach { text ->
                                        ChangelogEntryText(text)
                                    }
                                }

                                is ChangelogGroup.Custom -> group.content(this)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChangelogReleaseHeaderTags(
    release: ChangelogRelease,
    categoryDotSize: Dp,
    categoryTextStyle: TextStyle,
) {
    val categories = remember(release) {
        release.groups
            .filterIsInstance<ChangelogGroup.Entries>()
            .map { it.category }
            .distinct()
    }
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(StyledTheme.spacing.medium),
        modifier = Modifier.fillMaxWidth(),
    ) {
        categories.forEach { category ->
            val color = category.color(StyledTheme.colorScheme)
            Row(horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacing.medium)) {
                Box(
                    modifier = Modifier
                        .size(categoryDotSize)
                        .background(color, CircleShape),
                )
                StyledText(
                    text = category.label,
                    color = color,
                    style = categoryTextStyle,
                )
            }
        }
    }
}

@Composable
private fun ChangelogReleaseHeader(
    release: ChangelogRelease,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacing.medium),
        modifier = Modifier.fillMaxWidth(),
    ) {
        StyledText(
            text = release.version.name,
            fontWeight = FontWeight.Bold,
            style = StyledTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.weight(1f))
        if (release.date != null) {
            StyledText(
                text = release.date,
                color = StyledTheme.supportingContentColor(
                    containerColor = StyledTheme.colorScheme.background,
                ),
                fontWeight = FontWeight.Bold,
                style = StyledTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun ChangelogGroupHeader(group: ChangelogGroup.Entries) {
    StyledText(
        text = group.category.label,
        color = group.category.color(StyledTheme.colorScheme),
        fontWeight = FontWeight.SemiBold,
        style = StyledTheme.typography.titleSmall
    )
}

internal const val ChangelogBulletContentId = "changelog-bullet"

internal val LocalChangelogSetup = compositionLocalOf<ChangelogSetup> {
    error("ChangelogEntryText must be composed inside ChangelogContent.")
}