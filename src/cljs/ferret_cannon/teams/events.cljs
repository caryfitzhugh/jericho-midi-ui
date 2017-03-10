(ns jericho-midi.teams.events
  (:require [ajax.core :refer [GET]]
            [re-frame.core :as rf]
            [jericho-midi.database :as db]))

(comment
  rf/reg-event-db
  :layout/set-view-settings
  (fn [db [_ key value]]
    (assoc-in db [:view-settings :layout key] value)))
