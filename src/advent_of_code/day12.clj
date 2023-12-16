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

(defn- unfold-springs [[springs broken]]
  [(str (str/join "?" (repeat 5 springs)) ".")
   (flatten (repeat 5 broken))])

(def count-valid-arrangements
  (memoize
   (fn [[springs broken] curr-num-broken]
     (if (empty? springs)
       (if (and (empty? broken) (zero? curr-num-broken)) 1 0)
       (let [curr-spring (first springs)
             use-it (if (or (= curr-spring \#) (= curr-spring \?))
                      (count-valid-arrangements
                       [(rest springs) broken]
                       (inc curr-num-broken))
                      0)
             lose-it (if (and (or (= curr-spring \.) (= curr-spring \?))
                              (or (= (first broken) curr-num-broken)
                                  (zero? curr-num-broken)))
                       (count-valid-arrangements
                        [(rest springs)
                         (if (zero? curr-num-broken)
                           broken
                           (rest broken))]
                        0)
                       0)]
         (+ use-it lose-it))))))

#_(defn part-1
    "Day 12 Part 1"
    [input]
    (->> input
         (u/to-lines)
         (map parse-springs)
         (map #(vec [(str (first %) ".") (second %)]))
         (pmap #(count-valid-arrangements % 0))
         (reduce +)))

(defn part-2
  "Day 12 Part 2"
  [input]
  (->> input
       (u/to-lines)
       (map parse-springs)
       (map unfold-springs)
       (pmap #(count-valid-arrangements % 0))       
       (reduce +)))
