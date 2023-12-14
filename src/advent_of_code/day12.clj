(ns advent-of-code.day12
  (:require [advent-of-code.utils :as u]
            [clojure.math.combinatorics :as combo]
            [clojure.string :as str]))

(defn- parse-springs [line]
  (let [springs (first (str/split line #" "))
        broken (u/parse-out-longs line)]
    [springs broken]))

(defn- is-valid-arrangement? [broken springs]
  (= (filter pos? (map count (str/split springs #"\.+"))) broken))

(defn- update-status [springs status]
  (str/replace-first springs #"\?" (str status)))

(defn- gen-arrangements [springs broken]
  (let [unknown-count (count (filter #(= % \?) springs))
        all-possible-placements (combo/selections [\. \#] unknown-count) ]
    (pmap #(reduce update-status springs %) all-possible-placements)))

(defn- gen-valid-arrangements [[springs broken]]
  (filter (partial is-valid-arrangement? broken)
          (gen-arrangements springs broken)))

(defn part-1
  "Day 12 Part 1"
  [input]
  (->> input
       (u/to-lines)
       (map parse-springs)
       (map gen-valid-arrangements)       
       (map count)
       (reduce +)))

(defn part-2
  "Day 12 Part 2"
  [input]
  "Implement this part")
