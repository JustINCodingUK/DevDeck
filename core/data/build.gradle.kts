plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.ksp)
}

kotlin {
    jvm("desktop")

    sourceSets {
        commonMain{
            dependencies {
                implementation(projects.core.network)
                implementation(projects.core.di)
                implementation(projects.core.model)
                implementation(projects.core.util)
                implementation(projects.core.deckApi)

                implementation(libs.kotlinx.coroutines.core)

                implementation(libs.koin.core)
            }
        }

        val desktopMain by getting {
            resources.srcDir("build/generated/ksp/desktop/desktopMain/resources")
        }
    }
}

dependencies {
    add("kspDesktop", projects.core.di)
}
