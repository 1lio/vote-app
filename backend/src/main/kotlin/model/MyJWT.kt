package model

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import utils.extensions.generateUniqueId
import java.util.*

class MyJWT (secret:String) {

    private val algorithm = Algorithm.HMAC256(secret)
    val expiration = 300
    lateinit var date: Date
    val verifier: JWTVerifier = JWT.require(algorithm).build()

    fun sign(username: String): String{
        date = Calendar.getInstance().apply {
            this.time = Date()
            this.roll(Calendar.MINUTE, 5)
        }.time

        return JWT.create()
            .withIssuer(username)
            .withExpiresAt(date)
            .withClaim("key", username)
            .withClaim("uniqueId", generateUniqueId())
            .sign(algorithm)
    }
}