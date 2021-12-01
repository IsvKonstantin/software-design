package visitor

import token.*
import java.util.*

class ParserVisitor : TokenVisitor {
    private val tokens: MutableList<Token> = mutableListOf()
    private val stack: Deque<Token> = ArrayDeque()

    override fun visit(token: NumberToken) {
        tokens.add(token)
    }

    override fun visit(token: Brace) {
        when (token.type) {
            BraceType.LEFT -> stack.push(token)
            BraceType.RIGHT -> {
                tokens.let { list ->
                    repeat(stack.takeWhile { t -> t !is Brace }.size) {
                        list.add(stack.pop())
                    }
                }

                stack.poll() ?: throw RuntimeException("Brace mismatch")
            }
        }
    }

    override fun visit(token: Operation) {
        tokens.let { list ->
            repeat(stack.takeWhile { it is Operation && it.priority >= token.priority }.size) {
                list.add(stack.pop())
            }
        }

        stack.push(token)
    }

    fun convert(tokens: List<Token>): List<Token> {
        tokens.forEach { it.accept(this) }
        this.tokens.addAll(stack)

        return this.tokens.toList()
    }
}