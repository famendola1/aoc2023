# advent-2023-clojure

This is my code for the 2023 [Advent of Code](https://adventofcode.com/20XX),
all solutions in [Clojure](https://clojure.org/).

All code is under the `src` directory. Each solution-file is named `dayNN.clj`
and contains both puzzle solutions for that day. These are the
publically-facing functions `part-1` and `part-2`. These files are the code
*exactly as I used it to solve and submit the answers*. If I revisit any of the
days and try to clean up or optimize the solutions, that work will be in a
separate file that will be named `dayNNbis.clj`
(["bis"](https://www.merriam-webster.com/dictionary/bis)). I may go back and
comment code after the fact, when I'm not racing the clock.

The `resources` directory contains the input data for each day. These files are
named for the day (`dayNN.txt`).

## Stats

Number of answers correct on first submission: 29/38 (76%)

Percentage would be higher if I realized I could use the sample to test my
solution before trying it on my input sooner :/

Highest finish for first half: -

Highest finish for second half: -

## Usage

This project is managed with [Leiningen](https://leiningen.org/). Running the
following will download any dependencies and start a REPL:

```
lein repl
```
