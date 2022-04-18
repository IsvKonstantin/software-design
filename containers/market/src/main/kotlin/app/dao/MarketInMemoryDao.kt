package app.dao

import app.model.Stock


class MarketInMemoryDao : MarketDao {
    private val stocks: ArrayList<Stock> = ArrayList()

    override fun createStock(name: String): Int {
        stocks.add(Stock(stocks.size, name, 0.0, 0))

        return stocks.size - 1
    }

    override fun getStock(id: Int): Stock? {
        return if (id >= stocks.size) null else stocks[id]
    }

    override fun addStocks(id: Int, amount: Int): Boolean {
        return when {
            id >= stocks.size || amount <= 0 -> {
                false
            }
            else -> if (stocks[id].amount + amount < 0) {
                false
            } else {
                stocks[id].amount += amount
                true
            }
        }
    }

    override fun changePrice(id: Int, difference: Double): Boolean {
        return when {
            id >= stocks.size || difference <= 0 -> {
                false
            }
            else -> if (stocks[id].amount + difference < 0) {
                false
            } else {
                stocks[id].price += difference
                true
            }
        }
    }

    override fun purchaseStocks(id: Int, amount: Int): Boolean {
        return when {
            id >= stocks.size || amount <= 0 -> {
                false
            }
            else -> if (stocks[id].amount - amount < 0) {
                false
            } else {
                stocks[id].amount -= amount
                true
            }
        }
    }

    override fun sellStocks(id: Int, amount: Int): Double {
        return when {
            id >= stocks.size || amount <= 0 -> {
                0.0
            }
            else -> {
                stocks[id].amount += amount
                return stocks[id].price * amount
            }
        }
    }

    override fun refresh() {
        stocks.clear()
    }
}