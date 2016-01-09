(ns brave-true.core
  (:gen-class))

; Chapter 1: Building, Running, and the REPL

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "I'm a little teapot!"))

; Chapter 2: How to Use Emacs, an Excellent Clojure Editor

; Chapter 3: Do Things: A Clojure Crash Course

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

; Chapter 4: Core Functions In Depth

; Note: This chapter has a lot of semantic, abstract concepts that are new or
;       not solid in my understanding of them. Thus, I will be taking notes in
;       comments like this

; Programming to Abstraction

; Comparison of Clojure to Elisp to illustrate programming to abstraction
; elsip: `mapcar` -> derive new list, `maphash` -> map over hashmap
; Clojure: `map` -> derive new list or map over a map

; Clojure defines `map` and `reduce` functions in terms of _sequence abstraction_
; not in terms of specific data structures.

; As long as a data structure responds to the core sequence operations (`first`,
; `rest`, and `cons`), it will work with `map`, `reduce`, and oodles of other
; sequence functions for free. That is what is meant by programming to
; abstraction, and it is a central tenet of Clojure philosophy

; Treating Lists, Vectors, Sets, and Maps as Sequences

; In Clojure, a sequence refers to a collection of elements organized in linear
; order.

; Note that sequences are not defined in terms of lists, vectors, sets or maps.
; Clojure is designed to allow us to think and program in such abstract terms
; as much as possible, typically by implementing functions in terms of data
; structures abstractions

; Terms like 'seq library', or 'seq functions' refer to functions that operate
; on sequences

; If the core sequence functions `first`, `rest` and `cons` work on a data
; structure, you can say the data structure implements the sequence abstraction

(defn titleize
  [topic]
  (str topic " for the Brave and True"))

; Vectors
(map titleize ["Hampsters" "Ragnarok"])

; Lists
(map titleize '("Empathy" "Decorating"))

; Sets
(map titleize #{"Elbows" "Soap Carving"})

; Maps
(map #(titleize (second %)) {:uncomfortable-thing "Winking"})

; `first`, `rest`, and `cons`

; It doesn't matter how a particular data structure is implemented: when it
; comes to using seq functions on a data structure, all Clojure asks is "can I
; `first`, `rest`, and `cons` it?" If yes, then you can use the seq lib with
; that data structure
