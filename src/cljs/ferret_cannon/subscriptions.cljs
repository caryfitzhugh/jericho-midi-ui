(ns jericho-midi.subscriptions
  (:require-macros [reagent.ratom :refer [reaction]]
                   [jericho-midi.view-macros :refer [fc-sub]])
  (:require [re-frame.core :as rf :refer [reg-sub]]
            [jericho-midi.teams.subscriptions]
            [jericho-midi.auth.subscriptions]
            [jericho-midi.access.subscriptions]
            ))

(fc-sub active-panel
  (fn [db _] (:panel (:navigation db))))

(fc-sub active-section
  (fn [db _] (:section (:navigation db))))

(fc-sub loading?
  (fn [db _]
    (:loading? db)))

(fc-sub error
  (fn [db _] (:error db)))

(fc-sub teams
  (fn [db _] (:teams db)))

(fc-sub organizations
  (fn [db _] (:organizations db)))

(fc-sub current-team-id
  (fn [db _]
    (:current-team db)))

(fc-sub current-team
  :<- [:teams]
  :<- [:current-team-id]
  (fn [[teams current-team-id] _]
    (get teams current-team-id)))

(fc-sub users
  (fn [db _]
    (reduce (fn [all [id user]]
      (let [initials (clojure.string/upper-case
                       (clojure.string/join
                         (take 2
                           (map first (clojure.string/split (:username user) " ")))))
            amended-user (assoc user :initials initials)
            ]
       (assoc all id amended-user)))
      {}
      (:users db))))
(fc-sub current-user-data
  (fn [db _] (:current-user db)))

(fc-sub current-user-token
  :<- [:current-user-data]
  (fn [cud _] (:access-token cud)))

(fc-sub current-user
  :<- [:current-user-data]
  (fn [cud _]
    (:identity cud)))

(fc-sub current-user-initials
  :<- [:current-user]
  (fn [cuser _]
    (let [names (list (:given-name cuser) (:family-name cuser))]
      (clojure.string/join
        (map (fn [nm]
          (clojure.string/upper-case (first nm)))
          (filter identity names))))))

(fc-sub labels (fn [db _] (:labels db)))

(fc-sub layout-view-settings
  (fn [db _] (:layout (:view-settings db))))

(fc-sub layout-view-setting
  :<- [:layout-view-settings]
  (fn [layout-settings [_ key-val]]
    (key-val layout-settings)))

(fc-sub [:layout show-team-selection]
  :<- [:layout-view-setting :show-team-selection]
  (fn [lvs _] lvs))

(fc-sub [:layout show-profile]
  :<- [:layout-view-setting :show-profile]
  (fn [lvs _] lvs))

(fc-sub current-locale
  (fn [db _] (:current-locale db)))
