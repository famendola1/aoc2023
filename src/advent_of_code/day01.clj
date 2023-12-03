(ns advent-of-code.day01
  (:require [advent-of-code.utils :as u]))

(defn- parse-digits [line]  
  (map #(Integer/parseInt %) (re-seq #"\d" line)))

(defn- mk-num [digits]
  (+ (* 10 (first digits)) (last digits)))

(defn part-1
  "Day 01 Part 1"
  [input]
  (->> input
       u/to-lines
       (map parse-digits)
       (map mk-num)
       (reduce +)))

(def ^:private text-to-digit
  {"zero" 0
   "0" 0
   "one" 1
   "1" 1
   "two" 2
   "2" 2
   "three" 3
   "3" 3
   "four" 4
   "4" 4
   "five" 5
   "5" 5
   "six" 6
   "6" 6
   "seven" 7
   "7" 7
   "eight" 8
   "8" 8
   "nine" 9
   "9" 9})

(defn parse-all-digits [line]
  (map (comp text-to-digit last)
       (re-seq #"(?=([0-9]|one|two|three|four|five|six|seven|eight|nine|ten))"
               line)))

(defn part-2
  "Day 01 Part 2"
  [input]
  (->> input
       u/to-lines
       (map parse-all-digits)
       (map mk-num)
       (reduce +)))
