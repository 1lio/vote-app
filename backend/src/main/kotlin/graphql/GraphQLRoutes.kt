package graphql

import com.apurebase.kgraphql.schema.Schema
import com.google.gson.Gson
import io.ktor.application.call
import io.ktor.locations.*
import io.ktor.request.receive
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.locations.post
import org.slf4j.Logger

@Location("/graphql")
data class GraphQLRequest(val query: String = "", val variables: Map<String, Any> = emptyMap())

fun GraphQLError.asMap(): Map<String, Map<String, String>> {
    return mapOf("errors" to mapOf("message" to e.message!!.replace("\"", "")))
}

data class GraphQLError(val e: Exception)

fun Route.graphql(log: Logger, gson: Gson, schema: Schema) {
    post<GraphQLRequest> {
        val request = call.receive<GraphQLRequest>()

        val query = request.query
        log.info("The GrapQL query: $query")

        val variables = gson.toJson(request.variables)
        log.info("The GrapQL variables: $variables")

        try {
            val result = schema.execute(query, variables)
            call.respondText(result)
        } catch (e: Exception) {
            call.respondText(gson.toJson(GraphQLError(e).asMap()))
        }
    }
}