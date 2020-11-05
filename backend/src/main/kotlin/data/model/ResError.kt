package data.model

import io.ktor.http.*

data class ResError(
    val request: String,
    val message: String,
    val code: HttpStatusCode,
    val cause: Throwable? = null
)