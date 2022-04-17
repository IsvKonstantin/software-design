package actor

import akka.actor.AbstractActor
import dto.QueryParams
import dto.Request
import dto.Response
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class ChildActor(private val queryParams: QueryParams) : AbstractActor() {

    override fun createReceive(): Receive {
        return receiveBuilder()
            .match(Request::class.java, this::action)
            .build()
    }

    private fun action(message: Any) {
        if (message is Request) {
            val uri = URI.create("https://${queryParams.host}:${queryParams.port}/search?q=${message.data}")
            val client = HttpClient.newBuilder().build()
            val request = HttpRequest
                .newBuilder()
                .uri(uri)
                .build()
            val response = client
                .send(request, HttpResponse.BodyHandlers.ofString())
                .body()
                .intern()

            sender.tell(Response(engine = message.engine, data = response), self)
            context.stop(self)
        }
    }
}