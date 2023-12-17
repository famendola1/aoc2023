(ns advent-of-code.day11
  (:require [advent-of-code.utils :as u]))

(defn- parse-galaxies [y line]
  (let [enumerated (map vector (range) line)
        galaxies (filter #(= (second %) \#) enumerated)]
    (map #(vec [y (first %)]) galaxies)))

(defn- parse-universe [input]
  (let [lines (u/to-lines input)
        galaxies (apply concat (map-indexed parse-galaxies lines))]
    {:galaxies galaxies
     :empty-row (apply disj (set (range 0 (count lines))) (map first galaxies))
     :empty-col (apply disj (set (range 0 (count (first lines)))) (map second galaxies))}))

(defn- get-distance [expansion-factor [y-a x-a] [y-b x-b] empty-row empty-col]
  (let [y-diff (abs (- y-a y-b))
        x-diff (abs (- x-a x-b))
        min-y (min y-a y-b)
        min-x (min x-a x-b)
        expanded-rows (if (pos? y-diff)
                        (count (filter empty-row
                                       (range min-y (+ min-y y-diff))))
                        0)
        expanded-cols (if (pos? x-diff)
                        (count (filter empty-col
                                       (range min-x (+ min-x x-diff))))
                        0)]
    (+ x-diff
       y-diff
       (* expanded-rows (dec expansion-factor))
       (* expanded-cols (dec expansion-factor)))))

(defn- get-pairwise-distances [expansion-factor {:keys [galaxies empty-row empty-col]}]
  (for [galaxy-a galaxies
        galaxy-b galaxies
        :when (not= galaxy-a galaxy-b)]
    (get-distance expansion-factor galaxy-a galaxy-b empty-row empty-col)))

(defn part-1
  "Day 11 Part 1"
  [input]
  (->> input
       (parse-universe)
       (get-pairwise-distances 2)
       (reduce +)
       (#(/ % 2))))

(defn part-2
  "Day 11 Part 2"
  [input]
  (->> input
       (parse-universe)
       (get-pairwise-distances 1000000)
       (reduce +)
       (#(/ % 2))))
