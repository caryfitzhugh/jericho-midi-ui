(ns jericho-midi.access.events
  (:require [ajax.core :refer [GET]]
            [re-frame.core :as rf]
            [cljs.spec     :as s]
            [jericho-midi.routes :as r]
            [accountant.core :as accountant]))

(rf/reg-event-db
  :access/invite-user-show
  (fn [db _ ]
    (let [show? (rf/subscribe [:access/invite-user-show?])]
      (r/replace-query-parameter! :invite-user (not @show?))
    db)))

(rf/reg-event-db
  :access/set-invite-user-show
  (fn [db [_ v]]
    (assoc-in db [:view :access :invite-user :show] v)))

(rf/reg-event-db
  :access/create-label-show
  (fn [db _ ]
    (let [show? (rf/subscribe [:access/create-label-show?])]
      (r/replace-query-parameter! :create-label (not @show?))
    db)))

(rf/reg-event-db
  :access/set-create-label-show
  (fn [db [_ v]]
    (assoc-in db [:view :access :create-label :show] v)))

(rf/reg-event-db
  :access/toggle-details-user
  (fn [db [_ toggle-id]]
    (let [opened (toggle-id @(rf/subscribe [:access/show-details-users]))]
     (assoc-in db [:view :access :show-details :users toggle-id] (not opened)))))

(rf/reg-event-db
  :access/cancel-details-user
  (fn [db [_ toggle-id]]
    (assoc-in db [:view :access :show-details :users toggle-id] false)))

(rf/reg-event-db
  :access/toggle-details-label
  (fn [db [_ toggle-id detail-type]]
    (let [opened (toggle-id @(rf/subscribe [:access/show-details-labels]))
          outcome (cond
                    (and opened (= opened detail-type)) nil
                    :default detail-type)]
     (assoc-in db [:view :access :show-details :labels toggle-id] outcome))))

(rf/reg-event-db
  :access/cancel-details-label
  (fn [db [_ toggle-id]]
    (assoc-in db [:view :access :show-details :labels toggle-id] false)))

(rf/reg-event-db
  :access/modify-details
  (fn [db [_ toggle-type toggle-id data-key value]]
    (assoc-in db [:view :access :delta-details toggle-type toggle-id data-key] value)))

(rf/reg-event-db
  :access/save-label-details
  (fn [db [_ value]]
    (js/alert "We need to take the modifications and submit them via AJAX")
    ;; REG-EVENT-FX
    ;;;; Return :dispatch!!!
    (assoc-in db [:view :access :editing-label-details value] nil)))

(rf/reg-event-db
  :access/users-filter-update
  (fn [db [_ value]]
    (r/replace-query-parameter! :search value)
    db))

(rf/reg-event-db
  :access/set-users-filter
  (fn [db [_ value]]
    (assoc-in db [:view :access :users-filter] value)))

(rf/reg-event-db
  :access/labels-filter-update
  (fn [db [_ value]]
    (r/replace-query-parameter! :search value)
    db))

(rf/reg-event-db
  :access/set-labels-filter
  (fn [db [_ value]]
    (assoc-in db [:view :access :labels-filter] value)))
