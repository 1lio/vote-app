plugins {
    application
    kotlin("jvm")
}

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

dependencies {

    implementation(kotlin("stdlib-jdk8"))

    implementation(Config.Libs.Ktor.serverNetty)
    implementation(Config.Libs.Ktor.auth)
    implementation(Config.Libs.Ktor.gson)
    implementation(Config.Libs.Ktor.htmlBuilder)
    implementation(Config.Libs.Ktor.logbackClassic)
    testImplementation(Config.Libs.Ktor.serverTests)

    implementation(Config.Libs.Ktor.koinKtor)
    implementation(Config.Libs.Misc.graphql)

    implementation(Config.Libs.DB.mongoCoroutines)
}
