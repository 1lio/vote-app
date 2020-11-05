buildscript {

    repositories {
        google()
        mavenCentral()
        jcenter()
        maven("https://plugins.gradle.org/m2/")
        maven("https://kotlin.bintray.com/ktor")
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }

    dependencies {
        classpath(Config.Plugins.gradleKotlin)
        classpath(Config.Plugins.gradleAndroid)
    }
}

allprojects {

    group = "ru.vote"
    version = "1.0"

    repositories {
        google()
        jcenter()
        mavenCentral()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        maven("https://kotlin.bintray.com/kotlinx")
    }
}