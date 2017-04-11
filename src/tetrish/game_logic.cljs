(ns tetrish.game-logic
  (:require
    [tetrish.board              :refer [empty-placeholder empty-board empty-row row-count]]
    [tetrish.game-logic-helpers :refer [get-gravity get-random-piece initial-state points-for-clear to-board-coordinate valid-position?]]))

(defn update-level [state]
  (let [start-level   (-> state :stats :start-level)
        lines-cleared (-> state :stats :lines-cleared)
        threshold     (min (+ (* start-level 10) 10) 100)
        new-level     (if (< lines-cleared threshold)
                        start-level
                        (-> lines-cleared (- threshold) (/ 10) int inc (+ start-level)))]
    (-> state
        (assoc-in [:stats :level] new-level)
        (assoc-in [:gravity] (get-gravity new-level)))))

(defn update-stats [state]
  (let [not-empty?        (partial not= empty-placeholder)
        cleared?          (partial every? not-empty?)
        level             (-> state :stats :level)
        partial-state     (update state :board (partial remove cleared?))
        line-clear-count  (- row-count (count (:board partial-state)))
        line-clear-points (points-for-clear line-clear-count level)]
    (-> partial-state
        (update-in [:stats :lines-cleared] (partial + line-clear-count))
        (update-in [:stats :points] (partial + line-clear-points))
        (update-in [:board] #(->> %
                                 (concat (take line-clear-count empty-board))
                                 (into [])))
        update-level)))

(defn merge-piece [{board :board {:keys [coordinates position name]} :piece :as state}]
  (let [to-board-coordinate (partial to-board-coordinate position)
        all-not-negative? #(every? (comp not neg?) %)
        updates   (->> coordinates (map to-board-coordinate) (filter all-not-negative?))]
    (as-> updates %
      (reduce (fn [board [x y]] (assoc-in board [y x] name)) board %)
      (assoc state :board %)
      (dissoc % :piece))))

(defn assoc-new-piece [state]
  (let [new-piece (get-random-piece)]
    (-> state
        (assoc :piece (get-random-piece))
        (update-in [:stats :piece-counts (:name new-piece)] inc))))

(defn merge-and-get-piece [{:keys [board piece] :as state}]
  (let [new-state (-> state merge-piece update-stats assoc-new-piece)]
     (if (valid-position? (:board new-state) (:piece new-state))
       new-state
       (assoc new-state :game-over? true))))

(defn get-new-game []
  (assoc-new-piece initial-state))
