(ns sudoclu.core
  (:import [javax.swing JFrame JPanel JLabel JButton SwingUtilities]
           [java.awt GridLayout]))

(def *board-size* 81)

(def board (atom (vec (take *board-size* (cycle [0])))))

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
  (let [a-char (filter #(not= \newline %) (.toCharArray input ))
        char-to-int (fn [c] (let [i (- (int c) 48)]
                             (if (> i 0) i 0)))]
    (reset! board (vec (map char-to-int a-char)))))


;;; GUI

(defn make-square []
  (doto (JButton. )))

(def board-panel
  (let [panel (JPanel.)
        squares (make-array JButton *board-size*)]
    (.setLayout panel (GridLayout. 9 9))
    (dotimes [i *board-size*]
      (let [square (make-square)]
        (.add panel square)
        (aset squares i square)))
    (hash-map :jpanel panel :squares squares)))

(defn update-board-panel []
  (SwingUtilities/invokeLater
   (fn []
     (dotimes [i *board-size*]
       (let [square (aget (:squares board-panel) i)]
         (.setLabel square (str (nth @board i)))
         (.invalidate square)))
     (.repaint (:jpanel board-panel)))))


(defn -main []
  (add-watch board "update"
             (fn [k r o n]
               (update-board-panel)))
  (doto (JFrame. "sudoclu")
                                        ;   (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
    (.setSize 1000 1000)
    (.add (:jpanel board-panel))
    (.setVisible true))
  (read-sudoku (slurp "input.txt")))
