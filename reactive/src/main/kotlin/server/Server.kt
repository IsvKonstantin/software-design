package server


import database.Database
import io.reactivex.netty.protocol.http.server.HttpServer
import rx.Observable

class Server(private val database: Database) {
    fun runServer() {
        HttpServer.newServer(8080).start { serverRequest, serverResponse ->
            val request = serverRequest.decodedPath.substring(1).split("/").toTypedArray()
            when (request[0]) {
                "add-product" -> {
                    val name = serverRequest.queryParameters["name"]!!.first()
                    val price = serverRequest.queryParameters["price"]!!.first().toDouble()
                    database.addProduct(name, price)
                    val response = Observable.just("Added product: [$name:$price]")

                    return@start serverResponse.writeString(response)
                }
                "get-users" -> {
                    val users = database.getUsers()
                    val response = users.map { "$it\n" }

                    return@start serverResponse.writeString(response)
                }
                "get-products" -> {
                    val id = serverRequest.queryParameters["id"]!!.first().toInt()
                    val products = database.getProducts()
                    val user = database.getUser(id)
                    val response =
                        user.map { it.currency }.flatMap { currency -> products.map { "${it.toString(currency)}\n" } }

                    return@start serverResponse.writeString(response)
                }
                "create-user" -> {
                    val id = serverRequest.queryParameters["id"]!!.first().toInt()
                    val username = serverRequest.queryParameters["username"]!!.first()
                    val currency = serverRequest.queryParameters["currency"]!!.first()


                    database.createUser(id, username, currency)
                    val response = Observable.just("Created user: [$id:$username:$currency]")

                    return@start serverResponse.writeString(response)
                }
                else -> {
                    val response = Observable.just("Invalid request")

                    return@start serverResponse.writeString(response)
                }
            }
        }.awaitShutdown()
    }
}