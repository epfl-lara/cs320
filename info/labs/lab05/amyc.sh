#!/bin/bash
# Script similar in structure to amytc.sh, see explanations there.
if [ $# -eq 0 ]; then
    echo "Usage: amyc Prog1.amy Prog2.amy ... ProgN.amy"
    exit 1
fi
echo Compiling: $*
AMYJAR=target/scala-3.5.2/amyc-assembly-1.7.jar
if test -r "${AMYJAR}"; then
    echo "Reusing existing jar: ${AMYJAR}"
else
    sbt assembly
fi
java -jar ${AMYJAR} library/Std.amy library/Option.amy library/List.amy $*
echo Run wasmtime on one of the wasm files:
ls wasmout/*.wasm
