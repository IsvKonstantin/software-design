package visitor

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import token.Brace
import token.NumberToken
import token.Operation

internal class CalcVisitorTest {
    @Test
    fun `CalcVisitor simple test`() {
        val calcVisitor = CalcVisitor()
        val tokens = listOf(
            NumberToken(10),
            NumberToken(5),
            Operation('+'),
        )

        assertThat(calcVisitor.evaluate(tokens)).isEqualTo(15.0)
    }

    @Test
    fun `CalcVisitor complex test`() {
        val calcVisitor = CalcVisitor()
        val tokens = listOf(
            NumberToken(10),
            NumberToken(5),
            NumberToken(6),
            NumberToken(2),
            Operation('/'),
            NumberToken(3),
            Operation('-'),
            Operation('*'),
            Operation('+'),
        )

        assertThat(calcVisitor.evaluate(tokens)).isEqualTo(10.0)
    }

    @Test
    fun `CalcVisitor invalid expression`() {
        val calcVisitor = CalcVisitor()
        val tokens = listOf(
            NumberToken(5),
            Operation('+')
        )

        Assertions.assertThatThrownBy { calcVisitor.evaluate(tokens) }
            .isInstanceOf(RuntimeException::class.java)
            .hasMessage("Invalid expression")
    }

    @Test
    fun `CalcVisitor has brace in input tokens`() {
        val calcVisitor = CalcVisitor()
        val tokens = listOf(
            NumberToken(5),
            NumberToken(5),
            Operation('+'),
            Brace(')')
        )

        Assertions.assertThatThrownBy { calcVisitor.evaluate(tokens) }
            .isInstanceOf(RuntimeException::class.java)
            .hasMessage("Found brace in RPN")
    }

    @Test
    fun `empty test`() {
        val calcVisitor = CalcVisitor()

        Assertions.assertThatThrownBy { calcVisitor.evaluate(emptyList()) }
            .isInstanceOf(RuntimeException::class.java)
            .hasMessage("Invalid expression")
    }
}