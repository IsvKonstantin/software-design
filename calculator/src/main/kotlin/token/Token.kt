package token

import token.BraceType.LEFT
import token.BraceType.RIGHT
import token.OperationType.*
import visitor.TokenVisitor

interface Token {
    fun accept(visitor: TokenVisitor)
}

data class NumberToken(val value: Int) : Token {
    override fun accept(visitor: TokenVisitor) = visitor.visit(this)

    override fun toString(): String = "NUMBER($value)"
}

data class Brace(val value: Char) : Token {
    val type = braceMap[value]!!

    override fun accept(visitor: TokenVisitor) = visitor.visit(this)

    override fun toString(): String = type.toString().uppercase()
}

data class Operation(val value: Char) : Token {
    val type = operationMap[value]!!
    val priority = getPriority(type)

    override fun accept(visitor: TokenVisitor) = visitor.visit(this)

    override fun toString(): String = type.toString().uppercase()
}

enum class BraceType {
    LEFT, RIGHT
}

enum class OperationType {
    PLUS, MINUS, MUL, DIV
}

private val operationMap = mapOf('+' to PLUS, '-' to MINUS, '*' to MUL, '/' to DIV)
private val braceMap = mapOf('(' to LEFT, ')' to RIGHT)

private fun getPriority(operationType: OperationType): Int {
    return when (operationType) {
        PLUS, MINUS -> 1
        MUL, DIV -> 2
    }
}
