(ns tetrish.pieces)

(def o-pieces
          [[[-1  0] [ 0  0]
            [-1  1] [ 0  1]]])

(def i-pieces
  [[[-2  0] [-1  0] [ 0  0] [ 1  0]]

                   [[ 0 -2]
                    [ 0 -1]
                    [ 0  0]
                    [ 0  1]]])

(def s-pieces
                  [[[ 0  0] [ 1  0]
            [-1  1] [ 0  1]]

                   [[ 0 -1]
                    [ 0  0] [ 1  0]
                            [ 1  1]]])

(def z-pieces
          [[[-1  0] [ 0  0]
                    [ 0  1] [ 1  1]]

                           [[ 1 -1]
                    [ 0  0] [ 1  0]
                    [ 0  1]]])

(def j-pieces
          [[[-1  0] [ 0  0] [ 1  0]
                            [ 1  1]]

                   [[ 0 -1]
                    [ 0  0]
            [-1  1] [ 0  1]]

           [[-1 -1]
            [-1  0] [ 0  0] [ 1  0]]

                   [[ 0 -1] [ 1 -1]
                    [ 0  0]
                    [ 0  1]]])

(def l-pieces
          [[[-1  0] [ 0  0] [ 1  0]
            [-1  1]]

           [[-1 -1] [ 0 -1]
                    [ 0  0]
                    [ 0  1]]

                           [[ 1 -1]
            [-1  0] [ 0  0] [ 1  0]]

                   [[ 0 -1]
                    [ 0  0]
                    [ 0  1] [ 1  1]]])

(def t-pieces
          [[[-1  0] [ 0  0] [ 1  0]
                    [ 0  1]]

                   [[ 0 -1]
            [-1  0] [ 0  0]
                    [ 0  1]]

                   [[ 0 -1]
            [-1  0] [ 0  0] [ 1  0]]

                   [[ 0 -1]
                    [ 0  0] [ 1  0]
                    [ 0  1]]])

(def pieces
  {:i {:name :i :position [5 0] :coordinates (first i-pieces)}
   :j {:name :j :position [5 0] :coordinates (first j-pieces)}
   :l {:name :l :position [5 0] :coordinates (first l-pieces)}
   :o {:name :o :position [5 0] :coordinates (first o-pieces)}
   :s {:name :s :position [5 0] :coordinates (first s-pieces)}
   :t {:name :t :position [5 0] :coordinates (first t-pieces)}
   :z {:name :z :position [5 0] :coordinates (first z-pieces)}})
