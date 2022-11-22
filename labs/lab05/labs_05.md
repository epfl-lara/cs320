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
    tool provided by the WebAssembly developers. Note that
    this tool performs a purely mechanical translation and thus its
    output (for instance, `p.wasm`) corresponds to a binary
    representation of `p.wat`.
-   `p.js` is a JavaScript wrapper which we will run with nodejs and
    serve as an entrypoint into your generated binary.

To run the program, simply type `nodejs wasmout/p.js`

### Installing nodejs and wat2wasm

-   You can find directions for your favorite operating system
    [here](https://nodejs.org/en/). You should have nodejs 12 or later
    (run `nodejs --version` to make sure).
-   Once you have installed nodejs, run `npm install deasync` from the
    directory you plan to run `amyc` in, i.e. the toplevel directory of
    the compiler.
-   Install `wat2wasm` using your favorite package manager, the name of
    the package is usually `wabt` (`apt install wabt`, `pacman -Sy wabt`, etc).
    If you are not on linux, you can download it here: 
    <https://github.com/WebAssembly/wabt/releases/tag/1.0.31>, then copy the file 
    `bin/wat2wasm` (or `/bin/wat2wasm.exe` for windows) from the archive to 
    \<root of the project\>/bin 
-   Make sure the `wat2wasm` executable is visible: either in a system path,
    or in the \<root of the project\>/bin folder (that you may have to create). 

## WebAssembly and Amy

Links to the material will be provided here after the presentation of the lab.

Presentation by Georg Schmid from a few years ago: <https://tube.switch.ch/videos/00568845>, slides <https://lara.epfl.ch/~gschmid/clp20/codegen.pdf>
The lab has changed a tiny bit, for instance `set_global`, `get_global`, `set_local` and `get_local` are outdated and replaced with `global.set`, `global.get`, `local.set` and `local.get`, but otherwise it is a very good resource.

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
contains a pair `i -> 4`, we know that `local.get 4` in wasm will push
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

You are given **TBD weeks** for this assignment.

Deadline: **TBD**.
