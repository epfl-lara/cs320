Below you will find the instructions for the first lab assignment in which you will get to know and implement an interpreter for the Amy language. If you haven't looked at the [Labs Setup](https://gitlab.epfl.ch/lara/cs320/-/blob/main/labs/labs_setup.md) page yet, please do so before starting out with the assignment.


# Part 1: Your first Amy programs

Write two example Amy programs each and make sure you can compile them using the [Amy Reference Compiler](https://gitlab.epfl.ch/lara/cs320/-/blob/main/labs/amy_reference_compiler.md). Put them under `/examples`. Please be creative when writing your programs: they should be nontrivial and not reproduce the functionality of the examples in the `/library` and `/examples` directories of the repository. Of course you are welcome to browse these directories for inspiration.

Remember that you will use these programs in the remaining of the semester to test your compiler, so don't make them too trivial! Try to test many features of the language.

If you have questions about how a feature of Amy works, you can always look at the [Amy Specification](https://gitlab.epfl.ch/lara/cs320/-/blob/main/labs/amy_specification.md). It's a good idea to keep a local copy of this document handy -- it will be your reference for whenever you are asked to implement an aspect of the Amy language throughout this semester.


# Part 2: An Interpreter for Amy 

The main task of the first lab is to write an interpreter for Amy. 

(If you haven't been assigned your repository yet, you can download a packaged version of the interpreter lab's skeleton [here](https://gitlab.epfl.ch/lara/cs320/-/blob/main/labs/labs01_material/clp-lab01.zip). If you already have your repository assigned, you can simply check out the `lab01` branch. Note that future labs will only be distributed through the repository, so be sure to familiarize yourself with the setup.)


## Interpreters 

The way to execute programs you have mostly seen so far is compilation to some kind of low-level code (bytecode for a virtual machine such as Java's; native binary code in case of languages such as C). An alternative way to execute programs is interpretation. According to Wikipedia, "an interpreter is a computer program that directly executes, i.e. performs, instructions written in a programming or scripting language, without previously compiling them into a machine language program". In other words, your interpreter is supposed to directly look at the code and *interpret* its meaning. For example, when encountering a call to the 'printString' function, your interpreter should print its argument on the standard output.

## The general structure of the Interpreter 

The skeleton of the assignment is provided by us in three files:

- The `Main.scala` source file

- The `Interpreter.scala` source file, and

- the `amyc-frontend-1.7.jar` bytecode file, which is located under `lib/` .

Now let's look into the code in a little more detail.

In `Main.scala`, take a look at the main method, which is the entry point to your program. After processing the command line arguments of the interpreter, the main method creates a Pipeline, which contains the different stages of the compiler (more on it in later assignments). The Pipeline will first call the Amy frontend, which will parse the source program into an abstract syntax tree (AST) and check it for correctness according to the [Amy specification](https://gitlab.epfl.ch/lara/cs320/-/blob/main/labs/amy_specification.md), and then passes the result to the Interpreter.
The implementation of the frontend is given to you in compiled form, because you will need to write your own version in the next assignments. **Note**: You are only allowed to use this binary code to link against your interpreter.

So what is this AST we've mentioned? For the computer to "understand" the meaning of a program, it first has to transform it from source (text) form to a more convenient form, which we call an abstract syntax tree. The AST abstracts away uninteresting things of the program (e.g. parentheses, whitespace, operator precedence...) and keeps the essential structure of the program. 

In Scala, we represent the AST as a tree-form object. The tree has different types of nodes, each one representing a different programming structure. The types of nodes are of course represented as different classes, which all inherit from a class called Tree. Conveniently enough, the classes correspond pretty much one-to-one to the rules of the BNF grammar given in the language specification. E.g. in the language spec we read that a module looks as follows:

Module ::= **object** Id Definition* Expr? **end** Id

and indeed in the implementation we find a class 

`case class ModuleDef(name: Identifier, defs: List[ClassOrFunDef], optExpr: Option[Expr]) extends Definition`

You can find the source code of the AST [here](https://gitlab.epfl.ch/lara/cs320/-/blob/main/labs/labs01_material/SymbolicTreeModule.scala).
Note: This is not exactly the code we will use in later assignments, but it's good enough to serve as a reference while implementing this first assignment.


## The Interpreter class

Now let's delve into `Interpreter.scala`. This file currently only contains a partial implementation, and it is your task to complete it! The entrypoint into the interpreter is `interpret`, which takes an expression as input and executes its meaning. The main loop at the end of the class will just take the modules in order and interpret their expression, if present.

`interpret` returns a `Value`, which is a type that represents a value that an Amy expression can produce. Value is inherited by classes which represent the different types of values present in Amy (`Int(32)`, `Booleans`, `Unit`, `String` and ADT values). `Value` has convenience methods to cast to `Int(32)`, `Boolean` and `String` (`as*`). Remember we can always call these methods safely when we know the types of an expression (e.g. the operands of an addition), since we know that the program type-checks.

`interpret` takes an additional implicit parameter as an argument, which is a mapping from variables to values (in the interpreted language). In Scala, when an implicit parameter is expected, the compiler will look in the scope for some binding of the correct type and pass it automatically. This way we do not have to pass the same mapping over and over to all recursive calls to `interpret`. Be aware, however, that there are some cases when you need to change the `locals` parameter! Think carefully about when you have to do so.

A few final notes:

  * You can print program output straight to the console.
  * You can assume the input programs are correct. This is guaranteed by the Amy frontend. 
  * To find constructors and functions in the program, you have to search in the `SymbolTable` passed along with the program. To do this, use the three helper methods provided in the interpreter:
    * `isConstrutor` will return whether the `Identifier` argument is a type constructor in the program
    * `findFunctionOwner` will return the module which contains the given `Identifier`, which has to be a function in the program. E.g. if you give it the `printInt` function of the `Std` module, you will get the string `"Std"`.
    * `findFunction` will return the function definition given a pair of Strings representing the module containing the function, and the function name. The return value is of type `FunDef` (see [the AST definitions](https://gitlab.epfl.ch/lara/cs320/-/blob/main/labs/labs01_material/SymbolicTreeModule.scala)).
  * When comparing Strings by reference, compare the two `StringValue`s directly and not the underlying Strings. The reason is that the JVM may return true when comparing Strings by equality when it is not expected (it has to do with JVM constant pools).
  * Some functions contained in the `Std` module are built-in in the language, i.e. they are hard-coded in the interpreter because they cannot be implemented in Amy otherwise. An example of a built-in function is `printString`. When you implement the interpreter for function calls, you should first check if the function is built-in, and if so, use the implementation provided in the `builtIns` map in the interpreter.
  * When a program fails (e.g. due to a call to `error` or a match fail), you should call the dedicated method in the Context: `ctx.reporter.fatal`.

## Implementation skeleton 

If you have followed [Labs Setup](https://gitlab.epfl.ch/lara/cs320/-/blob/main/labs/labs_setup.md) for Lab 01, you should have a working project with a stub implementation, containing the following files:

  * `src/amyc/interpreter/Interpreter.scala` contains a partially implemented interpreter
  * `src/amyc/Main.scala` contains the `main` method which runs the interpreter on the input files
  * The `library` directory contains library definitions you can call from your programs.
  * The `examples` directory contains some example programs on which you can try your implementation. Remember that most of them also use library files from `/library`.
  * `lib/amy-frontend-1.7.jar` contains the frontend of the compiler as a library, allowing you directly work with type-checked ASTs of input programs.

You will have to complete the interpreter by implementing the missing methods (marked with the placeholder `???`).


## Testing

When you are done, use sbt to try some of your programs from Part 1:

  $ sbt
  > run library/Std.scala examples/Hello.scala
  Hello world!

There is also testing infrastructure under `/test`. To add your own tests, you have to add your testcases under `/test/resources/interpreter/passing`
and the expected output under 
`/test/resources/interpreter/outputs`.
Then, you have to add the name of the new test in `InterpreterTests`, similarly to the examples given.
To allow a test to also use the standard library (e.g., `Std.printString`), you can copy `Std.scala` from `library/Std.scala` to `/test/resources/interpreter/passing`.

For example, to add a test that expects only "Hello world" to be printed, you can add "/test/resources/interpreter/passing/Hello.scala" containing `object Hello Std.printString("Hello world") end Hello` and `/test/resources/interpreter/outputs/Hello.txt` containing `Hello world` (with a newline in the end!). You will also have to add a line to `/test/scala/amyc/test/InterpreterTests.scala`:  `@Test def testHello = shouldOutput(List("Std", "Hello"), "Hello")`. This will pass both files `Std.scala` and `Hello.scala` as inputs of the test. When you now run `test` from sbt, you should see the additional test case (called `testHello`).


## Deliverables
You are given **2 weeks** for this assignment.

Deadline: **Friday October 8 at noon**.


## Related documentation 

  * End of Chapter 1 in the Tiger Book presents a similar problem for another mini-language. A comparison of the implementation of ASTs in Java (as shown in the book) and Scala is instructive.
