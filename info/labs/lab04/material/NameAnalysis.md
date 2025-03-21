# Name Analysis

In the following, we will briefly discuss the purpose and implementation of the name analyzer phase in Amy. Name analysis has three goals:

* To reject programs that do not follow the Amy naming rules.
* For correct programs, to assign a unique identifier to every name. Remember that trees coming out of the parser contain plain strings wherever a name is expected. This might lead to confusion as to what each name refers to. Therefore, during name analysis, we assign a unique identifier to each name at its definition. Later in the program, every reference to that name will use the same unique identifier.
* To populate the symbol table. The symbol table contains a mapping from identifiers to all information that you could need later in the program for that identifier. For example, for each constructor, the symbol table contains an entry with the argument types, parent, and an index for this constructor.

After name analysis, only name-correct programs should survive, and they should contain unique identifiers that correspond to the correct symbol in the program.

## The Symbol Table

The symbol table contains information for all kinds of entities in the program. In the first half of name analysis, we discover all definitions of symbols, assign each of them a fresh identifier, and store these identifier-definition entries in the symbol table.

The `SymbolTable` API contains three kinds of methods:

* `addX` methods will add a new object to the symbol table. Among other things, these methods turn the strings found in nominal trees into the fresh `Identifier`s we will use to construct symbolic trees.
* `getX` methods which take an `Identifier` as an argument. This is what you will be using to resolve symbols you find in the program, for example, during type checking.
* `getX` methods which take two strings as arguments. These are only useful for name analysis and should not be used later: since during name analysis unique identifiers have not been assigned to everything from the start, sometimes our compiler will need to look up a definition based on its name and the name of its containing module. Of course you should not use these methods once you already have an identifier (in particular, not during type checking).

## The different tree modules

It is time to talk in detail about the different tree modules in the `TreeModule` file. As explained earlier, our goal is to define two very similar tree modules, with the only difference being how a (qualified) name is represented: In a *nominal* tree, i.e. one coming out of the parser, names are plain strings and qualified names are pairs of strings. On the other hand, in a *symbolic* tree, both kinds of names are unique identifiers.

To represent either kind of tree, we define a single Scala trait called `TreeModule` which defines two *abstract type fields* `Name` and `QualifiedName`. This trait also defines all types we need to represent Amy ASTs. Many of these types depend on the abstract types.

These abstract types are filled in when we instantiate the trait. Further down in the same file you can see that we define two objects `NominalTreeModule` and `SymbolicTreeModule`, which instantiate the abstract types. In addition all types within `TreeModule` are conceptually defined separately in each of the two implementations. As a result, there is a type called `NominalTreeModule.Ite` which is *different* from the type called `SymbolicTreeModule.Ite`.

## The NameAnalyzer class

The `NameAnalyzer` class implements Amy's naming rules (section 3.4 of the Amy specification). It takes a nominal program as an input and produces a symbol table and a symbolic program.

Name analysis is split into well-defined steps. The idea is the following: we first discover all definitions in the program in the correct order, i.e., modules, types, constructors, and, finally, functions. We then rewrite function bodies and expressions to refer to the newly-introduced identifiers.

Notice how name analysis takes as input the `NominalTreeModule.Program` output by the Parser, and returns a `SymbolicTreeModule.Program` along with a populated symbol table. During the last step we therefore transform the program and each of its subtrees from `NominalTreeModule.X` into `SymbolicTreeModule.X`. For instance, a `NominalTreeModule.Program` will be transformed into a `SymbolicTreeModule.Program`, a `NominalTreeModule.Ite` into a `SymbolicTreeModule.Ite` and so forth. To save some typing, we have imported NominalTreeModule as `N` and SymbolicTreeModule as `S`. So to refer e.g. to a `Plus` in the original (nominal) tree module we can simply use `N.Plus` -- to refer to one in the symbolic tree module we can use `S.Plus`.
