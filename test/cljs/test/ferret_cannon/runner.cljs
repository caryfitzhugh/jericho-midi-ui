(ns test.jericho-midi.runner
  (:require [doo.runner :as doo :refer-macros [doo-tests]]
            [test.jericho-midi.database-test]
            [test.jericho-midi.events-test]
            ;;[test.jericho-midi.views-test]
            [test.jericho-midi.subscriptions-test])
  (:require-macros [cljs.test :refer [run-tests]]))
;; https://www.packtpub.com/books/content/testing-your-application-cljstest

;; This isn't strictly necessary, but is a good idea depending
;; upon your application's ultimate runtime engine.
(enable-console-print!)

(doo-tests 'test.jericho-midi.database-test
           'test.jericho-midi.events-test
           ;;'test.jericho-midi.views-test
           'test.jericho-midi.subscriptions-test)
