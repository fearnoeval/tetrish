(ns tetrish.game-loop
  (:require
    [tetrish.ui              :as ui]
    [tetrish.game-logic      :as game-logic]
    [tetrish.player-actions  :as player-actions]
    [cljs.core.async        :refer [<! >! chan put! sliding-buffer timeout]])
  (:require-macros
    [cljs.core.async.macros :refer [go-loop]]))

(defn make-key-chan []
  (let [channel (chan (sliding-buffer 1) ui/key-filterer)]
    (ui/listen ui/window :key-down channel)
    channel))

(defn make-time-chan [gravity-chan]
  (let [channel (chan)]
    (go-loop [ms (<! gravity-chan)]
      (<! (timeout ms))
      (>! channel player-actions/move-down)
      (recur (<! gravity-chan)))
    channel))

(defn game-loop [state]
  (let [gravity-chan (chan (sliding-buffer 1))
        key-chan     (make-key-chan)
        time-chan    (make-time-chan gravity-chan)
        render!      (ui/make-renderer gravity-chan)]
    (go-loop [state state]
      (render! state)
      (when-not (:game-over? state)
        (let [[f _] (alts! [key-chan time-chan])]
          (recur (f state)))))))

(game-loop (game-logic/get-new-game))
