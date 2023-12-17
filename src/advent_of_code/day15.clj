(ns advent-of-code.day15
  (:require [advent-of-code.utils :as u]
            [clojure.string :as str]))

(defn- hash-sequence [init-seq]
  (reduce (fn [val curr]            
            (rem (* (+ val (int curr)) 17) 256))
          0
          init-seq))

(defn part-1
  "Day 15 Part 1"
  [input]
  (->> input
       (str/trim)
       (#(str/split % #","))
       (map hash-sequence)
       (reduce +)))

(defn- process-equals [hmap op]
  (let [box-num (hash-sequence (take-while #(not= % \=) op))
        [lens focal] (str/split op #"=")
        box (hmap box-num)
        {:keys [idxs lenses]} box]
    (cond (not (seq box))
          (assoc hmap box-num {:idxs {lens 0} :lenses [focal]})
          (nil? (idxs lens))
          (assoc
           hmap
           box-num
           {:idxs (assoc idxs lens (count idxs))
            :lenses (conj lenses focal)})
          :else
          (assoc-in hmap [box-num :lenses (idxs lens)] focal))))

(defn- remove-lens [{:keys [idxs lenses]} lens]  
  {:idxs (into {} (map-indexed #(vector (first %2) %1)
                               (sort-by last (dissoc idxs lens))))
   :lenses (vec (keep-indexed #(if-not (= (idxs lens) %1) %2)
                              lenses))})

(defn- process-dash [hmap op]
  (let [box-num (hash-sequence (drop-last op))
        lens (apply str (drop-last op))
        box (hmap box-num)]
    (if-not (get-in hmap [box-num :idxs lens])
      hmap
      (assoc hmap box-num (remove-lens box lens)))))

(defn- process-seq [hmap op]  
  (if (str/includes? op "=")
    (process-equals hmap op)
    (process-dash hmap op)))

(defn- hashmap [ops]
  (reduce process-seq
          {}
          ops))

(defn- lens-power [box-num idx focal]
  (* (inc box-num) (inc idx) (Integer/parseInt focal)))

(defn- box-power [[num box]]
  (reduce + (map-indexed (partial lens-power num) (:lenses box))))

(defn- focusing-power [hmap]
  (reduce + (map box-power hmap)))

(defn part-2
  "Day 15 Part 2"
  [input]
  (->> input
       (str/trim)
       (#(str/split % #","))
       (hashmap)
       (focusing-power)))
