(ns test.jericho-midi.events-test
  (:require
   [jericho-midi.events :as events]
   [test.jericho-midi.utils :as u]
   [devcards.core :refer-macros [deftest]]
   [cljs.test :refer-macros [is testing run-tests]]))

(deftest initialize
  (is (events/initialize-db nil)))
