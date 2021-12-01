package visitor

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import token.Brace
import token.NumberToken
import token.Operation
import java.io.ByteArrayOutputStream
import java.io.PrintStream


internal class PrintVisitorTest {
    private val outContent = ByteArrayOutputStream()
    private val originalOut = System.out

    @BeforeEach
    fun setUpStreams() {
        System.setOut(PrintStream(outContent))
    }

    @AfterEach
    fun restoreStreams() {
        System.setOut(originalOut)
    }

    @Test
    fun `PrintVisitor test`() {
        val printVisitor = PrintVisitor()
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
        printVisitor.println(tokens)
        assertThat(outContent.toString())
            .isEqualToIgnoringNewLines("[NUMBER(10) PLUS NUMBER(5) MUL LEFT NUMBER(6) DIV NUMBER(2) MINUS NUMBER(3) RIGHT]")
    }

    @Test
    fun `empty test`() {
        val printVisitor = PrintVisitor()
        printVisitor.println(emptyList())
        assertThat(outContent.toString())
            .isEqualToIgnoringNewLines("[]")
    }
}