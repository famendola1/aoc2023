(ns advent-of-code.day13
  (:require [advent-of-code.utils :as u]))

(defn- parse-cols [rows]
  (reduce (fn [cols idx]
            (conj cols (map #(nth % idx) rows)))
          []
          (range 0 (count (first rows)))))

(defn- parse-patterns [block]
  (let [rows (u/to-lines block)]
    {:rows rows
     :cols (parse-cols rows)}))

(defn- diffs [a b]
  (count (filter false? (map = a b))))

(defn- is-mirror? [coll idx max-smudges]  
  (let [before (reverse (take (inc idx) coll))
        after (drop (inc idx) coll)]
    (= max-smudges (reduce + (map diffs before after)))))

(defn- find-mirror [max-smudges coll]
  (reduce (fn [_ idx]
            (when (is-mirror? coll idx max-smudges)
              (reduced idx)))
          nil
          (range 0 (dec (count coll)))))


(defn- summarize [max-smudges {:keys [rows cols]}]
  (let [cols-mirror (find-mirror max-smudges cols)
        rows-mirror (find-mirror max-smudges rows)]
    (cond (not (nil? cols-mirror)) (inc cols-mirror)
          (not (nil? rows-mirror)) (* 100 (inc rows-mirror))
          :else 0)))

(defn part-1
  "Day 13 Part 1"
  [input]
  (->> input
       (u/to-blocks)
       (map parse-patterns)
       (map (partial summarize 0))
       (reduce +)))

(defn part-2
  "Day 13 Part 2"
  [input]
  (->> input
       (u/to-blocks)
       (map parse-patterns)
       (map (partial summarize 1))
       (reduce +)))
