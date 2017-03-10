(ns jericho-midi.util
  (:require [clojure.string :as s]
            [clojure.walk :as cw]
            [clojure.zip :as zip]))

; TODO: regex is slow, should try iterating the string.
(defn dasherize-string
  "Converts an underscored or camelized string
  into an dasherized one."
  [s]
  (when s
    (-> s
        (s/replace #"([A-Z][a-z]+)" (fn [[match c]]
                                      (if c (str "-" (s/lower-case c)) "")))
        (s/replace #"([A-Z]+)" "-$1")
        (s/replace #"[-_\s]+" "-")
        (s/replace #"^-" ""))))

(defn camelize-string
  "Converts a string from dash-case to camelCase."
  [^String s]
  (when s
    (some-> s
            (s/replace #"[-_\s]+(.)?"
                       (fn [[match c]]
                         (if c (s/upper-case c) ""))))))

;(time (dotimes [x 400000] (dasherize-keyword :clientOSVersion)))
;"Elapsed time: 915.063554 msecs"
(defn dasherize-keyword
  [k]
  (when k
    (keyword (dasherize-string (name k)))))

;(time (dotimes [x 400000] (camelize-keyword :client-OS-version)))
;"Elapsed time: 537.186386 msecs"
(defn camelize-keyword
  [k]
  (when k
    (keyword (camelize-string (name k)))))

(defn camelize-map-1 [m]
  (into {} (map (fn [[k v]]
                  [(camelize-keyword k) v]) m)))

(defn dasherize-map-1 [m]
  (into {} (map (fn [[k v]]
                  [(dasherize-keyword k) v]) m)))

(defn camelize-map-recur [m]
  (into {} (map (fn [[k v]]
                  [(camelize-keyword k) (if (map? v) (camelize-map-recur v) v)]) m)))

(defn dasherize-map-recur [m]
  (into {} (map (fn [[k v]]
                  [(dasherize-keyword k) (if (map? v) (dasherize-map-recur v) v)]) m)))

(defn map-zipper [m]
  (zip/zipper
    (fn [x] (or (map? x) (map? (nth x 1))))
    (fn [x] (seq (if (map? x) x (nth x 1))))
    (fn [x children]
      (if (map? x)
        (into {} children)
        (assoc x 1 (into {} children))))
    m))

(defn edit-map-keys-zip [m f]
  (loop [loc (map-zipper m)]
    (if (zip/end? loc)
      (zip/root loc)
      (recur (zip/next
               (if (vector? (zip/node loc))
                 (zip/edit loc #(do [(f (first %)) (second %)]))
                 loc))))))

(defn camelize-map-zip [m]
  (edit-map-keys-zip m camelize-keyword))

(defn dasherize-map-zip [m]
  (edit-map-keys-zip m dasherize-keyword))


(defn dasherize-keys
  "Recursively transforms all map keys from strings to keywords."
  {:added "1.1"}
  [m]
  (let [convert-key (fn [[k v]]

                      (if (string? k)
                        [(keyword (dasherize-string k)) v]
                        (if (keyword? k)
                          [(dasherize-keyword k) v]
                          [k v])))]
    ;; only apply to maps
    (clojure.walk/postwalk (fn [x] (if (map? x) (into {} (map convert-key x)) x)) m)))
