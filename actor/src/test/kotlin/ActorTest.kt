import actor.ActorService
import dto.QueryParams
import dto.SearchEngine
import dto.SearchEngine.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Duration

class ActorTest {
    companion object {
        const val LOCALHOST = "localhost"
        const val PORT_1 = 1111
        const val PORT_2 = 2222
        const val PORT_3 = 3333
        val TIMEOUT_ZERO: Duration = Duration.ofSeconds(0)
        val TIMEOUT_ONE: Duration = Duration.ofSeconds(1)
        val TIMEOUT_TWO: Duration = Duration.ofSeconds(2)
    }

    @Test
    fun `one search engine, zero timeout`() {
        StubServer(PORT_1, TIMEOUT_ZERO).use {
            val engine = GOOGLE
            val query = "something"
            val service = ActorService(listOf(QueryParams(engine, LOCALHOST, PORT_1)))
            val response: HashMap<SearchEngine, List<String>> = service.performSearch(query, TIMEOUT_ONE)

            val expected = List(5) { "$PORT_1: searching '$query': ${it + 1}" }
            assertThat(response).isEqualTo(mapOf(GOOGLE to expected))
        }
    }

    @Test
    fun `multiple search engines, zero timeout`() {
        StubServer(PORT_1, TIMEOUT_ZERO).use {
            StubServer(PORT_2, TIMEOUT_ZERO).use {
                StubServer(PORT_3, TIMEOUT_ZERO).use {
                    val query = "three_engines"
                    val service = ActorService(
                        listOf(
                            QueryParams(GOOGLE, LOCALHOST, PORT_1),
                            QueryParams(BING, LOCALHOST, PORT_2),
                            QueryParams(YANDEX, LOCALHOST, PORT_3),
                        )
                    )

                    val response: HashMap<SearchEngine, List<String>> = service.performSearch(query, TIMEOUT_ONE)
                    assertThat(response).isEqualTo(
                        mapOf(
                            GOOGLE to List(5) { "$PORT_1: searching '$query': ${it + 1}" },
                            BING to List(5) { "$PORT_2: searching '$query': ${it + 1}" },
                            YANDEX to List(5) { "$PORT_3: searching '$query': ${it + 1}" },
                        )
                    )
                }
            }
        }
    }

    @Test
    fun `multiple search engines, non-zero timeout (YANDEX -- 1, PORT_3 -- 2)`() {
        StubServer(PORT_1, TIMEOUT_ZERO).use {
            StubServer(PORT_2, TIMEOUT_ZERO).use {
                StubServer(PORT_3, TIMEOUT_TWO).use {
                    val query = "timeouts"
                    val service = ActorService(
                        listOf(
                            QueryParams(GOOGLE, LOCALHOST, PORT_1),
                            QueryParams(BING, LOCALHOST, PORT_2),
                            QueryParams(YANDEX, LOCALHOST, PORT_3),
                        )
                    )

                    val response: HashMap<SearchEngine, List<String>> = service.performSearch(query, TIMEOUT_ONE)
                    assertThat(response).isEqualTo(
                        mapOf(
                            GOOGLE to List(5) { "$PORT_1: searching '$query': ${it + 1}" },
                            BING to List(5) { "$PORT_2: searching '$query': ${it + 1}" },
                        )
                    )
                }
            }
        }
    }
}