(ns advent-of-code.day09
  (:require [advent-of-code.utils :as u]))

(defn- unfold* [f accum prior depth]
  (cond
    (empty? prior) accum
    (zero? depth) (concat accum prior)
    :else (recur f
                 (concat accum prior)
                 (f prior)
                 (dec depth))))

(defn unfold [f]
  (unfold* f [] (f) 10000000))

(defn- get-differences [nums]
  (unfold (fn
            ([] [nums])
            ([prev]
             (if (every? zero? (first prev))
               []
               [(map #(* -1 (apply - %))
                     (partition 2 1 (first prev)))])))))

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

(defn- extrapolate-backwards [histories]
  (reduce #(- %2 %1) (reverse (map first histories))))

(defn part-2
  "Day 09 Part 2"
  [input]
  (->> input
       (u/to-lines)
       (map u/parse-out-longs)
       (map get-differences)
       (map extrapolate-backwards)
       (reduce +)))
