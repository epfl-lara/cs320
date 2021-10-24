# Labs 06: Compiler extension project

You have now written a compiler for Amy, a simple functional language.
The final lab project is to design and implement a new functionality of
your own choice on top of the compiler you built so far. In preparation
for this, you should aim to learn about the problem domain by searching
the appropriate literature. The project includes:

-   designing and implementing the new functionality
-   documenting the results in a written report document

This project has several deadlines, detailed below. Please note that the
first of them (choosing the topic) is already coming up on Sunday!

## Selecting a Project Topic

**Deadline: TBD**

In the following document, we list several project ideas, but you should
also feel free to submit your own by email. All groups will rank the
projects in order of preference, and we will then do our best to assign
the preferred projects to as many groups as possible. Because not all
projects are equally difficult, we annotated each of them with the
expected workload. The suggested projects cover a wide range of
complexity, and we will evaluate your submissions with that complexity
in mind. For instance, for a project marked with `(1)` (relatively low
complexity) we will be expecting a polished, well-tested and
well-documented extension, whereas projects on the other end (`(3)`) may
be more prototypical. For all submissions, however, we require that you
deliver code that compiles and a set of example input files that
demonstrate the new functionality.

[Project ideas](labs06_material/extensions.pdf)

To announce your preferences, [please fill out this form by Sunday at
the latest](). You\'ll have to
provide **the names of the top exactly 5** projects you would like to
work on, in order of descending preference. We will do our best to
assign you the project you are most interested in.

## Project Orientation

**Deadline: TBD**

We will try to inform you about the project assignment as soon as
possible. To give you a chance to validate your understanding of the
project and what\'s expected of you, we will offer dedicated slots
during the project sessions next week. Before you join, you should think
about the following questions

-   What are the features you will add to the compiler/language?
-   What would be some (short) programs highlighting the use of these
    features?
-   What changes might be required in each compiler phase and/or what
    new phases would you add? (Very roughly)

**TODO: define slots**

## Project Presentation

You will present your idea during the lab sessions on the last regular
week of the semester (Dec 16th/22nd/23rd). We\'ll announce the concrete
schedule of presentations at a later point. [Instructions on what and
how to present your project can be found here.](labs06_material/presentation.md)

## Project Implementation and Report

**Deadline: TBD**

Your implementation and a report are due on this date, and both will be
delivered using Git. You will develop your project on top of your
implementation of Amy. Please push all development on a new branch
`lab06`, ideally building on top of the codegen lab (branch `lab05`). 
**TODO: define submission method**

Your repository should contain:

-   Your implementation, which must, to be graded at all, compile and be
    able to run non-trivial examples.
-   A subdirectory `extension-examples/` which includes some examples
    that demonstrate your compiler extension in action.
-   A subdirectory `report/` which includes a PDF summarizing your
    extension.

**If you did not manage to complete your planned features, or they are
partially implemented, make this clear in your report!**

You are encouraged to use the following (LaTeX) template for your
report:

-   [LaTeX sources](labs06_material/report-template.tar.gz)

A PDF version of the template with the required section is available
here:

-   [PDF Example](labs06_material/report-template.pdf)

Although you are not required to use the above template, your report
must contain at least the sections described in it with the appropriate
information. Note that writing this report will take some time, and you
should not do it in the last minute. The final report is an important
part of the compiler project. If you have questions about the template
or the contents of the report, make sure you ask them early.

A common question is \"how long should the report be?\". There\'s no
definitive answer to that. Considering that the report will contain code
examples and a technical description of your implementation, it would be
surprising if it were shorter than 3 pages. Please try to stay within 6
pages. A concise, but well-written report is preferable to a long, but
poorly-written one.
