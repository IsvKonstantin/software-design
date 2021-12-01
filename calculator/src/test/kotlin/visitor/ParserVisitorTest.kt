package visitor

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import token.Brace
import token.NumberToken
import token.Operation

internal class ParserVisitorTest {

    @Test
    fun `ParserVisitor simple test`() {
        val parserVisitor = ParserVisitor()
        val tokens = listOf(NumberToken(10), Operation('+'), NumberToken(5))
        val tokensConverted = listOf(NumberToken(10), NumberToken(5), Operation('+'))

        assertThat(parserVisitor.convert(tokens)).isEqualTo(tokensConverted)
    }

    @Test
    fun `ParserVisitor complex test`() {
        val parserVisitor = ParserVisitor()
        val tokens = listOf(
            NumberToken(10),
            Operation('+'),
            NumberToken(5),
            Operation('*'),
            Brace('('),
            NumberToken(6),
            Operation('/'),
            NumberToken(2),
            Operation('-'),
            NumberToken(3),
            Brace(')')
        )
        val tokensConverted = listOf(
            NumberToken(10),
            NumberToken(5),
            Operation('+'),
            NumberToken(6),
            NumberToken(2),
            NumberToken(3),
            Operation('-'),
            Operation('/'),
            Operation('*'),
        )

        assertThat(parserVisitor.convert(tokens)).isEqualTo(tokensConverted)
    }

    @Test
    fun `missing brace`() {
        val parserVisitor = ParserVisitor()
        val tokens = listOf(
            NumberToken(10),
            Brace(')')
        )

        assertThatThrownBy { parserVisitor.convert(tokens) }
            .isInstanceOf(RuntimeException::class.java)
            .hasMessage("Brace mismatch")
    }
}