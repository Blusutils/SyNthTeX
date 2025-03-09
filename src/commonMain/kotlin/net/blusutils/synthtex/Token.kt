package net.blusutils.synthtex

/**
 * A TeX tokens.
 * @author EgorBron <bron@blusutils.net>
 * @since 1.0.0
 * @version 1.0.0
 * @see Node
 * @see Text
 * @see Command
 * @see OpenBrace
 * @see CloseBrace
 * @see OpenBracket
 * @see CloseBracket
 */
sealed class Token {
    /**
     * A plain text token that does not contain any special characters.
     * All whitespace characters are not stripped and are included in the text.
     * @param value The text content of the token.
     * @see Node.Text
     * @author EgorBron <bron@blusutils.net>
     * @since 1.0.0
     * @version 1.0.0
     */
    data class Text(val value: String) : Token()

    /**
     * A command token that starts with a backslash.
     * @param name The name of the command (sequence of letters).
     * @see Node.Command
     * @author EgorBron <bron@blusutils.net>
     * @since 1.0.0
     * @version 1.0.0
     */
    data class Command(val name: String) : Token()

    /**
     * An opening argument brace token.
     * @see Node.Group
     * @see Node.Command.arguments
     * @author EgorBron <bron@blusutils.net>
     * @since 1.0.0
     * @version 1.0.0
     */
    data object OpenBrace : Token()       // {

    /**
     * A closing argument brace token.
     * @see Node.Group
     * @see Node.Command.arguments
     * @author EgorBron <bron@blusutils.net>
     * @since 1.0.0
     * @version 1.0.0
     */
    data object CloseBrace : Token()      // }

    /**
     * An opening attribute bracket token.
     * @see Node.Command.attribute
     * @author EgorBron <bron@blusutils.net>
     * @since 1.0.0
     * @version 1.0.0
     */
    data object OpenBracket : Token()     // [

    /**
     * A closing attribute bracket token.
     * @see Node.Command.attribute
     * @author EgorBron <bron@blusutils.net>
     * @since 1.0.0
     * @version 1.0.0
     */
    data object CloseBracket : Token()    // ]
}