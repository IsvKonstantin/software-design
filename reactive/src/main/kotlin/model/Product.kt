package model

import org.bson.Document

enum class CURRENCY(var rate: Double = 1.0) {
    USD(80.75), EUR(89.42), RUB(1.0)
}

data class Product(val name: String, val price: Double) {
    constructor(doc: Document) : this(doc.getString("name"), doc.getDouble("price"))

    fun toString(currencyString: String): String {
        val currency = when (currencyString) {
            "RUB", "USD", "EUR" -> CURRENCY.valueOf(currencyString)
            else -> CURRENCY.RUB
        }

        return "Product: $name\nPrice: ${price / currency.rate} $currency"
    }
}