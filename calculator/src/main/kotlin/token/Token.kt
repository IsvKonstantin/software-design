package token

import token.BraceType.*
import visitor.TokenVisitor

interface Token {
    fun accept(visitor: TokenVisitor)
}

data class NumberToken(val value: Int) : Token {
    override fun accept(visitor: TokenVisitor) = visitor.visit(this)

    override fun toString(): String = "NUMBER($value)"
}

data class Brace(val value: Char) : Token {
    val type = if (value == '(') LEFT else RIGHT

    override fun accept(visitor: TokenVisitor) = visitor.visit(this)

    override fun toString(): String = type.toString().uppercase()
}

data class Operation(val value: Char) : Token {
    val priority = getPriority(value)

    override fun accept(visitor: TokenVisitor) = visitor.visit(this)

    override fun toString(): String = operationMap[value]!!
}

enum class BraceType {
    LEFT, RIGHT
}

private val operationMap = mapOf('+' to "PLUS", '-' to "MINUS", '*' to "MUL", '/' to "DIV")

private fun getPriority(operation: Char): Int {
    return when (operation) {
        '+', '-' -> 2
        '*', '/' -> 1
        else -> throw IllegalArgumentException("Illegal operation")
    }
}
