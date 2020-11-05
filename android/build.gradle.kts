plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.android.extensions")
}

android {

    compileSdkVersion(Config.SdkVersions.compile)
    buildToolsVersion = Config.buildTools

    defaultConfig {

        minSdkVersion(Config.SdkVersions.min)
        targetSdkVersion(Config.SdkVersions.target)
        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = Config.testRunner
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets {
        getByName("androidTest").java.srcDirs(Config.androidTestDir)
        getByName("main").java.srcDirs(Config.mainDir)
        getByName("test").java.srcDirs(Config.testDir)
    }

    defaultConfig {
        applicationId = Config.applicationID

        versionCode = Config.appVersionCode
        versionName = Config.appVersionName
    }
}

dependencies {
    implementation(Config.Libs.Androidx.appCompat)
    implementation(Config.Libs.Androidx.material)

    // Тестирование
    testImplementation(Config.Libs.Test.junit)
    androidTestImplementation(Config.Libs.Test.testRunner)
    androidTestImplementation(Config.Libs.Test.espresso)
}
