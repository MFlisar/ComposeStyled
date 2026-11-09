import com.michaelflisar.kmpdevtools.Targets
import com.michaelflisar.kmpdevtools.BuildFileUtil
import com.michaelflisar.kmpdevtools.core.Target
import com.michaelflisar.kmpdevtools.configs.*
import com.michaelflisar.kmpdevtools.setupDependencies
import com.michaelflisar.kmpdevtools.setupBuildKonfig
import com.michaelflisar.kmpdevtools.isDebug
import com.michaelflisar.kmpdevtools.isExe

plugins {
    // kmp + app/library
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.android.library.kmp)
    // org.jetbrains.kotlin
    alias(libs.plugins.jetbrains.kotlin.compose)
    alias(libs.plugins.jetbrains.kotlin.parcelize)
    // org.jetbrains.compose
    alias(libs.plugins.jetbrains.compose)
    // docs, publishing, validation
    // --
    // build tools
    alias(mflisar.plugins.kmpdevtools.buildplugin)
    alias(libs.plugins.buildkonfig)
    // others
    // ...
}

// ------------------------
// Setup
// ------------------------

val module = LibraryModuleConfig.read(project)

val buildTargets = Targets(
    Target.ANDROID,
    Target.IOS,
    Target.JVM,
    Target.MACOS,
    Target.WASM
)

val androidConfig = AndroidLibraryConfig.create(
    libraryModuleConfig = module,
    compileSdk = app.versions.compileSdk,
    minSdk = app.versions.minSdk,
    enableAndroidResources = true
)

// ------------------------
// Kotlin
// ------------------------

buildkonfig {
    setupBuildKonfig(
        project = project,
        config = module.config,
        isDebug = isDebug(project),
        isExe = isExe(project),
    )
}

compose.resources {
    packageOfResClass = "${module.projectNamespace}.demo.shared.resources"
    publicResClass = true
}

kotlin {

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    //-------------
    // Targets
    //-------------

    buildTargets.setupTargetsLibrary(module)
    android {
        buildTargets.setupTargetsAndroidLibrary(module, androidConfig, this)
    }

    // -------
    // Sources
    // -------

    sourceSets {

        // ---------------------
        // custom source sets
        // ---------------------

        // --

        // ---------------------
        // dependencies
        // ---------------------

        commonMain.dependencies {

            // compose
            api(libs.jetbrains.compose.components.resources)
            api(libs.jetbrains.compose.runtime)
            api(libs.jetbrains.compose.material3)
            api(libs.jetbrains.compose.material.icons.core)
            api(libs.jetbrains.compose.material.icons.extended)

            // modules
            api(project(":composestyled:core"))
            // implementations do add their themes via api dependency!
            api(project(":composestyled:implementations:cupertino-flat"))
            api(project(":composestyled:implementations:cupertino-liquid"))
            api(project(":composestyled:implementations:fluent2"))
            api(project(":composestyled:implementations:material3"))
            // additional themes fitting all platforms
            api(project(":composestyled:themes:pastel"))
            api(project(":composestyled:themes:nature"))

            // ---------------------------
            // libraries with style support
            // ---------------------------

            api(project(":composestyled:libraries:composetable"))
            api(project(":composestyled:libraries:composedialogs"))
            api(project(":composestyled:libraries:composepreferences"))

            // TODO
            // composedialogs + composepreferences + composechangelog
        }

    }
}

dependencies {
    coreLibraryDesugaring(libs.desugar)
}