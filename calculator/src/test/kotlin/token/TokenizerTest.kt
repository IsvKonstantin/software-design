package token

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

internal class TokenizerTest {
    private val tokenizer = Tokenizer()

    @Test
    fun `empty input test`() {
        assertThat(tokenizer.tokenize("")).isEqualTo(emptyList<Token>())
    }

    @Test
    fun `numbers test`() {
        assertThat(tokenizer.tokenize("  123 1 123")).isEqualTo(
            listOf(
                NumberToken(123), NumberToken(1), NumberToken(123)
            )
        )
    }

    @Test
    fun `invalid characters`() {
        assertThatThrownBy { tokenizer.tokenize("1 + b") }
            .isInstanceOf(RuntimeException::class.java)
            .hasMessage("Invalid char: b")
    }

    @Test
    fun `some tokenizer tests`() {
        val testString = "(23 + 10) * 5 - 3 * (32 + 5) * (10 - 4 * 5) + 8 / 2"
        val noWhitespaces = "(23+10)*5-3*(32+5)*(10-4*5)+8/2"
        val extraWhitespaces = " (  23   +  10   ) *5-3* (32 + 5) * (10 - 4 * 5) + 8/2   "

        val expected: List<Token> = listOf(
            Brace('('),
            NumberToken(23),
            Operation('+'),
            NumberToken(10),
            Brace(')'),
            Operation('*'),
            NumberToken(5),
            Operation('-'),
            NumberToken(3),
            Operation('*'),
            Brace('('),
            NumberToken(32),
            Operation('+'),
            NumberToken(5),
            Brace(')'),
            Operation('*'),
            Brace('('),
            NumberToken(10),
            Operation('-'),
            NumberToken(4),
            Operation('*'),
            NumberToken(5),
            Brace(')'),
            Operation('+'),
            NumberToken(8),
            Operation('/'),
            NumberToken(2),
        )

        assertThat(tokenizer.tokenize(testString)).isEqualTo(expected)
        assertThat(tokenizer.tokenize(noWhitespaces)).isEqualTo(expected)
        assertThat(tokenizer.tokenize(extraWhitespaces)).isEqualTo(expected)
    }
}