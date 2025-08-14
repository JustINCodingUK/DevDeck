import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.buildconfig)
    alias(libs.plugins.ksp)
}

repositories {
    mavenCentral()
    mavenLocal()
    google()
}

val deckReferencesDirectory = "\"${System.getProperty("user.home")}/.devdeck/install_refs/\""
buildConfig {
    buildConfigField("String", "DECK_REFERENCE_DIR", deckReferencesDirectory)
}

kotlin {
    jvm("desktop")

    sourceSets {
        commonMain {

            dependencies {
                implementation(projects.core.deckApi)
                implementation(projects.core.model)
                implementation(projects.core.probe)
                implementation(projects.core.data)
                implementation(projects.core.ui)

                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.koin.compose.nav)
                implementation(libs.koin.core)
                implementation(libs.kotlin.stdlib)
                implementation(projects.core.di)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(compose.desktop.currentOs)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}

