(ns jericho-midi.persistence
  (:require-macros [jericho-midi.view-macros :refer [fc-evt fc-sub]])
  (:require [re-frame.core :as rf]
            [cljs.reader]))

(def persisted-keys [:auth :current-user])
(def ls-key "jericho-midi")                          ;; localstore key

(fc-sub persistence-stream
  (fn [db _]
    (select-keys db persisted-keys)))

(fc-sub store-persistence-stream
  :<- [:persistence-stream]
  (fn [stream _]
    (.setItem js/localStorage ls-key (pr-str stream))
    stream))

(fc-evt load-persisted-stream
  (fn [db _]
    (let [stored-str (.getItem js/localStorage ls-key)
          loaded-hsh (if stored-str (cljs.reader/read-string stored-str)
                        {})]
      (merge db loaded-hsh))))
