import data.model.ResError
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.netty.*
import io.ktor.util.*
import java.util.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

@KtorExperimentalAPI
@Suppress("unused")
fun Application.module() {

    install(DefaultHeaders)     // Подрубаем заголовки
    install(Compression)        // Сжимаем
    install(CallLogging)        // Включаем логирование
    install(ConditionalHeaders) // Автоматический респон при 304
    install(PartialContent)     // Поддержка диапозонов в заголовках
    install(AutoHeadResponse)   // в GET подрубает заголовки

    // Конвертация заголовков в JSON формат
    install(ContentNegotiation) {
        register(ContentType.Application.Json, GsonConverter())
    }

    // Подрубаем CORS (Cross-Origin Resource Sharing)
    install(CORS) {
        anyHost()
        allowCredentials = true
        listOf(HttpMethod("PATCH"), HttpMethod.Put, HttpMethod.Delete).forEach {
            method(it)
        }
    }

    // Перхватываем исключения в роутах
    install(StatusPages) {
        exception<Throwable> {
            environment.log.error(it)
            val error = ResError(
                code = HttpStatusCode.InternalServerError,
                request = call.request.local.uri,
                message = it.toString(),
                cause = it)
            call.respond(error)
        }
    }

    // Folder from the File System that we are going to use to serve static files.
   /* val staticfilesDir = File("resources/static")
    require(staticfilesDir.exists()) { "Cannot find ${staticfilesDir.absolutePath}" }*/

    // Fake Authorization with user:password "test:test"
/*    val hashedUserTable = UserHashedTableAuth(
        getDigestFunction("SHA-256") { "ktor${it.length}" },
        table = mapOf(
            "test" to Base64.getDecoder().decode("GSjkHCHGAxTTbnkEDBbVYd+PUFRlcWiumc4+MWE9Rvw=") // sha256 for "test"
        ))*/

    routing {

        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }
    }
}

