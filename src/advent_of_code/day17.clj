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

(defn- get-neighbors [[[y x] dir cnt] limit max-y max-x]
  (filter #(and (in-bounds? (first %) max-y max-x)
                (<= (last %) limit))
          (condp = dir
            :right [[[y (inc x)] :right (inc cnt)]
                    [[(dec y) x] :up 1]
                    [[(inc y) x] :down 1]]
            :left  [[[y (dec x)] :left (inc cnt)]
                    [[(dec y) x] :up 1]
                    [[(inc y) x] :down 1]]
            :up    [[[(dec y) x] :up (inc cnt)]
                    [[y (dec x)] :left 1]
                    [[y (inc x)] :right 1]]
            :down  [[[(inc y) x] :down (inc cnt)]
                    [[y (dec x)] :left 1]
                    [[y (inc x)] :right 1]])))

(defn- update-heat-loss [grid curr heat-loss block]  
  (let [curr-heat-loss (heat-loss curr)
        block-heat-loss (heat-loss block)
        new-heat-loss (+ curr-heat-loss (get-in grid (first block)))]
    (cond (nil? block-heat-loss)
          [block new-heat-loss]
          (< new-heat-loss block-heat-loss)
          [block new-heat-loss]
          :else [block block-heat-loss])))

(defn- update-neighbors [grid heat-loss neighbors curr]  
  (into heat-loss
        (map (partial update-heat-loss grid curr heat-loss) neighbors)))

(defn- update-heat-losses [grid heat-loss limit max-y max-x curr]
  (update-neighbors grid heat-loss (get-neighbors curr limit max-y max-x) curr))

(defn- find-min-heat-loss [start dir limit city]
  (let [max-y (count city)
        max-x (count (first city))
        target [(dec max-y) (dec max-x)]]
    (loop [curr [start dir 0]
           heat-loss (priority-map [start dir 0] 0)
           visited #{[start dir 0]}]
      (if (= target (first curr))
        (heat-loss curr)
        (let [heat-loss' (update-heat-losses city heat-loss limit  max-y max-x curr)
              curr' (ffirst (apply dissoc heat-loss' (seq visited)))]
          (recur curr'
                 heat-loss'
                 (conj visited curr')))))))

(defn part-1
  "Day 17 Part 1"
  [input]
  (->> input
       (parse-city)
       (find-min-heat-loss [0 0] :right 3)))

(defn part-2
  "Day 17 Part 2"
  [input]
  "Implement this part")
