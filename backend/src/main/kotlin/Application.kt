import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.netty.*
import io.ktor.util.*
import java.io.File
import java.util.*

val gson: Gson = GsonBuilder().setPrettyPrinting().create()

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
            val error = "${HttpStatusCode.InternalServerError}, " +
                    "${call.request.local.uri},${it}"
            call.respond(error)
        }
    }

    install(Authentication){

    }

    routing {
        // Route to test plain 'get' requests.
        // ApplicationCall.sendHttpBinResponse is an extension method defined in this project that sends
        // information about the request as an object, that will be converted into JSON
        // by the ContentNegotiation feature.
        get("/get") {
            call.sendHttpBinResponse()
        }


    }
}

