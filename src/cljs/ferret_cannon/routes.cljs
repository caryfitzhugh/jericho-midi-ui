(ns jericho-midi.routes
  (:require-macros [jericho-midi.view-macros :refer [user-route]])
  (:require
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [reagent.session :as session]
              [cemerick.url :as url]
              [re-frame.core :as rf]
              ))

(def truthy? #{"true" "t"})

(defn replace-query-parameter!
  [& params]
  (let [params (clojure.walk/stringify-keys (apply hash-map params))
        current-url (url/url (-> js/window .-location .-href))
        updated-url (update current-url :query merge params)]
  (.replaceState js/history nil nil (str updated-url))
  (accountant/dispatch-current!)))

;; -------------------------
;; Routes

(user-route ev-activity "/test/nasa-ev-activity" []
  (rf/dispatch [:test/ensure-ev-activity-loaded])
  (rf/dispatch [:set-active-panel :teams :ev-activity]))
(user-route ev-outgas "/test/nasa-outgas-data" []
  (rf/dispatch [:test/ensure-outgas-loaded])
  (rf/dispatch [:set-active-panel :teams :outgas-data]))

(user-route teams-dashboard "/team/dashboard" []
  (rf/dispatch [:set-active-panel :teams :dashboard]))

(user-route access-users "/team/TODO-ID/access/users" [query-params]
  (rf/dispatch [:set-active-panel :access :users])
  (rf/dispatch [:access/set-users-filter (:search query-params)])
  (rf/dispatch [:access/set-invite-user-show (truthy? (:invite-user query-params))]))

(user-route access-labels "/team/TODO-ID/access/labels" [query-params]
  (rf/dispatch [:set-active-panel :access :labels])
  (rf/dispatch [:access/set-labels-filter (:search query-params)])
  (rf/dispatch [:access/set-create-label-show (truthy? (:create-label query-params))]))

(user-route access "/team/TODO-ID/access" []
  (accountant/navigate! (access-users)))

(user-route teams-applications "/team/TODO-ID/applications" []
  (rf/dispatch [:set-active-panel :teams :applications]))

(user-route teams-clusters "/team/TODO-ID/clusters" []
  (rf/dispatch [:set-active-panel :teams :clusters]))

(user-route teams-settings "/team/TODO-ID/settings" []
  (rf/dispatch [:set-active-panel :teams :settings]))

(secretary/defroute logout "/logout" []
  (if-let [current-user @(re-frame.core/subscribe [:current-user])]
    (do
      (rf/dispatch [:set-active-panel :auth :logging-out])
      (rf/dispatch [:auth/request-logout]))
    (accountant/navigate! "/")))

(secretary/defroute login-required "/auth/login-required" [query-params]
  (if-let [current-user @(re-frame.core/subscribe [:current-user])]
    (accountant/navigate! "/")
    (do
      (rf/dispatch [:auth/set-return-to (:return-to query-params)])
      (rf/dispatch [:set-active-panel :auth :login-required]))))

(secretary/defroute login-redirect #"/auth/oauth-redirect#(.*)" []
  (rf/dispatch [:set-active-panel :auth :loading])
  (rf/dispatch [:auth/oauth-callback (subs (str (-> js/location .-hash)) 1)]))

(secretary/defroute home "/" [query-params]
  (if-let [current-user @(re-frame.core/subscribe [:current-user])]
    (accountant/navigate! (teams-dashboard))
    (accountant/navigate! (login-required {:query-params {:return-to (:return-to query-params)}}))))

(secretary/defroute not-found "/*" []
  (rf/dispatch [:set-active-panel  :not-found nil]))

(defn initialize! []
  (set! accountant/history.transformer_
      (let [transformer (goog.history.Html5History.TokenTransformer.)]
        (set! (.. transformer -retrieveToken)
              (fn [path-prefix location]
                (str (.-pathname location) (.-search location))))
        (set! (.. transformer -createUrl)
              (fn [token path-prefix location]
                (str path-prefix token)))
        transformer))
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
)
