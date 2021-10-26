import java.io.BufferedReader
import java.io.IOException
import java.io.UncheckedIOException
import java.net.MalformedURLException
import java.net.URL

class UrlReader {
    fun readAsText(sourceUrl: String): String {
        val url = sourceUrl.toUrl()

        return try {
            url.openStream()
                .bufferedReader()
                .use(BufferedReader::readText)
        } catch (e: IOException) {
            throw UncheckedIOException(e)
        }
    }

    private fun String.toUrl(): URL =
        try {
            URL(this)
        } catch (e: MalformedURLException) {
            throw RuntimeException("Malformed url: $this")
        }
}