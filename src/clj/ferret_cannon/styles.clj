(ns jericho-midi.styles
  (:require [jericho-midi.style-helpers :refer :all]
            [garden.def :refer [defstylesheet defstyles]]
            [thi.ng.color.core :as c]                     ;; Color manipulation
            [thi.ng.math.core  :as cm]                     ;; Color manipulation
            [garden.units :refer [px]]))

;; RULES
;; Hovers *darken* the link a little

(def primary purple)

(def colors {:bg white
             :links (darken primary 0.3)
             :links-hover (darken primary 0.7)
             :dark @(c/as-css (c/css primary))
             :lighter (lighten primary 0.2)

             :layout-fullscreen-bg (lighten primary 0.4)
             :layout-fullscreen-box-shadow (darken primary 0.5)
             :text black ;;"#efefff"
             :top-menu-bg (lighten primary 0.1)
             :top-menu-color white

             :section-link (lighten primary 0.3)
             :section-link-active primary
             :section-link-hover (darken primary 0.3)

             :button-primary-bg primary
             :button-primary-color white
             :button-primary-bg-hover (darken primary 0.2)
             :button-primary-color-hover white

             :table-header-bg blue
             :table-header-color white
             :overlay-bg @(c/as-css (c/rgba 0 0 0 0.2))
             :section-nav-bg (lighten primary 0.85)
             :sub-text-color (darken "#efefff" 0.3)
             :menu-border "#1f3040"
             :breadcrumb-color grey1 ;;(lighten primary 0.3)
             :breadcrumb-separator grey0 ;;(lighten primary 0.3)
             :breadcrumb-hover-color grey0 ;;primary
             :avatar-hover-cog-bg  grey1 ;;(lighten primary 0.6)
             :avatar-hover-cog-color grey2 ;;primary
             :avatar-bg white ;;"#eeeeee"
             :avatar-color grey0 ;;"#111111"
})

(def force-square-corners
  [:.navbar :.dropdown-menu :.list-group-item:first-child
   :.btn
   :.form-control
   :.input-group-addon
   {:border-radius "0"}])

(def pattern-background
  [:&:before {:position :absolute
              :top 0
              :right 0
              :left 0
              :bottom 0
              :content "' '"
              :opacity 0.25
              :background-repeat :repeat
              :background-image "url(/images/sidebar-pattern.png)"}])

(defstyles main
  border-pulse-red-keyframes
  border-pulse-yellow-keyframes
  border-pulse-grey2-keyframes
  border-pulse-blue-keyframes
  border-pulse-purple-keyframes
  border-pulse-white-keyframes
  [:body {:font-family "'Raleway', sans-serif"
          :height :100%
          :padding 0
          :margin 0}
   force-square-corners
   [:a
    :a:link
    :a:visited
    :a:focus
    :a:active
    {:cursor :pointer
     :color (:links colors)
     :text-decoration :none}]
   [:a:hover {:transition "0.5s all"}]
   [:.btn {:border-radius 0}
    [:&.btn-primary {:background (:button-primary-bg colors)
                     :border-color (:button-primary-bg colors)
                     :color (:button-primary-color colors)}
     [:&:hover {:color (:button-primary-color-hover colors)
                :background (:button-primary-bg-hover colors)}]]]
   [:#loading_overlay {:opacity 1
                       :transition "0.2s all"}
    [:.bubble (rounded {:background white
                        :padding :10px
                        :box-shadow (str "0 0 0 " blue)
                        ;;:animation "border-pulse-red-keyframes 1.5s infinite"
                        ;;:animation "border-pulse-grey2-keyframes 1.5s infinite"
                        ;;:animation "border-pulse-yellow-keyframes 1.5s infinite"
                        ;;:animation "border-pulse-blue-keyframes 1.5s infinite"
                        ;;:animation "border-pulse-white-keyframes 1.5s infinite"
                        :animation "border-pulse-purple-keyframes 1.5s infinite"})
     [:.logo-image
      {:position :relative
       :top :-5px
       :left :-3px
       :width :100px}]]
    [:&.hide {:opacity 0
              :z-index -1
              :transition "0.2s all"}]]
   [:.layout-fullscreen {:position :fixed
                         :top 0
                         :right 0
                         :left 0
                         :bottom 0
                         :background (:layout-fullscreen-bg colors)
                         :display :flex
                         :align-items :center
                         :justify-content :center}
    [:.content {:background (:bg colors)
                :padding :16px
                :box-shadow (str "1px 1px 10px " (:layout-fullscreen-box-shadow colors))}
     [:&.centered {:text-align :center}]]]
   [:.login-form
    [:input {:min-width :350px}]]
   [:.dropdown-item
    [:a :a:active :a:focus :a:visited {:border 0}]]
   [:#wrapper {:width "100%"
               :overflow-x :hidden}]

   [:.main-content {:margin-top :50px}]
   [:.section-nav {:padding "10px 20px"
                   :background (:section-nav-bg colors)}
    [:h2 {:font-size :24px :margin-bottom 0}]]

   [:.section-content {:padding "10px 20px"}]

   [:#top_menu {:padding "10px"
                :margin 0
                :background-color (:top-menu-bg colors)
                :color (:top-menu-color colors)
                :font-size :12px
                :height :50px}
    [:.team-name {:font-size :18px
                  :line-height :40px
                  :position :relative
                  :top :-4px
                  :color (:top-menu-color colors)}
     [:&:hover {:border-bottom-color (:text colors)}]]
    [:img.team-logo {:height :40px
                     :margin-right :8px :position :relative
                     :background white
                     :top :-8px
                     :left :-5px}]

    [:.team-name-selector {:position :relative
                           :display :inline-block}
     [:.dropdown-overlay {:background (:overlay-bg colors)
                          :position :fixed :top 0 :left 0 :right 0 :bottom 0}]
     [:.dropdown-menu {}]
     [:.fa-caret-down {:position :relative
                       :top :-4px
                       :left :8px
                       :font-size :18px
                       :line-height :40px}]]
    [:.avatar {:margin-bottom "10px"
               :background (:avatar-bg colors)
               :color (:avatar-color colors)

               :height :30px
               :width :30px
               :line-height :30px
               :text-align :center
               :position :relative
               :display :inline-block}
     [:&:hover  [:.hover {:opacity 1 :z-index 10}]]
     [:.hover {:cursor :pointer
               :position :absolute
               :top 0
               :left 0
               :right 0
               :bottom 0
               :background (:avatar-hover-cog-bg colors)
               :color (:avatar-hover-cog-color colors)
               :opacity 0
               :transition "0.3s all"
               :z-index -1}]
     [:.dropdown-overlay {:background (:overlay-bg colors)
                          :position :fixed :top 0 :left 0 :right 0 :bottom 0}]

     [:.initials {:font-weight 700
                  :letter-spacing :4px ;; These need to match
                  :text-indent    :4px ;; These need to match
}]]]
   [:.breadcrumbs {:font-size :75%}
    [:.crumb {:color (:breadcrumb-color colors)
              :font-weight 400
              :margin-right :16px
              :position :relative}
     [:&:hover {:color (:breadcrumb-hover-color colors)}]
     [:&:before {:content "'/'"
                 :margin "0px 8px"
                 :position :absolute
                 :left "-20px"
                 :color (:breadcrumb-separator colors)}]
     [:&:first-child:before {:content "''"
                             :color (:breadcrumb-separator colors)}]]]

   [:.section-navigation {:font-size "60%"
                          :display :inline-block
                          :padding-left :20px}
    [:a {:margin "0 20px"
         :border-bottom "2px solid transparent"
         :color (:section-link colors)}
     [:&.active {:color (:section-link-active colors)
                 :border-bottom-color (:section-link-active colors)}]
     [:&:hover  {:color (:section-link-hover colors)}]]]

   [:.list-group {:padding-top :10px}]
   [:.clickable {:cursor :pointer}
    [:&:hover {}]]
   [:.list-group-item.header {:color (:table-header-color colors)
                              :background (:table-header-bg colors)
                              :padding-top :8px
                              :padding-bottom :8px
                              :line-height :27px}]
   [:.tag-more {:color (:dark colors)
                :padding "0px 8px"
                :font-weight 100}]
   [:.user-listing {:padding-bottom :4px}
    [:.name {:padding "4px 8px"
             :margin "4px 4px"
             :display :inline-block}]]
   [:.users-list :.labels-list {}
    [:.fa {:font-size :125%
           :line-height :14px
           :position :absolute
           :right 0
           :top 0
           :bottom 0
           :background (:text colors)
           :color (:dark colors)}]
    [:span {:position :relative
            :display :inline-block
            :white-space :nowrap
            :background (:dark colors)
            :color (:text colors)
            :padding "4px 8px"
            :margin "4px 4px"
            :font-size :80%}]]
   [:.empty-results {:text-align :center
                     :padding :32px}]
   [:.user-details :.label-details {:border-top "1px solid black"
                                    :padding-top :12px
                                    :padding-bottom :6px
                                    :display :block
                                    :margin-right :-25px
                                    :width :100%}
    [:&.margin-top {:margin-top :8px}]
    [:label {:font-weight :bold :margin-right :16px}]]
   [:.edit-list {:width :100%}
    [:span {:display :inline-block :width :50% :float :left}
     [:input {:margin-right :8px}]]
    [:label {:text-decoration :line-through}]
    ["input[type=checkbox]:checked + label" {:text-decoration :none}]]])
