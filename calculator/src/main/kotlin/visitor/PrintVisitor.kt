package visitor

import token.Brace
import token.NumberToken
import token.Operation
import token.Token

class PrintVisitor : TokenVisitor {
    private val tokens: MutableList<Token> = mutableListOf()

    override fun visit(token: NumberToken) {
        tokens.add(token)
    }

    override fun visit(token: Brace) {
        tokens.add(token)
    }

    override fun visit(token: Operation) {
        tokens.add(token)
    }

    fun println(tokens: List<Token>) {
        tokens.forEach { it.accept(this) }
        println(this.tokens.joinToString(" ", "[", "]"))
    }
}