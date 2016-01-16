; Chapter 5: Functional Programming
(ns brave-true.5-functional-programming)

; Pure Functions

; Every function up until this point, except `println` and `rand` have been pure
; functions.

; Two Rules to be 'pure'
; 1. Always returns the same results given the same arguments. Also called
;    'referential transparency'.

; 2. Never causes side-effects. A pure function can never make changes that are
;    observable outside of itself.

; These qualities make it easier for you to reason about your programs because
; the functions are completely isolated, unable to impact other parts of your
; system. You never find yourself wondering "What could break if I call this
; function"

; Referential transparency

; In order to uphold referential transparency, pure functions rely on two things
; 1. Their own arguments
; 2. Immutable values to determine their return value. Mathematical functions of
;    all kinds are referentially transparent.

; If a function reads from a file or a database, it's not referentially
; transparent because the files contents can change

; Reality is largely referentially transparent. Gravitation force is the return
; value of calling that function of gravity on two objects.

; No Side Effects

; To perform a side effect is to change the association between a name and its
; value within a given scope.

; Of course, programs must have side effects. It writes to a disk, which changes
; the association between a filename and a collection of diskspace. it changes
; the RGB values of your monitor's pixels, etc.

; However, side effects can be harmful. They introduce uncertainty about what
; the names in your code are referring to. This leads to situations where it's
; very difficult to trace why and how a name came to be associated with a value
; which makes it hard to debug the program. When a function that doesn't cause
; side effects is cause, only the relationship between input and output must
; be considered. There is no worry about other changes that could be rippling
; through the system.

; Living with Immutable data

; Immutable data structures ensure that your code will not have side effects.
; As we now know with all our heart, this is a good thing.

; Recursion
(defn sum
  ([vals] (sum vals 0))
  ([vals accumulating-total
    (if (empty? vals)
        accumulating-total
        (sum (rest vals) (+ (first vals) accumulating-total)))]))

; Like all recursive solutions, this function checks the argument it's
; processing against a base condition. In this case, we check whether `vals` is
; empty. If it is, we know that we've processed all the elements in the
; collection, so we return `accumulating-total`.

; If `vals` isn't empty, it means we're still working our way through the
; sequence, so we recursively call `sum`, passing it two arguments: the tail of
; vals with (rest vals) and the sum of the first element of `vals` plus the
; accumulating total with `(+ (first vals) accumulating-total)`. In this way,
; we build up `accumulating-total` and at the same time reduce `vals` until it
; reaches the base case of an empty collection

; Note: use `recur` when doing recursion for performance reasons. Clojure
; doesn't provide tail call optimization, so this is the language construct for
; performant recursion

(defn sum-recur
  ([vals] (sum vals 0))
  ([vals accumulating-total
    (if (empty? vals)
      accumulating-total
      (recur (rest vals) (+ (first vals) accumulating-total)))]))

; Using recur will help mitigate stack overflows that you can cause if you don't

; Functional Composition Instead of Attribute Mutation
(require '[clojure.string :as s])
(defn clean
  [text]
  (s/replace (s/trim text) #"lol" "LOL"))

(clean "My boa constrictor is so sassy lol!   ")
; "My boa constrictor is so sassy LOL!"

; Here, pure functions' return values are used as arguments to larger functions
; This is called function composition, and it is used instead of mutating
; 'ans' and 'total' variables (side effects!)

; Recursion also does exactly this, except it is passing its return value to
; itself

; `comp`

; It's always safe to compose pure functions. Function composition is so common
; in Clojure that there is a built-in function, `comp` which creates a new
; function from the composition of any number of functions
((comp inc *) 2 3)
; 7

; Here you create an anonymous function by composing the `inc` and `*` functions
; Then, you immediately apply this function to the arguments `2` and `3`. The
; function multiplies 2 by 3 and then increments the result

(fn [a b] (inc (* a b)))

; That is the same thing without `comp`. But `comp` is more elegent because
; it uses less code to convey more meaning. When you see `comp`, you immediately
; know that the resulting function's purpose is to combine existing functions
; in a well-known way

; `memoize`

; Clojure has built-in memoization for pure functions.
; Memoization lets you take advantage of referential transparency by storing
; the arguments passed to a function and the return value of the function
; Subsequent calls to the function with the same arguments can return the result
; immediately

(defn sleep-identity
  "Returns the given value after 1 second!"
  [x]
  (Thread/sleep 1000)
  x)

(sleep-identity "Mr. Fantastico")
; "Mr. Fantastico" after 1 second

(sleep-identity "Mr. Fantastico")
; "Mr. Fantastico" after 1 second

; However, if you create a memoized version of `sleep-identity` with `memoize`
; only the first call waits one second

(def memo-sleepy-identity (memoize sleep-identity))

(sleep-identity "Mr. Fantastico")
; "Mr. Fantastico" after 1 second

(sleep-identity "Mr. Fantastico")
; "Mr. Fantastico" immediately

; `memoize` is invaluable for functions that do expensive computations, or make
; network requests
