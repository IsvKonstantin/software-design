package app.market

import org.springframework.http.HttpStatus
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration


class MarketRepositoryImpl(
    private val host: String,
    private val port: Int,
    timeoutDuration: Duration
) : MarketRepository {
    var client: HttpClient = HttpClient.newBuilder()
        .connectTimeout(timeoutDuration)
        .build()

    override fun getStockPrice(id: Int): Double? {
        val uriString = "$host:$port/stock-price?id=$id"
        val response: HttpResponse<String> = getResponse(uriString)

        return if (response.statusCode() == HttpStatus.OK.value()) {
            response.body().toDouble()
        } else null
    }

    override fun getStockAmount(id: Int): Int? {
        val uriString = "$host:$port/stock-amount?id=$id"
        val response = getResponse(uriString)

        return if (response.statusCode() == HttpStatus.OK.value()) {
            response.body().toInt()
        } else null
    }

    override fun getStockName(id: Int): String? {
        val uriString = "$host:$port/stock-name?id=$id"
        val response = getResponse(uriString)

        return if (response.statusCode() == HttpStatus.OK.value()) {
            response.body()
        } else null
    }

    override fun purchaseStocks(id: Int, amount: Int): Boolean {
        val uriString = "$host:$port/purchase-stocks?id=$id&amount=$amount"
        val response = getResponse(uriString)

        return response.statusCode() == HttpStatus.OK.value()
    }

    override fun sellStocks(id: Int, amount: Int): Boolean {
        val uriString = "$host:$port/sell-stocks?id=$id&amount=$amount"
        val response = getResponse(uriString)

        return response.statusCode() == HttpStatus.OK.value()
    }

    override fun createStock(name: String): Int {
        val uriString = "$host:$port/super/create-stock?name=$name"
        val response = getResponse(uriString)

        return response.body().toInt()
    }

    override fun addStocks(id: Int, amount: Int): Boolean {
        val uriString = "$host:$port/super/add-stocks?id=$id&amount=$amount"
        val response = getResponse(uriString)

        return response.statusCode() == HttpStatus.OK.value()
    }

    override fun changePrice(id: Int, difference: Double): Boolean {
        val uriString = "$host:$port/super/change-price?id=$id&difference=$difference"
        val response = getResponse(uriString)

        return response.statusCode() == HttpStatus.OK.value()
    }

    override fun refresh() {
        val uriString = "$host:$port/super/refresh"
        val response = getResponse(uriString)
    }

    private fun getResponse(uriString: String): HttpResponse<String> {
        val request: HttpRequest = HttpRequest.newBuilder()
            .uri(URI(uriString))
            .GET()
            .build()

        return client.send(request, HttpResponse.BodyHandlers.ofString())
    }
}