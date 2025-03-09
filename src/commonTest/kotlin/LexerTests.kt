import net.blusutils.synthtex.Lexer
import net.blusutils.synthtex.Token
import kotlin.test.*

class LexerTests {
    @Test
    fun testEmptyInput() {
        val lexer = Lexer("")
        val tokens = lexer.tokenize()
        assertTrue(tokens.isEmpty())
    }

    @Test
    fun testPlainText() {
        val lexer = Lexer("Hello world")
        val tokens = lexer.tokenize()
        assertEquals(1, tokens.size)
        assertEquals(Token.Text("Hello world"), tokens[0])
    }

    @Test
    fun testSimpleCommand() {
        val lexer = Lexer("\\bold")
        val tokens = lexer.tokenize()
        assertEquals(1, tokens.size)
        assertEquals(Token.Command("bold"), tokens[0])
    }

    @Test
    fun testEscapedBackslash() {
        val lexer = Lexer("\\\\")
        val tokens = lexer.tokenize()
        assertEquals(1, tokens.size)
        assertEquals(Token.Text("\\"), tokens[0])
    }

    @Test
    fun testComment() {
        val lexer = Lexer("Hello % this is comment\nworld")
        val tokens = lexer.tokenize()
        assertEquals(2, tokens.size)
        assertEquals(Token.Text("Hello "), tokens[0])
        assertEquals(Token.Text("\nworld"), tokens[1])
    }

    @Test
    fun testBrackets() {
        val lexer = Lexer("[option]")
        val tokens = lexer.tokenize()
        assertEquals(3, tokens.size)
        assertEquals(Token.OpenBracket, tokens[0])
        assertEquals(Token.Text("option"), tokens[1])
        assertEquals(Token.CloseBracket, tokens[2])
    }

    @Test
    fun testBraces() {
        val lexer = Lexer("{content}")
        val tokens = lexer.tokenize()
        assertEquals(3, tokens.size)
        assertEquals(Token.OpenBrace, tokens[0])
        assertEquals(Token.Text("content"), tokens[1])
        assertEquals(Token.CloseBrace, tokens[2])
    }

    @Test
    fun testComplexStructure() {
        val lexer = Lexer("\\command[opt]{\\nested{value}}")
        val tokens = lexer.tokenize()
        assertEquals(listOf(
            Token.Command("command"),
            Token.OpenBracket,
            Token.Text("opt"),
            Token.CloseBracket,
            Token.OpenBrace,
            Token.Command("nested"),
            Token.OpenBrace,
            Token.Text("value"),
            Token.CloseBrace,
            Token.CloseBrace
        ), tokens)
    }

    @Test
    fun testMultipleCommands() {
        val lexer = Lexer("\\first\\second\\third")
        val tokens = lexer.tokenize()
        assertEquals(listOf(
            Token.Command("first"),
            Token.Command("second"),
            Token.Command("third")
        ), tokens)
    }

    @Test
    fun testMixedContent() {
        val lexer = Lexer("Text \\cmd{value} more text")
        val tokens = lexer.tokenize()
        assertEquals(listOf(
            Token.Text("Text "),
            Token.Command("cmd"),
            Token.OpenBrace,
            Token.Text("value"),
            Token.CloseBrace,
            Token.Text(" more text")
        ), tokens)
    }
}
