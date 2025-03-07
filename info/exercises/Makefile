.PHONY = all clean

OUT_DIR ?= $(shell dirname $(realpath $(firstword $(MAKEFILE_LIST))))

DIRS := $(wildcard src/ex-??)

EXPDFS := $(patsubst src/ex-%,ex-%.pdf,$(DIRS))
SOLPDFS := $(patsubst src/ex-%,ex-%-sol.pdf,$(DIRS))

TEXARGS := -shell-escape -interaction=batchmode

all: $(EXPDFS) $(SOLPDFS)

ex-%.pdf: src/ex-%/main.tex
	cd src/ex-$* && \
	lualatex $(TEXARGS) -jobname=ex-$* "\def\ANSWERS{0}\input{main.tex}" && \
	cp ex-$*.pdf $(OUT_DIR)/ex-$*.pdf

ex-%-sol.pdf: src/ex-%/main.tex
	cd src/ex-$* && \
	lualatex $(TEXARGS) -jobname=ex-$*-sol "\def\ANSWERS{1}\input{main.tex}" && \
	cp ex-$*-sol.pdf $(OUT_DIR)/ex-$*-sol.pdf 

clean:
	rm -f $(EXPDFS) $(SOLPDFS)
	for d in $(DIRS); do \
		pushd $$d && rm -f *.aux *.log *.out main.pdf; popd; \
	done
