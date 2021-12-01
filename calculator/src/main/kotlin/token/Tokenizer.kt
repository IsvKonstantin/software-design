package token

class TokenizerException(message: String) : RuntimeException(message)

abstract class State {
    abstract fun next(tokens: MutableList<Token>): State
}

class Tokenizer {
    private var input: String = ""
    private var position: Int = 0
    private var state: State = Start()

    companion object {
        private val digitChars = '0'..'9'
        private val operationChars = listOf('+', '-', '*', '/')
        private val braceChars = listOf('(', ')')
    }

    fun tokenize(input: String): List<Token> {
        this.input = input
        this.position = 0
        this.state = Start()

        val tokens = mutableListOf<Token>()
        while (state !is End) {
            state = state.next(tokens)
        }

        return tokens
    }

    private inner class Start : State() {
        override fun next(tokens: MutableList<Token>): State {
            when {
                position >= input.length -> return End()
                Character.isWhitespace(input[position]) -> return Whitespace()
                input[position] in digitChars -> return Number()
                input[position] in braceChars -> tokens.add(Brace(input[position]))
                input[position] in operationChars -> tokens.add(Operation(input[position]))
                else -> throw TokenizerException("Invalid char: ${input[position]}")
            }

            position++
            return this
        }
    }

    private inner class Number : State() {
        private var numberString = ""

        override fun next(tokens: MutableList<Token>): State {
            return when {
                position >= input.length -> {
                    tokens.add(NumberToken(numberString.toInt()))
                    End()
                }
                Character.isDigit(input[position]) -> {
                    numberString += input[position]
                    position++
                    this
                }
                else -> {
                    tokens.add(NumberToken(numberString.toInt()))
                    Start()
                }
            }
        }
    }

    private inner class Whitespace : State() {
        override fun next(tokens: MutableList<Token>): State {
            return when {
                position >= input.length -> End()
                Character.isWhitespace(input[position]) -> {
                    position++
                    this
                }
                else -> Start()
            }
        }
    }

    private inner class End : State() {
        override fun next(tokens: MutableList<Token>): State {
            return this
        }
    }
}