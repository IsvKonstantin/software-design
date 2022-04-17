import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.mockserver.integration.ClientAndServer
import org.mockserver.integration.ClientAndServer.startClientAndServer
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.mockserver.model.HttpStatusCode.OK_200
import java.time.Duration


class StubServer(private val port: Int, timeoutDuration: Duration) : AutoCloseable {
    private val stubServer: ClientAndServer = startClientAndServer(port)

    init {
        stubServer.`when`(
            request()
                .withMethod("GET")
                .withPath("/search")
        ).respond { request ->
            if (!timeoutDuration.isZero) {
                Thread.sleep(timeoutDuration.toMillis())
            }

            response()
                .withStatusCode(OK_200.code())
                .withBody(createResponse(request.getFirstQueryStringParameter("q")))
        }
    }

    private fun createResponse(query: String): String {
        val response = List(5) { "$port: searching '$query': ${it + 1}" }
        return Json.encodeToString(response)
    }

    override fun close() {
        stubServer.close()
    }
}