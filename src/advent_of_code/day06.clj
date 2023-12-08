(ns advent-of-code.day06
  (:require [advent-of-code.utils :as u]
            [clojure.string :as str]))

(defn- parse-races [lines]
  (partition 2 (apply interleave (map u/parse-out-longs lines))))

(defn- calculate-distance [race-time charge-time]
  (* charge-time (- race-time charge-time)))

(defn- num-ways-to-beat-record [[race-time record]]
  (count (filter #(> % record)
                 (map (partial calculate-distance race-time)
                      (range 1 race-time)))))

(defn part-1
  "Day 06 Part 1"
  [input]
  (->> input
       (u/to-lines)
       (parse-races)
       (map num-ways-to-beat-record)
       (reduce *)))

(defn- improved-num-ways-to-beat-record [[race-time record]]
  (let [under-record (take-while #(< (calculate-distance race-time %) record)
                                 (range 0 (inc race-time)))]
    (- (inc race-time) (* 2 (count under-record)))))

(defn part-2
  "Day 06 Part 2"
  [input]
  (->> input
       (u/to-lines)
       (map #(str/replace % #"[^\d]" ""))
       (map parse-long)
       (improved-num-ways-to-beat-record)))
