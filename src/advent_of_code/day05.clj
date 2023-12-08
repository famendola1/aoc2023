(ns advent-of-code.day05
  (:require [advent-of-code.utils :as u]))

(defn- parse-map [block]
  (sort-by first
           (reduce (fn [mappings line]
                     (let [[dest src len] (u/parse-out-longs line)]
                       (cons [src dest len] mappings)))
                   []
                   (drop 1 (u/to-lines block)))))

(defn- parse-maps [[seeds & maps]]
  {:seeds (u/parse-out-longs seeds)
   :maps (map parse-map maps)})

(defn- convert [[src dest len] val]
  (if (>= val (+ src len))
    val
    (+ dest (- val src))))

(defn- find-in-map [val curr-map]
  (if-let [entry (last (take-while #(<= (first %) val) curr-map))]
    (convert entry val)
    val))

(defn- find-seed-location [maps seed]
  (reduce find-in-map seed maps))

(defn- find-seed-locations [{:keys [seeds maps]}]
  (map (partial find-seed-location maps) seeds))

(defn part-1
  "Day 05 Part 1"
  [input]
  (->> input
       (u/to-blocks)    
       (parse-maps)
       (find-seed-locations)
       (reduce min)))

(defn- adjust-seeds [input]
  (update input :seeds (partial partition 2)))

(defn- find-all-seed-locations [{:keys [seeds maps]}]
  (mapcat (fn [[start len]]
            (let [all-seeds (range start (+ start len))]
              (find-seed-locations {:seeds all-seeds :maps maps})))
          seeds))

(defn part-2
  "Day 05 Part 2"
  [input]
  (->> input
       (u/to-blocks)    
       (parse-maps)
       (adjust-seeds)
       (find-all-seed-locations)
       (reduce min)))
