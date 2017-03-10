(ns jericho-midi.events
  (:require-macros [jericho-midi.view-macros :refer [fc-evt]])
  (:require [ajax.core :as ajax :refer [GET]]
            [jericho-midi.access.events]
            [jericho-midi.auth.events]
            [re-frame.core :as rf]
            [cljs.spec     :as s]
            [cljs-time.core :as ctime]
            [accountant.core :as accountant]
            [jericho-midi.database :as db]))

(defn check-and-throw
  "throw an exception if db doesn't match the spec."
  [a-spec db]
  (when-not (s/valid? a-spec db)
    (throw (ex-info (str "spec check failed: " (s/explain-str a-spec db)) {}))))

(def check-spec-interceptor (rf/after (partial check-and-throw :jericho-midi.database/db-spec)))

;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
(defn load-ev-activity- [{:keys [db]} [_ response]]
  (if (:ev-activity (:test db))
    {:db db}
    ;; If not present, then load
    { :http-xhrio {
                :method :get
                :uri  "https://data.nasa.gov/resource/q8u9-7uq7.json",
                :response-format (ajax/json-response-format {:keywords? true})
                :on-success [:test/ev-activity-loaded]
                :on-failure [:test/ev-activity-failed]}}))

(rf/reg-event-fx
  :test/ensure-ev-activity-loaded
  load-ev-activity-)

(defn ev-activity-loaded- [{:keys [db]} [_ response]]
    {:db (assoc-in db [:test :ev-activity] response)})

(rf/reg-event-fx
  :test/ev-activity-loaded
  ev-activity-loaded-)

(defn ev-activity-failed- [{:keys [db]} [_ response]]
    {:db (assoc-in db [:test :ev-activity] [])})

(rf/reg-event-fx
  :test/ev-activity-failed
  ev-activity-failed-)

(defn load-outgas- [{:keys [db]} [_ response]]
  (if (:outgas (:test db))
    {:db db}
    ;; If not present, then load
    { :http-xhrio {
                :method :get
                :uri  "https://data.nasa.gov/resource/gymh-eyc2.json"
                :response-format (ajax/json-response-format {:keywords? true})
                :on-success [:test/outgas-loaded]
                :on-failure [:test/outgas-failed]}}))

(rf/reg-event-fx
  :test/ensure-outgas-loaded
  load-outgas-)

(defn outgas-loaded- [{:keys [db]} [_ response]]
    {:db (assoc-in db [:test :outgas] response)})

(rf/reg-event-fx
  :test/outgas-loaded
  outgas-loaded-)

(defn outgas-failed- [{:keys [db]} [_ response]]
    {:db (assoc-in db [:test :outgas] [])})

(rf/reg-event-fx
  :test/outgas-failed
  outgas-failed-)

;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;
;;;;











(fc-evt initialize-db (fn [_ _] db/default-db))

(fc-evt update-time (fn [db _] (assoc db :time (ctime/now))))

(fc-evt set-active-panel
  (fn [db [_ active-panel active-section]]
    (-> db
      (assoc-in [:view active-panel] {})
      (assoc :navigation {:section active-section :panel active-panel}) )))

(fc-evt
  set-active-section
  (fn [db [_ active-section]]
    (-> db
      (assoc-in [:view (:active-section (:navigation db))] {})
      (assoc-in [:navigation :section] active-section))))

(fc-evt
  close-modal
  (fn [db [_ active-section]]
    (assoc-in db [:navigation :modal] nil)))

(fc-evt
  navigate
  (fn [db [_ path]]
    (accountant/navigate! path)
    db))

(fc-evt [:layout set-view-settings]
  (fn [db [_ key value]]
    (assoc-in db [:view-settings :layout key] value)))

(fc-evt [:layout toggle-team-selection]
  (fn [db [_ key value]]
    (let [path [:view-settings :layout :show-team-selection]
          current (get-in db path)]
      (assoc-in db [:view-settings :layout :show-team-selection] (not current)))))

(fc-evt [:layout toggle-show-profile]
  (fn [db [_ key value]]
    (let [path [:view-settings :layout :show-profile]
          current (get-in db path)]
      (assoc-in db [:view-settings :layout :show-profile] (not current)))))
