# Labs Setup

This page contains instructions on how to set up your computer to work on assignments.


## Step 0: Find a group

All work in this year's edition of CS-320 will be done in groups of two students.
If you are looking for teammates, please consider using the course's [Moodle](https://moodle.epfl.ch/course/view.php?id=4241) forums.
One of you should then submit the SCIPER numbers and names of all teammates to rodrigo.raya@epfl.ch.
By the end of the first week you *must* have registered in a group, so you can start working on the lab 1.


## Step 1: Version control 

Since you will be working with others throughout the course, we provide all labs via Git repositories.
Once you have registered as a group, you will be assigned a repository on [EPFL's GitLab](https://gitlab.epfl.ch/).
For every new lab we are going to push the starter code ("skeleton") on a separate branch of that repository.
Since all labs starting from the second are cumulative, you should ideally merge your work resulting from the preceding, completed lab into the starter code of the new lab.
Once the deadline for a lab comes around, we will simply consider your most recent commit on the respective branch as your submission.


## Step 2: Java / Scala / SBT 

We will be using Scala 3 to implement our compiler project.
Please make sure your system is running at least Java 8 and has [SBT](http://www.scala-sbt.org/) (the default build tool for Scala) installed.


## Step 3: Working on the labs 

Once you have formed a group and installed SBT, you are ready to start the labs. The workflow for each assignment is roughly as follows:
  * Check out the branch of the current lab from your repository. Merge with your work from prior labs (when starting work on lab 3, 4 or 5).
  * Run ''compile'' from inside SBT to make sure your build still succeeds. If you run ''test'' at this point it should fail on some of the new tests provided by us.
  * Implement the assignment according to the specification. Throughout the semester we will be providing you with details on each specific assignment.
  * Once you are happy with your solution, make sure that the most recent commit of the current lab's branch is the one you want to submit. We will automatically fetch it at the lab's deadline and use it to evaluate your solution.
