(ns chapter-03.core
  (:gen-class))

(def asym-hobbit-body-parts [{:name "head" :size 3}
                             {:name "left-eye" :size 1}
                             {:name "left-ear" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "left-shoulder" :size 3}
                             {:name "left-upper-arm" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "left-forearm" :size 3}
                             {:name "abdomen" :size 6}
                             {:name "left-kidney" :size 1}
                             {:name "left-hand" :size 2}
                             {:name "left-knee" :size 2}
                             {:name "left-thigh" :size 4}
                             {:name "left-lower-leg" :size 3}
                             {:name "left-achilles" :size 1}
                             {:name "left-foot" :size 2}])

(defn matching-part-generator
  [parts]
  (fn [part]
    (map (fn [new-part]
            (hash-map :name (clojure.string/replace (:name part) #"^left-" (str new-part "-"))
                      :size (:size part)))
         parts)))

(defn matching-part
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   :size (:size part)})

(defn symmetrize-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts parts-generator]
  (reduce (fn [final-body-parts part]
             (into final-body-parts
                   (set (conj (parts-generator part) part))))
          []
          asym-body-parts))

(defn hit
  [asym-body-parts]
  (let [generator (matching-part-generator ["right"])
        sym-parts (symmetrize-body-parts asym-body-parts generator)
        body-part-size-sum (reduce + (map :size sym-parts))
        target (rand body-part-size-sum)]
    (loop [[part & remaining] sym-parts
           accumulated-size (:size part)]
      (if (> accumulated-size target)
        part
        (recur remaining
               (+ accumulated-size (:size (first remaining))))))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [{:keys [name size]} (hit asym-hobbit-body-parts)]
    (println (str "Hit hobbit in '" name "' of size '" size "'"))))
