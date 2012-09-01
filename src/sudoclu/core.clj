(ns sudoclu.core
  (:import [javax.swing JFrame JPanel JLabel JButton SwingUtilities]
           [java.awt GridLayout]))

(def board (ref (make-array Integer/TYPE 9 9)))


(defn read-sudoku
  "Read a string representing a sudoku board and populates the board.
Exemple of input
2..1.5..3
.54...71.
.1.2.3.8.
6.28.73.4
.........
1.53.98.6
.2.7.1.6.
.81...24.
7..4.2..1"
  [input]
  (let [a-char (to-array (filter #(not= \newline %) (.toCharArray input )))
        char-to-int (fn [c] (let [i (- (int c) 48)]
                             (if (> i 0) i 0)))]
    (loop [idx 0
           x 0
           y 0]
      (when (< idx 81)
        (aset-int @board x y (char-to-int (aget a-char idx)))
        (recur
         (inc idx)
         (if (>= y 8) (inc x) x)
         (mod (inc y) 9))))))


(defn make-square []
  (doto (JButton. )))


(defn make-board []
  (let [panel (JPanel.)]
    (.setLayout panel (GridLayout. 9 9))
    (dotimes [i 81] (.add panel (make-square)))
    panel))


(defn update-board [board]
  (SwingUtilities/invokeLater
   (fn []
     (dotimes [i 81]
       (doto (.getComponent board i)
         (.setLabel (aget @board (int (Math/floor (/ i 9))) (mod i 9))))))))


(defn main-window []
  (doto (JFrame. "sudoclu")
                                        ;   (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
    (.setSize 800 600)
    (.add (make-board))
    (.pack)
    (.setVisible true)))