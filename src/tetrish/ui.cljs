(ns tetrish.ui
  (:require
    [tetrish.board          :refer [empty-board empty-placeholder]]
    [tetrish.game-logic     :refer [get-new-game merge-piece]]
    [tetrish.hiccup         :refer [to-html]]
    [tetrish.pieces         :refer [pieces]]
    [tetrish.player-actions :refer [load-game move-down move-left move-right rotate save-game]]
    [cljs.core.async       :refer [chan put!]]
    [goog.dom              :refer [getElement]]
    [goog.events :as gevents])
  (:import
    [goog.events EventType KeyCodes]))

(def window js/window)

(def keycode-routes
  {KeyCodes.LEFT  move-left
   KeyCodes.RIGHT move-right
   KeyCodes.DOWN  move-down
   KeyCodes.UP    rotate
   KeyCodes.L     load-game
   KeyCodes.S     save-game})

(def keyword->event-type
  {:key-down    EventType.KEYDOWN})

(def key-filterer
  (comp
    (map #(keycode-routes (.-keyCode %)))
    (filter (comp not nil?))))

(defn listen [listenee event-type channel]
  (let [event-type (keyword->event-type event-type)]
    (if event-type
      (gevents/listen listenee event-type (fn [e] (put! channel e)))
      (throw (js/Error. (str event-type " is not a known event type."))))))

(defn col->hiccup [col]
  [:div {:class (str "child " (name col) "-piece")}])

(defn row->hiccup [row]
  (let [container [:div {:class "parent"}]]
    (into container (map col->hiccup row))))

(defn board->hiccup [{:keys [board game-over?] :as state}]
  (let [game-over-string (if game-over? "game-over" "")
        container [:div {:id "game-board" :class game-over-string}]]
    (into container (map row->hiccup board))))

(defn stats->hiccup [state]
  (let [score         (-> state :stats :points)
        lines-cleared (-> state :stats :lines-cleared)
        level         (-> state :stats :level)
        game-over?    (if (-> state :game-over?) "Yes" "Not yet")]
    [:div {:id "stats"}
           [:span {:class "bold"} "Score"] ": " score
     [:br] [:span {:class "bold"} "Lines cleared"] ": " lines-cleared
     [:br] [:span {:class "bold"} "Level"] ": " level
     [:br] [:span {:class "bold"} "Game over?"] " " game-over?]))

(defn render! [state]
  (let [score-html    (-> state stats->hiccup to-html)
        board-html    (-> state merge-piece board->hiccup to-html)]
    (.log js/console (clj->js state))
    (set! (.-innerHTML (getElement "main")) (str score-html board-html))))

(defn make-renderer [gravity-chan]
  (fn [state]
    (let [gravity (-> state :gravity)]
      (put! gravity-chan gravity)
      (render! state))))
