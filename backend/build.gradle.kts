plugins {
    application
    kotlin("jvm")
}

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

dependencies {

    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation(Config.Libs.Kotlin.serialization)

    implementation(Config.Libs.Ktor.serverNetty)
    implementation(Config.Libs.Ktor.serverCore)
    implementation(Config.Libs.Ktor.serverHostCommon)

    implementation(Config.Libs.Ktor.locations)
    implementation(Config.Libs.Ktor.sessions)
    implementation(Config.Libs.Ktor.auth)
    implementation(Config.Libs.Ktor.authJWT)
    implementation(Config.Libs.Ktor.gson)

    implementation(Config.Libs.Ktor.logbackClassic)
    implementation(Config.Libs.Ktor.tests)
}
