(ns jericho-midi.views
  (:require
      [jericho-midi.routes :as routes]
      [jericho-midi.locales :as locales]
      [jericho-midi.views.utils :as utils :refer [tr]]
      [jericho-midi.teams.views :as team-views]
      [jericho-midi.auth.views :as auth-views]
      [jericho-midi.access.views :as access-views]
      [re-frame.core :as rf])
  (:require-macros [jericho-midi.view-macros :refer [evt fc-view]]))

(fc-view team-selector []
  current-team [:current-team]
  show-team-selection [:layout/show-team-selection]
  [:div.team-selector
    [:a {:href "/"} [:img.team-logo {:src "/images/trebuchet-logo-square.png"}]]

    [:div.team-name-selector {:on-click (fn [] (rf/dispatch [:layout/toggle-team-selection]))}
      [:a.team-name  (:name current-team) ]

      [:span.fa.fa-caret-down {}]
      (if show-team-selection
        [:div.open
          [:div.dropdown-overlay {:on-click (evt :layout/toggle-team-selection)}]
          [:div.dropdown-menu.dropdown-menu-left
            [:span.dropdown-header "Change your active team"]
            [:a.dropdown-item "Switch to Team 2"]
            [:a.dropdown-item "Switch to Team 3"]
            [:hr.divider]
            [:a.dropdown-item "Create New Team"]
             ]])]])

(fc-view avatar [current-user]
  show-profile [:layout/show-profile]
  initials     [:current-user-initials]
  [:div.avatar.pull-right
    [:div.initials
      [:span initials]]

    [:span.hover {
        :on-click (fn [] (rf/dispatch [:layout/toggle-show-profile]))
        }
      [:i.fa.fa-cog ]]
      (if show-profile ;; Leveraging the bootstrap dropdown menu
        [:div.open
          [:div.dropdown-overlay {:on-click (evt :layout/toggle-show-profile)}]
          [:div.dropdown-menu.dropdown-menu-right
            [:a.dropdown-item [:span.fa.fa-question] [tr :terms/help]]
            [:a.dropdown-item [tr :terms/profile]]
            [:a.dropdown-item {:href (routes/logout)} [tr :terms/logout]]
          ]])
  ])

(fc-view layout
  [content]
  current-user [:current-user]
  current-team [:current-team]
  view-settings [:layout-view-settings]

  [:div.body
    [:div#wrapper
      [:nav#top_menu.navber-default.navbar-fixed-top
        ;; Someday would be nice to have a quick search bar here ? (a'la Heroku)
        [avatar current-user view-settings]

        [team-selector]
      ]
    [:div.main-content content]
  ]])


(def views
  {:not-found
    [layout
      [:div.container
        [:h2 "Not Found!"]
        [:div [:a {:href "/"} "go to a happier place..."]]]]
   :teams [layout [team-views/team-root]]
   :access [layout [access-views/access-root]]
   :auth [auth-views/root]
   })

(defn current-page []
  (let [active-panel (rf/subscribe [:active-panel])
        save-persist (rf/subscribe [:store-persistence-stream])]
    (fn [] [:div {:data-saved @save-persist :key (str "panel-" @active-panel)}  (get views @active-panel)])))
