(ns advent-of-code.day21
  (:require [advent-of-code.utils :as u]
            [clojure.set :as set]))

(defn- parse-line [y line]
  (reduce (fn [out [idx ch]]
            (condp = ch
              \. (assoc out :plots (conj (:plots out) [y idx]))
              \S (assoc out :start [y idx] :plots (conj (:plots out) [y idx]))
              \# out))
          {:plots #{}}
          (map-indexed vector line)))

(defn- parse-garden [input]
  (let [lines (u/to-lines input)
        max-y (count lines)
        max-x (count (first lines))]
    (assoc (apply merge-with set/union (map-indexed parse-line lines))
           :max-y max-y
           :max-x max-x)))

(defn- neighbors [[y x]]
  [[y (inc x)] [y (dec x)] [(dec y) x] [(inc y) x]])

(defn- walk-garden [steps {:keys [start plots max-x max-y]}]
  (u/unfold-indexed
   (fn
     ([] [[start]])
     ([prev idx]
      (if (= idx steps)
        []
        [(distinct (filter #(and (plots %) (u/in-bounds? % max-y max-x))
                           (mapcat neighbors (first prev))))])))))

(defn part-1
  "Day 21 Part 1"
  [input]
  (->> input
       (parse-garden)
       (walk-garden 64)
       (last)
       (count)))

(defn part-2
  "Day 21 Part 2"
  [input]
  "Implement this part")
