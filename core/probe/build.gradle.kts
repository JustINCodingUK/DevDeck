plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting {
            dependencies {
                implementation(libs.jetbrains.exposed)
                implementation(libs.h2.database)
                implementation(libs.koin.core)
                implementation(projects.core.di)
                implementation(projects.core.deckApi)
            }
        }
    }
}
