import com.google.gson.JsonSyntaxException
import com.xebialabs.restito.builder.stub.StubHttp.whenHttp
import com.xebialabs.restito.semantics.Action.status
import com.xebialabs.restito.semantics.Action.stringContent
import com.xebialabs.restito.semantics.Condition.method
import com.xebialabs.restito.semantics.Condition.startsWithUri
import com.xebialabs.restito.server.StubServer
import org.glassfish.grizzly.http.Method
import org.glassfish.grizzly.http.util.HttpStatus
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.io.UncheckedIOException
import java.time.Instant
import java.util.function.Consumer


internal class VkManagerTest {
    private val port = 80
    private val currentTime = Instant.now().epochSecond

    private val urlReader: UrlReader = UrlReader()
    private val vkResponseParser: VkResponseParser = VkResponseParser()
    private val vkClient: VkClient = mock(VkClient::class.java)
    private val vkManager: VkManager = VkManager(vkClient)

    @Test
    fun `test vkManager getStatistics method, mocking vkClient`() {
        val hashTag = "#test"
        val timeRange = 10
        whenever(vkClient.query(any(), any(), any())).thenReturn(createAnswer())
        val statistics = vkManager.getQueryStatistics(hashTag, timeRange)
        assertEquals(listOf(0, 0, 0, 0, 0, 1, 0, 0, 1, 2), statistics)
    }

    private fun createAnswer(): List<VkInfo> {
        val firstHour = Instant.now().epochSecond - 1
        val secondHour = firstHour - 2
        val thirdHour = firstHour - 3800
        val fourthHour = firstHour - 3600 * 4

        return listOf(
            VkInfo(1, 1, firstHour),
            VkInfo(2, 2, secondHour),
            VkInfo(3, 3, thirdHour),
            VkInfo(4, 4, fourthHour)
        )
    }

    @Test
    fun `testing vkManager with a stub server`() = withStubServer() { s ->
        val path = "method/newsfeed.search"

        whenHttp(s)
            .match(method(Method.GET), startsWithUri("/$path"))
            .then(stringContent(sampleApiResponse))

        val hashTag = "#test"
        val timeRange = 5
        val result: String = urlReader.readAsText("http://localhost:$port/$path")
        whenever(vkClient.query(any(), any(), any())).thenReturn(vkResponseParser.parse(result))
        val statistics = vkManager.getQueryStatistics(hashTag, timeRange)

        assertEquals(sampleApiResponse, result)
        assertEquals(listOf(0, 0, 0, 0, 1), statistics)
    }

    @Test
    fun `read as text with not found error test`() = withStubServer() {
        val path = "method/newsfeed.search"

        whenHttp(it)
            .match(method(Method.GET), startsWithUri("/$path"))
            .then(status(HttpStatus.NOT_FOUND_404))

        Assertions.assertThrows(UncheckedIOException::class.java) {
            urlReader.readAsText("http://localhost:$port/$path")
        }
    }

    private fun withStubServer(callback: Consumer<StubServer?>) {
        var stubServer: StubServer? = null
        try {
            stubServer = StubServer(port).run()
            callback.accept(stubServer)
        } finally {
            stubServer?.stop()
        }
    }

    private val sampleApiResponse = """
        {
          "response": {
            "items": [
              {
                "id": 1,
                "date": ${currentTime - 1000},
                "owner_id": 2,
                "from_id": 3,
                "post_type": "post",
                "text": "test",
                "marked_as_ads": 0,
                "attachments": [],
                "comments": {},
                "likes": {
                    "can_like": 0,
                    "count": 3,
                    "user_likes": 0
                },
                "reposts": {},
                "views": {},
                "donut": {},
                "short_text_rate": 0.8
              }
            ],
            "count": 1,
            "total_count": 1
          }
        }
    """.trimIndent()
}