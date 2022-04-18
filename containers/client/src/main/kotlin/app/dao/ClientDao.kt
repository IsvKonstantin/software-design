package app.dao

import app.model.ClientStock


interface ClientDao {
    fun createClient(name: String): Int

    fun calculateNetWorth(id: Int): Double

    fun getStocks(id: Int): List<ClientStock>

    fun updateBalance(id: Int, newBalance: Double): Boolean

    fun purchaseStock(id: Int, companyId: Int, amount: Int): Boolean

    fun sellStock(id: Int, companyId: Int, amount: Int): Boolean
}