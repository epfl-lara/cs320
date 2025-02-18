# EPFL CS320 - Computer language processing, Spring 2025

[Course Description and Schedule](https://edu.epfl.ch/coursebook/en/computer-language-processing-CS-320)

## IMPORTANT

Please register for groups on Moodle as soon as this is possible

## Grading

The grade is based on a midterm (30%) as well as team project work (70%).

The project work is done in groups of 2-3 people (no individual groups; the goal is in part to learn how to work together).
The work has many aspects: the implementation in [Scala](https://www.scala-lang.org/) of aspects of an interpreter and compiler (labs 1 to 5) and Lab 6, which is an open project. There will be no written exam at the _end_ of the semester and no exam in the exam period. Here are the weights of the milestones in the overall course grade:

  * Midterm exam: 30% (see [the archive of past exams](past-exams/); note that we will have fewer multiple-choice questions this time)
  * 10% Lab 1
  * 10% Lab 2
  * 10% Lab 3 (First team work statement to be sent afterwards)
  * 10% Lab 4
  * 10% Lab 5
  * 20% Lab 6 (Compiler extension, customized, the final team work statement)

For the final Lab 6, each group will need to do their own project (based on our suggestions or your own ideas that you check with the teaching staff). Each member of the group must present the project in a slot in one of the last two weeks of the semester and answer questions. The presentation part of of each person will be graded individually and includes answers to questions (a person not presenting will be given a 0 points for the presentation part of the Compiler extension lab). The final report on the project will need to handed in after the end of the semester but the students are encouraged to complete it during the semester as this is a continuous control course. 

To monitor whether everyone is doing their share of work, we ask each student to submit via email their teamwork statement, twice during the semester: once right after Lab 3 is due, and once at the end of the semester. Please read [Teamwork Statements](teamwork.md) on the format and the relevance of these emails.

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

## Books

Our goal is for lectures to be self-contained. The following books contain overlapping material with some recommendations for most relevant parts:

  * [Discrete Mathematics and Its Applications by Kenneth H. Rosen (8th edition)](https://epfl.swisscovery.slsp.ch/discovery/fulldisplay?docid=alma99116968862405516&context=L&vid=41SLSP_EPF:prod&lang=en&search_scope=MyInst_and_CI&adaptor=Local%20Search%20Engine&tab=41SLSP_EPF_MyInst_and_CI&query=any,contains,Discrete%20Mathematics%20and%20Its%20Applications&sortby=date_d&facet=frbrgroupid,include,9018235242682604086&offset=0), available in the library and you may have it already. Useful backround is in sections 1.7, 1.8, 2.1, 2.2, 5.1, 5.3, 9.1, 9.2, 13.1, 13.3, 13.4
  * [Basics of Compiler Design](http://hjemmesider.diku.dk/~torbenm/Basics/). Read pages 9-88 (omit Section 2.8) for lexical analysis and parsing
  * [Modern compiler implementation in ML](http://library.epfl.ch/en/beast?isbn=9781107266391). Read Sections 2.1-2.4 for Lexical analysis, Sections 3.1-3.2 for parsing, and 5.3-5.4 as well as 16.1-16.3 for type checking
  * [Compilers, principle, techniques and tools](http://library.epfl.ch/en/beast?isbn=9781292024349)

## Schedule and Material

| Week | Day | Date       | Time  | Room   | Topic                | Videos & Slides              |                              |
| :--  | :-- | :--        | :--   | :--    | :--                  | :--                          | :--                          |
| 1    | Wed | 19.02.2025 | 13:15 | BC 01  | Lecture 1            | [Intro to CLP](https://mediaspace.epfl.ch/media/01-01%2C+Intro+to+Computer+Language+Processing/0_okro5h0v) [(PDF)](lectures/lec01a.pdf), [Formal languages](https://mediaspace.epfl.ch/media/01-02%2C+Formal+Languages/0_segfj94w) [(PDF)](lectures/lec01b.pdf) |
|      | Fri | 21.02.2025 | 13:15 | ELA 2  | Lecture 2 | [Operations on Formal Languages](https://mediaspace.epfl.ch/media/02-01%2C+Operations+on+Formal+Languages/0_otyeghg6), [Regular Expressions and Idea of a Lexer](https://mediaspace.epfl.ch/media/02-02%2C+Regular+Expressions+and+Lexer+Idea/0_th59v9kx) |
|      | Fri | 21.02.2025 | 15:15 | ELA 2  | Lab 1 | Interpreter |
| 2    | Wed | 26.02.2025 | 13:15 | BC 01  | Lecture 3 | [First Symbols. Constructing a Lexer](https://mediaspace.epfl.ch/media/03-01%2C+First+Symbols.+Constructing+a+Lexer/0_a943fw0n), [From Regular Expressions to Automata](https://mediaspace.epfl.ch/media/03-02%2C+From+Regular+Expressions+to+Automata/0_icjqhfj0) |
|      | Fri | 28.02.2025 | 13:15 | ELA 2  | Exercise 1 | Languages, Automata and Lexers |
|      | Fri | 28.02.2025 | 15:15 | ELA 2  | Lab 2 | Lexer |
