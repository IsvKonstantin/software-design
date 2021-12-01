package token

import visitor.TokenVisitor

interface Token {
    fun accept(visitor: TokenVisitor)
}

data class NumberToken(val value: Int) : Token {
    override fun accept(visitor: TokenVisitor) = visitor.visit(this)

    override fun toString(): String = value.toString()
}

data class Brace(val value: Char) : Token {
    override fun accept(visitor: TokenVisitor) = visitor.visit(this)

    override fun toString(): String = value.toString()
}

data class Operation(val value: Char) : Token {
    override fun accept(visitor: TokenVisitor) = visitor.visit(this)

    override fun toString(): String = value.toString()
}