; Chapter 3: Do Things: A Clojure Crash Course
(ns do-things.core)

; The Shire's Next Top Model
(def asym-hobbit-body-parts
  [{:name "head" :size 3}
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

(defn matching-part
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   :size (:size part)})

(defn symmetrize-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  (loop [remaining-asym-parts asym-body-parts
         final-body-parts []]
    (if (empty? remaining-asym-parts)
      final-body-parts
      (let [[part & remaining] remaining-asym-parts]
        (recur remaining
               (into final-body-parts
                     (set [part (matching-part part)])))))))

(defn better-symmetrize-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  (reduce (fn [final-body-parts part]
            (into final-body-parts (set [part (matching-part part)])))
          []
          asym-body-parts))

(defn hit
  "Hits a body part at random when passed a vector of asymetical body parts"
  [asym-body-parts]
  (let [sym-parts (better-symmetrize-body-parts asym-body-parts)
        body-part-size-sum (reduce + (map :size sym-parts))
        target (rand body-part-size-sum)]
    (loop [[part & remaining] sym-parts
           accumulated-size (:size part)]
      (if (> accumulated-size target)
          part
          (recur remaining (+ accumulated-size (:size (first remaining))))))))

; Chapter 3 Exercises

; 1. Use `str`, `vector`, `list`, `hash-map` and `hash-set` functions
(str "Hello " "there.")
(vector 1 2 3 4)
(list 1 2 3 4)
(hash-map :a 1 :b 2)
(hash-set 1 1 2 3 4)

; 2. Write a function that takes a number and adds 100 to it
(defn add-hundred
  "Adds 100 to the number passed"
  [x]
  (+ 100 x))

; 3. Write a function, `dec-maker` that works exactly like the function
;    `inc-maker`, except with subtraction
(defn dec-maker
  "Create a custom decrementor"
  [dec-by]
  #(- % dec-by))
