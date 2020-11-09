import com.google.gson.Gson
import com.google.gson.GsonBuilder
import graphql.AppSchema
import graphql.graphql
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.netty.*
import model.LoginRegister
import model.MyJWT
import org.koin.core.context.startKoin
import org.koin.ktor.ext.inject
import org.litote.kmongo.eq
import utils.AppConstants.DB_HOST
import utils.appModule
import org.litote.kmongo.coroutine.*
import org.litote.kmongo.reactivestreams.*
import utils.AppConstants.SECRET_KEY

val gson: Gson = GsonBuilder().setPrettyPrinting().create()

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused", "AuthLeak")
fun Application.module() {

    startKoin { modules(appModule) }

    val myJWT = MyJWT(SECRET_KEY)

    install(Authentication) {

        jwt {
            verifier(myJWT.verifier)
            validate {
                UserIdPrincipal(it.payload.getClaim("key").asString())
            }
        }
    }

    install(ContentNegotiation) { gson { setPrettyPrinting() } }

    install(Locations)

    routing {

        val appSchema: AppSchema by inject()
        val gson: Gson by inject()
        val client = KMongo.createClient(DB_HOST).coroutine

        fun clientLoginRegister() = client.getDatabase("vote").getCollection<LoginRegister>("LoginRegister")

        suspend fun getLoginRegister(call: ApplicationCall): LoginRegister {
            val post = call.receive<LoginRegister>()
            return LoginRegister(
                gson.fromJson(post.userName, String::class.java),
                gson.fromJson(post.password, String::class.java)
            )
        }

        suspend fun setNewLoginRegister(call: ApplicationCall, login: LoginRegister) {
            clientLoginRegister().findOne(LoginRegister::userName eq login.userName)?.let {
                call.respond(HttpStatusCode.BadRequest, "This username already exist!")
                return@setNewLoginRegister
            }

            clientLoginRegister().insertOne(login)
        }

         fun respondLoginRegister(loginRegister: LoginRegister): Triple<String, String, String> {
            val newToken = myJWT.sign(loginRegister.userName)
            val id = loginRegister.id!!
            val userName = loginRegister.userName
            return Triple(newToken, id, userName)
        }


        post("/sign-up") {

            val loginRegister = getLoginRegister(call)
            setNewLoginRegister(call, loginRegister)

            val (newToken, id, username) = respondLoginRegister(loginRegister)
            call.respond(
                mapOf(
                    "token" to newToken,
                    "username" to username,
                    "expiresIn" to myJWT.expiration,
                    "id" to id
                )
            )
        }

        post("/sign-in") {

            val loginRegister = getLoginRegister(call)
            val hasMatch = clientLoginRegister().findOne(
                LoginRegister::userName eq loginRegister.userName,
                LoginRegister::password eq loginRegister::password
            )

            if (hasMatch != null) {
                val (newToken, id, username) = respondLoginRegister(loginRegister)
                call.respond(
                    mapOf(
                        "token" to newToken,
                        "username" to username,
                        "expiresIn" to myJWT.expiration,
                        "id" to id
                    )
                )
            } else call.respond(HttpStatusCode.BadRequest, "Account does not exist!")
        }

        graphql(log, gson, appSchema.schema)

        static("/") { default("index.html") }
    }

}
