(ns advent-of-code.day07
  (:require [advent-of-code.utils :as u]
            [clojure.string :as str]))

(defn- parse-hand [line]
  (let [[cards bid] (str/split line #" ")]
    [cards (parse-long bid)]))

(defn- five-of-a-kind? [card-counts]
  (= 1 (count (keys card-counts))))

(defn- four-of-a-kind? [card-counts]
  (some #(= 4 %) (vals card-counts)))

(defn- full-house? [card-counts]
  (and (not (four-of-a-kind? card-counts))
       (= 2 (count (keys card-counts)))))

(defn- three-of-a-kind? [card-counts]
  (some #(= 3 %) (vals card-counts)))

(defn- two-pair? [card-counts]
  (and (not (three-of-a-kind? card-counts))
       (= 3 (count (keys card-counts)))))

(defn- one-pair? [card-counts]
  (and (not (two-pair? card-counts))
       (some #(= 2 %) (vals card-counts))))

(def ^:private cards [\X \2 \3 \4 \5 \6 \7 \8 \9 \T \J \Q \K \A])

(def ^:private card-ranks
  (into {} (map vec (partition 2 (interleave cards
                                             (range 1 (inc (count cards))))))))

(defn- apply-jokers [card-counts]
  (let [jokers (card-counts \X)
        new-card-counts (dissoc card-counts \X)
        card (when (seq new-card-counts)
               (last (sort-by card-ranks
                              (filter #(= (new-card-counts %)
                                          (apply max (vals new-card-counts)))
                                      (keys new-card-counts)))))]    
    (if-not (nil? jokers)
      (if-not (nil? card)
        (update new-card-counts card + jokers)
        {\X 5})
      card-counts)))

(defn- count-cards [cards]
  (apply-jokers (reduce (fn [counts card]
                          (if (nil? (counts card))
                            (assoc counts card 1)
                            (update counts card inc)))
                        {}
                        cards)))

(defn- score-hand [hand]
  (let [card-counts (count-cards (first hand))]
    (cond (five-of-a-kind? card-counts) 7
          (four-of-a-kind? card-counts) 6
          (full-house? card-counts) 5
          (three-of-a-kind? card-counts) 4
          (two-pair? card-counts) 3
          (one-pair? card-counts) 2
          :else 1)))

(defn- compare-cards [card-a card-b]
  (compare (card-ranks card-a) (card-ranks card-b)))

(defn- tiebreak-cards [cards-a cards-b]
  (let [card-pairs (partition 2 (interleave cards-a cards-b))
        count-cards-a (count-cards cards-a)
        count-cards-b (count-cards cards-b)
        high-card-a (last (sort-by count-cards-a (keys count-cards-a)))
        high-card-b (last (sort-by count-cards-b (keys count-cards-b)))
        to-compare (first (drop-while #(zero? (compare-cards (first %) (second %)))
                                      card-pairs))]
    (if (or (= 5 (count (keys count-cards-a))) (= high-card-a high-card-b))
      (compare-cards (first to-compare) (second to-compare))
      (compare-cards high-card-a high-card-b))))

(defn- compare-hands [hand-a hand-b]
  (let [score-a (score-hand hand-a)
        score-b (score-hand hand-b)]
    (cond (> score-a score-b) 1
          (< score-a score-b) -1
          :else (tiebreak-cards (first hand-a) (first hand-b)))))

(defn part-1
  "Day 07 Part 1"
  [input]
  (->> input
       (u/to-lines)
       (map parse-hand)
       (sort compare-hands)
       (map-indexed (fn [idx itm] [(inc idx) (second itm)]))
       (map (partial apply *))
       (reduce +)))

(defn part-2
  "Day 07 Part 2"
  [input]
  (->> input
       (u/to-lines)
       (map #(str/replace % "J" "X"))
       (map parse-hand)
       (sort compare-hands)
       (map-indexed (fn [idx itm] [(inc idx) (second itm)]))
       (map (partial apply *))
       (reduce +)))
