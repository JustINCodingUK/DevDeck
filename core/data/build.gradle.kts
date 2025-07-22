plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.network)
            implementation(projects.core.di)
            implementation(projects.core.model)

            implementation(libs.kotlinx.coroutines.core)

            implementation(libs.koin.core)
        }
    }
}
