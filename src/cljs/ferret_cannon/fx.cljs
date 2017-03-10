(ns jericho-midi.fx
    (:require
              [accountant.core :as accountant]
              [jericho-midi.routes :as routes]
              [re-frame.core :as rf]))
(rf/reg-fx
  :redirect-to
  (fn redirect-to-effect [redirect-to]
    (accountant/navigate! redirect-to)
    nil))
