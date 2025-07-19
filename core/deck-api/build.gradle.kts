import com.vanniktech.maven.publish.SonatypeHost
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.maven.publish)
}

group = "io.github.justincodinguk.devdeck"
val artifactId = "deck-api"
version = "0.1.2"

val sonatypeProperties = Properties().apply {
    val file = rootProject.file("central.sonatype.properties")
    if(file.exists()) {
        file.inputStream().use { load(it) }
    }
}

sonatypeProperties.forEach { key, value ->
    project.extensions.extraProperties[key as String] = value
}

kotlin {
    jvm("desktop")

    sourceSets.commonMain.dependencies {
        api(libs.kotlinx.coroutines.core)
        implementation(libs.ktor.client.cio)
        implementation(libs.slf4j.api)
        implementation(libs.logback.classic)
        implementation(libs.ktor.client.content.negotiation)
        implementation(libs.ktor.client.json)
        implementation(libs.kotlinx.serialization)
        implementation(libs.commons.text)
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    coordinates(group.toString(), artifactId, version.toString())

    pom {
        name = "Deck API"
        description = "Official deckfile compiler and tasks API"
        inceptionYear = "2025"
        url = "https://github.com/JustINCodingUK/DevDeck/tree/master/core/deck-api"
        licenses {
            license {
                name = "MIT"
                url = "https://opensource.org/licenses/MIT"
            }
        }
        developers {
            developer {
                id = "justincodinguk"
                name = "Justin William"
                url = "https://github.com/justincodinguk"
            }
        }
        scm {
            url = "https://github.com/JustINCodingUK/DevDeck/tree/master/core/deck-api"
            connection = "scm:git:git://github.com/JustINCodingUK/DevDeck.git"
            developerConnection = "scm:git:ssh://git@github.com/JustINCodingUK/DevDeck.git"
        }
    }
}
