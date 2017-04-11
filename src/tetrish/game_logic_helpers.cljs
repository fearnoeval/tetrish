(ns tetrish.game-logic-helpers
  (:require
    [tetrish.board :refer [col-count row-count] :as board]
    [tetrish.pieces :refer [pieces i-pieces j-pieces l-pieces o-pieces s-pieces t-pieces z-pieces]]))

(def rotate-clockwise*
  (let [[o1]          o-pieces
        [i1 i2]       i-pieces
        [s1 s2]       s-pieces
        [z1 z2]       z-pieces
        [j1 j2 j3 j4] j-pieces
        [l1 l2 l3 l4] l-pieces
        [t1 t2 t3 t4] t-pieces]
    {o1 o1
     i1 i2 i2 i1
     s1 s2 s2 s1
     z1 z2 z2 z1
     j1 j2 j2 j3 j3 j4 j4 j1
     l1 l2 l2 l3 l3 l4 l4 l1
     t1 t2 t2 t3 t3 t4 t4 t1}))

(def rotate-counter-clockwise*
  (let [[o1]          o-pieces
        [i1 i2]       i-pieces
        [s1 s2]       s-pieces
        [z1 z2]       z-pieces
        [j1 j2 j3 j4] j-pieces
        [l1 l2 l3 l4] l-pieces
        [t1 t2 t3 t4] t-pieces]
    {o1 o1
     i1 i2 i2 i1
     s1 s2 s2 s1
     z1 z2 z2 z1
     j1 j4 j4 j3 j3 j2 j2 j1
     l1 l4 l4 l3 l3 l2 l2 l1
     t1 t4 t4 t3 t3 t2 t2 t1}))

(def initial-state
  {:board board/empty-board
   :game-over? false
   :gravity 800
   :piece nil
   :stats {:level 0
           :lines-cleared 0
           :points 0
           :start-level 0
           :piece-counts {:i 0 :j 0 :l 0 :o 0 :s 0 :t 0 :z 0}}})

(def base-scores
  [0 40 100 300 1200])

(def level-gravity-numerators
  { 0 48,  1 43,  2 38,  3 33,  4 28,  5 23,  6 18,  7 13,  8  8,  9  6,
   10  5, 11  5, 12  5, 13  4, 14  4, 15  4, 16  3, 17  3, 18  3, 19  2,
   20  2, 21  2, 22  2, 23  2, 24  2, 25  2, 26  2, 27  2, 28  2})

(defn rotate-clockwise [piece]
  (update piece :coordinates rotate-clockwise*))

(defn rotate-counter-clockwise [piece]
  (update piece :coordinates rotate-counter-clockwise*))

(defn get-random-piece []
  (->> (count pieces)
       rand-int
       (nth (keys pieces))
       pieces))

(defn points-for-clear [line-clears level]
  (let [base-score (base-scores line-clears)]
    (* base-score (inc level))))

(defn get-gravity [level]
  (-> (or (level-gravity-numerators level) 1)
      (/ 60)
      (* 1000)))

(defn to-board-coordinate [[x1 y1] [x2 y2]]
  [(+ x1 x2) (+ y1 y2)])

(defn position-within-bounds? [[x y]]
  (and (> x -1)
       (< x board/col-count)
       (< y board/row-count)))

(defn position-available? [board [x y]]
  (or (neg? y) (= board/empty-placeholder (get-in board [y x]))))

(defn valid-position? [board {:keys [coordinates position]}]
  (let [to-board-coordinate (partial to-board-coordinate position)
        board-coordinates   (map to-board-coordinate coordinates)
        position-available? (partial position-available? board)
        valid?              #(and (position-available? %) (position-within-bounds? %))]
    (every? valid? board-coordinates)))
