package visitor

import token.Brace
import token.NumberToken
import token.Operation

class PrintVisitor : TokenVisitor {
    override fun visit(token: NumberToken) {
        TODO("Not yet implemented")
    }

    override fun visit(token: Brace) {
        TODO("Not yet implemented")
    }

    override fun visit(token: Operation) {
        TODO("Not yet implemented")
    }
}