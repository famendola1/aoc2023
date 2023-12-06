# Breakdown of Files

Jump to day: [1](#day01clj)&nbsp;|&nbsp;[2](#day02clj)&nbsp;|&nbsp;[3](#day03clj)&nbsp;|&nbsp;[4](#day04clj)&nbsp;|&nbsp;[5](#day05clj)&nbsp;|&nbsp;[6](#day06clj)&nbsp;|&nbsp;[7](#day07clj)&nbsp;|&nbsp;[8](#day08clj)&nbsp;|&nbsp;[9](#day09clj)&nbsp;|&nbsp;[10](#day10clj)&nbsp;|&nbsp;[11](#day11clj)&nbsp;|&nbsp;[12](#day12clj)&nbsp;|&nbsp;[13](#day13clj)&nbsp;|&nbsp;[14](#day14clj)&nbsp;|&nbsp;[15](#day15clj)&nbsp;|&nbsp;[16](#day16clj)&nbsp;|&nbsp;[17](#day17clj)&nbsp;|&nbsp;[18](#day18clj)&nbsp;|&nbsp;[19](#day19clj)&nbsp;|&nbsp;[20](#day20clj)&nbsp;|&nbsp;[21](#day21clj)&nbsp;|&nbsp;[22](#day22clj)&nbsp;|&nbsp;[23](#day23clj)&nbsp;|&nbsp;[24](#day24clj)&nbsp;|&nbsp;[25](#day25clj)

Here is a breakdown of the various files in this directory. Files with names of
the form `dayNN.clj` represent the code actually used to solve the problems
(with some tweaking done using a static analysis plug-in for Leiningen). Files
with `bis` in the name are modified/tuned versions of the given original day.
(If you see comments in a file, I can usually promise you they were added after
the fact.)

The numbers in parentheses in the descriptions of the files represent the rank
I had for when my solutions were submitted and accepted. Time, if given, is a
rough estimate of how long it took to solve both halves.

A given day and part can be run via:

```
lein run DAY PART
```

where `DAY` is a number from 1-25 and `PART` is 1 or 2. If there is a "bis"
version of a day, that can be run via:

```
lein run -b DAY PART
```

## [day01.clj](day01.clj)

Day 1 (56042/55358).

### Part 1

For each line, find all numerals using the `\d` regex. The first and last
numerals found make the digits of the number. Lastly, find the sum.

### Part 2

For each line, find all numerals using a regex that includes the spelling of
the numbers one to nine. Proceed like Part 1.

## [day02.clj](day02.clj)

Day 2 (2439/63711).

### Part 1

For each game, we parse it for all the dice draws of that game. The game is
stored as an assoc containing the id of the game and a list of the draws where
the draws are stored as an assoc mapping the color to number of dice pulled in
that color. A draw is "possible" if the number of dice pulled is less than or
equal its corresponding max. A game is "possible" if every draw is possible.

### Part 2

For each game, we parse it like Part 1. To calculate the minimum set of a game,
we need the max die drawn for each color in that game. The we multiply those
values to get the power.

## [day03.clj](day03.clj)

Day 3 (560670/91622824).

### Part 1

Parse the input into a matrix. Additionally, for each row we parse the locations
of the numbers using the `re-pos` function in utils. Then we iterate over the
numbers and check if any of its digits borders a symbol, by checking the 8 cells
surrounding the digit. If a digit borders a symbol then the number is part
number.

### Part 2

Parse the input like Part 1. We then iterate over the numbers and search for
digits that border a "*". If it does, we save the number along with the
coordinates of the "*". We then group the results by the coordinates of the "*".
If there are exacly 2 numbers in the grouping then we have a gear and multiply
the numbers.

## [day04.clj](day04.clj)

Day 4 (23750/13261850).

### Part 1

First, we parse each line for all the numbers in that line. By inspecting the
data (at least my data), we see that there are always 10 winning numbers. So the
first 10 matched numbers, after the card id (I forgot this at first :/) are the
winners and the rest are our numbers. We store the winners as a set so that we
can do quick lookups to filter our numbers with ones that match in the set. We
we have the matches we count them and decrement by 1. We raise 2 to this number
to get the score of the card (effectively, `2^(m-1)`, where `m` is the number of
matches. Finally, we sum all the scores.

### Part 2

Parse each line like Part 1. Then, we count the expansion of the cards as
described. We start with a map if the card id to a count initialized to one. For
each card, we count the matches and update the counts of the subsequent cards
with count of the current card. Initially, I forgot to actually use the count of the
current card and instead just incremented the counts of the subsequent cards,
which I had as placeholder. Finally, we sum all these counts.

## [day05.clj](day05.clj)

Day 5 (--/--).

## [day06.clj](day06.clj)

Day 6 (--/--).

## [day07.clj](day07.clj)

Day 7 (--/--).

## [day08.clj](day08.clj)

Day 8 (--/--).

## [day09.clj](day09.clj)

Day 9 (--/--).

## [day10.clj](day10.clj)

Day 10 (--/--).

## [day11.clj](day11.clj)

Day 11 (--/--).

## [day12.clj](day12.clj)

Day 12 (--/--).

## [day13.clj](day13.clj)

Day 13 (--/--).

## [day14.clj](day14.clj)

Day 14 (--/--).

## [day15.clj](day15.clj)

Day 15 (--/--).

## [day16.clj](day16.clj)

Day 16 (--/--).

## [day17.clj](day17.clj)

Day 17 (--/--).

## [day18.clj](day18.clj)

Day 18 (--/--).

## [day19.clj](day19.clj)

Day 19 (--/--).

## [day20.clj](day20.clj)

Day 20 (--/--).

## [day21.clj](day21.clj)

Day 21 (--/--).

## [day22.clj](day22.clj)

Day 22 (--/--).

## [day23.clj](day23.clj)

Day 23 (--/--).

## [day24.clj](day24.clj)

Day 24 (--/--).

## [day25.clj](day25.clj)

Day 25 (--/--).
