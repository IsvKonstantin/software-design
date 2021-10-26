import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class VkClient(private val urlReader: UrlReader, private val vkResponseParser: VkResponseParser) {
    fun query(hashTag: String, hours: Int, currentTime: Long): List<VkInfo> {
        val response = urlReader.readAsText(createUrl(hashTag, hours, currentTime))
        return vkResponseParser.parse(response)
    }

    private fun createUrl(query: String, timeRange: Int, currentTime: Long): String {
        val encodedQuery = URLEncoder.encode(
            if (query.startsWith("#")) query else "#$query",
            StandardCharsets.UTF_8.name()
        )

        val startTime = currentTime - 3600 * timeRange

        return Configuration.HOST +
                Configuration.METHOD +
                "?access_token=${Configuration.ACCESS_TOKEN}" +
                "&start_time=$startTime" +
                "&end_time=$currentTime" +
                "&count=${Configuration.COUNT}" +
                "&q=$encodedQuery" +
                "&v=${Configuration.API_VERSION}"
    }
}