(ns jericho-midi.auth.events
  (:require-macros [jericho-midi.view-macros :refer [fc-evt]])
  (:require [ajax.core :refer [GET]]
            [jericho-midi.access.events]
            [goog.crypt.base64 :as b64]
            [re-frame.core :as rf]
            [cljs.spec     :as s]
            [ajax.core :as ajax]
            [cljs-time.core :as ctime]
            [cemerick.url :refer (url url-encode query->map)]
            [jericho-midi.util :as u]
            [accountant.core :as accountant]
            [jericho-midi.database :as db]))

(fc-evt [:auth set-return-to]
  (fn [db [_ data]]
    (assoc-in db [:auth :return-to] data)))

(defn request-login- [{:keys [db]} [_ response]]
  (let [endpoint @(rf/subscribe [:auth/login-endpoint])]
    (aset js/location "href" endpoint)
    {:db db}))

(rf/reg-event-fx
  :auth/request-login
  request-login-)

(defn request-logout- [{:keys [db]} [_ response]]
  { :db (dissoc db :current-user :auth)
    :redirect-to "/"
  })

(rf/reg-event-fx
  :auth/request-logout
  request-logout-)

(defn load-identity- [{:keys [db]} [_ response]]
  (let [identity-endpoint @(rf/subscribe [:auth/identity-endpoint])
        access-token @(rf/subscribe [:current-user-token])]
  { :http-xhrio {
                :method :get
                :uri  identity-endpoint
                :params {:token access-token}
                :response-format (ajax/json-response-format {:keywords? true})
                :on-success [:auth/load-identity-success]
                :on-failure [:auth/load-identity-failure]}}))

(rf/reg-event-fx
  :auth/load-identity
  load-identity-)

(defn load-identity-success- [{:keys [db]} [_ response]]
  (let [return-to @(rf/subscribe [:auth/return-to])
        dasherized-resp (u/dasherize-keys response)]
    {:db (assoc-in db [:current-user :identity] dasherized-resp)
     :redirect-to (or return-to "/")}))

(rf/reg-event-fx
  :auth/load-identity-success
  load-identity-success-)

(defn load-identity-failure- [{:keys [db]} [_ response]]
(.log js/console "Failed to load... " response)
  (let [return-to @(rf/subscribe [:auth/return-to])]
    {:db (assoc-in db [:current-user :identity] nil)
     :redirect-to (or return-to "/")}))

(rf/reg-event-fx
  :auth/load-identity-failure
  load-identity-failure-)

(defn oauth-callback- [{:keys [db]} [_ hsh]]
  (let [access-token (get (query->map hsh) "access_token")]
    {:db (assoc db :current-user {:access-token access-token})
     :dispatch [:auth/load-identity]}))

(rf/reg-event-fx
  :auth/oauth-callback
  oauth-callback-)
