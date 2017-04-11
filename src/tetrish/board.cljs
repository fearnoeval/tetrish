(ns tetrish.board)

(def empty-placeholder :empty)

(def row-count 20)
(def col-count 10)

(def empty-row
  (vec (repeat col-count empty-placeholder)))

(def empty-board
  (vec (repeat row-count empty-row)))
