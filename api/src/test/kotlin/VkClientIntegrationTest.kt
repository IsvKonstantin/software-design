import org.junit.jupiter.api.Test
import java.time.Instant

internal class VkClientIntegrationTest {
    private val urlReader = UrlReader()
    private val vkResponseParser = VkResponseParser()
    private val vkClient = VkClient(urlReader, vkResponseParser)


    @Test
    fun `calling vk app expecting to receive some valid results`() {
        val hashTag = "#Test"
        val timeRange = 10
        val currentTime = Instant.now().epochSecond

        val result = vkClient.query(hashTag, timeRange, currentTime)
        result.forEach {
            assert(it.date in currentTime - timeRange * 3600 until currentTime)
        }
    }
}