(ns tetrish.hiccup
  (:require
    [clojure.string :as string]))

(defn map-join [f v]
  (string/join (map f v)))

(def void-elements
  #{:area :base :br :col :command :embed :hr :img :input :keygen :link :meta :param :source :track :wbr})

(defprotocol HTMLRenderer
  (to-html [input]))

(extend-protocol HTMLRenderer
  nil
  (to-html [input]
    "")

  number
  (to-html [input]
    (.toString input))

  string
  (to-html [input]
    (reduce
      (fn [acc [from to]] (string/replace acc from to))
      input
      [["&" "&amp;"] ["<" "&lt;"] [">" "&gt;"] ["\"" "&quot;"] ["'" "&#39;"]]))

  object
  (to-html [input]
    (cond
      (satisfies? IMap input)
      (map-join (fn [[k v]] (str " " (name k) "=\"" v "\"")) input)

      (satisfies? ISequential input)
      (if-not (some #(% (first input)) [keyword? string? symbol?])
        (throw (js/Error. (str input " is not a valid tag name")))
        (let [tag-name    (name (first input))
              attributes  (if (map? (second input)) (to-html (second input)) "")
              contents    (map-join to-html ((if (empty? attributes) next nnext) input))
              opening-tag (str "<" tag-name attributes ">")
              closing-tag (if (contains? void-elements (keyword (first input))) "" (str "</" tag-name ">"))]
          (str opening-tag contents closing-tag)))

      :else
      (throw (js/Error. (str "No protocol method HTMLRenderer.to-html defined for type object: " input))))))
