package com.michaelflisar.demo

import androidx.compose.runtime.Composable
import com.michaelflisar.composestyled.cupertino.flat.rememberCupertinoFlatThemeProvider
import com.michaelflisar.composestyled.cupertino.liquid.rememberCupertinoLiquidThemeProvider
import com.michaelflisar.composestyled.fluent2.rememberFluent2ThemeProvider
import com.michaelflisar.composestyled.material3.rememberMaterial3ThemeProvider
import com.michaelflisar.composestyled.themes.cupertino.CupertinoThemes
import com.michaelflisar.composestyled.themes.fluent2.Fluent2Themes
import com.michaelflisar.composestyled.themes.material3.Material3Themes
import com.michaelflisar.composestyled.themes.nature.NatureThemes
import com.michaelflisar.composestyled.themes.pastel.PastelThemes
import com.michaelflisar.demo.demos.libraries.DemoPageComposeDialogs
import com.michaelflisar.demo.demos.libraries.DemoPageComposeTable
import com.michaelflisar.demo.demos.libraries.DemoPagePreferences
import com.michaelflisar.demo.demos.recipes.DemoPageRecipes
import com.michaelflisar.demo.demos.showcase.DemoPageShowCase
import com.michaelflisar.demo.pages.DemoPageInsets

object Definitions {

    val ImplementationCollections = listOf(
        "Fluent 2" to @Composable { rememberFluent2ThemeProvider() },
        "Material 3" to @Composable { rememberMaterial3ThemeProvider() },
        "Cupertino Flat" to @Composable { rememberCupertinoFlatThemeProvider() },
        "Cupertino Liquid" to @Composable { rememberCupertinoLiquidThemeProvider() },
    )

    val ThemeCollections = listOf(
        Fluent2Themes.collection,
        Material3Themes.collection,
        CupertinoThemes.collection,
        PastelThemes.collection,
        NatureThemes.collection,
    )

    fun createMainPages(onBackPress: () -> Unit) = listOf(
        "Recipes" to @Composable { DemoPageRecipes(onBackPress) },
        "Component showcase" to @Composable { DemoPageShowCase(onBackPress) },
        "Insets showcase" to @Composable { DemoPageInsets(onBackPress) },
    )

    // Labels ohne "Compose" Prefix
    fun createLibraryPages(onBackPress: () -> Unit) = listOf(
        "Table" to @Composable { DemoPageComposeTable(onBackPress) },
        "Preferences" to @Composable { DemoPagePreferences(onBackPress) },
        "Dialogs" to @Composable { DemoPageComposeDialogs(onBackPress) },
    )
}