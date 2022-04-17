package actor

import akka.actor.AbstractActor
import akka.actor.Props
import akka.actor.ReceiveTimeout
import dto.QueryParams
import dto.Request
import dto.Response
import dto.SearchEngine
import java.time.Duration
import java.util.concurrent.CompletableFuture


class MasterActor(
    private val queryParamsList: List<QueryParams>,
    private val futureResult: CompletableFuture<HashMap<SearchEngine, List<String>>>,
    timeoutDuration: Duration
) : AbstractActor() {
    private val result = HashMap<SearchEngine, List<String>>()

    init {
        context.receiveTimeout = timeoutDuration
    }

    override fun createReceive(): Receive {
        return receiveBuilder()
            .match(String::class.java, this::sendRequests)
            .match(Response::class.java, this::receiveResponse)
            .match(ReceiveTimeout::class.java) { complete() }
            .build()
    }

    private fun sendRequests(query: String) {
        queryParamsList.forEach { queryParams ->
            context
                .actorOf(Props.create(ChildActor::class.java, queryParams))
                .tell(Request(engine = queryParams.engine, data = query), self)
        }
    }

    private fun receiveResponse(response: Response) {
        result[response.engine] = response.data

        if (result.size == queryParamsList.size) {
            complete()
        }
    }

    private fun complete() {
        futureResult.complete(result)
        context.system().stop(self)
    }
}