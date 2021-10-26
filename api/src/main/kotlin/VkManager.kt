import java.time.Instant

class VkManager(private val vkClient: VkClient) {
    fun getQueryStatistics(hashTag: String, timeRange: Int): List<Int> {
        require(hashTag.isNotBlank())
        require(timeRange in 1..24)

        val currentTime = Instant.now().epochSecond
        val result = vkClient.query(hashTag, timeRange, currentTime)
        val statistics = MutableList(timeRange) { 0 }

        result.forEach {
            (0 until timeRange)
                .asSequence()
                .filter { hour -> it.date in currentTime - (hour + 1) * 3600 until currentTime - hour * 3600 }
                .forEach { statistics[it] += 1 }
        }

        return statistics.reversed()
    }
}