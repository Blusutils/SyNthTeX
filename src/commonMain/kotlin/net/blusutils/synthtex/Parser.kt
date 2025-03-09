package net.blusutils.synthtex

/**
 * A simple recursive parser for a TeX-like syntax.
 *
 * The parser reads the list of tokens and returns a list of nodes,
 * which present a plain text, commands, which consist of a command name
 * and a list of arguments with attributes,
 * and groups of those nodes.
 *
 * @param tokens The list of tokens to parse.
 * @author EgorBron <bron@blusutils.net>
 * @since 1.0.0
 * @version 1.0.0
 */
class Parser(private val tokens: List<Token>) {
    /**
     * An internal position in the list of tokens.
     */
    private var pos = 0

    /**
     * Parse the entire input into a list of Nodes.
     * @return A list of nodes parsed from the input tokens.
     * @throws IllegalStateException if the parser founds unexpected or missing brace.
     * @author EgorBron <bron@blusutils.net>
     * @since 1.0.0
     * @version 1.0.0
     */
    fun parse(): List<Node> {
        val nodes = mutableListOf<Node>()
        while (pos < tokens.size) {
            nodes.add(parseNode())
        }
        return nodes
    }

    /**
     * Parse a single node.
     * @return A single node parsed from the input tokens.
     * @throws IllegalStateException if the parser founds unexpected or missing brace.
     */
    private fun parseNode(): Node {
        return when (val token = tokens[pos]) {
            is Token.Text -> {
                pos++
                Node.Text(token.value)
            }
            is Token.Command -> {
                pos++
                // Check for an optional attribute in square brackets.
                var attribute: Node? = null
                if (pos < tokens.size && tokens[pos] is Token.OpenBracket) {
                    pos++ // Consume the '['
//                    attribute = parseUntilCloseBracket()
                    attribute = parseUntilClose(isBrace = false)
                }
                // Collect all arguments provided in curly braces.
                val args = mutableListOf<Node>()
                while (pos < tokens.size && tokens[pos] is Token.OpenBrace) {
                    pos++ // Consume '{'
//                    args.add(parseUntilCloseBrace())
                    args.add(parseUntilClose(isBrace = true))
                }
                Node.Command(token.name, attribute, args)
            }
            is Token.OpenBrace -> {
                pos++ // Consume '{'
//                parseUntilCloseBrace()
                parseUntilClose(isBrace = true)
            }
            is Token.OpenBracket -> {
                // If a bracket group appears outside a command attribute,
                // parse it as a normal group.
                pos++ // Consume '['
//                parseUntilCloseBracket()
                parseUntilClose(isBrace = false)
            }
            is Token.CloseBrace -> {
                throw RuntimeException("Unexpected closing brace at token position $pos")
            }
            is Token.CloseBracket -> {
                throw RuntimeException("Unexpected closing bracket at token position $pos")
            }
        }
    }

    /**
     * Parse nodes until a closing delimiter is encountered.
     * @param isBrace Whether to parse a brace ({}) group or a bracket ([]) group.
     * @return A single node parsed from the input tokens.
     * @throws IllegalStateException if the parser founds unexpected or missing brace.
     */
    private fun parseUntilClose(isBrace: Boolean): Node {
        val nodes = mutableListOf<Node>()
        val closeToken = { tk: Token ->
            if (isBrace)
                tk !is Token.CloseBrace
            else
                tk !is Token.CloseBracket
        }
        val delimiter = if (isBrace) "brace" else "bracket"
        while (pos < tokens.size && closeToken(tokens[pos])) {
            nodes.add(parseNode())
        }
        if (pos >= tokens.size || closeToken(tokens[pos])) {
            throw IllegalStateException("Missing closing $delimiter for group starting at token position $pos")
        }
        pos++ // Consume the closing delimiter
        return if (nodes.size == 1) nodes[0] else Node.Group(nodes)
    }
}