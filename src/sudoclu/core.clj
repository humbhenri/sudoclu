(ns sudoclu.core
  (:use [clojure.pprint :only [pprint]]))


(defn from-str [board-repr]
  (->>
   (filter #(Character/isDigit %) board-repr)
   (map (comp - (partial - 48) int))
   (partition 9)
   (map (partial into []))
   (into [])))


(defn to-str [board]
  (.replaceAll (with-out-str (pprint board)) "[\\[\\]]" ""))


(defn next-empty [board]
  (first (for [x (range 10) y (range 10) :when (= 0 (get-in board [x y]))]
           [x y])))


(defn solve [board]
  )
