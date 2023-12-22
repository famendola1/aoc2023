(ns advent-of-code.day20
  (:require [advent-of-code.utils :as u]
            [clojure.string :as str]))

(defn- parse-module [module]
  (let [[_ typ name dests] (re-matches #"(%|&)?([a-z]+) -> (.*)" module)]
    [(keyword typ) (keyword name) (map keyword (str/split dests #", "))]))

(defn- parse-modules [modules]
  (map parse-module modules))

(defn- mk-flip-flop [name dests]
  (fn [[_ _ typ] state mem]
    (if (= :hi typ)
      [[] state mem]
      (let [state' (update state name not)
            typ' (if (state name) :low :hi)]
        [(mapv vector (repeat name) dests (repeat typ'))
         state'
         mem]))))

(defn- mk-conj [name dests]
  (fn [[from _ typ] state mem]
    (let [mem' (assoc-in mem [name from] typ)
          typ' (if (every? #(= :hi %) (vals (mem' name))) :low :hi)]
      [(mapv vector (repeat name) dests (repeat typ'))
       state
       mem'])))

(defn- mk-broadcaster [dests]
  (fn []
    (mapv vector
          (repeat :broadcaster)
          dests
          (repeat :low))))

(defn- mk-normal []
  (fn [_ state mem]
    [[] state mem]))

(defn- mk-module-fn [typ name dests]
  (cond (= :broadcaster name) (mk-broadcaster dests)
        (= :% typ) (mk-flip-flop name dests)
        (= :& typ) (mk-conj name dests)))

(defn- init-module [[typ name dests]]
  [name (mk-module-fn typ name dests)])

(defn- init-mem [modules]
  (select-keys (reduce (fn [out path] (assoc-in out path :low))
                       {}
                       (mapcat (fn [m] (map #(vector % (second m)) (last m))) modules))
               (map second (filter #(= :& (first %)) modules))))

(defn- init-types [modules]
  (into {} (map #(vector (second %) (first %)) modules)))

(defn- init-machine [modules]
  {:machine (into {} (map init-module modules))
   :mem (init-mem modules)})

(defn- press-button-once [machine {:keys [state mem]}]
  (loop [[p & ps] ((:broadcaster machine))
         state state
         mem mem
         res {:low 0 :hi 0}]
    (if-not p
      {:state state :mem mem :pulses res}
      (let [[_ to typ] p
            module-fn (get machine to (mk-normal))
            [nxt state' mem'] (module-fn p state mem)]
        (recur (concat ps nxt) state' mem' (update res (last p) inc))))))

(defn- press-button [times {:keys [machine mem]}]
  (cons {:low times}
        (take times
              (map :pulses (rest (iterate (partial press-button-once machine)
                                          {:state {} :mem mem :pulses {}}))))))

(defn- count-pulses [pulses]
  (apply * (vals (apply merge-with + pulses))))

(defn part-1
  "Day 20 Part 1"
  [input]
  (->> input
       (u/to-lines)
       (parse-modules)
       (init-machine)
       (press-button 1000)
       (count-pulses)))

;; PART 2 NOT WORKING

(defn- press-button-with-tracking [modules n machine {:keys [state mem track]}]
  (loop [[p & ps] ((:broadcaster machine))
         state state
         mem mem
         track track]
    (if-not p
      {:mem mem :state state :track track}
      (let [[from to typ] p
            module-fn (get machine to (mk-normal))
            [nxt state' mem'] (module-fn p state mem)]
        (if (and (modules from) (= typ :low) (zero? (track from)))
          (recur (concat ps nxt) state' mem' (assoc track from n))
          (recur (concat ps nxt) state' mem' track))))))

(defn- track-modules [modules {:keys [machine mem]}]
  (loop [n 1 track (into {} (map vector modules (repeat 0)))]
    (if (some zero? (vals track))
      (let [{state :state mem :mem track' :track}
            (press-button-with-tracking
             modules
             n                                
             machine
             {:state {} :mem mem :track track})]
        (recur (inc n) track'))
      (reduce u/lcm (vals track)))))

(defn part-2
  "Day 20 Part 2"
  [input]
  (->> input
       (u/to-lines)
       (parse-modules)
       (init-machine)
       (track-modules #{:ks :jf :zk :qs})))
