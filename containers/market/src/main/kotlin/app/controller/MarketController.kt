package app.controller

import app.dao.MarketDao
import app.dao.MarketInMemoryDao
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class ExchangeController {
    private val dao: MarketDao = MarketInMemoryDao()

    @RequestMapping("/stock-name")
    fun getStockName(id: Int): ResponseEntity<String> {
        val stock = dao.getStock(id)

        return if (stock != null) {
            ResponseEntity.ok(stock.companyName)
        } else ResponseEntity.status(BAD_REQUEST).body(null)
    }

    @RequestMapping("/stock-price")
    fun getStockPrice(id: Int): ResponseEntity<Double> {
        val stock = dao.getStock(id)

        return if (stock != null) {
            ResponseEntity.ok(stock.price)
        } else ResponseEntity.status(BAD_REQUEST).body(null)
    }

    @RequestMapping("/stock-amount")
    fun getStockCount(id: Int): ResponseEntity<Int> {
        val stock = dao.getStock(id)

        return if (stock != null) {
            ResponseEntity.ok(stock.amount)
        } else ResponseEntity.status(BAD_REQUEST).body(null)
    }

    @RequestMapping("/purchase-stocks")
    fun purchaseStocks(id: Int, amount: Int): ResponseEntity<Int> {
        return if (dao.purchaseStocks(id, amount)) {
            ResponseEntity.ok(amount)
        } else ResponseEntity.status(BAD_REQUEST).body(null)
    }

    @RequestMapping("/sell-stocks")
    fun sellStocks(id: Int, amount: Int): ResponseEntity<Double> {
        val profit = dao.sellStocks(id, amount)

        return if (profit != 0.0) {
            ResponseEntity.ok(profit)
        } else ResponseEntity.status(BAD_REQUEST).body(null)
    }

    @RequestMapping("/super/create-stock")
    fun createStock(name: String): ResponseEntity<Int> {
        return ResponseEntity.ok(dao.createStock(name))
    }

    @RequestMapping("/super/add-stocks")
    fun addStocks(id: Int, amount: Int): ResponseEntity<Int> {
        return if (dao.addStocks(id, amount)) {
            ResponseEntity.ok(amount)
        } else ResponseEntity.status(BAD_REQUEST).body(null)
    }

    @RequestMapping("/super/change-price")
    fun changeStock(id: Int, difference: Double): ResponseEntity<Double> {
        return if (dao.changePrice(id, difference)) {
            ResponseEntity.ok(difference)
        } else ResponseEntity.status(BAD_REQUEST).body(null)
    }

    @RequestMapping("/super/refresh")
    fun refresh(): ResponseEntity<String> {
        dao.refresh()
        return ResponseEntity.ok("refreshed")
    }
}