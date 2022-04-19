package model

import org.bson.Document

data class User(val id: Int, val username: String, val currency: String) {
    constructor(doc: Document) : this(doc.getInteger("id"), doc.getString("username"), doc.getString("currency"))

    override fun toString(): String {
        return "$id:$username:$currency"
    }
}
