pluginManagement {
    repositories {
        mavenCentral()
        maven { url = uri("https://kotlin.bintray.com/kotlin-eap") }
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }
}

rootProject.name = "VoteApp"

enableFeaturePreview("GRADLE_METADATA")

// modules
include(":android", ":backend")

