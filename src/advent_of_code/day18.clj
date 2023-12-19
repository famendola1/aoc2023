(ns advent-of-code.day18
  (:require [advent-of-code.utils :as u]))

(def delta {:U [-1 0] :R [0 1] :L [0 -1] :D [1 0]})

(defn- parse-instructions [line]
  (let [[_ dir len color] (re-matches #"^([URDL]) (\d+) \(#([0-9a-f]{6})\)"
                                      line)]
    [(keyword dir) (parse-long len) color]))

(defn- next-point [point dir len]
  (mapv + point (mapv #(* % len) (delta dir))))

(defn- make-polygon [instructions]
  (reduce (fn [{:keys [verts perim]}[dir len _]]
            {:verts (conj verts (next-point (last verts) dir len))
             :perim (+ perim len)})
          {:verts [[0 0]]
           :perim 0}
          instructions))

(defn- shoelace [[[y1 x1] [y2 x2]]]
  (- (* x1 y2) (* y1 x2)))

(defn- calculate-area [{:keys [verts perim]}]
  (inc (quot (+ perim
                (reduce + (map shoelace (partition 2 1 verts))))
             2)))

(defn part-1
  "Day 18 Part 1"
  [input]
  (->> input
       (u/to-lines)
       (map parse-instructions)
       (make-polygon)
       (calculate-area)))

(def to-dir {\0 :R \1 :D \2 :L \3 :U})

(defn- fix-instructions [[_ _ color]]
  (let [new-dir (to-dir (last color))
        new-len (Integer/parseInt (apply str (butlast color)) 16)]
    [new-dir new-len color]))

(defn part-2
  "Day 18 Part 2"
  [input]
  (->> input
       (u/to-lines)
       (map parse-instructions)
       (map fix-instructions)
       (make-polygon)
       (calculate-area)))
