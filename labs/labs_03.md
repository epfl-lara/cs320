# Lab 03: Parser

## Introduction

Starting from this week you will work on the second stage of the Amy
compiler, the parser. The task of the parser is to take a sequence of
tokens produced by the lexer and transform it into an Abstract Syntax
Tree (AST).

For this purpose you will write a grammar for Amy programs in a Domain
Specific Language (DSL) that can be embedded in Scala. Similarly to what
you have seen in the Lexer lab, each grammar rule will also be
associated with a transformation function that maps the parse result to
an AST. The overall grammar will then be used to automatically parse
sequences of tokens into Amy ASTs, while abstracting away extraneous
syntactical details, such as commas and parentheses.

As you have seen (and will see) in the lectures, there are various
algorithms to parse syntax trees corresponding to context-free grammars.
Any context-free grammar (after some normalization) can be parsed using
the CYK algorithm. However, this algorithm is rather slow: its
complexity is in O(n\^3 \* g) where n is the size of the program and g
the size of the grammar. On the other hand, a more restricted LL(1)
grammar can parse inputs in linear time. Thus, the goal of this lab will
be to develop an LL(1) version of the Amy grammar.

### The Parser Combinator DSL

In the previous lab you already started working with **Silex**, which
was the library we used to tokenize program inputs based on a
prioritized list of regular expressions. In this lab we will start using
its companion library, **Scallion**: Once an input string has been
tokenized, Scallion allows us to parse the token stream using the rules
of an LL(1) grammar and translate to a target data structure, such as an
AST.

To familiarize yourself with the parsing functionality of Scallion,
please make sure you read the [Introduction to (Scallion) Parser
Combinators](lab03_material/scallion.md). In it, you will learn how to describe grammars
in Scallion\'s parser combinator DSL and how to ensure that your grammar
lies in LL(1) (which Scallion requires to function correctly).

Once you understand parser combinators, you can get to work on your own
implementation of an Amy parser in `Parser.scala`. Note that in this lab
you will essentially operate on two data structures: Your parser will
consume a sequence of `Token`s (defined in `Tokens.scala`) and produce
an AST (as defined by `NominalTreeModule` in `TreeModule.scala`). To
accomplish this, you will have to define appropriate parsing rules and
translation functions for Scallion.

In `Parser.scala` you will already find a number of parsing rules given
to you, including the starting non-terminal `program`. Others, such as
`expr` are stubs (marked by `???`) that you will have to complete
yourself. Make sure to take advantage of Scallion\'s various helpers
such as the `operators` method that simplifies defining operators of
different precedence and associativity.

### An LL(1) grammar for Amy

As usual, the [Amy specification](amy specification) will guide you when
it comes to deciding what exactly should be accepted by your parser.
Carefully read Section 2 (*Syntax*).

Note that the EBNF grammar in Figure 2 merely represents an
over-approximation of Amy\'s true grammar \-- it is too imprecise to be
useful for parsing: Firstly, the grammar in Figure 2 is ambiguous. That
is, it allows multiple ways to parse an expression. E.g. `x + y * z`
could be parsed as either `(x + y) * z` or as `x + (y * z)`. In other
words, the grammar doesn\'t enforce either operator precedence or
associativity correctly. Additionally, the restrictions mentioned
throughout Section 2 of the specification are not followed.

Your task is thus to come up with appropriate rules that encode Amy\'s
true grammar. Furthermore, this grammar should be LL(1) for reasons of
efficiency. Scallion will read your grammar, examine if it is in LL(1),
and, if so, parse input programs. If Scallion determines that the
grammar is not in LL(1), it will report an error. You can also instruct
Scallion to generate some counter-examples for you (see the `checkLL1`
function).

### Translating to ASTs

Scallion will parse a sequence of tokens according to the grammar you
provide, however, without additional help, it does not know how to build
Amy ASTs. For instance, a (non-sensical) grammar that only accepts
sequences of identifier tokens, e.g.

    many(elem(IdentifierKind)): Syntax[Seq[Token]]

will be useful in deciding whether the input matches the expected form,
but will simply return the tokens unchanged when parsing succeeds.

Scallion does allow you to map parse results from one type to another,
however. For instance, in the above example we might want to provide a
function `f(idTokens: Seq[Token]): Seq[Variable]` that transforms the
identifier tokens into (Amy-AST) variables of those names.

For more information on how to use Scallion\'s `Syntax#map` method
please refer to the [Scallion introduction](lab03_material/scallion.md).

## Notes

### Understanding the AST: Nominal vs. Symbolic Trees

If you check the TreeModule file containing the ASTs, you will notice it
is structured in an unusual way: There is a `TreeModule` class extended
by `NominalTreeModule` and `SymbolicTreeModule`. The reason for this
design is that we need two very similar ASTs, but with different types
representing names in each case: Just after parsing (this assignment),
all names are just Strings and qualified names are essentially pairs of
Strings. We call ASTs that only use such String-based names `Nominal`
\-- the variant we will be using in this lab. Later, during name
analysis, these names will be resolved to unique identifiers, e.g. two
variables that refer to different definitions will be distinct, even if
they have the same name. For now you can just look at the TreeModule and
substitute the types that are not defined there (`Name` and
`QualifiedName`) with their definitions inside `NominalTreeModule`.

### Positions

As you will notice in the code we provide, all generated ASTs have their
position set. The position of each node of the AST is defined as its
starting position. It is important that you set the positions in all the
trees that you create for better error reporting later. Although our
testing infrastructure cannot directly check for presence of positions,
we will check it manually.

### Pretty Printing

Along with the stubs, we provide a printer for Amy ASTs. It will print
parentheses around all expressions so you can clearly see how your
parser interprets precedence and associativity. You can use it to test
your parser, and it will also be used during our testing to compare the
output of your parser with the reference parser.

## Skeleton

As usual, you can find the skeleton in the git repository. This lab
builds on your previous work, so \-- given your implementation of the
lexer \-- you will only unpack two files from the skeleton.

The structure of your project `src` directory should be as follows:

    amyc
     ├── Main.scala                   (updated)
     │
     ├── ast                          (new)
     │    ├── Identifier.scala
     │    ├── Printer.scala
     │    └── TreeModule.scala
     │
     ├── lib
     │    ├── scallion_3.0.6.jar   (new)
     │    └── silex_3.0.6.jar
     │
     ├── parsing
     │    ├── Parser.scala            (new)
     │    ├── Lexer.scala
     │    └── Tokens.scala
     │
     └── utils
          ├── AmycFatalError.scala
          ├── Context.scala
          ├── Document.scala
          ├── Pipeline.scala
          ├── Position.scala
          ├── Reporter.scala
          └── UniqueCounter.scala

## Deliverables

You have TBD weeks to complete this assignment.

**Deadline: Wednesday TBD October, 23:00**
