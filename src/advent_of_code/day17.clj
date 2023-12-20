(ns advent-of-code.day17
  (:require [advent-of-code.utils :as u]
            [clojure.data.priority-map :refer [priority-map]]))

(defn- parse-city [input]
  (mapv (fn [line] (mapv #(Integer/parseInt (str %)) line)) (u/to-matrix input)))

(defn- in-bounds? [[y x] max-y max-x]
  (and (>= y 0)
       (>= x 0)
       (< y max-y)
       (< x max-x)))

(defn- neighbors [max-y max-x min-steps max-steps [[y x] dir cnt]]
  (filter #(and (in-bounds? (first %) max-y max-x)
                (<= (last %) max-steps))
          (condp = dir
            :right [[[y (inc x)] :right (inc cnt)]
                    [[(- y min-steps) x] :up min-steps]
                    [[(+ y min-steps) x] :down min-steps]]
            :left  [[[y (dec x)] :left (inc cnt)]
                    [[(- y min-steps) x] :up min-steps]
                    [[(+ y min-steps) x] :down min-steps]]
            :up    [[[(dec y) x] :up (inc cnt)]
                    [[y (- x min-steps)] :left min-steps]
                    [[y (+ x min-steps)] :right min-steps]]
            :down  [[[(inc y) x] :down (inc cnt)]
                    [[y (- x min-steps)] :left min-steps]
                    [[y (+ x min-steps)] :right min-steps]]
            :any [[[y (- x min-steps)] :left min-steps]
                  [[y (+ x min-steps)] :right min-steps]
                  [[(- y min-steps) x] :up min-steps]
                  [[(+ y min-steps) x] :down min-steps]])))

(defn- get-pos-in-between [[y x] dir steps]
  (map (fn [step]
         (condp = dir
           :right [y (- x step)]
           :left [y (+ x step)]
           :up [(+ y step) x]
           :down [(- y step) x]))
       (range 0 steps)))

(defn- get-heat-loss [city min-steps [pos dir steps]]
  (if (= min-steps steps)
    (reduce + (map (partial get-in city) (get-pos-in-between pos dir steps)))
    (get-in city pos)))

(defn- get-neighbors [city max-y max-x min-steps max-steps block]
  (into {}
        (map #(vector % (get-heat-loss city min-steps %))
             (neighbors max-y max-x min-steps max-steps block))))

(defn map-vals [f m]
  (reduce #(assoc %1 (first %2) (f (last %2))) {} m))

(defn- remove-keys [pred m]
  (select-keys m (filter (complement pred) (keys m))))

(defn- dijkstra [start target nbrs]
  (loop [q (priority-map start 0)
         res {}]
    (let [[curr dist] (peek q)]
      (if (= (first curr) target)
        dist
        (let [new-dists (->> (nbrs curr)
                             (remove-keys res)
                             (map-vals (partial + dist)))]
          (recur (merge-with min (pop q) new-dists) (assoc res curr dist)))))))

(defn- find-min-heat-loss [start dir min-steps max-steps city]
  (let [max-y (count city)
        max-x (count (first city))
        start' [start dir 0]]
    (dijkstra start'
              [(dec max-y) (dec max-x)]
              (partial get-neighbors city max-y max-x min-steps max-steps))))
(defn part-1
  "Day 17 Part 1"
  [input]
  (->> input
       (parse-city)
       (find-min-heat-loss [0 0] :any 1 3)))

(defn part-2
  "Day 17 Part 2"
  [input]
  (->> input
       (parse-city)
       (find-min-heat-loss [0 0] :any 4 10)))
