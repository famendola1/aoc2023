(ns advent-of-code.day03
  (:require [advent-of-code.utils :as u]))

(defn- create-grid [input]
  (let [lines (u/to-lines input)
        matrix (u/to-matrix input)]
    {:matrix matrix
     :nums (mapv #(u/re-pos #"\d+" %) lines)}))

(defn- is-sym? [char]
  (let [not-sym #{\. \1 \2 \3 \4 \5 \6 \7 \8 \9 \0}]
    (not (not-sym char))))

(defn- in-bounds? [x y max-x max-y]
  (and (>= x 0)
       (>= y 0)
       (< x max-x)
       (< y max-y)))

(defn- is-part-number? [matrix row col num]
  (let [len (count num)
        max-x (count (first matrix))
        max-y (count matrix)]
    (some identity (for [y (range (dec row) (+ row 2))
                         x (range (dec col) (+ col len 1))]
                     (and (in-bounds? x y max-x max-y)
                          (is-sym? (get-in matrix [y x])))))))

(defn- find-part-numbers [{:keys [matrix nums]}]
  (map parse-long
       (for [row (range (count nums))
             [col num] (nums row)
             :when (is-part-number? matrix row col num)]
         num)))

(defn part-1
  "Day 03 Part 1"
  [input]
  (->> input
       create-grid
       find-part-numbers
       (reduce +)))

(defn- get-possible-gears [matrix row col num]
  (let [len (count num)
        max-x (count (first matrix))
        max-y (count matrix)
        n (parse-long num)]
    (for [y (range (dec row) (+ row 2))
          x (range (dec col) (+ col len 1))
          :when (and (in-bounds? x y max-x max-y)
                     (= (get-in matrix [y x]) \*))]
      (list n [y x]))))

(defn- find-possible-gears [{:keys [matrix nums]}]
  (group-by
   last
   (map first (filter seq (for [row (range (count nums))
                                [col num] (nums row)]
                            (get-possible-gears matrix row col num))))))

(defn- get-gear-ratios [gears]
  (map #(* (ffirst %) (first (second %)))
       (filter #(= 2 (count %)) (vals gears))))

(defn part-2
  "Day 03 Part 2"
  [input]
  (->> input
       create-grid
       find-possible-gears
       get-gear-ratios
       (reduce +)))
