package database

import com.mongodb.client.model.Filters.eq
import com.mongodb.rx.client.MongoClient
import com.mongodb.rx.client.MongoClients
import com.mongodb.rx.client.MongoDatabase
import model.Product
import model.User
import org.bson.Document
import rx.Observable
import rx.Subscription

class Database {
    companion object {
        private lateinit var client: MongoClient
        private lateinit var database: MongoDatabase
        const val CONNECTION_URL = "mongodb://localhost:27017"
        private const val DATABASE_NAME = "reactive"
    }

    init {
        client = MongoClients.create(CONNECTION_URL)
        database = client.getDatabase(DATABASE_NAME)
    }

    fun getProducts(): Observable<Product> {
        return database
            .getCollection("PRODUCTS")
            .find()
            .toObservable()
            .map { Product(it) }
    }

    fun addProduct(name: String, price: Double): Subscription {
        return database
            .getCollection("PRODUCTS")
            .insertOne(Document(mapOf("name" to name, "price" to price)))
            .subscribe()
    }

    fun getUser(id: Int): Observable<User> {
        return database
            .getCollection("USERS")
            .find(eq("id", id))
            .first()
            .map { User(it) }
    }

    fun getUsers(): Observable<User> {
        return database
            .getCollection("USERS")
            .find()
            .toObservable()
            .map { User(it) }
    }

    fun createUser(id: Int, username: String, currency: String): Subscription {
        return database
            .getCollection("USERS")
            .insertOne(Document(mapOf("id" to id, "username" to username, "currency" to currency)))
            .subscribe()
    }
}
