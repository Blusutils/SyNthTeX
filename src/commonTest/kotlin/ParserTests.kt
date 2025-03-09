import net.blusutils.synthtex.Lexer
import net.blusutils.synthtex.Node
import net.blusutils.synthtex.Parser
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ParserTests {
    private fun prepare(input: String): Parser {
        val lexer = Lexer(input)
        val tokens = lexer.tokenize()
        return Parser(tokens)
    }

    @Test
    fun parse_a_plain_text() {
        val parser = prepare("Hello, world!")
        val nodes = parser.parse()

        assertTrue("Expected 1 node, got ${nodes.size}") {
            nodes.size == 1
        }
        assertTrue("Expected a text node, got ${nodes[0]}") {
            nodes[0] is Node.Text
        }
        assertTrue("Expected 'Hello, world!', got ${nodes[0]}") {
            (nodes[0] as Node.Text).text == "Hello, world!"
        }
    }

    @Test
    fun parse_a_command() {
        val parser = prepare("\\textbf{Hello, world!}")
        val nodes = parser.parse()

        assertTrue("Expected 1 node, got ${nodes.size}") {
            nodes.size == 1
        }
        assertTrue("Expected a command node, got ${nodes[0]}") {
            nodes[0] is Node.Command
        }

        val nodeCommand = nodes[0] as Node.Command
        assertTrue("Expected 'textbf' as command name, got ${nodeCommand.name}") {
            nodeCommand.name == "textbf"
        }
        assertNull(nodeCommand.attribute, "Expected attribute to be null, got ${nodeCommand.attribute}")
        assertTrue("Expected 1 argument for node, got ${nodeCommand.arguments.size}") {
            nodeCommand.arguments.size == 1
        }
        assertTrue("Expected a text node as the argument, got ${nodeCommand.arguments[0]}") {
            nodeCommand.arguments[0] is Node.Text
        }

        val text = (nodeCommand.arguments[0] as Node.Text).text
        assertTrue("Expected 'Hello, world!' as contents of the argument, got $text") {
            text == "Hello, world!"
        }
    }


    @Test
    fun parse_command_with_no_body() {
        val parser = prepare("\\textbf")
        val nodes = parser.parse()
        assertTrue { nodes.size == 1 }
        assertTrue { nodes[0] is Node.Command }
        val command = nodes[0] as Node.Command
        assertTrue { command.name == "textbf" }
        assertTrue { command.arguments.isEmpty() }
    }

    @Test
    fun parse_empty_command_name() {
        val parser = prepare("\\{content}")
        val nodes = parser.parse()
        assertTrue { nodes.size == 1 }
        assertTrue { nodes[0] is Node.Command }
        val command = nodes[0] as Node.Command
        assertTrue { command.name.isEmpty() }
        assertTrue { command.arguments.size == 1 }
        assertTrue { (command.arguments[0] as Node.Text).text == "content" }
    }


    @Test
    fun parse_nested_commands() {
        val parser = prepare("\\section{\\textbf{Title}}")
        val nodes = parser.parse()

        assertTrue { nodes.size == 1 }
        assertTrue { nodes[0] is Node.Command }

        val outerCommand = nodes[0] as Node.Command
        assertTrue { outerCommand.name == "section" }
        assertTrue { outerCommand.arguments.size == 1 }
        assertTrue { outerCommand.arguments[0] is Node.Command }

        val innerCommand = outerCommand.arguments[0] as Node.Command
        assertTrue { innerCommand.name == "textbf" }
        assertTrue { innerCommand.arguments.size == 1 }
        assertTrue { (innerCommand.arguments[0] as Node.Text).text == "Title" }
    }

    @Test
    fun parse_command_with_attribute() {
        val parser = prepare("\\begin[center]{document}")
        val nodes = parser.parse()

        assertTrue { nodes.size == 1 }
        val command = nodes[0] as Node.Command
        assertTrue { command.attribute is Node.Text }
        assertTrue { (command.attribute as Node.Text).text == "center" }
        assertTrue { command.name == "begin" }
        assertTrue { command.arguments.size == 1 }
        assertTrue { (command.arguments[0] as Node.Text).text == "document" }
    }

    @Test
    fun parse_empty_input() {
        val parser = prepare("")
        val nodes = parser.parse()
        assertTrue { nodes.isEmpty() }
    }

    @Test
    fun parse_multiple_nodes() {
        val parser = prepare("Text1 \\textit{italic} Text2")
        val nodes = parser.parse()

        assertTrue { nodes.size == 3 }
        assertTrue { nodes[0] is Node.Text }
        assertTrue { nodes[1] is Node.Command }
        assertTrue { nodes[2] is Node.Text }

        assertTrue { (nodes[0] as Node.Text).text == "Text1 " }
        val command = nodes[1] as Node.Command
        assertTrue { command.name == "textit" }
        assertTrue { (command.arguments[0] as Node.Text).text == "italic" }
        assertTrue { (nodes[2] as Node.Text).text == " Text2" }
    }

    @Test
    fun parse_command_without_arguments() {
        val parser = prepare("\\linebreak")
        val nodes = parser.parse()

        assertTrue { nodes.size == 1 }
        assertTrue { nodes[0] is Node.Command }
        val command = nodes[0] as Node.Command
        assertTrue { command.name == "linebreak" }
        assertTrue { command.arguments.isEmpty() }
    }

    @Test
    fun parse_unclosed_command() {
        assertFailsWith<IllegalStateException> {
            prepare("\\textbf{unclosed").parse()
        }
    }

    @Test
    fun parse_unclosed_attribute() {
        assertFailsWith<IllegalStateException> {
            prepare("\\begin[unclosed{document}").parse()
        }
    }
}

