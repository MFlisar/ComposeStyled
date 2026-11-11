package com.michaelflisar.composepreferences.styled

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme

/** Colors shared by a preference row's title, icon and supporting text. */
@Immutable
@ConsistentCopyVisibility
data class PreferenceRowColors internal constructor(
    val containerColor: Color,
    /** Applies to the title, leading icon and trailing content. */
    val contentColor: Color,
    val supportingColor: Color,
)

/** Style for ordinary preference items. */
@Immutable
@ConsistentCopyVisibility
data class PreferenceItemStyle internal constructor(
    val colors: PreferenceRowColors,
    val shape: Shape,
    val contentPadding: PaddingValues,
    val minimumItemHeight: Dp,
    val titleTextStyle: TextStyle,
    val subtitleTextStyle: TextStyle,
)

/** Style for preference section headers and their grouped content. */
@Immutable
@ConsistentCopyVisibility
data class PreferenceSectionStyle internal constructor(
    val colors: PreferenceRowColors,
    val shape: Shape,
    val contentPadding: PaddingValues,
    /** Minimum height for non-expandable section headers. */
    val minimumItemHeight: Dp,
    val titleTextStyle: TextStyle,
    val subtitleTextStyle: TextStyle,
    /** When true, the header and its rows form one clipped shape; otherwise each has its own shape. */
    val headerIsPartOfShape: Boolean,
    /** Corner radius used by the section's grouped header shape. */
    val groupCornerSize: Dp,
    /** Whether sections expand by default; an individual section can override this. */
    val isExpandable: Boolean,
    /** Minimum height for expandable section headers. */
    val minimumExpandableItemHeight: Dp,
)

/** Styling for the back row shown at the top of an opened preference group. */
@Immutable
@ConsistentCopyVisibility
data class PreferenceGroupHeaderStyle internal constructor(
    val containerColor: Color,
    /** Applies to the back-row title and its leading icon. */
    val contentColor: Color,
    val shape: Shape,
    val contentPadding: PaddingValues,
    val minimumItemHeight: Dp,
    val titleTextStyle: TextStyle,
)

/** Whether an opened group page shows its title and back row, and how that row is styled. */
@Immutable
sealed interface PreferenceOpenGroupHeaderStyle {
    @Immutable
    data object Disabled : PreferenceOpenGroupHeaderStyle

    @Immutable
    data class Style(val value: PreferenceGroupHeaderStyle) : PreferenceOpenGroupHeaderStyle
}

/** Style for content shown in the trailing slot of a preference row. */
@Immutable
@ConsistentCopyVisibility
data class PreferenceTrailingStyle internal constructor(
    val textStyle: TextStyle,
    val contentColor: Color?,
    val maxWidthFraction: Float,
)

/** Separator used between entries inside an optically grouped section or group. */
@Immutable
sealed interface PreferenceInGroupSeparatorStyle {
    /** Keeps grouped entries separated by vertical space. */
    @Immutable
    data class Spacing(val spacing: Dp) : PreferenceInGroupSeparatorStyle

    /** Draws a divider aligned with preference item content. */
    @Immutable
    data class Divider(
        val verticalPadding: Dp = 0.dp,
    ) : PreferenceInGroupSeparatorStyle

    val spacingOrZero: Dp
        get() = (this as? Spacing)?.spacing ?: 0.dp
}

/** Spacing and separators used between preference list entries. */
@Immutable
@ConsistentCopyVisibility
data class PreferenceSpacingStyle internal constructor(
    /** Spacing between ordinary sibling entries outside grouped sections. */
    val itemSpacing: Dp,
    /** Spacing before a detached section header and between it and its content. */
    val sectionSpacing: Dp,
    /** Separator used between entries inside an optically grouped section or group. */
    val inGroupSeparator: PreferenceInGroupSeparatorStyle,
)

/** Styling shared by preference lists and their standard rows. */
@Immutable
@ConsistentCopyVisibility
data class PreferenceStyle internal constructor(
    val item: PreferenceItemStyle,
    val section: PreferenceSectionStyle,
    val spacing: PreferenceSpacingStyle,
    val openedGroupHeader: PreferenceOpenGroupHeaderStyle,
    val trailingStyle: PreferenceTrailingStyle,
    val outerPadding: PaddingValues,
    /** Keeps leading content aligned by rendering an empty icon when a row has no icon. */
    val reserveLeadingIconSpace: Boolean = true,
)

object PreferenceStyleDefaults {

    /** Returns the default minimum height for a preference item from the active theme. */
    @Composable
    fun minimumItemHeight(): Dp =
        maxOf(StyledTheme.sizes.minimumInteractiveSize, 40.dp)

    /** Creates colors for a preference row from the active theme. */
    @Composable
    fun rowColors(
        containerColor: Color = StyledTheme.colorScheme.surfaceVariant,
        contentColor: Color = StyledTheme.colorScheme.onSurfaceVariant,
        supportingColor: Color = StyledTheme.supportColorOf(contentColor, containerColor),
    ): PreferenceRowColors = PreferenceRowColors(
        containerColor = containerColor,
        contentColor = contentColor,
        supportingColor = supportingColor,
    )

    /** Creates the default style for an ordinary preference item. */
    @Composable
    fun item(
        colors: PreferenceRowColors = rowColors(),
        shape: Shape = StyledTheme.shapes.medium,
        contentPadding: PaddingValues = PaddingValues(
            horizontal = StyledTheme.padding.medium,
            vertical = StyledTheme.padding.medium,
        ),
        minimumItemHeight: Dp = minimumItemHeight(),
        titleTextStyle: TextStyle = StyledTheme.typography.bodyLarge,
        subtitleTextStyle: TextStyle = StyledTheme.typography.bodySmall,
    ): PreferenceItemStyle = PreferenceItemStyle(
        colors = colors,
        shape = shape,
        contentPadding = contentPadding,
        minimumItemHeight = minimumItemHeight,
        titleTextStyle = titleTextStyle,
        subtitleTextStyle = subtitleTextStyle,
    )

    /** Creates the default style for a preference section. */
    @Composable
    fun section(
        isExpandable: Boolean = false,
        colors: PreferenceRowColors = rowColors(
            containerColor = Color.Transparent,
            contentColor = StyledTheme.colorScheme.primary,
        ),
        shape: Shape = StyledTheme.shapes.medium,
        contentPadding: PaddingValues = PaddingValues(
            horizontal = StyledTheme.padding.medium,
            vertical = StyledTheme.padding.small,
        ),
        minimumItemHeight: Dp = 0.dp,
        minimumExpandableItemHeight: Dp = minimumItemHeight(),
        titleTextStyle: TextStyle = StyledTheme.typography.titleSmall,
        subtitleTextStyle: TextStyle = StyledTheme.typography.bodySmall,
        headerIsPartOfShape: Boolean = false,
        groupCornerSize: Dp = 8.dp
    ): PreferenceSectionStyle = PreferenceSectionStyle(
        colors = colors,
        shape = shape,
        contentPadding = contentPadding,
        minimumItemHeight = minimumItemHeight,
        minimumExpandableItemHeight = minimumExpandableItemHeight,
        titleTextStyle = titleTextStyle,
        subtitleTextStyle = subtitleTextStyle,
        headerIsPartOfShape = headerIsPartOfShape,
        groupCornerSize = groupCornerSize,
        isExpandable = isExpandable,
    )

    /** Creates spacing and separator defaults from the active Styled theme tokens. */
    @Composable
    fun spacing(
        itemSpacing: Dp = StyledTheme.spacing.large,
        sectionSpacing: Dp = StyledTheme.spacing.small,
        inGroupSeparator: PreferenceInGroupSeparatorStyle =
            PreferenceInGroupSeparatorStyle.Divider(
                verticalPadding = 0.dp,
            ),
    ): PreferenceSpacingStyle = PreferenceSpacingStyle(
        itemSpacing = itemSpacing,
        sectionSpacing = sectionSpacing,
        inGroupSeparator = inGroupSeparator,
    )

    /** Creates the default style for preference row trailing content. */
    @Composable
    fun trailing(
        textStyle: TextStyle = StyledTheme.typography.bodySmall,
        contentColor: Color? = StyledTheme.colorScheme.primary,
        maxWidthFraction: Float = 0.4f,
    ): PreferenceTrailingStyle {
        require(maxWidthFraction in 0f..1f) {
            "maxWidthFraction must be between 0 and 1."
        }
        return PreferenceTrailingStyle(
            textStyle = textStyle.copy(
                textAlign = if (textStyle.textAlign == TextAlign.Unspecified) {
                    TextAlign.End
                } else {
                    textStyle.textAlign
                },
            ),
            contentColor = contentColor,
            maxWidthFraction = maxWidthFraction,
        )
    }

    /** Creates the default style for the back row shown in an opened preference group. */
    @Composable
    fun openedGroupHeader(
        item: PreferenceItemStyle = item(),
        containerColor: Color = StyledTheme.colorScheme.surface,
        contentColor: Color = StyledTheme.colorScheme.onSurface,
        shape: Shape = item.shape,
        contentPadding: PaddingValues = item.contentPadding,
        minimumItemHeight: Dp = item.minimumItemHeight,
        titleTextStyle: TextStyle = StyledTheme.typography.titleMedium,
    ): PreferenceOpenGroupHeaderStyle.Style =
        PreferenceOpenGroupHeaderStyle.Style(
            PreferenceGroupHeaderStyle(
                containerColor = containerColor,
                contentColor = contentColor,
                shape = shape,
                contentPadding = contentPadding,
                minimumItemHeight = minimumItemHeight,
                titleTextStyle = titleTextStyle,
            ),
        )

}

/** Creates a screen-wide preference style from the active Styled theme tokens. */
@Composable
fun rememberPreferenceStyle(
    item: PreferenceItemStyle = PreferenceStyleDefaults.item(),
    section: PreferenceSectionStyle = PreferenceStyleDefaults.section(),
    openedGroupHeader: PreferenceOpenGroupHeaderStyle =
        PreferenceStyleDefaults.openedGroupHeader(item),
    outerPadding: PaddingValues = PaddingValues(
        horizontal = StyledTheme.padding.medium,
        vertical = StyledTheme.padding.medium,
    ),
    reserveLeadingIconSpace: Boolean = true,
    spacing: PreferenceSpacingStyle = PreferenceStyleDefaults.spacing(),
    trailingStyle: PreferenceTrailingStyle = PreferenceStyleDefaults.trailing(),
): PreferenceStyle {
    return remember(
        item,
        section,
        spacing,
        openedGroupHeader,
        trailingStyle,
        outerPadding,
        reserveLeadingIconSpace,
    ) {
        PreferenceStyle(
            item = item,
            section = section,
            spacing = spacing,
            openedGroupHeader = openedGroupHeader,
            trailingStyle = trailingStyle,
            outerPadding = outerPadding,
            reserveLeadingIconSpace = reserveLeadingIconSpace,
        )
    }
}
