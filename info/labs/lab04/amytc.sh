#!/bin/bash
# The above line tells the operating system to use the 
# bash shell interpreter to execute the commands in this file.
# the hash sign, '#' means that characters following it are comments
if [ $# -eq 0 ]; then # script is called with 0 parameters
    # output short usage instructions on the command line
    echo "Usage: amytc.sh Prog1.amy Prog2.amy ... ProgN.amy"
    echo "Example invocation:"
    echo "./amytc.sh examples/Arithmetic.amy"

    echo "Example output:"
    echo "  Type checking: examples/Arithmetic.amy"
    echo "  Reusing existing jar: target/scala-3.5.2/amyc-assembly-1.7.jar"
    echo "  Type checking successful!"
    # Now, terminate the script with an error exit code 1:
    exit 1
fi
# print progress message. $* denotes all arguments
echo Type checking: $*
# jar file is a zip file containing .class files of our interpreter/compiler
# sbt generates the file in this particular place.
AMYJAR=target/scala-3.5.2/amyc-assembly-1.7.jar
# You can copy this file into test.zip and run unzip test.zip to see inside
if test -r "${AMYJAR}"; then
    # jar file exists, so we just reuse it
    echo "Reusing existing jar: ${AMYJAR}"
    # Note that we do not check if scala sources changed!
    # Hence, our jar file can be old
else
    # If there is no jar file, we invoke `sbt assembly` to create it
    sbt assembly
fi
# We should have the jar file now, so we invoke it
# java starts the Java Virtual Machine. 
# Here, it will unpack the jar file and find META-INF/MANIFEST.MF file
# which specifies the main class of the jar file (entry point).
# java will execute `public static void main` method of that class.
java -jar ${AMYJAR} --type-check library/Std.amy library/Option.amy library/List.amy $*
# Here, we ask amy to only type check the give files. 
# We always provide standard library files as well as 
# the explicitly files explicit given to the script (denoted $*)
