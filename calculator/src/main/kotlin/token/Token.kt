package token

import visitor.TokenVisitor

interface Token {
    fun accept(visitor: TokenVisitor)
}

class NumberToken(val value: Int) : Token {
    override fun accept(visitor: TokenVisitor) = visitor.visit(this)
}

class Brace(val value: Char) : Token {
    override fun accept(visitor: TokenVisitor) = visitor.visit(this)
}

class Operation(val value: Char) : Token {
    override fun accept(visitor: TokenVisitor) = visitor.visit(this)
}