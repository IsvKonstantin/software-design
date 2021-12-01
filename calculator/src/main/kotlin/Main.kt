import token.Tokenizer
import visitor.CalcVisitor
import visitor.ParserVisitor
import visitor.PrintVisitor

fun main(args: Array<String>) {
    try {
        val expression = args.firstOrNull() ?: "(23 + 10) * 5 - 3 * (32 + 5) * (10 - 4 * 5) + 8 / 2"

        val tokenizer = Tokenizer()
        val parserVisitor = ParserVisitor()
        val calcVisitor = CalcVisitor()

        var tokens = tokenizer.tokenize(expression)
        println("Parsed tokens:")
        PrintVisitor().println(tokens)
        println()

        tokens = parserVisitor.convert(tokens)
        println("Converted tokens (RPN):")
        PrintVisitor().println(tokens)
        println()

        val result = calcVisitor.evaluate(tokens)
        println("Result = $result")
    } catch (e: RuntimeException) {
        println(e.message)
    }
}