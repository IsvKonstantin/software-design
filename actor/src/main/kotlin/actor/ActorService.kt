package actor

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import dto.QueryParams
import dto.SearchEngine
import java.time.Duration
import java.util.concurrent.CompletableFuture


class ActorService(private val queryParamsList: List<QueryParams>) {

    fun performSearch(query: String, timeout: Duration): HashMap<SearchEngine, List<String>> {
        val actorSystem = ActorSystem.create("ActorSystem")
        val result = CompletableFuture<HashMap<SearchEngine, List<String>>>()

        return try {
            val master = actorSystem.actorOf(Props.create(MasterActor::class.java, queryParamsList, result, timeout))
            master.tell(query, ActorRef.noSender())
            result.get()
        } finally {
            actorSystem.terminate()
        }
    }
}
