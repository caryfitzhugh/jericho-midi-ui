(ns test.jericho-midi.utils
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [clojure.walk :refer [postwalk]]
            [cljs.test :refer [deftest is testing run-tests]]))

(comment defmacro test-subscriber
         [test-name input func output]
         `(cljs.test/deftest ~test-name
            (is (= ~output
                   (~func ~input)))))

(defn sub-test
  [input sub-func output]
  (is (= output
         (sub-func input))))

;;https://github.com/cjohansen/hiccup-find
(defn hiccup-tree [tree]
  (tree-seq #(or (vector? %) (seq? %)) seq tree))

(defn hiccup-nodes
  "Takes a hiccup tree and returns a list of all the nodes in it.

[:html
 [:body
  '([:p \"Hey\"]
    [:p \"There\"])]]

turns into

([:html [:body '([:p \"Hey\"] [:p \"There\"])]]
 [:body '([:p \"Hey\"] [:p \"There\"])]
 [:p \"Hey\"]
 [:p \"There\"])"
  [root]
  (->> root
       hiccup-tree
       (filter vector?)))

(defn split-hiccup-symbol
  "Split the hiccup 'tag name' symbol into the tag name, class names and id"
  [symbol]
  (re-seq #"[:.#][^:.#]+" (str symbol)))

(defn hiccup-symbol-matches?
  "Determine if a query matches a single hiccup node symbol"
  [q symbol]
  (set/subset? (set (split-hiccup-symbol q))
               (set (split-hiccup-symbol symbol))))

(defn hiccup-find
  "Return the node from the hiccup document matching the query, if any.
   The query is a vector of hiccup symbols; keywords naming tag names, classes
   and ids (either one or a combination) like :tag.class.class2#id"
  [query root]
  (if (and (seq root) (seq query))
    (recur (rest query)
           (->> root
                (hiccup-nodes)
                (filter #(hiccup-symbol-matches? (first query) (first %)))))
    root))

(def inline-elements
  #{:b :big :i :small :tt :abbr :acronym :cite :code :dfn :em :kbd
    :strong :samp :var :a :bdo :br :img :map :object :q :script
    :span :sub :sup :button :input :label :select :textarea})

(defn inline? [node]
  (and (vector? node) (contains? inline-elements (first node))))

(defn hiccup-text
  "Return only text from the hiccup structure; remove
   all tags and attributes"
  [tree]
  (->> (hiccup-tree tree)
       (reduce (fn [text node]
                 (cond
                   (inline? node) text
                   (vector? node) (str/replace text #"(.+)\n?$" #(str (second %1) "\n"))
                   (string? node) (str text node)
                   (number? node) (str text node)
                   :else text)) "")))

(defn hiccup-string
  "Return the hiccup-text as a one-line string; removes newlines and collapses
   multiple spaces."
  [tree]
  (-> tree
      hiccup-text
      (str/replace #"\n" " ")
      (str/replace #"\s+" " ")))