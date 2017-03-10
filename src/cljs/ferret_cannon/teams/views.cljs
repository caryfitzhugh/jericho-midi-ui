(ns jericho-midi.teams.views
  (:require [jericho-midi.views.utils :as utils :refer [tr active-section?] ]
            [jericho-midi.routes :as r]
            [re-frame.core :as rf])
  (:require-macros [jericho-midi.view-macros :refer [evt fc-view]])
  )

(defn section-navigation []
  [:div
    [utils/section-nav-link :key :dashboard :name [tr :terms/dashboard] :href (r/teams-dashboard) :default true]
    [utils/section-nav-link :key :applications :name [tr :terms/applications] :href (r/teams-applications)]
    [utils/section-nav-link :key :clusters :name [tr :terms/clusters] :href (r/teams-clusters)]
    [utils/section-nav-link :key :settings :name [tr :terms/settings] :href (r/teams-settings)]
  ])

(fc-view outgas-display []
  data [:test/outgas-data]
  [:div
    [:h2 "Outgassing Data"]
    [:p "This tenth compilation of outgassing data of materials intended for spacecraft use supersedes Reference Publication 1124, Revision 3, September 1993. The data were obtained at the Goddard Space Flight Center (GSFC), utilizing equipment developed at Stanford Research Institute (SRI) under contract to the Jet Propulsion Laboratory (JPL)."]
    (if data
      [:table.table.table-striped
        [:thead
          [:tr
            [:th "Sample Material"]
            [:th "ID"]
            [:th "MFR"]
          ]
        ]
        [:tbody
          (doall (map
                  (fn [row]
                      [:tr {:key (:id row)}
                        [:td (:sample_material row)]
                        [:td (:id row)]
                        [:td (:mfr row)]
                      ])
                 data))
        ]
      ]
      [:div [:span.fa.fa-spinner] "Loading"])
  ])

(fc-view ev-activity-display []
  data [:test/ev-activity-data]
  [:div
    [:h2 "Extra-vehicular Activity"]
    [:p "Activities done by an astronaut or cosmonaut outside a spacecraft beyond the Earth's appreciable atmosphere."]

    (if data
      [:table.table.table-striped
        [:thead
          [:tr
            [:th "Crew"]
            [:th "Country"]
            [:th "Date"]
            [:th "Duration"]
            [:th "Purpose"]
            [:th "Vehicle"]
          ]
        ]
        [:tbody
          (doall (map-indexed
                  (fn [i row]
                      [:tr {:key i}
                        [:td (:crew row)]
                        [:td (:country row)]
                        [:td (:date row)]
                        [:td (:duration row)]
                        [:td (:purpose row)]
                        [:td (:vehicle row)]
                      ])
                 data))
        ]
      ]
      [:div [:span.fa.fa-spinner] "Loading"])
  ])


(def sections
  {
    :ev-activity
      [ev-activity-display]
    :outgas-data
        [outgas-display]

    :dashboard
      [:div
        [:h2 "Dashboard"]
        [:p "Choose a button to load the data and display in a table"]
        [:ul
          [:li [:a {:href (r/ev-activity)} "NASA Extra-vehicular Activity"]]
          [:li [:a {:href (r/ev-outgas)} "NASA Outgassing Report"]]
        ]]
    :applications
      [:div
        [:h2 "Applications"]]
    :clusters
      [:div
        [:h2 "Clusters"]]
    :settings
      [:div
        [:h2 "Settings"]
        [:a {:href (r/access-users)} "Manage Team Access"]]
   })

(defn team-root []
  (let [section (rf/subscribe [:active-section])
        team (rf/subscribe [:current-team])]
    (fn []
      (utils/section
        :section-name (:name @team)
        :section-navigation [section-navigation]
        :content (or (get sections @section) [:h1.alert "Unknown Team Section: " @section])
        ))))
