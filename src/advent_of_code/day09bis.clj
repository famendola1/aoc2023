(ns advent-of-code.day09bis
  (:require [advent-of-code.utils :as u]))

(defn- unfold* [f accum prior]
  (if (empty? prior)
    accum
    (recur f (concat accum prior) (f prior))))

(defn unfold [f]
  (unfold* f [] (f)))

(defn- get-differences [nums]
  (unfold (fn
            ([] [nums])
            ([prev]
             (if (every? zero? (first prev))
               []
               [(map - (next (first prev)) (first prev))])))))

(defn- extrapolate [histories]
  (reduce + (map last histories)))

(defn part-1
  "Day 09 Part 1"
  [input]
  (->> input
       (u/to-lines)
       (map u/parse-out-longs)
       (map get-differences)
       (map extrapolate)
       (reduce +)))

(defn part-2
  "Day 09 Part 2"
  [input]
  (->> input
       (u/to-lines)
       (map u/parse-out-longs)
       (map reverse)
       (map get-differences)
       (map extrapolate)
       (reduce +)))
