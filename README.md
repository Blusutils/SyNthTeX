# SyNthTeX

A simple parser for TeX-like syntax.

## Installation

Add the following to your `build.gradle.kts` file:
```kotlin
implementation("net.blusutils.synthtex:synthtex:1.0.0")
```

Or, if you're using Maven:
```xml
<dependency>
    <groupId>net.blusutils.synthtex</groupId>
    <artifactId>synthtex</artifactId>
    <version>1.0.0</version>
</dependency>
```

Make sure to include the Maven Central repository!

## Not-a-doc

Here is some example code.

From this source:
```latex
% This is a comment
\documentclass{article}
\usepackage[utf8x]{inputenc}
\begin{document}
    \section{Hello, world!}
    Hello, world!
    {document}
\end{document}
```

...we are going to parse it like this:
```kotlin
val source = "..."
val lexer = Lexer(source)
val tokens = lexer.tokenize()

val parser = Parser(tokens)
val nodes = parser.parse()

printAST(nodes)
```

Now you got an AST of your document:
```
AST:
 Command: documentclass
  Arguments: 
     Text: article

 Command: usepackage
  Attribute: Text(text=utf8x)
  Arguments: 
     Text: inputenc

 Command: begin
  Arguments: 
     Text: document

 Command: section
  Arguments: 
     Text: Hello, world!

 Text: 
    Hello, world!
    
 Text: document
 Command: end
  Arguments: 
     Text: document
```

It consists of
- `Node.Text` with `text` (string)
- `Node.Command` with `name`, `attribute` (nullable `Node`) and `arguments` (list of `Node`)
- `Node.Group` with `nodes` (list of `Node`)

More info in the doc-comments.

## License

Licensed under the [MIT License].

```
MIT License

Copyright (c) 2025 Blusutils

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```