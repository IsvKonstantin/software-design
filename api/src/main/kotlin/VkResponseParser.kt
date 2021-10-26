import com.google.gson.JsonObject
import com.google.gson.JsonParser

class VkResponseParser {
    fun parse(response: String): List<VkInfo> {
        val jsonResponse = JsonParser.parseString(response).asJsonObject
        val jsonEntries = jsonResponse.getAsJsonObject("response").getAsJsonArray("items")

        return jsonEntries.map { entry ->
            VkInfo(
                (entry as JsonObject).get("id").asLong,
                entry.get("owner_id").asLong,
                entry.get("date").asLong,
            )
        }
    }
}