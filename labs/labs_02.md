# Lab 02: Lexer

This assignment is the first stage of the Amy compiler.

## Code Skeleton

In this lab you will start your own compiler from scratch, meaning that you will no longer rely on the compiler frontend which was previously provided to you as a jar file. In this lab you will build the lexical analysis phase (`lexer`). Since practically none of the compiler's code will be shared with the previous lab, the new branch (lab02) contains a fresh skeleton.

Compared to the previous lab, the structure of your src directory will be as follows:
```
amyc
 ├── Main.scala                     (updated)
 │
 ├── lib
 │    ├── amy-frontend_2.12-1.7.jar (removed)
 │    └── silex_2.12-0.5.jar        (new)
 │
 ├── parsing                        (new)
 │    ├── Lexer.scala
 │    └── Tokens.scala
 │
 └── utils                          (new)
      ├── AmycFatalError.scala
      ├── Context.scala
      ├── Document.scala
      ├── Env.scala
      ├── Pipeline.scala
      ├── Position.scala
      ├── Reporter.scala
      └── UniqueCounter.scala


```
This lab will focus on the following two files:

* `src/amyc/parsing/Tokens.scala`: list of tokens and token kinds.
* `src/amyc/parsing/Lexer.scala`: skeleton for the `Lexer` phase.

Below you will find the instructions for the first lab assignment in which you will get to know and implement an interpreter for the Amy language. If you haven't looked at the [Labs Setup](https://gitlab.epfl.ch/lara/cs320/-/blob/main/labs/labs_setup.md) page yet, please do so before starting out with the assignment.

## A Lexer for Amy

The role of a lexer is to read the input text and convert it to a list of tokens. Tokens are the smallest useful units in a source file: a name referring to a variable, a bracket, a keyword etc. The role of the lexer is to group together those useful units (e.g. return the keyword else as a unit, as opposed to individual characters e, l, s, e) and to abstract away all useless information (i.e. whitespace, comments).

## Code structure

You can find the `lexer` in the `Lexer.scala` file. It is based on Scallion and Silex, a pair of Scala libraries which simplify the implementation of parsing pipelines. Silex allows you to transform an input character stream (such as the contents of an Amy source file) into a sequence of Tokens. We are going to take a closer look at Scallion in the next lab, where our goal will be to build Amy's parser. You can find more information on Scallion and Silex [here](https://github.com/epfl-lara/scallion), but we also included a short reference of Silex's API in `Lexer.scala`.

The Lexer has the following components:

* The public method is `run`. It just calls `lexer.spawn`(`source`) for every input file and concatenates the results.
* `lexer` is the Silex-based definition of tokenization rules. Each rule corresponds to a regular expression matching a prefix of the remaining program input. Silex will compose all of these rules into one finite state machine and apply the maximum-munch rule you've seen in class.
* Whenever a rule is found to match a (maximal) prefix of the remaining input, Scallion will call the transformation function provided using the |> operator in the rule. This function is given the matched input characters (cs) along with positional information (range) and should then produce an instance of Token. You can find its definition in `Tokens.scala`, which includes a list of all the different kinds of tokens that your Amy compiler should process. For instance, KeywordToken(`if`) represents an occurence of the reserved word if in a program.
For more details on how to write new rules, read the short introduction to Silex's API at the top of `Lexer.scala` or consider the examples on the Scallion website. You can also refer to [Silex's Scaladoc page](https://epfl-lara.github.io/silex/silex/index.html).

Your task is to complete the rules in `Lexer.scala` and implement the filtering of irrelevant tokens.

## Notes
Here are some details you should pay attention to:

* Make sure you recognize keywords as their own token kind. if, for instance, should be lexed as a token KeywordToken(“if”), not as an identifier with the content `if`.
* Make sure you correctly register the position of all tokens. Note the range parameter of the transformer functions. Once you have created a token, use `setPos`(`range._1`) to associate it with its position in the program source.
* In general, it is good to output as many errors as possible (this will be helpful to whomever uses your compiler, including yourself). Your lexer should therefore not give up after the first error, but rather skip the erroneous token, emit an error message, and then continue lexing. Scallion takes care of this for you for the most part. However, there are certain inputs that you might explicitly want to map to `ErrorToken`, such as unclosed multi-line comments.
* The Lexer does not immediately read and return all tokens, it returns an `Iterator`[`Token`] that will be used by future phases to read tokens on demand.
Comments and whitespace should not produce tokens. (The most convenient way of doing this in Scallion is to first produce dedicated tokens and then filter them out later; See the related TODO in `Lexer.scala`.)
* Returned tokens should be fresh instances of the the appropriate Token subclass. Value tokens (tokens that carry a value, such as identifiers), need to be constructed with the appropriate value.
* Make sure to correctly implement the Amy lexing rules for literals and identifiers.

## Example Output
For reference, here is a possible output for the example under `examples/Hello.scala`. You can always get reference output for the lexer from the reference compiler by typing

```
java -jar amyc_2.12-1.X.jar --printTokens <files>
```
```
KeywordToken(object)(1:1)
IdentifierToken(Hello)(1:8)
DelimiterToken({)(1:14)
IdentifierToken(Std)(2:3)
DelimiterToken(.)(2:6)
IdentifierToken(printString)(2:7)
DelimiterToken(()(2:18)
StringLitToken(Good morning!)(2:19)
DelimiterToken())(2:34)
DelimiterToken(})(3:1)
EOFToken()(4:1)
```

## Deliverables
You are given **2 weeks** for this assignment.

Deadline: **Wed, Oct. 7, 23.00pm**.
