(ns tetrish.player-actions
  (:require
    [tetrish.game-logic         :refer [merge-and-get-piece]]
    [tetrish.game-logic-helpers :refer [rotate-clockwise]])
  (:require-macros
    [tetrish.macros             :refer [defmove]]))

(defmove move-left
  {[:piece :position] (fn [[x y]] [(dec x) y])}
  identity)

(defmove move-right
  {[:piece :position] (fn [[x y]] [(inc x) y])}
  identity)

(defmove move-down
  {[:piece :position] (fn [[x y]] [x (inc y)])}
  merge-and-get-piece)

(defmove rotate
  {[:piece] rotate-clockwise}
  identity)

(defn- js-state->clj [state]
  (let [raw-clj              (js->clj state :keywordize-keys true)
        board-keyworder      (partial mapv (partial mapv keyword))]
    (-> raw-clj
        (update :board board-keyworder)
        (update-in [:piece :name] keyword))))

(defn load-game [state]
  (try
    (if-let [response (.prompt js/window "Load game")]
      (->> response (.parse js/JSON) js-state->clj)
      state)
    (catch js/Error e
      state)))

(defn save-game [state]
  (.prompt js/window "Save game" (.stringify js/JSON (clj->js state)))
  state)
