(ns advent-of-code.day14
  (:require [advent-of-code.utils :as u]))

(defn- transpose [lines]
  (reduce (fn [matrix idx]
            (conj matrix (map #(nth % idx) lines)))
          []
          (range 0 (count (first lines)))))

(defn- tilt-one-north [col]
  (loop [[[idx curr] & rem] (map-indexed #(vector %1 %2) col)
         stop -1
         res []]
    (if (nil? curr)
      res
      (condp = curr
        \O (recur rem (inc stop) (conj res (inc stop)))
        \# (recur rem idx res)
        \. (recur rem stop res)))))

(defn- tilt-north' [matrix]
  (reduce + (map #(- (count matrix) %) (mapcat tilt-one-north matrix))))

(defn part-1
  "Day 14 Part 1"
  [input]
  (->> input
       (u/to-lines)
       (u/transpose)
       (tilt-north')))

(defn- shift-row-left [row]
  (loop [[[idx curr] & rem] (map-indexed #(vector %1 %2) row)
         stop -1
         res []]
    (if (nil? curr)      
      (concat res (repeat (- (count row) stop 1) \.))      
      (condp = curr
        \O (recur rem (inc stop) (conj res \O))
        \# (recur rem idx (conj (vec (concat res (repeat (- idx stop 1) \.))) \# ))
        \. (recur rem stop res)))))

(defn- shift-row-right [row]
  (reverse (shift-row-left (reverse row))))

(defn- shift-left [matrix]
  (mapv shift-row-left matrix))

(defn- shift-right [matrix]
  (mapv shift-row-right matrix))

(defn- tilt-north [matrix]
  (u/transpose (shift-left (u/transpose matrix))))

(defn- tilt-south [matrix]
  (u/transpose (shift-right (u/transpose matrix))))

(defn- tilt-west [matrix]
  (shift-left matrix))

(defn- tilt-east [matrix]
  (shift-right matrix))

(defn- spin-once [matrix]
  (->> matrix
       (tilt-north)
       (tilt-west)
       (tilt-south)
       (tilt-east)))

(defn- find-matrix [seen loc idx target]
  (let [cycle (- idx loc)
        inverse-seen (reduce (fn [out [m idx]] (assoc out idx m)) {} seen)
        rem (mod (- target idx) cycle)]
    (inverse-seen (+ rem loc))))

(defn- spin [times matrix]
  (loop [[m & ms] (iterate spin-once matrix)
         idx 0
         seen {}]
    (if (seen m)
      (find-matrix seen (seen m) idx times)
      (recur ms (inc idx) (assoc seen m idx)))))

(defn- summarize [matrix]
  (reduce + (map-indexed (fn [idx row] (* (- (count matrix) idx)
                                          (count (filter #(= % \O) row))))
                         matrix)))

(defn part-2
  "Day 14 Part 2"
  [input]
  (->> input
       (u/to-lines)
       (spin 1000000000)
       (summarize)))
