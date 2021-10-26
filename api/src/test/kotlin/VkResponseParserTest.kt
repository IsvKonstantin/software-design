import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class VkResponseParserTest {
    private val vkResponseParser = VkResponseParser()

    @Test
    fun `successfully parse Json`() {
        val parsed = vkResponseParser.parse(jsonString)
        assertEquals(1, parsed.size)
        assertEquals(listOf(VkInfo(995L, -204255031L, 1635141604L)), parsed)
    }

    @Test
    fun `should fail on a non-Json string`() {
        assertThrows(com.google.gson.JsonSyntaxException::class.java) { vkResponseParser.parse(nonJsonString) }
    }

    private val nonJsonString = "{[\"fail\"}"

    private val jsonString = "{\n" +
            "\"response\": {\n" +
            "\"items\": [{\n" +
            "\"id\": 995,\n" +
            "\"date\": 1635141604,\n" +
            "\"owner_id\": -204255031,\n" +
            "\"from_id\": -204255031,\n" +
            "\"post_type\": \"post\",\n" +
            "\"text\": \"#Kiryu_Coco\n" +
            "#Hololive_JP\n" +
            "#HololiveJP\n" +
            "#Hololive\n" +
            "#Vtuber\",\n" +
            "\"marked_as_ads\": 0,\n" +
            "\"attachments\": [{\n" +
            "\"type\": \"photo\",\n" +
            "\"photo\": {\n" +
            "\"album_id\": -7,\n" +
            "\"date\": 1635138342,\n" +
            "\"id\": 457239493,\n" +
            "\"owner_id\": -204255031,\n" +
            "\"has_tags\": false,\n" +
            "\"access_key\": \"33d26395957b33a9be\",\n" +
            "\"post_id\": 994,\n" +
            "\"sizes\": [{\n" +
            "\"height\": 130,\n" +
            "\"url\": \"https://sun9-42.u...YGjQ&type=album\",\n" +
            "\"type\": \"m\",\n" +
            "\"width\": 92\n" +
            "}, {\n" +
            "\"height\": 183,\n" +
            "\"url\": \"https://sun9-42.u...cuFs&type=album\",\n" +
            "\"type\": \"o\",\n" +
            "\"width\": 130\n" +
            "}, {\n" +
            "\"height\": 282,\n" +
            "\"url\": \"https://sun9-42.u...3EAQ&type=album\",\n" +
            "\"type\": \"p\",\n" +
            "\"width\": 200\n" +
            "}, {\n" +
            "\"height\": 452,\n" +
            "\"url\": \"https://sun9-42.u...RdTQ&type=album\",\n" +
            "\"type\": \"q\",\n" +
            "\"width\": 320\n" +
            "}, {\n" +
            "\"height\": 720,\n" +
            "\"url\": \"https://sun9-42.u...Vl28&type=album\",\n" +
            "\"type\": \"r\",\n" +
            "\"width\": 510\n" +
            "}, {\n" +
            "\"height\": 75,\n" +
            "\"url\": \"https://sun9-42.u...4An8&type=album\",\n" +
            "\"type\": \"s\",\n" +
            "\"width\": 53\n" +
            "}, {\n" +
            "\"height\": 1600,\n" +
            "\"url\": \"https://sun9-42.u...zHGU&type=album\",\n" +
            "\"type\": \"w\",\n" +
            "\"width\": 1133\n" +
            "}, {\n" +
            "\"height\": 604,\n" +
            "\"url\": \"https://sun9-42.u...Iq-Q&type=album\",\n" +
            "\"type\": \"x\",\n" +
            "\"width\": 428\n" +
            "}, {\n" +
            "\"height\": 807,\n" +
            "\"url\": \"https://sun9-42.u...vUPU&type=album\",\n" +
            "\"type\": \"y\",\n" +
            "\"width\": 572\n" +
            "}, {\n" +
            "\"height\": 1080,\n" +
            "\"url\": \"https://sun9-42.u...KHaw&type=album\",\n" +
            "\"type\": \"z\",\n" +
            "\"width\": 765\n" +
            "}],\n" +
            "\"text\": \"\",\n" +
            "\"user_id\": 100\n" +
            "}\n" +
            "}],\n" +
            "\"post_source\": {\n" +
            "\"platform\": \"android\",\n" +
            "\"type\": \"api\"\n" +
            "},\n" +
            "\"comments\": {\n" +
            "\"can_post\": 1,\n" +
            "\"count\": 0,\n" +
            "\"groups_can_post\": true\n" +
            "},\n" +
            "\"likes\": {\n" +
            "\"can_like\": 1,\n" +
            "\"count\": 1,\n" +
            "\"user_likes\": 0,\n" +
            "\"can_publish\": 1\n" +
            "},\n" +
            "\"reposts\": {\n" +
            "\"count\": 0,\n" +
            "\"user_reposted\": 0\n" +
            "},\n" +
            "\"views\": {\n" +
            "\"count\": 6\n" +
            "},\n" +
            "\"is_favorite\": false,\n" +
            "\"donut\": {\n" +
            "\"is_donut\": false\n" +
            "},\n" +
            "\"short_text_rate\": 0.8\n" +
            "}],\n" +
            "\"next_from\": \"1/-204255031_995\",\n" +
            "\"count\": 1000,\n" +
            "\"total_count\": 108858\n" +
            "}\n" +
            "}"
}