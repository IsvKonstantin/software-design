package app.market

interface MarketRepository {
    fun getStockPrice(id: Int): Double?

    fun getStockAmount(id: Int): Int?

    fun getStockName(id: Int): String?

    fun purchaseStocks(id: Int, amount: Int): Boolean

    fun sellStocks(id: Int, amount: Int): Boolean

    fun createStock(name: String): Int

    fun addStocks(id: Int, amount: Int): Boolean

    fun changePrice(id: Int, difference: Double): Boolean

    fun refresh()
}