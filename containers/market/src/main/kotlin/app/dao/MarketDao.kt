package app.dao

import app.model.Stock


interface MarketDao {
    fun createStock(name: String): Int

    fun getStock(id: Int): Stock?

    fun addStocks(id: Int, amount: Int): Boolean

    fun changePrice(id: Int, difference: Double): Boolean

    fun purchaseStocks(id: Int, amount: Int): Boolean

    fun sellStocks(id: Int, amount: Int): Double

    fun refresh()
}