import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import java.time.Instant

internal class VkManagerTest {
    private val vkClient: VkClient = mock(VkClient::class.java)
    private var vkManager: VkManager = VkManager(vkClient)

    @Test
    fun `test vkManager getStatistics method, mocking vkClient`() {
        val hashTag = "#test"
        val timeRange = 10
        val currentTime = Instant.now().epochSecond
        whenever(vkClient.query(hashTag, timeRange, currentTime)).thenReturn(createAnswer())
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
}