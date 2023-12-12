(ns advent-of-code.day10
  (:require [advent-of-code.utils :as u]))

(defn- in-bounds? [[y x] [max-y max-x]]
  (and (>= y 0)
       (>= x 0)
       (< y max-y)
       (< x max-x)))

(defn- connects-to [tile y x max-y max-x]
  (condp = tile
    \. '()
    \S (list \x)
    \| (filter #(in-bounds? % [max-y max-x]) [[(dec y) x] [(inc y) x]])
    \- (filter #(in-bounds? % [max-y max-x]) [[y (dec x)] [y (inc x)]])
    \F (filter #(in-bounds? % [max-y max-x]) [[(inc y) x] [y (inc x)]])
    \7 (filter #(in-bounds? % [max-y max-x]) [[(inc y) x] [y (dec x)]])
    \L (filter #(in-bounds? % [max-y max-x]) [[(dec y) x] [y (inc x)]])
    \J (filter #(in-bounds? % [max-y max-x]) [[(dec y) x] [y (dec x)]])))

(defn- parse-field [input]
  (let [matrix (u/to-matrix input)
        max-x (count (first matrix))
        max-y (count matrix)]
    {:matrix matrix
     :field (into {} (for [y (range max-y)
                           x (range max-x)]
                       [[y x] (set (connects-to (get-in matrix [y x])
                                                y
                                                x
                                                max-y
                                                max-x))]))}))

(defn- step [field [curr prev]]
  [(first (disj (field curr) prev)) curr])

(defn- find-loop [field]
  (let [start (first (filter #((field %) \x) (keys field)))
        one-step (first (filter #((field %) start) (keys field)))]    
    (into [start one-step]
          (map first
               (take-while
                #(not= (first %) start)
                (rest (iterate (partial step field) [one-step start])))))))

(defn part-1
  "Day 10 Part 1"
  [input]
  (-> input
      (parse-field)
      (:field)
      (find-loop)
      (count)
      (/ 2)))

(defn- count-interior-line [{:keys [field matrix]}]
  (let [path (set (find-loop field))]
    (first (reduce (fn [[res n-seen] node]
                     (cond
                       (and (path node) (#{\S \| \J \L} (get-in matrix node)))
                       [res (inc n-seen)]
                       (and (odd? n-seen) (not (path node)))
                       [(inc res) n-seen]
                       :else
                       [res n-seen]))
                   [0 0]            
                   (for [y (range (count matrix))
                         x (range (count (first matrix)))]
                     [y x])))))

(defn part-2
  "Day 10 Part 2"
  [input]  
  (->> input
       (parse-field)
       (count-interior-line)))
