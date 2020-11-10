plugins {
    application
    kotlin("jvm")
}

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    // KTOR
    implementation(Config.Libs.Ktor.serverNetty)
    implementation(Config.Libs.Ktor.serverCore)
    implementation(Config.Libs.Ktor.auth)
    implementation(Config.Libs.Ktor.authJWT)
    implementation(Config.Libs.Ktor.gson)
    implementation(Config.Libs.Ktor.htmlBuilder)
    implementation(Config.Libs.Ktor.locations)
    implementation(Config.Libs.Ktor.hostCommon)
    // DB
    implementation(Config.Libs.DB.kMongo)
    implementation(Config.Libs.DB.kMongoCoroutines)
    // MISC
    implementation(Config.Libs.Ktor.koinKtor)
    implementation(Config.Libs.Misc.graphql)
    implementation(Config.Libs.Ktor.logbackClassic)
    // TEST
    testImplementation(Config.Libs.Ktor.serverTests)
}

// In IDEA: Settings > Kotlin compiler (1.6 -> 1.8)
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

kotlin.sourceSets {
    main {
        kotlin.srcDir("src/main/kotlin")
        resources.srcDir("src/main/resources")
    }

    test {
        kotlin.srcDir("src/test/kotlin")
        resources.srcDir("src/test/resources")
    }
}
