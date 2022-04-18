package app.model

import java.util.*

data class Client(val id: Int, val username: String, val stocks: HashMap<Int, Int>, var balance: Double)