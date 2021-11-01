# EPFL CS320 - Computer language processing, 2021

[Moodle](https://moodle.epfl.ch/course/view.php?id=4241), [Coursebook](https://edu.epfl.ch/coursebook/en/computer-language-processing-CS-320)

**Implement a programming language!** All project based.

This repository is the website for EPFL Course CS320, Computer languge processing. 

Please check here regularly for course updates.

## Grading

The entire grade is based on project work, which, however, has many aspects: the implementation in [Scala](https://www.scala-lang.org/), a written report, oral presentation, and answers to questions, in presentation and throughout the semester. There will be no written exam.

## Staff

| Role            | People |
| :---            | :--- |
| Professors      | [Viktor Kunčak](https://people.epfl.ch/viktor.kuncak), [Self-Introduction](https://tube.switch.ch/videos/82f0eb14) |
| Head PhD TA     | [Rodrigo Raya](https://people.epfl.ch/rodrigo.raya/)
| PhD TA          | [Fereshte Mozafari](https://people.epfl.ch/fereshte.mozafari) |
| MSc Student TAs | [Julie Giunta](http://people.epfl.ch/julie.giunta), Solène Husseini, [Benoît Maillard](https://people.epfl.ch/benoit.maillard), [Noé De Santo](https://people.epfl.ch/noe.desanto), [Alexandre Pinazza](https://people.epfl.ch/alexandre.pinazza) |

## Schedule and material

Monday slots will typically focus on lecture materials. The remaining slots will focus on labs, **except for the first week's Wednesday and Thursday** when we will have lectures to get basic understanding of the metarial. We will provide an online version of the activity as well. So far, as long as there is demand, the plan is to also be present in the physical room, as scheduled.

The material we cover will be similar to [last year](https://lara.epfl.ch/w/cc20/top). 

Note that slides can be found **underneath each lecture video** on switch tube linkes below.


| Week | Day | Date       | Time  | Room   | Topic                | Videos & Slides              |                              |
| :--  | :-- | :--        | :--   | :--    | :--                  | :--                          | :--                          |
| 1    | Wed | 22.09.2021 | 08:15 | INM202 | Lecture 1            | [Intro to CLP](https://tube.switch.ch/videos/3351ec99), [Formal languages](https://tube.switch.ch/videos/6df3a6ba), [Live Recording](https://tube.switch.ch/videos/N0KUZHSEnt) |
|      | Thu | 23.09.2021 | 08:15 | INF119 | Lecture 2            | [Operations on Languages](https://tube.switch.ch/videos/1846c6f2), [Regular Expressions and Lexer Idea](https://tube.switch.ch/videos/c903b9d5), [Live Recording 1](https://tube.switch.ch/videos/cdQxQvZcV8), [Live Recording 2](https://tube.switch.ch/videos/26pmlTMe8j) |
| 2    | Mon | 27.09.2021 | 13:15 | CE1100 | Lecture 3            | [First Symbols. Constructing a Lexer](https://tube.switch.ch/videos/fc864ce0), [Live Recording 1](https://tube.switch.ch/videos/WjS9tOAVex) [From Regular Expressions to Automata](https://tube.switch.ch/videos/a15b1c5f), [Live Recording 2](https://tube.switch.ch/videos/sBurwXgGlO) |
|      | Wed | 29.09.2011  | 08:15 | INM202 | Labs 1              | [Labs setup](https://gitlab.epfl.ch/lara/cs320/-/blob/main/labs/labs_setup.md), [Amy Interpreter](https://gitlab.epfl.ch/lara/cs320/-/blob/main/labs/labs_01.md)         |
|      | Thu | 30.09.2011  | 08:15 | INF119 | Labs 1     |  |
| 3    | Mon | 04.10.2021 | 13:15 | CE1100 | Lecture 4            | [Introduction to Grammars](https://tube.switch.ch/videos/ef9ae6c1), [Syntax Trees](https://tube.switch.ch/videos/9314ac16), [LL(1) Parsing](https://tube.switch.ch/videos/38dd46b4) |
|     | Wednesday | 06.10.2021 | 8:15 | INM202 | Labs 2           | [Lexer](https://gitlab.epfl.ch/lara/cs320/-/blob/main/labs/labs_02.md) |
|     | Thursday | 07.10.2021 | 8:15 | INF119 | Labs 2           | [Lexer](https://gitlab.epfl.ch/lara/cs320/-/blob/main/labs/labs_02.md) |
| 4    | Mon | 11.10.2021 | 13:15 | CE1100 | Lecture 5            | [CYK Algorithm for Parsing General Context-Free Grammars](https://tube.switch.ch/videos/672add06), [Live Recording 1](https://tube.switch.ch/videos/MOGmWUzw1x), [Chomsky Normal Form Transformation](https://tube.switch.ch/videos/2d3503f4), [Live Recording 2](https://tube.switch.ch/videos/1pUjEGgFk7) |
|     | Wednesday | 13.10.2021 | 8:15 | INM202 | Labs 2           | [Lexer](https://gitlab.epfl.ch/lara/cs320/-/blob/main/labs/labs_02.md) |
|     | Thursday | 14.10.2021 | 8:15 | INF119 | Labs 2           | [Lexer](https://gitlab.epfl.ch/lara/cs320/-/blob/main/labs/labs_02.md) |
| 5    | Mon | 18.10.2021 | 13:15 | CE1100 | Lecture 6            | [Name Analysis](https://tube.switch.ch/videos/a842b90d), [Live Recording 1](https://tube.switch.ch/videos/SCjOc2ZpTo), [Inductive Relations](https://tube.switch.ch/videos/5d67c147) [Live Recording 2](https://tube.switch.ch/videos/rB0nQIXfV4), [Operational Semantics](https://tube.switch.ch/videos/465af7b1), [Live Recording 3](https://tube.switch.ch/videos/lvOsDDcL1L) |
|     | Wednesday | 20.10.2021 | 8:15 | INM202 | Labs 2/3           | [Lexer](https://gitlab.epfl.ch/lara/cs320/-/blob/main/labs/labs_02.md), [Parser](https://gitlab.epfl.ch/lara/cs320/-/blob/main/labs/labs_03.md) | |
|     | Thursday | 21.10.2021 | 8:15 | INF119 | Labs 3           | [Parser](https://gitlab.epfl.ch/lara/cs320/-/blob/main/labs/labs_03.md) |
| 6    | Mon | 25.10.2021 | 13:15 | CE1100 | Lecture 7            | [Type Rules, Progress, Preservation](https://tube.switch.ch/videos/bdb5c902), [Live Recording 1](https://tube.switch.ch/videos/LsU9vOYA2y), [Type Inference](https://tube.switch.ch/videos/14facfc5), [Live Recording 2](https://tube.switch.ch/videos/RCKc4K0POS) |
| 7    | Mon | 01.11.2021 | 13:15 | CE1100 | Lecture 8        | [Live Recording on Solving Type Constraints](https://tube.switch.ch/videos/C1IyJDwZEk/edit) [Web Assembly](https://tube.switch.ch/videos/fd21d42e), [Compiling Expressions](https://tube.switch.ch/videos/e0f59928), [Short Live Recording on Code generation 1](https://tube.switch.ch/videos/13qwGs8vV5), [Live Recording on Code Generation 2](https://tube.switch.ch/videos/uUrfgpUg6l) |
