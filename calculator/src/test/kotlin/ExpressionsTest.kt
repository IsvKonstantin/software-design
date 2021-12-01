import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import token.Tokenizer
import visitor.CalcVisitor
import visitor.ParserVisitor

@DisplayName("Testing correct expressions")
internal class ExpressionsTest {
    @Test
    fun `test expression 1`() {
        val expression = "(23 + 10) * 5 - 3 * (32 + 5) * (10 - 4 * 5) + 8 / 2"

        val tokenizer = Tokenizer()
        val parserVisitor = ParserVisitor()
        val calcVisitor = CalcVisitor()

        val result = calcVisitor.evaluate(parserVisitor.convert(tokenizer.tokenize(expression)))

        assertThat(result).isEqualTo(1279.0)
    }

    @Test
    fun `test expression 2`() {
        val expression = "1 + 1"

        val tokenizer = Tokenizer()
        val parserVisitor = ParserVisitor()
        val calcVisitor = CalcVisitor()

        val result = calcVisitor.evaluate(parserVisitor.convert(tokenizer.tokenize(expression)))

        assertThat(result).isEqualTo(2.0)
    }

    @Test
    fun `test expression 3`() {
        val expression = "10 + 5 * (6 / 3 - 2)"

        val tokenizer = Tokenizer()
        val parserVisitor = ParserVisitor()
        val calcVisitor = CalcVisitor()

        val result = calcVisitor.evaluate(parserVisitor.convert(tokenizer.tokenize(expression)))

        assertThat(result).isEqualTo(10.0)
    }

    @Test
    fun `test expression 4`() {
        val expression = "1 * 2 * 3 * 4 * 5 * 6 * (0 - 1)"

        val tokenizer = Tokenizer()
        val parserVisitor = ParserVisitor()
        val calcVisitor = CalcVisitor()

        val result = calcVisitor.evaluate(parserVisitor.convert(tokenizer.tokenize(expression)))

        assertThat(result).isEqualTo(-720.0)
    }

    @Test
    fun `test expression 5`() {
        val expression = "(((0 * (1 - 0 * 15)))) * (((((10)))))"

        val tokenizer = Tokenizer()
        val parserVisitor = ParserVisitor()
        val calcVisitor = CalcVisitor()

        val result = calcVisitor.evaluate(parserVisitor.convert(tokenizer.tokenize(expression)))

        assertThat(result).isEqualTo(0.0)
    }
}