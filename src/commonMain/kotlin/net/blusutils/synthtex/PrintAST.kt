package net.blusutils.synthtex

/**
 * Prints the AST to the console.
 *
 * This is for debugging purposes only.
 * @param ast The AST to print.
 * @param indentLevel The level of indentation. Do not pass this parameter.
 * @author EgorBron
 * @since 1.0.0
 * @version 1.0.0
 */
@Suppress("unused")
fun printAST(ast: List<Node>, indentLevel: Int = 0) {
    val indent = " ".repeat(indentLevel * 2)
    for (node in ast) {
        when (node) {
            is Node.Text -> {
                if (node.text.isBlank()) continue
                println("$indent Text: ${node.text}")
            }
            is Node.Command -> {
                println("$indent Command: ${node.name}")
                if (node.attribute != null) {
                    println("$indent  Attribute: ${node.attribute}")
                }
                println("$indent  Arguments: ")
                printAST(node.arguments, indentLevel + 2)
                println()
            }
            is Node.Group -> {
                println("$indent Group:")
                for (child in node.nodes) {
                    printAST(listOf(child), indentLevel + 2)
                }
            }
        }
    }
}