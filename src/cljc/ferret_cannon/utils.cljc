(ns jericho-midi.utils)

(defn select-values
  [values keys]
  (remove nil? (reduce #(conj %1 (values %2)) [] keys)))
