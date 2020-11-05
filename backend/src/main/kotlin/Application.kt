import io.ktor.application.*
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.netty.EngineMain
import java.text.DateFormat

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {

    install(CallLogging)          // Включаем логирование
    install(DefaultHeaders)       // Включаем заголовки (Date and Server)

    install(ContentNegotiation) {

        // Серилизация
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }
    }
}

