(ns jericho-midi.core
    (:require [reagent.core :as reagent :refer [atom]]
              [re-frame.core :as rf]
              [jericho-midi.fx]
              [day8.re-frame.http-fx]
              [jericho-midi.subscriptions]
              [jericho-midi.events]
              [jericho-midi.persistence]
              [jericho-midi.views :as views]
              [jericho-midi.routes :as routes]))

(defn mount-root []
  (reagent/render [views/current-page] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [:initialize-db])
  (rf/dispatch-sync [:load-persisted-stream])
  ;; Initialize after the localstorage has been loaded...
  (routes/initialize!)
  (js/setInterval (fn [] (rf/dispatch [:update-time] )) 1000)
  (mount-root)

  (let [loading-el (.getElementById js/document "loading_overlay")]
    (set! (.-className loading-el) (str (.-className loading-el) " hide"))))
