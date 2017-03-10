(ns jericho-midi.auth.subscriptions
  (:require-macros [reagent.ratom :refer [reaction]]
                   [jericho-midi.view-macros :refer [fc-sub]])
  (:require [re-frame.core :as rf :refer [reg-sub]]
            ))

(fc-sub [:auth return-to]
  (fn [db _]
    (get-in [:auth :return-to] db)))

(fc-sub [:auth endpoints]
  (fn [db _]
    (:endpoints (:auth (:config db)))))

(fc-sub [:auth login-endpoint]
  :<- [:auth/endpoints]
  (fn [endpoints _]
    (let [host (-> js/window .-location .-host)
          endpoints (:login endpoints)]
      (.log js/console "Looking up endpoint: " host " in " endpoints)
      (or (get endpoints host)
          (get endpoints :missing)))))

(fc-sub [:auth identity-endpoint]
  :<- [:auth/endpoints]
  (fn [endpoints _]
    (let [host (-> js/window .-location .-host)
          endpoints (:identity endpoints)]
      (get endpoints host))))
