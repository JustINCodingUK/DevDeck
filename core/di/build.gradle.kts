plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm("desktop")
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
        }
    }
}
