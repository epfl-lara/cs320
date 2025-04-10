## Demo

```
(func $Factorial_f (param i32 i32) (result i32) (local i32)
  ;;> fn f(i: Int(32), j: Int(32)): Int(32) = {
  ;;|   val res: Int(32) =
  ;;|     (i + j);
  ;;|   res
  ;;| }
  ;;> i
  local.get 0
  ;;> j
  local.get 1
  ;;> (i + j)
  i32.add
  ;;> val res: Int(32)
  local.set 2
  ;;> res
  local.get 2
)

(func $Factorial_fact (param i32) (result i32)
  ;;> fn fact(i: Int(32)): Int(32) = {
  ;;|   (if((i < 2)) {
  ;;|     1
  ;;|   } else {
  ;;|     (i * fact((i - 1)))
  ;;|   })
  ;;| }
  ;;> i
  local.get 0
  ;;> 2
  i32.const 2
  ;;> (i < 2)
  i32.lt_s
  ;;> (if((i < 2)) {
  ;;|   1
  ;;| } else {
  ;;|   (i * fact((i - 1)))
  ;;| })
  if (result i32)
    ;;> 1
    i32.const 1
  else
    ;;> i
    local.get 0
    ;;> fact((i - 1))
    ;;> i
    local.get 0
    ;;> 1
    i32.const 1
    ;;> (i - 1)
    i32.sub
    call $Factorial_fact
    ;;> (i * fact((i - 1)))
    i32.mul
  end
)
```

## WASM basics

### Stack machine

- WASM is a stack based machine.
- WASM has types. We will use exclusively i32.
- Instructions can push or pop values from the stack.
    - i32.const x : push x to the stack.
    - i32.add     : pop 2 values, add them and push the result.
    - drop        : pop a value and ignore it.
- Locals can store values inside a function. Useful for val definitions among others.
    - local.get x : get xth local
    - local.set x : set xth local
- Globals store program wide values.
    - global.get x : get xth global
    - global.set x : set xth global
- Control flow.
    - if   : pop value from stack, if 0 goto else, otherwise continue.
    - call : pop arguments from the stack, jump to function.


## Function calls

How to call a function:
- Push the required number of arguments on the stack.
- Call the function. The call instruction will pop the arguments and place them in the locals.
- The result will be placed on top of the stack.

```
(func $f (param i32 i32) (result i32)
    local.get 0
    local.get 1
    i32.add
)

(
    i32.const 3 ;; arg 0
    i32.const 4 ;; arg 1
    ;; A
    call $f
    ;; B
)

A:
|       |
|   4   | <-- arg 1
|   3   | <-- arg 0
|-------|

B:
|       |
|       |
|   7   | <-- result
|-------|

```


## Store

```
Store 3 at address 48

|        |
|        |
|        |
|--------| <-- bottom of the stack


`i32.const 48`

|        |
|        |
|   48   | <-- address
|--------| <-- bottom of the stack


`i32.const 3`

|        |
|    3   | <-- value
|   48   | <-- address
|--------| <-- bottom of the stack


`i32.store` pops 2 values from the stack

|        |
|        |
|        |
|--------| <-- bottom of the stack

Heap

| address |  0 |  1 |  2 | .. | 47 | 48 | 49 | .. |
|---------|----|----|----|----|----|----|----|----|
|  value  |  0 |  0 |  0 | .. |  0 |  3 |  0 | .. |
                                      ^
                                value written
```


## Values

Very similar to java.

- Ints  are represented simply with an i32.
- Bools are represented with an i32, false = 0, true = 1.
- Unit  is  represented with an i32 with value 0. 


### Strings

```
| address | 24 | 25 | 26 | 27 | 28 | 29 | 30 | 31 |
|---------|----|----|----|----|----|----|----|----|
|  value  | 104| 101| 108| 108| 111| 33 |  0 |  0 |
|  ascii  |  h |  e |  l |  l |  o |  ! | \0 |    |

|        |
|        |
|   24   | <-- pointer to string
|--------| <-- bottom of the stack

```


### ADTs

- store the value on the heap to reduced the size to the size of a pointer.
- store which constructor the value holds.

```scala
def getList(): List = { ... }

val ls: List = getList();
// What is the size of list here?
// Is it a Nil or a Cons?
```

```
Cons(42, Nil())

| address |  value  |
|---------|---------|
|    0    |    1    | \
|    1    |         | | constructor id.
|    2    |         | | Cons
|    3    |         | /
|    4    |   42    | \
|    5    |         | | first member: int
|    6    |         | |  42
|    7    |         | /
|    8    |  1234   | \
|    9    |         | | seconder member: pointer to Nil
|   10    |         | | 1234
|   11    |         | /

Field offset = 4 + 4 * field number
==> Utils.scala:adtField

```


## Allocation

Utils.scala:memoryBoundary is the index of a global variable that holds a pointer to the next free bytes.

### Example in pseudocode:

Start of the program:

    global.set(memoryBoundary, 0)

We want to allocate "hello!" = 7 bytes (don't forget the null terminator).

Store current memory pointer as pointer to our new string:
    
    hello_string = global.get(memoryBoundary)

Increment the memory boundary by 7 (size of string).

    global.set(memoryBoundary, global.get(memoryBoundary) + 7)

### With webassembly instructions:

```
;; With memoryBoundary = 0.
;; Load the current boundary for string
global.get 0
;; Load it again for the arithmetic
global.get 0
;; length of string
i32.const 7
;; base + length = new boundary
i32.add
;; store new boundary
global.set 0
;; now the string pointer is on the stack, we just
;; need to copy the character's bytes into it.
...
```


## Pattern matching

A pattern matching expression:

    e match {
        case p1 => e1
        ...
        case pn => en
    }

can be considered to be equivalent to the following pseudocode:

    val v = e;
    if      (matchAndBind(v, p1)) e1
    else if (matchAndBind(v, p2)) e2
    else if ...
    else if (matchAndBind(v, pn)) en
    else error("Match error!")

matchAndBind is equivalent to this:

    WildcardPattern:
    "case _ => ..."
    matchAndBind(v, _) = true

    IdPattern:
    "case id => ..."
    matchAndBind(v, id) = { id = v; true }

    LiteralPattern:
    "case 3 => ..."
    matchAndBind(v, lit) = { v == lit }

    CaseClassPattern:
    "case Cons(x, _) => ..."
    matchAndBind(C_1(v_1, ..., v_n), C_2(p_1, ..., p_m)) = {
        C_1 == C_2 &&
        matchAndBind(v_1, p_1) &&
        ...
        matchAndBind(v_m, p_m)
    }
