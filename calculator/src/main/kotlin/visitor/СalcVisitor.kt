package visitor

import token.*
import java.util.*

class CalcVisitor : TokenVisitor {
    private val stack: Deque<Double> = ArrayDeque()

    override fun visit(token: NumberToken) {
        stack.push(token.value.toDouble())
    }

    override fun visit(token: Brace) {
        throw RuntimeException("Found brace in RPN")
    }

    override fun visit(token: Operation) {
        if (stack.size < 2) {
            throw RuntimeException("Invalid expression")
        }

        val y = stack.removeFirst()
        val x = stack.removeFirst()

        when (token.type) {
            OperationType.PLUS -> stack.push(x + y)
            OperationType.MINUS -> stack.push(x - y)
            OperationType.MUL -> stack.push(x * y)
            OperationType.DIV -> stack.push(x / y)
        }
    }

    fun evaluate(tokens: List<Token>): Double {
        tokens.forEach { it.accept(this) }
        return stack.poll() ?: throw RuntimeException("Invalid expression")
    }
}