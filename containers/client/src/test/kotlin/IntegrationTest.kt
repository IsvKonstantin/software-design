import app.dao.ClientInMemoryDao
import app.market.MarketRepositoryImpl
import app.model.ClientStock
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.testcontainers.containers.FixedHostPortGenericContainer
import java.time.Duration


@Suppress("DEPRECATION")
class IntegrationTest {
    private lateinit var clientsDao: ClientInMemoryDao
    private lateinit var repository: MarketRepositoryImpl

    companion object {
        @ClassRule
        @JvmField
        var simpleWebServer: FixedHostPortGenericContainer<*> = FixedHostPortGenericContainer("market:1.0-SNAPSHOT")
            .withFixedExposedPort(8080, 8080)
            .withExposedPorts(8080)

        val stockMap = hashMapOf("Google" to 0, "Amazon" to 1, "Netflix" to 2)
    }

    @Before
    fun init() {
        repository = MarketRepositoryImpl("http://127.0.0.1", 8080, Duration.ofSeconds(30))
        clientsDao = ClientInMemoryDao(repository)

        val stock1 = repository.createStock("Google")
        val stock2 = repository.createStock("Amazon")
        val stock3 = repository.createStock("Netflix")

        repository.addStocks(stock1, 100)
        repository.addStocks(stock2, 50)
        repository.addStocks(stock3, 5)

        repository.changePrice(stock1, 10.0)
        repository.changePrice(stock2, 55.5)
        repository.changePrice(stock3, 5.0)
    }

    @After
    fun teardown() {
        repository.refresh()
    }

    @Test
    fun `test getStockPrice()`() {
        val incorrectId = 404

        assertThat(repository.getStockPrice(stockMap["Google"]!!)).isEqualTo(10.0)
        assertThat(repository.getStockPrice(stockMap["Amazon"]!!)).isEqualTo(55.5)
        assertThat(repository.getStockPrice(stockMap["Netflix"]!!)).isEqualTo(5.0)
        assertThat(repository.getStockPrice(incorrectId)).isNull()
    }

    @Test
    fun `test getStockAmount()`() {
        val incorrectId = 404

        assertThat(repository.getStockAmount(stockMap["Google"]!!)).isEqualTo(100)
        assertThat(repository.getStockAmount(stockMap["Amazon"]!!)).isEqualTo(50)
        assertThat(repository.getStockAmount(stockMap["Netflix"]!!)).isEqualTo(5)
        assertThat(repository.getStockAmount(incorrectId)).isNull()
    }

    @Test
    fun `test getStockName()`() {
        val incorrectId = 404

        assertThat(repository.getStockName(stockMap["Google"]!!)).isEqualTo("Google")
        assertThat(repository.getStockName(stockMap["Amazon"]!!)).isEqualTo("Amazon")
        assertThat(repository.getStockName(stockMap["Netflix"]!!)).isEqualTo("Netflix")
        assertThat(repository.getStockName(incorrectId)).isNull()
    }

    @Test
    fun `test createStock()`() {
        val expectedId = stockMap.size

        assertThat(repository.createStock("Yandex")).isEqualTo(expectedId)
        assertThat(repository.getStockName(expectedId)).isEqualTo("Yandex")
        assertThat(repository.getStockPrice(expectedId)).isEqualTo(0.0)
        assertThat(repository.getStockAmount(expectedId)).isEqualTo(0)
    }

    @Test
    fun `test changePrice()`() {
        val incorrectId = 404
        val previousPrice = repository.getStockPrice(stockMap["Google"]!!)!!

        assertThat(repository.changePrice(stockMap["Google"]!!, 20.0)).isTrue
        assertThat(repository.getStockPrice(stockMap["Google"]!!)).isEqualTo(previousPrice + 20.0)

        assertThat(repository.changePrice(incorrectId, 100.0)).isFalse
        assertThat(repository.changePrice(stockMap["Google"]!!, -1000.0)).isFalse
    }

    @Test
    fun `test client-market integration()`() {
        // Current stocks on the market:
        // Google -- 100 -- 10.0
        // Amazon -- 50 -- 55.5
        // Netflix -- 5 -- 5.0

        val client1 = clientsDao.createClient("Chovy")
        val client2 = clientsDao.createClient("Faker")

        // Clients were created, assigned with correct ids
        assertThat(client1).isEqualTo(0)
        assertThat(client2).isEqualTo(1)

        // By default, their net worth is zero. No stocks purchased
        assertThat(clientsDao.calculateNetWorth(client1)).isZero
        assertThat(clientsDao.calculateNetWorth(client2)).isZero
        assertThat(clientsDao.getStocks(client1)).isEmpty()
        assertThat(clientsDao.getStocks(client2)).isEmpty()

        // Updated client1 balance, purchase all Netflix stocks
        clientsDao.updateBalance(client1, 25.0)
        assertThat(clientsDao.calculateNetWorth(client1)).isEqualTo(25.0)
        clientsDao.purchaseStock(client1, stockMap["Netflix"]!!, 5)
        assertThat(clientsDao.calculateNetWorth(client1)).isEqualTo(25.0)
        assertThat(clientsDao.calculateNetWorth(client2)).isZero
        assertThat(repository.getStockAmount(stockMap["Netflix"]!!)).isZero
        assertThat(clientsDao.getStocks(client1)).isEqualTo(
            listOf(ClientStock(2, "Netflix", 5))
        )

        // Net worth changes when stocks price changes
        repository.changePrice(stockMap["Netflix"]!!, 5.0)
        assertThat(clientsDao.calculateNetWorth(client1)).isEqualTo(50.0)
        assertThat(clientsDao.calculateNetWorth(client2)).isZero

        // Cant buy out-of-stock
        clientsDao.updateBalance(client2, 2000.0)
        clientsDao.purchaseStock(client2, stockMap["Google"]!!, 60)
        clientsDao.purchaseStock(client2, stockMap["Amazon"]!!, 10)
        assertThat(clientsDao.purchaseStock(client2, stockMap["Netflix"]!!, 50)).isFalse
        assertThat(clientsDao.getStocks(client2)).isEqualTo(
            listOf(
                ClientStock(0, "Google", 60),
                ClientStock(1, "Amazon", 10)
            )
        )
        assertThat(clientsDao.calculateNetWorth(client2)).isEqualTo(2000.0)

        // Selling stocks for profit
        repository.changePrice(stockMap["Google"]!!, 10.0)
        clientsDao.sellStock(client2, stockMap["Google"]!!, 59)
        assertThat(repository.getStockAmount(stockMap["Google"]!!)).isEqualTo(99)
        assertThat(clientsDao.calculateNetWorth(client2)).isEqualTo(1440.0)
        assertThat(clientsDao.getStocks(client2)).isEqualTo(
            listOf(
                ClientStock(0, "Google", 1),
                ClientStock(1, "Amazon", 10)
            )
        )
    }
}