import com.michaelflisar.kmpdevtools.Targets
import com.michaelflisar.kmpdevtools.config.LibraryModuleData
import com.michaelflisar.kmpdevtools.config.sub.AndroidLibraryConfig
import com.michaelflisar.kmpdevtools.core.configs.Config
import com.michaelflisar.kmpdevtools.core.configs.LibraryConfig

plugins {
    // kmp + app/library
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    // org.jetbrains.kotlin
    alias(libs.plugins.jetbrains.kotlin.compose)
    // org.jetbrains.compose
    alias(libs.plugins.jetbrains.compose)
    // docs, publishing, validation
    // --
    // build tools
    alias(deps.plugins.kmpdevtools.buildplugin)
    // others
    // ...
}

// ------------------------
// Setup
// ------------------------

val config = Config.read(rootProject)
val libraryConfig = LibraryConfig.read(rootProject)

val buildTargets = Targets(
    // mobile
    android = true,
    iOS = true,
    // desktop
    windows = true,
    macOS = false, // because compose-unstyled does not support macOS yet
    // web
    wasm = true
)
val androidConfig = AndroidLibraryConfig(
    compileSdk = app.versions.compileSdk,
    minSdk = app.versions.minSdk,
    enableAndroidResources = true
)

val libraryModuleData = LibraryModuleData(
    project = project,
    config = config,
    libraryConfig = libraryConfig,
    androidConfig = androidConfig
)

// ------------------------
// Kotlin
// ------------------------

compose.resources {
    packageOfResClass = "${libraryConfig.library.namespace}.shared.resources"
    publicResClass = true
}

kotlin {

    //-------------
    // Targets
    //-------------

    buildTargets.setupTargetsLibrary(libraryModuleData)

    // ------------------------
    // Source Sets
    // ------------------------

    sourceSets {

        // ---------------------
        // custom source sets
        // ---------------------

        // --

        // ---------------------
        // dependencies
        // ---------------------

        commonMain.dependencies {

            // resources
            api(compose.components.resources)

            // Kotlin
            // ..

            // Compose
            // ..

            // ------------------------
            // Library
            // ------------------------


            implementation(project(":composestyled:themes:android"))
            //implementation(project(":composestyled:themes:material3"))

        }
    }
}