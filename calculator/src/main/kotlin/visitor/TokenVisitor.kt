package visitor

import token.Brace
import token.NumberToken
import token.Operation

interface TokenVisitor {
    fun visit(token: NumberToken)

    fun visit(token: Brace)

    fun visit(token: Operation)
}