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
  (first (for [x (range 9) y (range 9) :when (= 0 (get-in board [x y]))]
           [x y])))

(defn can-put? [board [row col] i]
  (and (nil? (some #{i} (get board row)))                                 ; test in row
       (nil? (some #{i} (for [x (range 9)] (get-in board [x col]))))      ; test in column
       (nil? (some #{i} (for [x (take 3 (iterate inc (- row (mod row 3)))); test in square
                              y (take 3 (iterate inc (- col (mod col 3))))]
                          (get-in board [x y]))))))

(defn solve [board]
  (if-let [[x y] (next-empty board)]
    (or (first (filter (comp nil? next-empty)
                       (for [i (range 1 10)
                             :when (can-put? board [x y] i)
                             :let [board* (-> (assoc-in board [x y] i) (solve))]] board*)))
        board) ; impossible from here, backtrack
    board))

(defn solve-batch [sudokus]
  (doseq [sudoku sudokus]
    (when (> (.length sudoku) 0)
      (-> (from-str sudoku)
          (solve)
          (to-str)
          (println)))))


(defn -main [& args]
  (let [into-vec (partial into [])]
    (when-let [[input] args]
      (-> (slurp input)
          (.split "-- SAMPLE.* --")
          (into-vec)
          (solve-batch)))))
