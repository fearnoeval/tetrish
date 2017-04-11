(ns tetrish.macros)

(defmacro defmove [name kvs failure-function]
  `(defn ~name [original-state#]
     (if (:game-over? original-state#)
       original-state#
       (let [reducing-fn# (fn [state-acc# [path# f#]] (update-in state-acc# path# f#))
             new-state# (reduce reducing-fn# original-state# ~kvs)]
         (if (tetrish.game-logic-helpers/valid-position? (:board new-state#) (:piece new-state#))
           new-state#
           (~failure-function original-state#))))))
