(ns advent-of-code.day02
  (:require [advent-of-code.utils :as u]
            [clojure.string :as str]))

(defn- parse-draw [draw]
  (let [draw (str/replace draw "," "")
        parts (partition 2 (str/split draw #"\s+"))]
    (reduce (fn [out [num color]]
              (assoc out (keyword color) (Integer/parseInt num)))
            {}
            parts)))

(defn- parse-game [game-line]
  (let [[_ game content] (re-matches #"Game (\d+): (.*)" game-line)
        draws (str/split content #"; ")
        id (Integer/parseInt game)]
    {:id id :draws (map parse-draw draws)}))

(defn- is-draw-possible? [r g b draw]
  (and (>= r (get draw :red 0))
       (>= g (get draw :green 0))
       (>= b (get draw :blue 0))))

(defn- is-game-possible? [r g b {draws :draws}]
  (every? (partial is-draw-possible? r g b) draws))

(defn- possible-games [r g b games]
  (filter (partial is-game-possible? r g b) games))

(defn part-1
  "Day 02 Part 1"
  [input]
  (->> input
       (u/to-lines)
       (map parse-game)
       (possible-games 12 13 14)
       (map :id)
       (reduce +)))

(defn- minimum-set [{draws :draws}]
  (reduce (fn [out [color num]]
            (if (> num (get out color))
              (assoc out color num)
              out))
          {:red 0
           :green 0
           :blue 0}
          (mapcat seq draws)))

(defn- power [{:keys [red green blue]}]
  (* red green blue))

(defn part-2
  "Day 02 Part 2"
  [input]
  (->> input
       (u/to-lines)
       (map parse-game)
       (map minimum-set)
       (map power)
       (reduce +)))
