package com.michaelflisar.composestyled.demo.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.components.layout.ContentRow
import com.michaelflisar.composestyled.core.ProvideStyledTheme
import com.michaelflisar.composestyled.core.classes.colors.BaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.components.StyledText
import com.michaelflisar.composestyled.demo.DemoThemes
import com.michaelflisar.composestyled.demo.composables.PreviewExpandableContent
import com.michaelflisar.composestyled.demo.composables.PreviewRegionCard
import com.michaelflisar.composestyled.demo.composables.PreviewScheme
import com.michaelflisar.composestyled.demo.composables.PreviewStateChip
import com.michaelflisar.composestyled.demo.styles.ThemeWithExtensions
import com.michaelflisar.composestyled.theme.cupertino.CupertinoUtil
import com.michaelflisar.composestyled.theme.fluent2.Fluent2Util
import com.michaelflisar.composestyled.theme.material3.Material3Util

@Composable
fun ButtonsScreen() {
    PreviewScheme(
        title = "Buttons",
        modifier = Modifier.fillMaxSize()
    ) {
        Content()
    }
}

@Composable
private fun Content() {
    FamilyRegion(
        familyName = "Material 3",
        resolve = { v -> Material3Util.resolveButtonColors(v) },
        style = DemoThemes.themeM3
    )
    FamilyRegion(
        familyName = "Cupertino",
        resolve = { v -> CupertinoUtil.resolveButtonColors(v) },
        style = DemoThemes.themeCupertino
    )
    FamilyRegion(
        familyName = "Fluent 2",
        resolve = { v -> Fluent2Util.resolveButtonColors(v) },
        style = DemoThemes.themeFluent2
    )
}

@Composable
private fun FamilyRegion(
    familyName: String,
    resolve: @Composable (StyledButton.Variant) -> StatefulBaseColorDef,
    style: ThemeWithExtensions,
) {
    ProvideStyledTheme(
        theme = style.theme
    ) {
        PreviewRegionCard(
            title = familyName
        ) {
            val variants = listOf(
                StyledButton.Variant.Primary,
                StyledButton.Variant.Secondary,
                StyledButton.Variant.Outlined,
                StyledButton.Variant.Text
            )

            // 1) Interactive examples
            ContentRow {
                variants.forEach { v ->
                    StyledButton(
                        onClick = {},
                        variant = v,
                        modifier = Modifier.weight(1f)
                    ) {
                        StyledText(v.name.first().toString())
                    }
                }
            }

            // 2) show variants
            PreviewExpandableContent("State Previews") {
                variants.forEach { v ->
                    val defs = resolve(v)
                    ContentRow(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val m = Modifier.size(48.dp)
                        StyledText(v.name, Modifier.weight(1f))
                        PreviewStateChip(
                            "N",
                            baseFor(defs, StateKind.Normal),
                            m
                        )
                        PreviewStateChip(
                            "H",
                            baseFor(defs, StateKind.Hovered),
                            m
                        )
                        PreviewStateChip(
                            "F",
                            baseFor(defs, StateKind.Focused),
                            m
                        )
                        PreviewStateChip(
                            "P",
                            baseFor(defs, StateKind.Pressed),
                            m
                        )
                    }
                }
            }
        }
    }
}

private enum class StateKind { Normal, Hovered, Focused, Pressed }

@Composable
@ReadOnlyComposable
private fun baseFor(defs: StatefulBaseColorDef, state: StateKind): BaseColorDef {
    // same fallback idea as your resolve(): hovered/focused/pressed may be null
    return when (state) {
        StateKind.Normal -> defs.normal
        StateKind.Hovered -> defs.hovered ?: defs.normal
        StateKind.Focused -> defs.focused ?: defs.normal
        StateKind.Pressed -> defs.pressed ?: defs.focused ?: defs.hovered ?: defs.normal
    }
}


