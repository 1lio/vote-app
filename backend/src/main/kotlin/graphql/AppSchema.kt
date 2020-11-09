package graphql

import com.apurebase.kgraphql.KGraphQL
import com.auth0.jwt.JWT
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.interfaces.DecodedJWT
import model.LoginRegister
import model.Session
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import utils.AppConstants.DB_HOST
import java.util.*

class AppSchema {

    private val client = KMongo.createClient(DB_HOST).coroutine

    val schema = KGraphQL.schema {

        configure {
            useDefaultPrettyPrinter = true
        }

        fun newUser(token: String): String? {
            return try {
                val decodedJWT = JWT.decode(token)
                if (Date() > decodedJWT.expiresAt) "Please SignIn" else null
            } catch (e: JWTDecodeException) {
                "Token failure"
            }
        }


        suspend fun isTokenActive(decodedJWT: DecodedJWT): Boolean {
            val tokenFound = client.getDatabase("Account")
                .getCollection<Session>("Session")
                .findOne(Session::token eq decodedJWT.token)
            return tokenFound == null
        }

        query("search") {

            description = "return some database"

            resolver { token: String, query: String ->
                if (newUser(token) == null) {
                    if (isTokenActive(JWT.decode(token))) "Your search $query on the database!"
                    else " This Session is expired. Sign In Again."

                } else newUser(token)
            }
        }

        mutation("add") {
            description = "add some to database"

            resolver { token: String, name: String ->

                if (newUser(token) == null) {
                    if (isTokenActive(JWT.decode(token))) "You've added $name on the database."
                    else "This Session is expired. Sign In Again/"
                } else newUser(token)
            }
        }

        suspend fun addSession(decodedJWT: DecodedJWT) {
            client.getDatabase("Account")
                .getCollection<Session>("Session")
                .insertOne(Session(username = decodedJWT.issuer, token = decodedJWT.token))
        }

        query("logout") {
            description = "Log out a user"

            resolver { token: String ->

                if (newUser(token) == null) {
                    val decodedJWT = JWT.decode(token)
                    if (isTokenActive(decodedJWT)) {
                        addSession(decodedJWT)
                        "Log out"
                    } else {
                        "This Session is expired. Sig In Again."
                    }
                } else newUser(token)
            }
        }

        suspend fun delUser(decodedJWT: DecodedJWT, user: String): String {
            return if (decodedJWT.issuer == "admin") {

                val deleteResult = client.getDatabase("Account")
                    .getCollection<LoginRegister>("LoginRegister")
                    .deleteOne(LoginRegister::userName eq user)
                if (deleteResult.wasAcknowledged()) "You've deleted $user on the database" else "$user failed to delete on the database"
            } else {
                "Unauthorized Request."
            }
        }


        query("deleteUser") {

            description = "Hard delete a user"

            resolver { token: String, user: String ->

                if (newUser(token) == null) {

                    val decodedJWT = JWT.decode(token)
                    if(isTokenActive(decodedJWT)) delUser(decodedJWT, user)
                    else "This session is expired. Sign in again!"

                } else newUser(token)


            }

        }

    }

}
