(ns advent-of-code.day04
  (:require [advent-of-code.utils :as u]
            [clojure.string :as str]))

(defn- parse-card [card-line]
  (let [nums (map parse-long (re-seq #"\d+" card-line))]
    {:id (first nums)
     :winners (set (take 10 (rest nums)))
     :nums (drop 10 (rest nums))}))

(defn- score-card [{:keys [winners nums]}]
  (let [matches (filter winners nums)]
    (if (seq matches)
      (reduce * (repeat (dec (count matches)) 2))
      0)))

(defn part-1
  "Day 04 Part 1"
  [input]
  (->> input
       (u/to-lines)
       (map parse-card)
       (map score-card)
       (reduce +)))

(defn- count-matches [{:keys [winners nums]}]
  (count (filter winners nums)))

(def ^:private next-cards
  (memoize (fn [x] (take (count-matches x) (iterate inc (inc (:id x)))))))

(defn- count-cards [cards]
  (let [num-cards (into
                   {}
                   (mapv vec (partition
                              2
                              (interleave (range 1 (inc (count cards)))
                                          (repeat (count cards) 1)))))]
    (reduce (fn [counts card]
              (let [new-cards (next-cards card)]
                (reduce (fn [out id]
                          (update out id (partial + (out (:id card)))))
                        counts
                        new-cards)))
            num-cards
            cards)))

(defn part-2
  "Day 04 Part 2"
  [input]
  (->> input
       (u/to-lines)
       (mapv parse-card)
       (count-cards)
       (vals)
       (reduce +)))
