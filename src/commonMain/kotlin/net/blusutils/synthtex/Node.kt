package net.blusutils.synthtex

/**
 * A node in a syntax tree.
 * @author EgorBron <bron@blusutils.net>
 * @since 1.0.0
 * @version 1.0.0
 * @see Text
 * @see Command
 * @see Group
 */
sealed class Node {
    /**
     * A plain text node.
     * @param text The text content of the node.
     * @author EgorBron <bron@blusutils.net>
     * @since 1.0.0
     * @version 1.0.0
     */
    data class Text(val text: String) : Node()

    /**
     * A command node with a name, an optional attribute, and a list of arguments.
     * @param name The name of the command (after a backslash).
     * @param attribute An optional attribute node defined in square brackets.
     * @param arguments A list of argument nodes, each passed in curly braces.
     * @author EgorBron <bron@blusutils.net>
     * @since 1.0.0
     * @version 1.0.0
     */
    data class Command(val name: String, val attribute: Node? = null, val arguments: List<Node>) : Node()

    /**
     * A group node representing a block of nodes (typically from curly braces).
     * @param nodes A list of nodes contained within the group.
     * @author EgorBron <bron@blusutils.net>
     * @since 1.0.0
     * @version 1.0.0
     */
    data class Group(val nodes: List<Node>) : Node()
}