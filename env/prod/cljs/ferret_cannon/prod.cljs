(ns jericho-midi.prod
  (:require [jericho-midi.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
