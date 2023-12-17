(ns advent-of-code.day16
  (:require [advent-of-code.utils :as u]))

(defn- in-bounds? [[y x] max-y max-x]
  (and (>= y 0)
       (>= x 0)
       (< y max-y)
       (< x max-x)))

(defn- continue-beam [{[y x] :pos dir :dir}]
  (condp = dir
    :right {:pos [y (inc x)] :dir dir}
    :left {:pos [y (dec x)] :dir dir}
    :up {:pos [(dec y) x] :dir dir}
    :down {:pos [(inc y) x] :dir dir}))

(defn- reflect-forward [{[y x] :pos dir :dir}]
  (condp = dir
    :right {:pos [(dec y) x] :dir :up}
    :left {:pos [(inc y) x] :dir :down}
    :up {:pos [y (inc x)] :dir :right}
    :down {:pos [y (dec x)] :dir :left}))

(defn- reflect-back [{[y x] :pos dir :dir}]
  (condp = dir
    :right {:pos [(inc y) x] :dir :down}
    :left {:pos [(dec y) x] :dir :up}
    :up {:pos [y (dec x)] :dir :left}
    :down {:pos [y (inc x)] :dir :right}))

(defn- split-pipe [beam]
  (condp = (:dir beam)
    :right [(reflect-forward beam) (reflect-back beam)]
    :left [(reflect-forward beam) (reflect-back beam)]
    :up [(continue-beam beam)]
    :down [(continue-beam beam)]))

(defn- split-dash [beam]  
  (condp = (:dir beam)
    :right [(continue-beam beam)]
    :left [(continue-beam beam)]
    :up [(reflect-forward beam) (reflect-back beam)]
    :down [(reflect-forward beam) (reflect-back beam)]))

(defn- add-beams [beams max-y max-x new-beams]
  (concat beams (filter #(in-bounds? (:pos %) max-y max-x)
                        new-beams)))

(defn- energize-grid [start-beam grid]
  (let [max-y (count grid)
        max-x (count (first grid))]
    (loop [[beam & beams] [start-beam]
           energized #{}
           seen #{}]
      (let [{:keys [pos dir]} beam]
        (if-not (seq beam)
          (count energized)
          (if (seen beam)
            (recur beams energized seen)
            (condp = (get-in grid pos)
              \. (recur (add-beams beams max-y max-x [(continue-beam beam)])
                        (conj energized pos)
                        (conj seen beam))
              \\ (recur (add-beams beams max-y max-x [(reflect-back beam)])
                        (conj energized pos)
                        (conj seen beam))
              \/ (recur (add-beams beams max-y max-x [(reflect-forward beam)])
                        (conj energized pos)
                        (conj seen beam))            
              \- (recur (add-beams beams max-y max-x (split-dash beam))
                        (conj energized pos)
                        (conj seen beam))
              \| (recur (add-beams beams max-y max-x (split-pipe beam))
                        (conj energized pos)
                        (conj seen beam)))))))))

(defn part-1
  "Day 16 Part 1"
  [input]
  (->> input
       (u/to-matrix)
       (energize-grid {:pos [0 0] :dir :right})))

(defn- energize-grid-edges [grid]
  (let [max-y (count grid)
        max-x (count (first grid))]
    (concat
     (map #(energize-grid % grid)
          [{:pos [0 0] :dir :down}
           {:pos [0 (dec max-x)] :dir :down}
           {:pos [(dec max-y) 0] :dir :up}
           {:pos [(dec max-y) (dec max-x)] :dir :up}])
     (for [y (range 0 max-y)
           x (range 0 max-x)
           :when (or (= 0 x)
                     (= 0 y)
                     (= (dec max-y) y)
                     (= (dec max-x) x))]
       (let [dir (cond (= 0 x) :right
                       (= (dec max-x) x) :left
                       (= 0 y) :down
                       (= (dec max-y) y) :up)]
         (energize-grid {:pos [y x] :dir dir} grid))))))

(defn part-2
  "Day 16 Part 2"
  [input]
  (->> input
       (u/to-matrix)
       (energize-grid-edges)
       (apply max)))
