package app.dao

import app.model.Client
import app.model.ClientStock
import app.market.MarketRepository


class ClientInMemoryDao(private val repository: MarketRepository) : ClientDao {
    private val clients: ArrayList<Client> = ArrayList()

    override fun createClient(name: String): Int {
        clients.add(Client(clients.size, name, hashMapOf(), 0.0))

        return clients.size - 1
    }

    override fun calculateNetWorth(id: Int): Double {
        return getStocks(id).sumOf { it.amount * repository.getStockPrice(it.id)!! } + clients[id].balance
    }

    override fun getStocks(id: Int): List<ClientStock> {
        val stocks = ArrayList<ClientStock>()

        clients[id].stocks.forEach{ e->
            stocks.add(ClientStock(e.key, repository.getStockName(e.key)!!, e.value))
        }

        return stocks
    }

    override fun updateBalance(id: Int, newBalance: Double): Boolean {
        return if (newBalance > 0.0) {
            clients[id].balance = newBalance
            true
        } else {
            false
        }
    }

    override fun purchaseStock(id: Int, companyId: Int, amount: Int): Boolean {
        val cost = repository.getStockPrice(companyId)!! * amount

        return when {
            clients[id].balance - cost < 0 -> {
                false
            }
            else -> if (repository.purchaseStocks(companyId, amount)) {
                clients[id].stocks[companyId] = clients[id].stocks.getOrDefault(companyId, 0) + amount
                clients[id].balance -= cost
                true
            } else {
                false
            }
        }

    }

    override fun sellStock(id: Int, companyId: Int, amount: Int): Boolean {
        val totalCost = repository.getStockPrice(companyId)!!

        return when {
            clients[id].stocks.getOrDefault(companyId, 0) < amount -> {
                false
            }
            else -> if (repository.sellStocks(companyId, amount)) {
                clients[id].stocks[companyId] = clients[id].stocks.getOrDefault(companyId, 0) - amount
                clients[id].balance += totalCost
                true
            } else {
                false
            }
        }

    }
}