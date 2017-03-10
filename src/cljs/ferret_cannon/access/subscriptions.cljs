(ns jericho-midi.access.subscriptions
  (:require-macros [reagent.ratom :refer [reaction]]
                   [jericho-midi.view-macros :refer [fc-sub]])
  (:require [re-frame.core :as rf :refer [reg-sub]]
            [jericho-midi.utils :as u]))


(fc-sub [:test outgas-data]
        (fn [db _] (:outgas (:test db))))

(fc-sub [:test ev-activity-data]
        (fn [db _] (:ev-activity (:test db))))








(fc-sub [:access view] (fn [db _] (:access (:view db))))

(fc-sub [:access invite-user]
  :<- [:access/view]
  (fn [av _] (:invite-user av)))

(fc-sub [:access invite-user-show?]
  :<- [:access/invite-user]
  (fn [iu _] (:show iu)))

(fc-sub [:access create-label]
  :<- [:access/view]
  (fn [av _] (:create-label av)))

(fc-sub [:access create-label-show?]
  :<- [:access/create-label]
  (fn [iu _] (:show iu)))

(fc-sub
  [:access show-details]
  :<- [:access/view]
  (fn [av _] (:show-details av)))

(fc-sub
  [:access show-details-labels]
  :<- [:access/show-details]
  (fn [av _] (:labels av)))

(fc-sub
  [:access show-details-users]
  :<- [:access/show-details]
  (fn [av _] (:users av)))

(fc-sub
  [:access delta-details]
  :<- [:access/view]
  (fn [[av] _] (:delta-details av)))

(fc-sub
  [:access delta-composite-labels]
  (fn [_ [_ label-id]]
    (let [labels (rf/subscribe [:access/labels])
          delta-details (rf/subscribe [:access/delta-details])]
      (merge (get @labels label-id) (get @delta-details label-id)))))

;;
;; (reg-sub
;;   :access/modified-user-details
;;   :<- [:access/
;;   (fn [db _]
;;     [(rf/subscribe [:access/
;; :<- [:access/show-user-details]
;;   (fn [[view-vars] [_ user-id]]
;;     (user-id (:editing-user-details view-vars)))


(fc-sub
  [:access users]
  :<- [:current-team]
  :<- [:users]
  (fn [[team users] _]
    (u/select-values users (:users team)) ))

(fc-sub
  [:access labels]
  :<- [:current-team]
  :<- [:labels]
  :<- [:users]
  (fn [[team labels users] _]
    (let [ annotated-labels (reduce (fn [all-outer [_ user]]
                              (reduce (fn [all-inner label-id]
                                ;; In each label, update the :users key (conj into a set) the user -id
                                (update all-inner label-id update :users (fnil conj #{}) (:id user)))
                                all-outer
                                (:labels user)))
                              labels
                              users)]
        (u/select-values annotated-labels (:labels team)) )))


(fc-sub
  [:access users-filter]
  (fn [db _] (:users-filter (:access (:view db)))))

(fc-sub
  [:access filtered-users]
  :<- [:access/users]
  :<- [:access/users-filter]
  (fn [[users-list filter] _]
    (let [results    (cond
                       filter
                          (reduce (fn [matches user]
                                    (if (re-find (js/RegExp. filter "i") (:username user))
                                      (conj matches user)
                                      matches))
                              []
                              users-list)
                      :default users-list)]
      results)))

(fc-sub
  [:access labels-filter]
  :<- [:access/view]
  (fn [av _] (:labels-filter av)))

(fc-sub
  [:access filtered-labels]
  :<- [:access/labels]
  :<- [:access/labels-filter]
  (fn [[labels filter] _]
    (let [results    (cond
                       filter
                          (reduce (fn [matches label]
                                    (if (re-find (js/RegExp. filter "i") (:name label))
                                      (conj matches label)
                                      matches))
                              []
                              labels)
                      :default labels)]
      results)))
