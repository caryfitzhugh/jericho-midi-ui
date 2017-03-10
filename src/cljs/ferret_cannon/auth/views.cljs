(ns jericho-midi.auth.views
  (:require [jericho-midi.views.utils :as utils :refer [tr active-section?] ]
            [jericho-midi.routes :as r]
            [re-frame.core :as rf])
  (:require-macros [jericho-midi.view-macros :refer [evt item-list fc-view]])
  )

(fc-view loading [] [:div])

(fc-view logging-out []
  [:div.layout-fullscreen
    [:div.content
      [:h3 "Logging out..."]]])

(fc-view login-required []
  [:div.layout-fullscreen
    [:div.content.centered
      [:h3 "You are not logged in"]
      [:a.btn.btn-lg.btn-primary {:on-click (evt :auth/request-login)}
        "Log in"]]])

(def sections
 {:loading
    [loading]
  :logging-out
    [logging-out]
  :login-required
    [login-required]})

(fc-view root []
  section [:active-section]
  (or (get sections section) [:h1.alert "Unknown Auth Section: " section]))
