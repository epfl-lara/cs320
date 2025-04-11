# Lab 05: Code Generation

## Introduction

Welcome to the last common assignment for the Amy compiler. At this point, we are finally done with the frontend: we have translated source programs to ASTs and have checked that certain correctness conditions hold for our program. We are ready to generate code for our program. In our case the target language will be _WebAssembly_.

WebAssembly is "a new portable, size- and load-time-efficient format suitable for compilation to the web" (<http://webassembly.org>). WebAssembly was initially designed to be called from JavaScript in browsers and lends itself to highly-performant execution. Nowadays, WebAssembly is getting some traction in many different contexts, including server-side applications and embedded systems.

For simplicity, we will not use a browser, but execute the resulting WebAssembly bytecode directly using `wasmtime` which is WebAssembly virtual machine implementation. When you run your complete compiler (or the reference compiler) with no options on program `p`, it will generate two different files under the `wasmout` directory:

- `p.wat` is the wasm output of the compiler in a human readable text format. You can use this representation to debug your generated code.
- `p.wasm` is the binary output of the compiler. This is what `wasmtime` will read. To translate to the binary format, we use the `wat2wasm` tool provided by the WebAssembly developers. Note that this tool performs a purely mechanical translation and thus its output (for instance, `p.wasm`) corresponds to a binary representation of `p.wat`.

To run the program, simply type `wasmtime wasmout/p.wasm`

### Installing `wat2wasm` and `wasmtime`

Both `wat2wasm` and `wasmtime` are bundled for you under `/<root of the project>/bin/<platform>`. You may however install them yourself and place them either in your `PATH`, or in `/<root of the project>/bin/<platform>`.

- To install `wat2wasm` using your favorite package manager, the name of the package is usually `wabt` (`apt install wabt`, `pacman -Sy wabt`, `brew install wabt`, etc). If you are not on linux or MacOS, you can download it here: <https://github.com/WebAssembly/wabt/releases/tag/1.0.31>
- To install `wasmtime` use the following command (on linux and MacOS): `curl https://wasmtime.dev/install.sh -sSf | bash`. If you are not on linux or MacOS, you can download it here: <https://docs.wasmtime.dev/cli-install.html>

## WebAssembly and Amy

Here you have some resources to get you started with WebAssembly:

- [WebAssembly demo](./material/webassembly-extra.md)
- Presentation by Georg Schmid from a few years ago: [Video](https://mediaspace.epfl.ch/media/09-10%2C+Code+Generation+Lab/0_8r1ahhhq/30820), [slides](https://lara.epfl.ch/~gschmid/clp20/codegen.pdf)

  The lab has changed a tiny bit, for instance `set_global`, `get_global`, `set_local` and `get_local` are outdated and replaced with `global.set`, `global.get`, `local.set` and `local.get`, but otherwise it is a very good resource.

## The assignment code

### Overview

The code for the assignment is divided into two directories: `wasm` for the modeling of the WebAssembly framework, and `codegen` for Amy-specific code generation. There is a lot of code here, but your task is only to implement code generation for Amy expressions within `codegen/CodeGen.scala`.

- `wasm/Instructions.scala` provides types that describe a subset of WebAssembly instructions. It also provides a type `Code` to describe sequences of instructions. You can chain multiple instructions or `Code` objects together to generate a longer `Code` with the `<:>` operator.
- `wasm/Function.scala` describes a wasm function.
  - `LocalsHandler` is an object which will create fresh indexes for local variables as needed.
  - A `Function` contains a field called `isMain` which is used to denote a main function without a return value, which will be handled differently when printing.
  - The only way to create a `Function` is using `Function.apply`. Its last argument is a function from a `LocalsHandler` to `Code`. The reason for this unusual choice is to make sure the `Function` object is instantiated with the number of local variables that will be requested from the LocalsHandler. To see how it is used, you can look in `codegen/Utils.scala` (but you won't have to use it directly).
- `wasm/Module.scala` and `wasm/ModulePrinter.scala` describe a wasm module, which you can think of as a set of functions and the corresponding module headers.
- `codegen/Utils.scala` contains a few utility functions (which you should use!) and implementations of the built-in functions of Amy. Use the builtins as examples. The builtins to read and write from and to StdIn and StdOut are particularly interesting; you might want to have a look at them (the comments are particularly insightful to understand the code).
- `codegen/CodeGen.scala` is the focus of the assignment. It contains code to translate Amy modules, functions and expressions to wasm code. It is a pipeline and returns a wasm Module.
- `codegen/CodePrinter.scala` is a Pipeline which will print output files from the wasm module.

### The cgExpr function

The focus of this assignment is the `cgExpr` function, which takes an expression and generates a `Code` object. It also takes two additional arguments: (1) a `LocalsHandler` which you can use to get a new slot for a local when you encounter a local variable or you need a temporary variable for your computation; (2) a map `locals` from `Identifiers` to locals slots, i.e. indices, in the wasm world. For example, if `locals` contains a pair `i -> 4`, we know that `local.get 4` in wasm will push the value of i to the stack. Notice how `locals` is instantiated with the function parameters in `cgFunction`.

## Skeleton

As usual, you can find the skeleton for this lab in a new branch of your group's repository. After merging it with your existing work, the structure of your project `src` directory should be as follows:

```plaintext
bin                                (new)
├── linux
│    ├── wasmtime
│    └── wat2wasm
├── macos
│    ├── wasmtime
│    └── wat2wasm
└── windows
     ├── wasmtime.exe
     └── wat2wasm.exe
src/
├── amyc
│    ├── Main.scala                (updated)
│    │
│    ├── analyzer
│    │    ├── SymbolTable.scala
│    │    ├── NameAnalyzer.scala
│    │    └── TypeChecker.scala
│    │
│    ├── ast
│    │    ├── Identifier.scala
│    │    ├── Printer.scala
│    │    └── TreeModule.scala
│    │
│    ├── codegen                                (new)
│    │    ├── CodeGen.scala
│    │    ├── CodePrinter.scala
│    │    └── Utils.scala
│    │
│    ├── interpreter
│    │    └── Interpreter.scala                  (to update with your own from lab01)
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
│    ├── utils
│    │    ├── AmycFatalError.scala
│    │    ├── Context.scala
│    │    ├── Document.scala
│    │    ├── Pipeline.scala
│    │    ├── Position.scala
│    │    ├── Reporter.scala
│    │    └── UniqueCounter.scala
│    │
│    └── wasm                                    (new)
│         ├── Function.scala
│         ├── Instructions.scala
│         ├── ModulePrinter.scala
│         └── Module.scala
│
│
└── test
     ├── scala
     │    └── amyc
     │         └── test
     │              ├── CodegenTests.scala
     │              ├── CompilerTest.scala
     │              ├── LexerTests.scala
     │              ├── NameAnalyzerTests.scala
     │              ├── ParserTests.scala
     │              ├── TestSuite.scala
     │              ├── TestUtils.scala
     │              └── TyperTests.scala
     └── resources
          ├── analyzer
          │    └── ...
          ├── execution                           (new)
          │    └── ...
          ├── lexer
          │    └── ...
          └── parser
               └── ...
```

## Deliverables

Deadline: **02.05.2025 23:59:59**

You should submit the following files:

- `CodeGen.scala`: The implementation of the code generator.
