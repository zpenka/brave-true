(ns brave-true.6-organization)

; Get name of current namespace
(ns-name *ns*)

; Quoting (ie 'inc) a form tells Clojure not to evaluate it and instead treat
; it as data
'inc

; Store objects with `def`
(def great-books ["East of Eden" "The Glass Bead Game"])

; That process is called 'interning a _var_', to get a map of interned vars:
(ns-interns *ns*)
; => {great-books #'user/great-books}

; to get the var
(get (ns-interns *ns*) 'great-books)
; => #'user/great-books

; full map
(ns-map *ns*)

; Uh oh
(def great-books ["The Power of Bees" "Journey to Upstairs"])
great-books
; => ["The Power of Bees" "Journey to Upstairs"]

; The var has been updated with the address of the new vector
; Clojure no longer holds the first one. This is a  name collision

; Clojure allows you to create namespaces to avoid collisions

; `create-ns` takes a symbol, creates a namespace with that name if it doesn't
; exist already, and returns it
(create-ns 'cheese.taxonomy)
; => #<Namespace cheese.taxonomy>

(ns-name (create-ns 'cheese.taxonomy))
; => cheese.taxonomy

; `in-ns` is more common because it creates the namespace if it doesn't exist
; and switches to it
(in-ns 'cheese.analysis)

; `refer` gives you fine-grained control over how you refer to objects in
; other namespaces.

; Always start a new namespace with (clojure.core/refer-clojure) to ensure you
; get the standard library and not have to use the entire symbol each time

; To create a private function that is not available to outside namespaces,
; use `defn-`
(defn- private-function
  "Just an example function that does nothing"
  [])

; Calling this from another namespace will throw an exception

; `alias`

; Use alias to shorten a namespace, but not import it
(alias 'taxonomy 'cheese.taxonomy)
