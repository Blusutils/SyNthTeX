package net.blusutils.synthtex

/**
 * A simple lexer for a TeX-like syntax.
 *
 * The lexer reads the input string and returns a list of tokens,
 * which present a raw text, commands and control braces.
 *
 * Note that the lexer does not validate the input string.
 *
 * Key notes about the syntax:
 * - text after '%' and till the end of the line is ignored (comments)
 * - text after a backslash is considered as a command
 * - square brackets (named as Brackets in the lexer) and curly brackets (named as Braces in the lexer) are parsed as separate tokens
 * - double backslash is parsed as a single backslash
 * - there is no support for escaping characters inside the text
 *
 * The following text would be parsed as follows:
 *
 *      \bold{This is \emph{bold} \\ \emph{italic} text.}
 *      % then include a file
 *      \include[sample]{texsamples}{main}
 * ...
 *
 *     listOf(
 *         Token.Command("bold"),
 *         Token.OpenBrace,
 *         Token.Text("This is "),
 *         Token.Command("emph"),
 *         Token.OpenBrace,
 *         Token.Text("bold"),
 *         Token.CloseBrace,
 *         Token.Text(" \\ "),
 *         Token.Command("emph"),
 *         Token.OpenBrace,
 *         Token.Text("italic"),
 *         Token.CloseBrace,
 *         Token.Text(" text."),
 *         Token.CloseBrace,
 *         Token.Command("include"),
 *         Token.OpenBracket,
 *         Token.Text("sample"),
 *         Token.CloseBracket,
 *         Token.OpenBrace,
 *         Token.Text("texsamples"),
 *         Token.CloseBrace,
 *         Token.OpenBrace,
 *         Token.Text("main"),
 *         Token.CloseBrace
 *     )
 * @param input The input string to be parsed.
 * @author EgorBron <bron@blusutils.net>
 * @since 1.0.0
 * @version 1.0.0
 */
class Lexer(private val input: String) {
    /**
     * Internal value of the current position in the input string.
     */
    private var pos = 0

    /**
     * Tokenizes the input string and returns a list of tokens.
     * @return A list of tokens parsed from the input string.
     */
    fun tokenize(): List<Token> {
        val tokens = mutableListOf<Token>()
        while (pos < input.length) {
            when (input[pos]) {
                '\\' -> {
                    // If the next character is also a backslash, treat it as an escaped backslash.
                    if (pos + 1 < input.length && input[pos + 1] == '\\') {
                        tokens.add(Token.Text("\\"))
                        pos += 2
                    } else {
                        pos++ // Skip the backslash.
                        // Read the command name (sequence of letters).
                        val start = pos
                        while (pos < input.length && input[pos].isLetter()) {
                            pos++
                        }
                        val commandName = input.substring(start, pos)
                        tokens.add(Token.Command(commandName))
                    }
                }
                '{' -> {
                    tokens.add(Token.OpenBrace)
                    pos++
                }
                '}' -> {
                    tokens.add(Token.CloseBrace)
                    pos++
                }
                '[' -> {
                    tokens.add(Token.OpenBracket)
                    pos++
                }
                ']' -> {
                    tokens.add(Token.CloseBracket)
                    pos++
                }
                '%' -> {
                    // Skip the comment until the end of the line.
                    pos++ // Skip '%'
                    while (pos < input.length && input[pos] != '\n') {
                        pos++
                    }
                }
                else -> {
                    // Collect plain text until a special character is encountered.
                    val start = pos
                    while (pos < input.length &&
                        input[pos] != '\\' &&
                        input[pos] != '{' &&
                        input[pos] != '}' &&
                        input[pos] != '[' &&
                        input[pos] != ']' &&
                        input[pos] != '%'
                    ) {
                        pos++
                    }
                    tokens.add(Token.Text(input.substring(start, pos)))
                }
            }
        }
        return tokens
    }
}