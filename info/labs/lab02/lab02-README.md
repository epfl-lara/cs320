# Lab 02: Lexer

This assignment is the first stage of the Amy compiler.

## Code Scaffold

In this lab you will start your own compiler from scratch, meaning that you will no longer rely on the compiler frontend which was previously provided to you as a jar file. In this lab you will build the lexical analysis phase (`lexer`).

As we are now starting to work on your own compiler, we will start with a fresh scaffold. We suggest to keep the interpreter alongside, and we will tell you at what point you can add back your interpreter in your project.

For now, you should work in this new project that will become your full compiler. Following labs will be delivered as files to add to this project.

The structure of your project `src` directory should be as follows:

```text
lib 
 └── scallion-assembly-0.6.1.jar    

library
 ├── ...
 └── ...

examples
 ├── ...
 └── ...

amyc
 ├── Main.scala                         
 │
 ├── parsing                             
 │    ├── Lexer.scala
 │    └── Tokens.scala
 │
 └── utils                               
      ├── AmycFatalError.scala
      ├── Context.scala
      ├── Document.scala
      ├── Env.scala
      ├── Pipeline.scala
      ├── Position.scala
      ├── Reporter.scala
      └── UniqueCounter.scala

test
├── scala
│    └── amyc
│         └── test
│               ├── CompilerTest.scala
│               ├── LexerTests.scala
│               ├── TestSuite.scala
│               └── TestUtils.scala
└── resources
      └── lexer
           └── ...

```

This lab will focus on the following two files:

* `src/amyc/parsing/Tokens.scala`: list of tokens and token kinds.
* `src/amyc/parsing/Lexer.scala`: skeleton for the `Lexer` phase.

Below you will find the instructions for the first lab assignment in which you will get to know and implement an lexer for the Amy language.

## A Lexer for Amy

The role of a lexer is to read the input text as a string and convert it to a list of tokens. Tokens are the smallest useful units in a source file: a name referring to a variable, a bracket, a keyword etc. The role of the lexer is to group together those useful units (e.g. return the keyword else as a unit, as opposed to individual characters e, l, s, e) and to abstract away all useless information (i.e. whitespace, comments).

## Code structure

You can find the `lexer` in the `Lexer.scala` file. It is based on Scallion and Silex, a pair of Scala libraries which simplify the implementation of parsing pipelines. Silex allows you to transform an input character stream (such as the contents of an Amy source file) into a sequence of Tokens. We are going to take a closer look at Scallion in the next lab, where our goal will be to build Amy's parser. You can find more information on Scallion [here](https://github.com/epfl-lara/scallion) and Silex [here](https://github.com/epfl-lara/silex), but we also included a short reference of Silex's API in `Lexer.scala`.

The `Lexer` has the following components:

* The public method is `run`. It just calls `lexer.spawn`(`source`) for every input file and concatenates the results.
* `lexer` is the Silex-based definition of tokenization rules. Each rule corresponds to a regular expression matching a prefix of the remaining program input. Silex will compose all of these rules into one finite state machine and apply the maximum-munch or longest match rule you've seen in class.
* Whenever a rule is found to match a (maximal) prefix of the remaining input, Scallion will call the transformation function provided using the `|>` operator in the rule. This function is given the matched input characters (`cs`) along with positional information (`range`) and should then produce an instance of `Token`. You can find its definition in `Tokens.scala`, which includes a list of all the different kinds of tokens that your Amy compiler should process. For instance, KeywordToken(`if`) represents an occurrence of the reserved word `if` in a program.
For more details on how to write new rules, read the short introduction to Silex's API at the top of `Lexer.scala` or consider the examples on the Scallion website. You can also refer to [Silex's Scaladoc page](https://epfl-lara.github.io/silex/).

Your task is to complete the rules in `Lexer.scala` and implement the filtering of irrelevant tokens.

## Notes

Here are some details you should pay attention to:

* Make sure you recognize keywords as their own token kind. `if`, for instance, should be lexed as a token `KeywordToken(“if”)`, not as an identifier with the content `if`.
* Make sure you correctly register the position of all tokens. Note the range parameter of the transformer functions. Once you have created a token, use `setPos`(`range._1`) to associate it with its position in the program source.
* In general, it is good to output as many errors as possible (this will be helpful to whomever uses your compiler, including yourself). Your lexer should therefore not give up after the first error, but rather skip the erroneous token, emit an error message, and then continue lexing. Scallion takes care of this for you for the most part. However, there are certain inputs that you might explicitly want to map to `ErrorToken`, such as unclosed multi-line comments.
* The Lexer does not immediately read and return all tokens, it returns an `Iterator`[`Token`] that will be used by future phases to read tokens on demand.
Comments and whitespace should not produce tokens. (The most convenient way of doing this in Scallion is to first produce dedicated tokens and then filter them out later; See the related TODO in `Lexer.scala`.)
* Returned tokens should be fresh instances of the the appropriate Token subclass. Value tokens (tokens that carry a value, such as identifiers), need to be constructed with the appropriate value.
* Make sure to correctly implement the Amy lexing rules for literals and identifiers.

## Example Output

For reference, you can look at resources in the test folder to see example outputs.

## Deliverables

Deadline: **Deadline: **14.03.2025 23:59:59****.

As for the previous lab, you should submit your work on the corresponding Moodle assignment. You should submit the following file:

* `Lexer.scala`: your implementation of the lexer.
