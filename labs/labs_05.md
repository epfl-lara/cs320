# Lab 05: Code Generation(Slides: [Markdown](slides/lab05.md)/[HTML](slides/lab05.html))

## Introduction

Welcome to the last common assignment for the Amy compiler. At this
point, we are finally done with the frontend: we have translated source
programs to ASTs and have checked that all correctness conditions hold
for our program. We are ready to generate code for our program. In our
case the target language will be *WebAssembly*.

WebAssembly is \"a new portable, size- and load-time-efficient format
suitable for compilation to the web\" (<http://webassembly.org>).
WebAssembly is designed to be called from JavaScript in browsers and
lends itself to highly-performant execution.

For simplicity, we will not use a browser, but execute the resulting
WebAssembly bytecode directly using `nodejs` which is essentially a
standalone distribution of the Chrome browser\'s JavaScript engine. When
you run your complete compiler (or the reference compiler) with no
options on program `p`, it will generate four different files under the
`wasmout` directory:

-   `p.wat` is the wasm output of the compiler in text format. You can
    use this representation to debug your generated code.
-   `p.wasm` is the binary output of the compiler. This is what `nodejs`
    will use. To translate to the binary format, we use the `wat2wasm`
    tool provided by the WebAssembly developers. For your convenience we
    have included it in the `bin` directory of the skeleton. Note that
    this tool performs a purely mechanical translation and thus its
    output (for instance, `p.wasm`) corresponds to a binary
    representation of `p.wat`.
-   `p.js` is a JavaScript wrapper which we will run with nodejs and
    serve as an entrypoint into your generated binary.

To run the program, simply type `nodejs wasmout/p.js`

### Installing nodejs

-   You can find directions for your favorite operating system
    [here](https://nodejs.org/en/). You should have nodejs 12 or later
    (run `nodejs --version` to make sure).
-   Once you have installed nodejs, run `npm install deasync` from the
    directory you plan to run `amyc` in, i.e. the toplevel directory of
    the compiler.
-   Make sure the `wat2wasm` executable is visible, i.e. it is in the
    system path or you are at the toplevel of the `amyc` directory.

## WebAssembly and Amy

The slides for this year's presentation are in the files called lab05-slides.

Look at [this
presentation](http://lara.epfl.ch/~gschmid/clp20/codegen.pdf) for the
main concepts of how to translate Amy programs to WebAssembly.

You can find the annotated compiler output to the concat example
[here](http://lara.epfl.ch/~gschmid/clp20/concat.wat).

## The assignment code

### Overview

The code for the assignment is divided into two directories: `wasm` for
the modeling of the WebAssembly framework, and `codegen` for
Amy-specific code generation. There is a lot of code here, but your task
is only to implement code generation for Amy expressions within
`codegen/CodeGen.scala`.

-   `wasm/Instructions.scala` provides types that describe a subset of
    WebAssembly instructions. It also provides a type `Code` to describe
    sequences of instructions. You can chain multiple instructions or
    `Code` objects together to generate a longer `Code` with the `<:>`
    operator.
-   `wasm/Function.scala` describes a wasm function.
    -   `LocalsHandler` is an object which will create fresh indexes for
        local variables as needed.
    -   A `Function` contains a field called `isMain` which is used to
        denote a main function without a return value, which will be
        handled differently when printing, and will be exported to
        JavaScript.
    -   The only way to create a `Function` is using `Function.apply`.
        Its last argument is a function from a `LocalsHandler` to
        `Code`. The reason for this unusual choice is to make sure the
        Function object is instantiated with the number of local
        variables that will be requested from the LocalsHandler. To see
        how it is used, you can look in `codegen/Utils.scala` (but you
        won\'t have to use it directly).
-   `wasm/Module.scala` and `wasm/ModulePrinter.scala` describe a wasm
    module, which you can think of as a set of functions and the
    corresponding module headers.
-   `codegen/Utils.scala` contains a few utility functions (which you
    should use!) and implementations of the built-in functions of Amy.
    Use the built-ins as examples.
-   `codegen/CodeGen.scala` is the focus of the assignment. It contains
    code to translate Amy modules, functions and expressions to wasm
    code. It is a pipeline and returns a wasm Module.
-   `codegen/CodePrinter.scala` is a Pipeline which will print output
    files from the wasm module.

### The cgExpr function

The focus of this assignment is the `cgExpr` function, which takes an
expression and generates a `Code` object. It also takes two additional
arguments: (1) a `LocalsHandler` which you can use to get a new slot for
a local when you encounter a local variable or you need a temporary
variable for your computation. (2) a map `locals` from `Identifiers` to
locals slots, i.e. indices, in the wasm world. For example, if `locals`
contains a pair `i -> 4`, we know that `get_local 4` in wasm will push
the value of i to the stack. Notice how `locals` is instantiated with
the function parameters in `cgFunction`.

## Skeleton

As usual, you can find the skeleton for this lab in a new branch of your
group\'s repository. After merging it with your existing work, the
structure of your project `src` directory should be as follows:

    src/amyc
     ├── Main.scala                (updated)
     │
     ├── analyzer   
     │    ├── SymbolTable.scala
     │    ├── NameAnalyzer.scala
     │    └── TypeChecker.scala
     │
     ├── ast
     │    ├── Identifier.scala
     │    ├── Printer.scala
     │    └── TreeModule.scala
     │
     ├── bin
     │    └── ...
     │
     ├── codegen                  (new)      
     │    ├── CodeGen.scala
     │    ├── CodePrinter.scala
     │    └── Utils.scala
     │
     ├── interpreter
     │    └── Interpreter.scala
     │
     ├── lib
     │    ├── scallion_3.0.6.jar
     │    └── silex_3.0.6.jar
     │
     ├── parsing
     │    ├── Parser.scala
     │    ├── Lexer.scala
     │    └── Tokens.scala
     │
     ├── utils
     │    ├── AmycFatalError.scala
     │    ├── Context.scala
     │    ├── Document.scala
     │    ├── Pipeline.scala
     │    ├── Position.scala
     │    ├── Reporter.scala
     │    └── UniqueCounter.scala
     │
     └── wasm                     (new)
          ├── Function.scala
          ├── Instructions.scala 
          ├── ModulePrinter.scala
          └── Module.scala

## Deliverables

You are given **2 weeks** for this assignment.

Deadline: **Wednesday December 2 at 11 pm.**.

Submission: one team member submits a zip file submission-groupNumber.zip to the [moodle submission page](https://moodle.epfl.ch/mod/assign/view.php?id=1181848).

Your submission only needs to contain your `src` directory. 
You can use the following command (from the root of your repository) to generate the archive:
```
zip -r submission-<groupNumber>.zip src/
```

You can then verify the content of the archive using `unzip -l submission-<groupNumber>.zip`
