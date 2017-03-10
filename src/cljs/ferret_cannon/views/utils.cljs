(ns jericho-midi.views.utils
  (:require
      [jericho-midi.locales :as locales]
      [taoensso.tempura :as tempura ]
      [re-frame.core :as rf])
  (:require-macros [jericho-midi.view-macros :refer [evt]]))

(defn tr [ & args]
  (let [current-locale (rf/subscribe [:current-locale])]
    (fn []
      [:span (tempura/tr {:dict locales/dictionary} [@current-locale] (vec args))])))

(defn active-section? [section-name & args]
  (let [current-section (rf/subscribe [:active-section])
        args (apply hash-map args)]
    (condp = @current-section
      section-name :active
      nil)))

(defn nav-breadcrumbs [history]
  [:div.breadcrumbs
   (doall (map (fn [hist]
      [:a.crumb {:href (:href hist) :key (:name hist)} (:name hist)]
    ) history))])

(defn section-nav-link
  [& {:keys [key href name default]}]
    [:a {:class (active-section? key :default default)
         :href href} name])

(defn section
  [& {:keys [breadcrumbs section-name section-navigation content modal]}]
  [:div {:key (str "section-" section-name)}
    [:nav.section-nav
      [nav-breadcrumbs breadcrumbs]
      [:h2 section-name
        [:div.section-navigation
          section-navigation]
      ]
    ]
    [:div.section-content content ]
    [:div modal]
  ])

(defn modal
  [& {:keys [content heading]}]

  [:div.modal {:style {:display :block} }
   [:h1 "NOT DONE"]
    [:div.overlay {:on-click (evt :close-modal)} ]
    [:div.body
      [:div.modal-content
        (if heading [:div.modal-header [:h4 heading]])
      ]
      [:div.modal-body
        content
      ]
    ]
  ])
(defn cancel-or
  [& {:keys [cancel text cancel-text save]}]
  [:div.row
    [:div.col-md.flex-items-xs-middle
      [:div.pull-right
        [:a.cancel {:on-click (fn [e] (cancel) (.stopPropagation e))} (or cancel-text "Cancel") ]
        " or "
        [:span.btn.btn-primary {:on-click (fn [e] (save) (.stopPropagation e))} (or text "Save") ]
      ]
    ]
  ])
