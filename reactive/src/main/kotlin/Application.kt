import database.Database
import server.Server

fun main() {
    val database = Database()
    Server(database).runServer()
}