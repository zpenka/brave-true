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

; Abstraction Through Indirection

; In programming, indirection is a generic term for the mechanism a language
; employs so that one name can have multiple, related meanings.

; Polymorphism is one way that Clojure provides indirection. Polymorphic functions
; dispatch to different function bodies based on the type of the argument
; supplied

; For sequences, Clojure creates indirection by doing a kind of lightweight type
; conversion, producing a data structure that works with an abstraction's
; functions. Whenever Clojure expects a sequence-for example, when you call
; `map`, `first`, `rest`, `cons`- it calls the `seq` function on the data
; structure in question to obtain a data structure that allows for `first`,
; `rest` and `cons`

(seq '(1 2 3))
; (1 2 3)

(seq [1 2 3])
; (1 2 3)

(seq #{1 2 3})
; (1 2 3)

(seq {:name "Bill Compton" :occupation "Dead mopey guy"})
; ([:name "Bill Compton"] [:occupation "Dead mopey guy"])

; First, `seq` always returns a value that looks and behaves like a list
; this value is usually called a sequence

; Second, the seq of a map consists of two-element key-value vectors
; That's why `map` treats your maps like lists of vectors!

; Convert the seq back to a map by using `into`
(into {} (seq {:a 1 :b 2 :c 3}))
; {:a 1, :c 3, :b 2}

; The takeaway here is that it's powerful to focus on what we can _do_ with a
; data structure and to ignore, as much as possible, its implementation

; Programming to abstraction gives you power by letting you use libraries
; of functions on different data structures regardless of how those data
; structures are implemented

; Seq Function Examples

; map
(map inc [1 2 3])
; (2 3 4)

; map with multiple collections
(map str ["a" "b" "c"] ["A" "B" "C"])
; ("aA" "bB" "cC")

(def human-consumption    [8.1 7.3 6.6 5.0])
(def critter-consumption  [0.0 0.2 0.3 1.1])
(defn unify-diet-data
  [human critter]
  {:human human
   :critter critter})

(map unify-diet-data human-consumption critter-consumption)

; You can even pass map a collection of functions. You could use this if you
; wanted to perform a set of calculations on different collections of numbers

(def sum #(reduce + %))
(def avg #(/ (sum %) (count %)))
(defn stats
  [numbers]
  (map #(% numbers) [sum count avg]))

(stats [3 4 10])
; (17 3 17/3)

(stats [80 1 44 13 6])
; (144 5 144/5)

; Clojurists use `map` to retrieve the value associated with a keyword from a
; collection of map data structures. Because keywords can be used as functions
; you can do this succinctly
(def identities
  [{:alias "Batman" :real "Bruce Wayne"}
   {:alias "Spider-Man" :real "Peter Parker"}
   {:alias "Santa" :real "Your Mom"}
   {:alias "Easter Bunny" :real "Your Dad"}])

(map :real identities)
; ("Bruce Wayne" "Peter Parker" "Your Mom" "Your Dad")

; `reduce`

; `reduce` is typically used to process each element in a sequence to build a
; result

; Some more ways you can use it:

; 1. transform a map's values, producing a new map with the same keys but with
; updated values
(reduce (fn [new-map [key values]]
          (assoc new-map key (inc val)))
        {}
        {:max 30 :min 10})
; {max 31 :min 11}

; 2. filter out keys from a map based on their value
(reduce (fn [new-map [key val]]
          (if (> val 4)
            (assoc new-map key val)
            new-map))
        {}
        {:human 4.1
         :critter 3.9})
; {:human 4.1}

; `take`, `drop`, `take-while`, and `drop-while`

; `take` and `drop` both take two arguments: a number and a sequence
; `take` returns the first n elements of the sequence
; `drop` returns the sequence with the first n elements removed

(take 3 [1 2 3 4 5 6 7 8 9 10])
; (1 2 3)

(drop 3 [1 2 3 4 5 6 7 8 9 10])
; (4 5 6 7 8 9 10)

; `take-while` and `drop-while` take a predicate function (return value is
; evaluated for truth or falsity) to determine when it should stop taking or
; dropping

(def food-journal
  [{:month 1 :day 1 :human 5.3 :critter 2.3}]
  [{:month 1 :day 2 :human 5.1 :critter 2.0}]
  [{:month 2 :day 1 :human 4.9 :critter 2.1}]
  [{:month 2 :day 2 :human 5.0 :critter 2.5}]
  [{:month 3 :day 1 :human 4.2 :critter 3.3}]
  [{:month 3 :day 2 :human 4.0 :critter 3.8}]
  [{:month 4 :day 1 :human 3.7 :critter 3.9}]
  [{:month 4 :day 2 :human 3.7 :critter 3.6}])

; With `take-while`, we could retrieve January's and February's data
; `take-while` traverses the sequence, appluying the predicate function to each
; element

(take-while #(< (:month %) 3) food-journal)

; When `take-while` reaches the first March entry, the anonymous function returns
; `false`, and `take-while` returns the sequence of every element it tested
; until that point

; The same idea applies with `drop-while` except that it keeps dropping elements
; until one tests true

(take-while #(< (:month %) 4)
            (drop-while #(< (:month %) 2) food-journal))

; This will drop all the January entries, and then use `take-while` on the result
; to keep taking entries until it reaches the first April one

; `filter` and `some`

; Use `filter` to return all elements of a sequence that test true for a predicate
; function. Here are all the journal entries where human consumption is less than
; 5 liters

(filter #(< (:human %) 5) food-journal)

; Often you want to know whether a collection contains any values that test
; true for a predicate function
; The `some` function will do that, returning the first truthy value returned
; by the predicate function

(some #(> (:critter %) 5) food-journal)
; nil

(some #(> (:critter %) 3) food-journal)
; true

; `sort` and `sort-by`

; You can sort elements in ascending order with `sort`
(sort [3 1 2])
; (1 2 3)

; If your sorting needs are more complicated, use `sort-by`, which allows you to
; apply a function (sometimes called a key function) to the elements of a sequence
; and use the values it returns to determine the sort order

(sort-by count ["aaa" "c" "bb"])
; ("c" "bb" "aaa")

; `sort` would have sorted them in alphabetical order, instead we sorted by the
; length of each element via `count`

; `concat`

; Finally, `concat` simply appends the members of one sequence to the end of
; another
(concat [1 2] [3 4])
; (1 2 3 4)

; Lazy Seqs

; Many functions, including `map` and `filter`, return lazy seqs. Lazy seqs
; members aren't computed until you try to access them. Computing a seq's
; members is called _realizing_ the seq

; Deferring computation until the moment it's needed makes programs more efficient
; and it has the benefit of allowing you to construct infinite sequences

(def vampire-database
  {0 {:makes-blood-puns? false :has-pulse? true :name "McFishwich"}
   1 {:makes-blood-puns? false :has-pulse? true :name "McMackson"}
   2 {:makes-blood-puns? true :has-pulse? false :name "Damon Salvatore"}
   3 {:makes-blood-puns? true :has-pulse? true :name "Mickey Mouse"}})

(defn vampire-related-details
  [social-security-number]
  (Thread/sleep 1000)
  (get vampire-database social-security-number))

(defn vampire?
  [record]
  (and (:makes-blood-puns? record)
       (not (:has-pulse? record))
       record))

(defn identify-vampire
  [social-security-numbers]
  (first (filter vampire?
                 (map vampire-related-details social-security-numbers))))

; Infinite Sequences

(concat (take 8 (repeat "na")) ["Batman!"])

; `repeat` returns an infinite sequence where each element is it's argument, in
; this case, "na"

; You can also use `repeatedly`, which will call the provided function to
; generate each element in the sequence

(take 3 (repeatedly (fn [] (rand-int 10))))

(defn even-numbers
  ([] (even-numbers 0))
  ([n] (cons n (lazy-seq (even-numbers (+ n 2))))))

(take 10 (even-numbers))
