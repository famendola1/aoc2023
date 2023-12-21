(ns advent-of-code.day19
  (:require [advent-of-code.utils :as u]
            [clojure.string :as str]))

(def op->fn {"<" < ">" >})

(defn- mk-eval-step-fn [field op val nxt]
  (fn [rating]
    (when (op (rating field) val)
      nxt)))

(defn- parse-step [step]
  (let [[_ field op val nxt ] (re-matches #"([xmas])([<>])(\d+):([a-zAR]+)"
                                          step)]
    (mk-eval-step-fn (keyword field) (op->fn op) (parse-long val) (keyword nxt))))

(defn- parse-workflow [workflow]
  (let [[_ name content] (re-matches #"([a-z]+)\{(.*?)\}" workflow)
        steps (str/split content #",")]
    {(keyword name) (conj (mapv parse-step (butlast steps))
                          (constantly (keyword (last steps))))}))

(defn- parse-workflows [block]
  (into {} (map parse-workflow (u/to-lines block))))

(defn- parse-rating [rating]
  (into {} (map vector [:x :m :a :s] (u/parse-out-longs rating))))

(defn- parse-ratings [block]
  (map parse-rating (u/to-lines block)))

(defn- parse-input [input]
  (let [blocks (u/to-blocks input)]
    {:workflows (parse-workflows (first blocks))
     :ratings (parse-ratings (last blocks))}))

(defn- eval-steps [steps rating]
  (some #(% rating) steps))

(defn- eval-rating [workflows rating]
  (loop [start :in]
    (cond (= start :R) 0
          (= start :A) (reduce + (vals rating))
          :else (recur (eval-steps (workflows start) rating)))))

(defn- eval-ratings [{:keys [workflows ratings]}]
  (map (partial eval-rating workflows) ratings))

(defn part-1
  "Day 19 Part 1"
  [input]
  (->> input
       (parse-input)
       (eval-ratings)
       (reduce +)))

(defn part-2
  "Day 19 Part 2"
  [input]
  "Implement this part")
