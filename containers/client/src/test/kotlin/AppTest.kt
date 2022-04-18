import org.junit.Assert
import org.junit.ClassRule
import org.junit.Test
import org.testcontainers.containers.FixedHostPortGenericContainer
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class AppTest {
    @Test
    @Throws(Exception::class)
    fun test() {
        val request = HttpRequest.newBuilder()
            .uri(URI("http://localhost:8080/hello?name=Flexatroid"))
            .GET()
            .build()
        val response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString())
        Assert.assertEquals("Hello, Flexatroid!", response.body())
    }

    companion object {
        @ClassRule @JvmField
        var simpleWebServer: FixedHostPortGenericContainer<*> = FixedHostPortGenericContainer("market:1.0-SNAPSHOT")
            .withFixedExposedPort(8080, 8080)
            .withExposedPorts(8080)
    }
}

