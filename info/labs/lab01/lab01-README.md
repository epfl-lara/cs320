# Amy Lab 01: Interpreter

Below you will find the instructions for the first lab assignment in which you will get to know and implement an interpreter for the Amy language.

## Logistics

As a reminder, the labs are done in groups of 2-3, please register on Moodle if not already done.

We advice you to create a private git repository to track your work and collaborate.

The labs are graded through Moodle assignments, similarly to Software Construction (CS-214) that you might have taken. You will have to submit your `.scala` files on Moodle and you will receive automatically a grade and feedback. You submit as many times as you want, only the last submission will be taken into account. The tests are the same as the ones you will receive for each lab, we do not use any hidden tests.

For this first lab, you can download the initial project scaffold from this folder.

## Part 1: Your first Amy programs

Write two example Amy programs each make sure that they typecheck (see [Type check examples](#type-check-examples)). Put them under `/examples`. Please be creative when writing your programs: they should be nontrivial and not reproduce the functionality of the examples in the `/library` and `/examples` directories of the repository. Of course you are welcome to browse these directories for inspiration.

Remember that you will use these programs in the remaining of the semester to test your compiler, so don't make them too trivial! Try to test many features of the language.

If you have questions about how a feature of Amy works, you can always look at the [Amy Specification](../amy-specification/AmySpec.md). It's a good idea to keep a local copy of this document handy -- it will be your reference for whenever you are asked to implement an aspect of the Amy language throughout this semester.

### Type check examples

You can use the provided Frontend to type check your programs. To do so, run the provided bash script:

```bash.sh
./amytc.sh examples/your_program.amy
```

This will run the compiler frontend up to type checking and report either `Type checking successful!` or an error message. If you get an error message, you should fix the error before moving on to the next step.

Please examine the bash scipt amytc.sh and its comments in your editor to understand how it works. Do not modify it.

#### Troubleshooting

- Your project must compile before you call the `amytc.sh` script.
- If you get unexpected errors or behaviour, try to delete the `target/scala-3.5.2/amyc-assembly-1.7.jar` and retry.

## Part 2: An Interpreter for Amy

The main task of the first lab is to write an interpreter for Amy.

### Interpreters

The way to execute programs you have mostly seen so far is compilation to some kind of low-level code (bytecode for a virtual machine such as Java's; native binary code in case of languages such as C). An alternative way to execute programs is interpretation. According to Wikipedia, "an interpreter is a computer program that directly executes, i.e. performs, instructions written in a programming or scripting language, without previously compiling them into a machine language program". In other words, your interpreter is supposed to directly look at the code and *interpret* its meaning. For example, when encountering a call to the 'printString' function, your interpreter should print its argument on the standard output. This is the way Python is executing your code.

### The general structure of the Interpreter

The skeleton of the assignment is provided by us as an `sbt` project. See the [Implementation skeleton](#implementation-skeleton) section for more details.

You will modify the `Interpreter.scala` file.

In `Main.scala` you find the main method which is the entry point to your program. After processing the command line arguments of the interpreter, the main method creates a Pipeline, which contains the different stages of the compiler which you will implement in the future labs. The Pipeline will first call the Amy frontend, which will parse the source program into an abstract syntax tree (AST) and check it for correctness according to the [Amy Specification](../amy-specification/AmySpec.md), and then passes the result to the Interpreter.

The AST abstracts away uninteresting things of the program (e.g. parentheses, whitespace, operator precedence...) and keeps the essential structure of the program. It describes the structure of programs recursively. For example, here you have the description of a module in Amy:

`Module ::= **object** Id Definition* Expr? **end** Id`

and in the implementation we find a class:

`case class ModuleDef(name: Identifier, defs: List[ClassOrFunDef], optExpr: Option[Expr]) extends Definition`

A comparison of the implementation of ASTs in Java (as shown in the book) and Scala is instructive.

You can find the source code of the AST in the [TreeModule.scala](./src/amyc/ast/TreeModule.scala).

### The Interpreter class

Now let's delve into `Interpreter.scala`. This file currently only contains a partial implementation, and it is your task to complete it! The entrypoint into the interpreter is `interpret`, which takes an expression as input and executes its meaning. The main loop at the end of the class will just take the modules in order and interpret their expression, if present.

`interpret` returns a `Value`, which is a type that represents a value that an Amy expression can produce. Value is inherited by classes which represent the different types of values present in Amy (`Int(32)`, `Booleans`, `Unit`, `String` and ADT values). `Value` has convenience methods to cast to `Int(32)`, `Boolean` and `String` (`as*`). Remember we can always call these methods safely when we know the types of an expression (e.g. the operands of an addition), since we know that the program type-checks.

`interpret` takes an additional implicit parameter as an argument, which is a mapping from variables to values (in the interpreted language). In Scala, when an implicit parameter is expected, the compiler will look in the scope for some binding of the correct type and pass it automatically. This way we do not have to pass the same mapping over and over to all recursive calls to `interpret`. Be aware, however, that there are some cases when you need to change the `locals` parameter! Think carefully about when you have to do so.

A few final notes:

- You can print program output straight to the console.
- You can assume the input programs are valid. This is guaranteed by the Amy frontend.
- To find constructors and functions in the program, you have to search in the `SymbolTable` passed along with the program. To do so, use the three helper methods provided in the interpreter:
  - `isConstrutor` will return whether the `Identifier` argument is a type constructor in the program
  - `findFunctionOwner` will return the module which contains the given `Identifier`, which has to be a function in the program. E.g. if you give it the `printInt` function of the `Std` module, you will get the string `"Std"`.
  - `findFunction` will return the function definition given a pair of Strings representing the module containing the function, and the function name. The return value is of type `FunDef` (see [the AST definitions](./src/amyc/ast/TreeModule.scala)).
- When comparing Strings by reference, compare the two `StringValue`s directly and not the underlying Strings. The reason is that the JVM may return true when comparing Strings by equality when it is not expected (it has to do with JVM constant pools).
- Some functions contained in the `Std` module are built-in in the language, i.e. they are hard-coded in the interpreter because they cannot be implemented in Amy otherwise. An example of a built-in function is `printString`. When you implement the interpreter for function calls, you should first check if the function is built-in, and if so, use the implementation provided in the `builtIns` map in the interpreter.
- When a program fails (e.g. due to a call to `error` or a match fail), you should call the dedicated method in the Context: `ctx.reporter.fatal`.

### Implementation skeleton

You can get the project scaffold from [this folder](.).

- `src/amyc/interpreter/Interpreter.scala` contains the partially implemented interpreter
- `src/amyc/Main.scala` contains the `main` method which runs the interpreter on the input files
- The `library` directory contains library definitions you can use in your Amy programs.
- The `examples` directory contains some example programs on which you can try your implementation. Remember that most of them also use library files from `/library`. This should also contain the programs you wrote in Part 1.
- `lib/amy-frontend-1.7.jar` contains the frontend of the compiler as a library, allowing you directly work with type-checked ASTs of input programs. You need this to be able to extract the AST from your source code to interpret it, as you did not implement this part of the compiler yet. This is also what allowed you to type check programs in part 1. **Note**: You are only allowed to use this binary code to link against your interpreter.

You will have to complete the interpreter by implementing the missing methods (marked with the placeholder `???`).

### Testing

When you are done, use sbt to try some of your programs from Part 1:

```bash
  $ sbt
  > run library/Std.amy examples/Hello.amy
  Hello world!
```

You can also run your interpreter with the `amyi.sh` script in a similar way as you did with the type checker:

```bash
  $ ./amyi.sh examples/Hello.amy
  Hello world!
```

**Note**: if you use this method, you have to delete `target/scala-3.5.2/amyc-assembly-1.7.jar` before running the script when you modified your interpreter. Otherwise, the script will reuse the previously compiled version of the interpreter and your new modifications would not be taken into account. Therefore this method is more recommended for testing multiple amy programs, rather than testing your interpreter while you are developing it.

There is also testing infrastructure under `/test`. To add your own tests, you have to add your testcases under `/test/resources/interpreter/passing`
and the expected output under `/test/resources/interpreter/outputs`.
Then, you have to add the name of the new test in `InterpreterTests`, similarly to the examples given.
To allow a test to also use the standard library (e.g., `Std.printString`), you can copy `Std.scala` from `library/Std.scala` to `/test/resources/interpreter/passing`.

For example, to add a test that expects only "Hello world" to be printed, you can add `/test/resources/interpreter/passing/Hello.amy` containing:

```scala
object Hello 
  Std.printString("Hello world") 
end Hello
```

and `/test/resources/interpreter/outputs/Hello.txt` containing

```text
Hello world

```

(with a newline in the end!).

You will also have to add a line to `/test/scala/amyc/test/InterpreterTests.scala`:  `@Test def testHello = shouldOutput(List("Std", "Hello"), "Hello")`. This will pass both files `Std.amy` and `Hello.amy` as inputs of the test. When you now run `test` from sbt, you should see the additional test case (called `testHello`).

The tests provided originally in `test/` are the ones used to grade your work on Moodle. Please note that the grade returned by the grader on Moodle is what you will get for the lab. Therefore you should submit regularly on Moodle to validate your progress. Also, if tests pass locally but not on the grader, the grader is the one that counts so submit your work regularly and check the feedback in case of discrepancies.

### Deliverables

You should submit the following files on Moodle:

- `Interpreter.scala` with your implementation of the interpreter

Deadline: **07.03.2025 23:59:59**

#### Related documentation

- End of Chapter 1 in the Tiger Book presents a similar problem for another mini-language. A comparison of the implementation of ASTs in Java (as shown in the book) and Scala is instructive.
