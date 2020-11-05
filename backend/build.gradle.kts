plugins {
    application
    kotlin("jvm")
}

sourceSets {
    main {
        java.srcDir("src/main/kotlin")
        resources.srcDir("src/main/resources")
    }

    test {
        java.srcDir("src/test/kotlin")
        resources.srcDir("src/test/resources")
    }
}

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(Config.Libs.Ktor.serverNetty)
    implementation(Config.Libs.Ktor.auth)
    implementation(Config.Libs.Ktor.authJWT)
    implementation(Config.Libs.Ktor.gson)
    implementation(Config.Libs.Ktor.htmlBuilder)
    implementation(Config.Libs.Ktor.logbackClassic)
    implementation(Config.Libs.Ktor.locations)

    implementation(Config.Libs.Ktor.koinKtor)
    implementation(Config.Libs.Misc.graphql)
    implementation(Config.Libs.DB.mongoCoroutines)

    testImplementation(Config.Libs.Ktor.serverTests)
}
