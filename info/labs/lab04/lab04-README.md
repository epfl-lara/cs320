# Lab 04: Type Checker

Parsing concludes the syntactical analysis of Amy programs. Having successfully constructed an abstract syntax tree for an input program, compilers typically run one or multiple phases containing checks of a more semantical nature. Virtually all high-level programming languages enjoy some form of name analysis, whose purpose is to disambiguate symbol references throughout the program. Some languages go further and perform a series of additional checks whose goal is to rule out runtime errors statically (i.e., during compilation, or in other words, without executing the program). While the exact rules for those checks vary from language to language, this part of compilation is typically summarized as "type checking". Amy, being a statically-typed language, requires both name and type analysis.

## Prelude: From Nominal to Symbolic Trees

Recall that during parsing we created (abstract syntax) trees of the *nominal* sort: Names of variables, functions and data types were simply stored as strings. However, two names used in the program could be the same, but not refer to one and the same "thing" at runtime. During name analysis we translate from nominal trees to symbolic ones, to make it clear whether two names refer to one and the same underlying entity. That is, we explicitly replace strings by fresh identifiers which will prevent us from mixing up definitions of the same name, or referring to things that have not been defined. Amy's name analyzer is provided to you as part of this lab's skeleton, but you should read the [dedicated name analyzer page](material/NameAnalysis.md) to understand how it works.

## Introduction to Type Checking

The purpose of this lab is to implement a type checker for Amy. Our type checking rules will prevent certain errors based on the kind or shape of values that the program is manipulating. For instance, we should prevent an integer from being added to a boolean value.

Type checking is the last stage of the compiler frontend. Every program that reaches the end of this stage without an error is correct (as far as the compiler is concerned), and every program that does not is wrong. After type checking we are finally ready to interpret the program or compile it to binary code!

Typing rules for Amy are presented in detail in the [Amy specification](../amy-specification/AmySpec.md). Make sure to check correct typing for all expressions and patterns.

## Implementation

The current assignment focuses on the file `TypeChecker.scala`. As usual, the skeleton and helper methods are given to you, and you will have to complete the missing parts. In particular, you will write a compiler phase that checks whether the expressions in a given program are well-typed and report errors otherwise.

To this end you will implement a simplified form of the Hindley-Milner (HM) type-inference algorithm that you'll hear about during the lectures. Note that while not advertised as a feature to users of Amy, behind the scenes we will perform type inference. It is usually straightforward to adapt an algorithm for type inference to type checking, since one can add the user-provided type annotations to the set of constraints. This is what you will do with HM in this lab.

Compared to the presentation of HM type inference in class your type checker can be simplified in another way: Since Amy does not feature higher-order functions or polymorphic data types, types in Amy are always *simple* in the sense that they are not composed of arbitrary other types. That is, a type is either a base type (one of `Int`, `Bool` and `String`) or it is an ADT, which has a proper name (e.g. `List` or `Option` from the standard library). In the latter case, all the types in the constructor of the ADT are immediately known. For instance, the standard library's `List` is really a list of integers, so we know that the `Cons` constructor takes an `Int` and another `List`.

As a result, your algorithm will never have to deal with complex constraints over type constructors (such as the function arrow `A => B`). Instead, your constraints will always be of the form `T1 = T2` where `T1` and `T2` are either *simple* types or type variables. This is most important during unification, which otherwise would have to deal with complex types separately.

Your task now is to

- complete the `genConstraints` method which will traverse a given expression and collect all the necessary typing constraints
- implement the *unification* algorithm in the function `solveConstraints`.

Familiarize yourself with the `Constraint` and `TypeVariable` data structures in `TypeChecker.scala` and then start by implementing `genConstraints`. The structure of this method will in many cases be analogous to the AST traversal for the name analyzer. Note that `genConstraints` also takes an *expected type*. For instance, in case of addition the expected type of both operands should be `Int`. For other constructs, such as pattern `match`es it is not inherently clear what should be the type of each `case` body. In this case you can create and pass a fresh type variable.

> Do not end the execution as soon as an error occurs! Instead, collect all the errors and report them at the end of the type checking phase.

### Running your frontend

Once you have a working implementation of both `genConstraints` and `solveConstraints` you can copy over your previous work on the interpreter and run the programs produced by your frontend! Don't forget that to debug your compiler's behavior you can also use the reference compiler with the `--interpret` flag and then compare the output.

You can also run using sbt using the following command:

```bash
sbt "run --type-check <path-to-file>"
```

or with the interpreter added back to your project:

```bash
sbt "run --interpret <path-to-file>"
```

### Example

#### Negative example

Consider the following program:

```scala
object Bogus
     "Amy <3" || 5
end Bogus
```

The following constraints should be generated:

```scala
TypeVar(0) == BooleanType     // Top-level type
BooleanType == StringType     // LHS of the || operator i.e., the type of "Amy <3")
BooleanType == IntType        // RHS of the || operator i.e., the type of 5
```

And these should lead to an error, as it is not possible to unify the last to constraints. The typechecker should then report an error and stop.

#### Positive example

Consider the following program:

```scala
object Correct
     3 + 4 == 5
end Correct
```

The following constraints should be generated:

```scala
TypeVar(0) == BooleanType     // Top-level type
TypeVar(1) == IntType         // LHS of the == operator i.e., the type of 3 + 4
TypeVar(1) == IntType         // RHS of the == operator i.e., the type of 5
IntType == IntType            // LHS of the + operator i.e., the type of 3
IntType == IntType            // RHS of the + operator i.e., the type of 4
```

And now the unification succeeds with

```scala
TypeVar(0) := BooleanType
TypeVar(1) := IntType
```

So this program typechecks successfully.

## Skeleton

As usual, you can find the skeleton for this lab in a new branch of your
group's repository. After merging it with your existing work, the
structure of your project `src` directory should be as follows:

```text
lib 
└── scallion-assembly-0.6.1.jar    

library
├── ...
└── ...

examples
├── ...
└── ...

src
├── amyc
│    ├── Main.scala                              (updated)
│    │
│    ├── analyzer                                (new)
│    │    ├── SymbolTable.scala
│    │    ├── NameAnalyzer.scala
│    │    └── TypeChecker.scala
│    │
│    ├── ast
│    │    ├── Identifier.scala
│    │    ├── Printer.scala
│    │    └── TreeModule.scala
│    │
│    ├── interpreter
│    │    └── Interpreter.scala
│    │
│    ├── lib
│    │    ├── scallion_3.0.6.jar
│    │    └── silex_3.0.6.jar
│    │
│    ├── parsing
│    │    ├── Parser.scala
│    │    ├── Lexer.scala
│    │    └── Tokens.scala
│    │
│    └── utils
│         ├── AmycFatalError.scala
│         ├── Context.scala
│         ├── Document.scala
│         ├── Pipeline.scala
│         ├── Position.scala
│         ├── Reporter.scala
│         └── UniqueCounter.scala
│
└── test
     ├── scala
     │    └── amyc
     │         └── test
     │              ├── CompilerTest.scala
     │              ├── LexerTests.scala
     │              ├── NameAnalyzerTests.scala  (new)
     │              ├── ParserTests.scala
     │              ├── TestSuite.scala
     │              ├── TestUtils.scala
     │              └── TyperTests.scala         (new)
     └── resources
          ├── analyzer                           (new)
          │    └── ...
          ├── lexer
          │    └── ...
          └── parser                          
               └── ...
```

## Deliverables

Deadline: **11.04.2025 23:59:59**

You should submit the following files:

- `TypeChecker.scala`: The implementation of the type checker.
