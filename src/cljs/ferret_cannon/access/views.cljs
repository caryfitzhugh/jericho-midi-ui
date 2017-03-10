(ns jericho-midi.access.views
  (:require [jericho-midi.views.utils :as utils :refer [tr active-section?] ]
            [jericho-midi.routes :as r]
            [re-frame.core :as rf])
  (:require-macros [jericho-midi.view-macros :refer [evt item-list fc-view]])
  )

(def max-show 5)

(fc-view permissions-list [permissions]
  [:table.table.margin-bottom
    [:tbody
      [:tr
        [:td
        [:input.form-control {:placeholder "create, read, update, delete"}]]
        [:td
          [:input.form-control {:placeholder "clusters/*, applications/*"}]]
        [:td
          [:input.form-control.btn.btn-primary {:value "Create"}]]]]
    [:tbody
      (doall (map (fn [permission]
        [:tr {:key (str "permission-" (pr-str permission))}
              [:td (:action permission)]
              [:td (:resource permission)]
              [:td [:a.btn.btn-danger.btn-sm "Remove"]] ])
          permissions))]
  ])
(fc-view section-navigation []
  [:div
    [utils/section-nav-link :key :users :name [tr :users/term] :href (r/access-users)]
    [utils/section-nav-link :key :labels :name [tr :labels/term] :href (r/access-labels)]
  ])

(fc-view labels-display [user]
  labels [:labels]
  [:div.labels-list (doall (map (fn [label-id]
      (let [label (get labels label-id)]
        [:span {:key (str "label-id-" label-id)} (if (:expiration label) [:span.fa.fa-clock-o]) (:name label )]))
      (:labels user)))])

(fc-view user-details [user expand]
  labels [:labels]
  organizations [:organizations]

  [:div
      [:div
        [:div.user-details
          [:div.input-group.margin-bottom
            [:input.form-control { :placeholder "Type to find and add label - Enter to add the label"}]]
        ]
        [:div.labels-list (doall (map (fn [label-id]
            [:span {:key (str "label-id-" label-id)} (:name (get labels label-id))
              [:span.fa.fa-times-circle-o]])
            (:labels user)))]
        (utils/cancel-or :cancel (evt  :access/cancel-details-user (:id user)))

        [:div.user-details.margin-top
          [:label "Email:"] [:span [:a {:href (str "mailto:" (:email user))} (:email user)]]
          [:label "Organization:"] [:span (:name (get organizations (:organization user)))]
        ]
      ]])

(fc-view user-users-display [user expand]
  users-details-flags [:access/show-details-users]
  (let [show-details (or expand (get users-details-flags (:id user)))]
    [:div.list-group-item.user-listing { :key (str "user-" (:id user))}
      [:div.row
        [:div.col-md-4 [:a.name {:on-click (evt :access/toggle-details-user (:id user)) } (:username user)]]
        [:div.col-md-8 (if (not show-details) [labels-display user])]
      ]
      (if show-details [user-details user show-details])
      ]))

(fc-view empty-users-display []
  filter     [:access/users-filter]
  [:div.empty-results
    (if filter
      [:h3 "Could not find any matches for \"" filter "\"."]
      [:h3 "There are no users available"])])

(fc-view invite-user-display []
  invite-user [:access/invite-user-show?]
  [:div
    (if invite-user
      [:div.list-group-item
        [:div.input-group.margin-bottom
          [:input.form-control { :placeholder "Find by username"}]
          [:div.input-group-addon "Invite"]
        ]])])

(fc-view access-content-users []
  users [:access/filtered-users]
  filter-string [:access/users-filter]
  [:div
    [:div
      [:div.input-group.margin-bottom
        [:div.input-group-addon [:span.fa.fa-search]]
        [:input.form-control {:value filter-string
                              :on-change (fn [e] (rf/dispatch [:access/users-filter-update (-> e .-target .-value)]))
                              :placeholder "Filter by username"}]
        (if (> (count filter-string) 0) [:div.input-group-addon {:on-click (fn [e] (rf/dispatch [:access/users-filter-update ""]))} [:span.fa.fa-times-circle]])
      ]
    ]
    [:div.list-group
      [:div.list-group-item.header
        [:div.row
                [:div.col-md-4 [tr :users/user-name]]
                [:div.col-md-6 [tr :users/user-in-label]]
                [:div.col-md-2 [:span.btn.btn-sm.btn-secondary.pull-right {:on-click (evt :access/invite-user-show)} [:span.fa.fa-plus] " " [tr :users/invite]]]
        ]
      ]
      [invite-user-display]
    (condp = (count users)
      1 [user-users-display (first users) true]
      0 [empty-users-display]
      (item-list user-users-display users))
    ]])

(fc-view label-details [label]
  labels-details-flags [:access/show-details-labels]
  users [:users]
  (condp = (get labels-details-flags (:id label))
    :members
      [:div
        [:div.label-details.margin-top
          [:h5 "Users"]
          [:div.input-group.margin-bottom
            [:input.form-control { :placeholder "Type to find and add user - Enter to add the user"}]]
        ]
        (utils/cancel-or :cancel (evt  :access/cancel-details-label (:id label)))
        [:div.edit-list.clearfix
          (doall (map (fn [user-id]
            (let [id (str "checking-users-" (name user-id) "-" (name (:id label)))]
              [:span {:key id}
                [:input {:id id :type :checkbox :defaultChecked true}]
                [:label.strikethrough {:for id} (:username (get users user-id))]
              ])) (:users label)))
        ]
        (utils/cancel-or :cancel (evt  :access/cancel-details-label (:id label)))
      ]
    :label
      [:div.label-details.margin-top
        [permissions-list (:permissions label)]
        (utils/cancel-or :cancel (evt  :access/cancel-details-label (:id label)))
      ]
      ;; False / missing - show nothing
      nil nil
      false nil
    ))

(fc-view label-labels-display [label]
  labels-details-flags [:access/show-details-labels]
  (let [showing (get labels-details-flags (:id label))]
    [:div.list-group-item.label-listing {:key (str "label-" (:id label))}
      [:div.row.clickable { :on-click (evt :access/toggle-details-label (:id label) :members)}
        [:div.col-md-4
            [:a (:name label)]
          ]
        [:div.col-md-4
          [:span (count (:users label)) " Users"]]
        [:div.col-md-4
          [:a.btn.btn-sm {:class (if (= :members showing) :btn-primary :btn-secondary ) :on-click (fn [e] (rf/dispatch [:access/toggle-details-label (:id label) :members]) (.stopPropagation e))} "Edit Members"]
          " "
          [:a.btn.btn-sm  {:class (if (= :label showing) :btn-primary :btn-secondary ) :on-click (fn [e] (rf/dispatch [:access/toggle-details-label (:id label) :label]) (.stopPropagation e))} "Edit Label"]]
      ]
      [label-details label]
    ]))

(fc-view empty-labels-display []
  filter [:access/labels-filter]
  [:div.empty-results
    (if filter
      [:h3 "Could not find any matches for \"" filter "\"."]
      [:h3 "There are no labels available"])])

(fc-view create-label-display []
  create-label [:access/create-label-show?]
  (if create-label
    [:div.list-group-item
      [:div.input-group.margin-bottom
        [:input.form-control { :placeholder "Enter new label name (on create, sets filter to that name so you can find/ edit)"}]
        [:div.input-group-addon "Create"]
      ]]))

(fc-view access-content-labels [ ]
  labels         [:access/filtered-labels]
  filter-string [:access/labels-filter]
  [:div
    [:div
      [:div.input-group.margin-bottom
        [:div.input-group-addon [:span.fa.fa-search]]
        [:input.form-control {
          :value filter-string
          :on-change (fn [e] (rf/dispatch [:access/labels-filter-update (-> e .-target .-value)]))
          :placeholder "Filter by label name"}]
        (if (> (count filter-string) 0) [:div.input-group-addon {:on-click (fn [e] (rf/dispatch [:access/labels-filter-update ""]))} [:span.fa.fa-times-circle]])
      ]
    ]
    [:div.list-group
      [:div.list-group-item.header
        [:div.row
                [:div.col-md-4 [tr :labels/label-name]]
                [:div.col-md-6 [tr :labels/users-in-label]]
                [:div.col-md-2 [:span.btn.btn-sm.btn-secondary.pull-right {:on-click (evt :access/create-label-show)} [:span.fa.fa-plus] " " [tr :labels/create]]]
        ]
      ]
      [create-label-display]
      (condp = (count labels)
        1 [label-labels-display (first labels)]
        0 [empty-labels-display]
        (item-list label-labels-display labels))
    ]])

(def sections
 { :users
     [access-content-users]
   :labels
     [access-content-labels]
   :manage-labels
     [:div
       [:h2 "Manage Labels"]]
  })

(fc-view access-root []
  section [:active-section]
  team [:current-team]
  (utils/section
    :breadcrumbs [{:href (r/teams-dashboard) :name (:name team)}]
    :section-name "Team Access"
    :section-navigation [section-navigation]
    :content (or (get sections section) [:h1.alert "Unknown Access Section: " section])
    ))
