# EPFL CS320 - Computer language processing, Spring 2025

Links: [Moodle](https://moodle.epfl.ch/course/view.php?id=4241) and [Course Description](https://edu.epfl.ch/coursebook/en/computer-language-processing-CS-320)

Important information:

  * Midterm exam will take place Friday 4 April within the time block 13:00-17:00 in two rooms: [ELA 2](https://plan.epfl.ch/?room==ELA%202) and [CM 1 120](https://plan.epfl.ch/?room==CM%201%20120). One reminder sheet (2 sided) will be allowed.

  * Please register for project groups on Moodle as soon as this is possible ([Registration link](https://moodle.epfl.ch/mod/choicegroup/view.php?id=1282182))

## Grading

The grade is based on a midterm (30%) as well as team project work (70%). Please read the [grading policy page](info/grading.md).

## Schedule and Materials - Past Weeks

| Week | Labs | Day | Date       | Time  | Room   | Topic                | Materials              
| :--  | :--  | :-- | :--        | :--   | :--    | :--                  | :--                          
| 1    |      | Wed | 19.02.2025 | 13:15 | BC 01  | Lecture 1            | [Intro to CLP](https://mediaspace.epfl.ch/media/01-01%2C+Intro+to+Computer+Language+Processing/0_okro5h0v) [(PDF)](info/lectures/lec01a.pdf), [Formal languages](https://mediaspace.epfl.ch/media/01-02%2C+Formal+Languages/0_segfj94w) [(PDF)](info/lectures/lec01b.pdf) |
|      |      | Fri | 21.02.2025 | 13:15 | ELA 2  | Lecture 2 | [Operations on Formal Languages](https://mediaspace.epfl.ch/media/02-01%2C+Operations+on+Formal+Languages/0_otyeghg6), [Regular Expressions and Idea of a Lexer](https://mediaspace.epfl.ch/media/02-02%2C+Regular+Expressions+and+Lexer+Idea/0_th59v9kx) [(PDF)](info/lectures/lec02.pdf) |
|      | 1.... | Fri | 21.02.2025 | 15:15 | ELA 2  | Lab 1 | [Interpreter lab released (due in 2 weeks)](./info/labs/lab01/) |
| 2    | 1.... | Wed | 26.02.2025 | 13:15 | BC 01  | Lecture 3 | [First Symbols. Constructing a Lexer](https://mediaspace.epfl.ch/media/03-01%2C+First+Symbols.+Constructing+a+Lexer/0_a943fw0n) [(PDF)](info/lectures/lec03a.pdf), [From Regular Expressions to Automata](https://mediaspace.epfl.ch/media/03-02%2C+From+Regular+Expressions+to+Automata/0_icjqhfj0) [(PDF)](info/lectures/lec03b.pdf) |
|      | 1..... | Fri | 28.02.2025 | 13:15 | ELA 2  | Exercise 1 | [Languages, Automata and Lexers](info/exercises/ex-01.pdf) |
|      | 12.... | Fri | 28.02.2025 | 15:15 | ELA 2  | Lab 2 | Lexer lab release |
| 3    | 12.... | Wed | 05.03.2025 | 13:15 | BC 01  | Lecture 4 | [Introduction to Grammars](https://mediaspace.epfl.ch/media/04-01%2C+Introduction+to+Grammars/0_krhjbo09) [(PDF)](info/lectures/lec04-grammars-intro.pdf), [Syntax Trees](https://mediaspace.epfl.ch/media/04-02%2C+Syntax+Trees/0_9h4g5k1c) [(PDF)](info/lectures/lec04-trees.pdf)
|      | 12.... | Fri | 07.03.2025 | 13:15 | ELA 2  | Exercises 2 | [Grammar Concepts](info/exercises/ex-02.pdf) [(solutions)](info/exercises/ex-02-sol.pdf) |
|      | 123... | Fri | 07.03.2025 | 15:15 | ELA 2  | Lab 3 | [Parser lab](info/labs/lab03/) release |
| 4    | .23... | Wed | 12.03.2025 | 13:15 | BC 01  | Lecture 5 | [LL(1) Parsing](https://mediaspace.epfl.ch/media/04-03%2C+LL%281%29+Parsing/0_se2zd8kt) [(PDF)](info/lectures/lec05-ll1.pdf). [Scallion Tutorial](https://mediaspace.epfl.ch/media/04-10%2C+Scallion+tutorial/0_lypn7l0x) |
|      | .23... | Fri | 14.03.2025 | 13:15 | ELA 2  | Lecture 6  | [Name Analysis](https://mediaspace.epfl.ch/media/06-01%2C+Name+Analysis/0_1b9t1hz8) [(PDF)](info/lectures/lec06-name-analysis.pdf), [Type Systems as Inductive Relations](https://mediaspace.epfl.ch/media/07-01%2C+Introduction+to+Types+and+Inductive+Relations/0_3hxblocu) [(PDF)](info/lectures/lec06-inductive.pdf) . [Operational Semantics](https://mediaspace.epfl.ch/media/07-02%2C+Operational+Semantics/0_3ru05nbo) [(PDF)](info/lectures/lec06-operational.pdf) |
|      | .23... | Fri | 14.03.2025 | 15:15 | ELA 2  | Lab 3 | [Parser lab](info/labs/lab03/) |
| 5    | ..3... | Wed | 19.03.2025 | 13:15 | BC 01  | Exercises 3 | [LL(1) Grammars](info/exercises/ex-03.pdf) [(solutions)](info/exercises/ex-03-sol.pdf) |
|      | ..3... | Fri | 21.03.2025 | 13:15 | ELA 2  | Lecture 7 | [Type Checking](https://mediaspace.epfl.ch/media/07-03%2C+Type+Rules%2C+Progress%2C+Preservation/0_znlmwvlt) [(PDF)](info/lectures/lec07-soundness.pdf), [Type Inference](https://mediaspace.epfl.ch/media/08-01%2C+Type+Inference/0_txnn92oh) [(PDF)](info/lectures/lec07-inference.pdf)  |
|      | ..34.. | Fri | 21.03.2025 | 15:15 | ELA 2  | Lab 4 | [Typer lab](info/labs/lab04/) release |
| 6    | ..34.. | Wed | 26.03.2025 | 13:15 | BC 01  | Exercises 4 | [Operational Semantics and Type Checking](info/exercises/ex-04.pdf) [(solutions)](info/exercises/ex-04-sol.pdf) |
|      | ..34.. | Fri | 28.03.2025 | 13:15 | ELA 2  | Lecture 8 | Finish [Type Inference](https://mediaspace.epfl.ch/media/08-01%2C+Type+Inference/0_txnn92oh) [(PDF)](info/lectures/lec07-inference.pdf). Start [Code generation: Examples and Web Assembly](https://mediaspace.epfl.ch/media/09-01%2C+Code+GenerationA+Examples%2C+WebAssembly/0_xowt7e6w) [(PDF)](info/lectures/lec08-codegen.pdf) [Compiling Expressions](https://mediaspace.epfl.ch/media/09-02%2C+Compiling+Expressions/0_33e91bye) [(PDF)](info/lectures/lec08-codegen-postfix.pdf) |
|      | ..34.. | Fri | 28.03.2025 | 15:15 | ELA 2  | Lab 4 | [Typer lab](info/labs/lab04/) |
| 7    | ...4.. | Wed | 02.04.2025 | 13:15 | BC 01  | Exercises 5 | [Type checking and more](info/exercises/ex-05.pdf) [(solutions)](info/exercises/ex-05-sol.pdf); [Review exercises](info/exercises/ex-review.pdf) [(solutions)](info/exercises/ex-review-sol.pdf)|
|      | ...4.. | Fri | 04.04.2025 | 13:15 | ELA+CM | **EXAM** |  |
|      | ...4.. | Fri | 04.04.2025 | 15:15 | ELA+CM | **EXAM** |  |

## Schedule and Materials - Current

| Week | Labs | Day | Date       | Time  | Room   | Topic                | Materials              |
| :--  | :--  | :-- | :--        | :--   | :--    | :--                  | :--                    | 
|      |        |     |            |       |        |                           | 
| 8    | ...45. | Wed | 09.04.2025 | 13:15 | BC 01  | Lab 5 | Codegen lab release |
|      | ...45. | Fri | 11.04.2025 | 13:15 | ELA 2  | Lecture 9 | Code generation |
|      | ...45. | Fri | 11.04.2025 | 15:15 | ELA 2  | Lab 5 | Codegen lab |
|      |        |     |            |       |        |                           | 
| 9    | ....56 | Wed | 16.04.2025 | 13:15 | BC 01  | Lab 5 | Codegen lab. Pick projects |
|      |        | Fri | 18.04.2025 | 13:15 | ELA 2  | **HOLIDAY** |  |
|      |        | Fri | 18.04.2025 | 15:15 | ELA 2  | **HOLIDAY** |  |
|      |        |     |            |       |        |                           | 
| -    |        | Wed | 23.04.2025 |       |        | **BREAK** |  |
|      |        | Fri | 25.04.2025 |       |        | **BREAK** |  |
|      |        | Fri | 25.04.2025 |       |        | **BREAK** |  |
|      |        |     |            |       |        |                           | 
| 10   | ....56 | Wed | 30.04.2025 | 13:15 | BC 01  | Labs | **Oral Lab Checks** |
|      | ....56 | Fri | 02.05.2025 | 13:15 | ELA 2  | Labs | **Oral Lab Checks** |
|      | ....56 | Fri | 02.05.2025 | 15:15 | ELA 2  | Lecture 10 | Parsing general grammars |
|      |        |     |            |       |        |                           | 
| 11   | .....6 | Wed | 07.05.2025 | 13:15 | BC 01  | Lecture 11 | Compiler correctness |
|      | .....6 | Fri | 09.05.2025 | 13:15 | ELA 2  | Labs |  |
|      | .....6 | Fri | 09.05.2025 | 15:15 | ELA 2  | Labs |  |
|      |        |     |            |       |        |                           | 
| 12   | .....6 | Wed | 14.05.2025 | 13:15 | BC 01  | Labs |  |
|      | .....6 | Fri | 16.05.2025 | 13:15 | ELA 2  | Labs |  |
|      | .....6 | Fri | 16.05.2025 | 15:15 | ELA 2  | Labs |  |
|      |        |     |            |       |        |                           | 
| 13   | .....6 | Wed | 21.05.2025 | 13:15 | BC 01  |    | Project presentations |
|      | .....6 | Fri | 23.05.2025 | 13:15 | ELA 2  |    | Project presentations |
|      | .....6 | Fri | 23.05.2025 | 15:15 | ELA 2  |    | Project presentations |
|      |        |     |            |       |        |                           | 
| 14   | .....6 | Wed | 28.05.2025 | 13:15 | BC 01  |    | Project presentations |
|      | .....6 | Fri | 30.05.2025 | 13:15 | ELA 2  |    | Project presentations |
|      | .....6 | Fri | 30.05.2025 | 15:15 | ELA 2  |    | Project presentations |

## Books

[Printed and online books](info/books.md) can be helpful, even if our goal is for lectures, exercises, and project instructions to be self-contained.

## Staff

|           |                                                                 |
|:----------|:----------------------------------------------------------------|
| Professor | [Viktor Kunčak](https://people.epfl.ch/viktor.kuncak)           |
| PhD TA    | [Samuel Chassot](https://people.epfl.ch/samuel.chassot)         |
| PhD TA    | [Sankalp Gambhir](https://people.epfl.ch/sankalp.gambhir)       |
| MSc TA    | [Sidonie Bouthors](https://people.epfl.ch/sidonie.bouthors)     |
| MSc TA    | [Sébastien Kobler](https://people.epfl.ch/sebastien.kobler)     |
| MSc TA    | [Marcin Wojnarowski](https://people.epfl.ch/marcin.wojnarowski) |
| MSc TA    | [Jacopo Moretti](https://people.epfl.ch/jacopo.moretti/)        |
