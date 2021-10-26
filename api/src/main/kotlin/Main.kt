fun main() {
    // simple usage example
    // Query params: (hashTag, timeRange) where
    // hashTag - hashtag name
    // timeRange - hours
    // Output: [count1, count2, ...] where
    // count1 is the oldest count (timeRange hours from now)

    val urlReader = UrlReader()
    val vkResponseParser = VkResponseParser()
    val vkClient = VkClient(urlReader, vkResponseParser)
    val vkManager = VkManager(vkClient)

    // Queries at 12:00, tuesday
    // Statistics for '#dota2' [7, 11, 9, 5, 3, 10, 15, 11, 5, 3, 5, 2, 3, 1, 2, 1, 5, 2, 0, 0, 4, 4, 4, 4]
    // Statistics for '#утро' [0, 0, 9, 18, 31, 57, 35, 16, 25, 9]
    // Statistics for '#ужин' [3, 6, 11, 4, 14, 9, 10, 9, 5, 6, 4, 2, 1, 1, 3, 1, 1, 2, 5, 4, 3, 5, 5, 7]
    println("Statistics for \'#dota2\' " + vkManager.getQueryStatistics("#dota2", 24))
    println("Statistics for \'#утро\' " + vkManager.getQueryStatistics("#утро", 10))
    println("Statistics for \'#ужин\' " + vkManager.getQueryStatistics("#ужин", 24))
} 