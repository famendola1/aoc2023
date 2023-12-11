(ns advent-of-code.day08
  (:require [advent-of-code.utils :as u]
            [clojure.string :as str]))

(defn- parse-nodes [nodes]
  (reduce (fn [out node]
            (let [[name left right] (re-seq #"[A-Z]{3}" node)]
              (assoc out name {\L left \R right})))
          {}
          nodes))

(defn- parse-network [blocks]
  (let [rl (first blocks)
        nodes (u/to-lines (second blocks))]
    {:insts rl
     :nodes (parse-nodes nodes)}))

(defn- traverse [network start end]  
  (reduce (fn [nodes inst]
            (let [curr-node (first nodes)]
              (if (= curr-node end)
                (reduced nodes)
                (cons (get-in network [:nodes curr-node inst]) nodes))))
          [start]
          (cycle (:insts network))))

(defn part-1
  "Day 08 Part 1"
  [input]
  (-> input
      (u/to-blocks)
      (parse-network)
      (traverse "AAA" "ZZZ")
      (count)
      (dec)))

(defn- traverse' [network start ends]
  (reduce (fn [nodes inst]
            (let [curr-node (first nodes)]
              (if (ends curr-node)
                (reduced nodes)
                (cons (get-in network [:nodes curr-node inst]) nodes))))
          [start]
          (cycle (:insts network))))

(defn- ghost-traverse [network starts ends]
  (map #(traverse' network % ends) starts))

(defn- ghost-traverse-a-to-z [network]
  (ghost-traverse network
                  (filter #(str/ends-with? % "A") (keys (:nodes network)))
                  (set (filter #(str/ends-with? % "Z") (keys (:nodes network))))))

(defn- gcd [a b]
  (if (zero? b)
    a
    (recur b (mod a b))))

(defn- lcm [a b]
  (/ (* a b)
     (gcd a b)))

(defn part-2
  "Day 08 Part 2"
  [input]  
  (->> input
       (u/to-blocks)
       (parse-network)
       (ghost-traverse-a-to-z)
       (map (comp dec count))
       (reduce lcm)))
