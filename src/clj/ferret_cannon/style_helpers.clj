(ns jericho-midi.style-helpers
  (:require [garden.def :refer [defstylesheet defstyles defkeyframes]]
            [thi.ng.color.core :as c]                     ;; Color manipulation
            [thi.ng.math.core  :as cm]                     ;; Color manipulation
            [garden.units :refer [px]])
  )


(def orange         "#FB6223")
(def red            "#f72f2f")
(def deep-red       "#771717")
(def blue           "#4453d5") ;;"#303a96") ;;"#1872aa") ;; 117fc4
(def yellow         "#f8f648")
(def green          "#00AA31")
(def purple         "#593389")

(def white     "#FCFBFF")
(def grey3     "#E2E2E5")
(def grey2     "#bdbcbf")
(def grey1     "#7e7e7f")
(def grey0     "#3f3f40")
(def black     "#120303")

(defn lighten [css-color ratio]
  @(c/as-css (cm/mix (c/css css-color) (c/rgba 1 1 1) ratio)))
(defn darken [css-color ratio]
  @(c/as-css (cm/mix (c/css css-color) (c/rgba 0 0 0) ratio)))

(defn rounded [rules]
  (assoc rules :border-radius :50%))

(def pulse-radius "10px")
(defkeyframes border-pulse-blue-keyframes
    [:0% {
      :-moz-box-shadow (str "0 0 0 0 " blue)
      :box-shadow (str "0 0 0 0 " blue)
      :-webkit-box-shadow (str "0 0 0 0 " blue)
      }]
    [:50% {
      :-moz-box-shadow (str "0 0 0 " pulse-radius " " blue)
      :box-shadow (str "0 0 0 " pulse-radius " " blue)
      :-webkit-box-shadow (str "0 0 0 " pulse-radius " " blue)
      }]
    [:70% {
      :-moz-box-shadow (str "0 0 0 " pulse-radius " " blue)
      :box-shadow (str "0 0 0 " pulse-radius " " blue)
      :-webkit-box-shadow (str "0 0 0 " pulse-radius " " blue)
      }]
    [:100% {
     :-moz-box-shadow (str "0 0 0 0px " blue)
     :box-shadow (str "0 0 0 0px " blue)
     :-webkit-box-shadow (str "0 0 0 0px " blue)
    }])

(defkeyframes border-pulse-red-keyframes
    [:0% {
      :-moz-box-shadow (str "0 0 0 0 " red)
      :box-shadow (str "0 0 0 0 " red)
      :-webkit-box-shadow (str "0 0 0 0 " red)
      }]
    [:50% {
      :-moz-box-shadow (str "0 0 0 " pulse-radius " " red)
      :box-shadow (str "0 0 0 " pulse-radius " " red)
      :-webkit-box-shadow (str "0 0 0 " pulse-radius " " red)
      }]
    [:70% {
      :-moz-box-shadow (str "0 0 0 " pulse-radius " " red)
      :box-shadow (str "0 0 0 " pulse-radius " " red)
      :-webkit-box-shadow (str "0 0 0 " pulse-radius " " red)
      }]
    [:100% {
     :-moz-box-shadow (str "0 0 0 0px " red)
     :box-shadow (str "0 0 0 0px " red)
     :-webkit-box-shadow (str "0 0 0 0px " red)
    }])

(defkeyframes border-pulse-yellow-keyframes
    [:0% {
      :-moz-box-shadow (str "0 0 0 0 " yellow)
      :box-shadow (str "0 0 0 0 " yellow)
      :-webkit-box-shadow (str "0 0 0 0 " yellow)
      }]
    [:50% {
      :-moz-box-shadow (str "0 0 0 " pulse-radius " " yellow)
      :box-shadow (str "0 0 0 " pulse-radius " " yellow)
      :-webkit-box-shadow (str "0 0 0 " pulse-radius " " yellow)
      }]
    [:70% {
      :-moz-box-shadow (str "0 0 0 " pulse-radius " " yellow)
      :box-shadow (str "0 0 0 " pulse-radius " " yellow)
      :-webkit-box-shadow (str "0 0 0 " pulse-radius " " yellow)
      }]
    [:100% {
     :-moz-box-shadow (str "0 0 0 0px " yellow)
     :box-shadow (str "0 0 0 0px " yellow)
     :-webkit-box-shadow (str "0 0 0 0px " yellow)
    }])

(defkeyframes border-pulse-purple-keyframes
    [:0% {
      :-moz-box-shadow (str "0 0 0 0 " purple)
      :box-shadow (str "0 0 0 0 " purple)
      :-webkit-box-shadow (str "0 0 0 0 " purple)
      }]
    [:50% {
      :-moz-box-shadow (str "0 0 0 " pulse-radius " " purple)
      :box-shadow (str "0 0 0 " pulse-radius " " purple)
      :-webkit-box-shadow (str "0 0 0 " pulse-radius " " purple)
      }]
    [:70% {
      :-moz-box-shadow (str "0 0 0 " pulse-radius " " purple)
      :box-shadow (str "0 0 0 " pulse-radius " " purple)
      :-webkit-box-shadow (str "0 0 0 " pulse-radius " " purple)
      }]
    [:100% {
     :-moz-box-shadow (str "0 0 0 0px " purple)
     :box-shadow (str "0 0 0 0px " purple)
     :-webkit-box-shadow (str "0 0 0 0px " purple)
    }])
(defkeyframes border-pulse-grey2-keyframes
    [:0% {
      :-moz-box-shadow (str "0 0 0 0 " grey2)
      :box-shadow (str "0 0 0 0 " grey2)
      :-webkit-box-shadow (str "0 0 0 0 " grey2)
      }]
    [:50% {
      :-moz-box-shadow (str "0 0 0 " pulse-radius " " grey2)
      :box-shadow (str "0 0 0 " pulse-radius " " grey2)
      :-webkit-box-shadow (str "0 0 0 " pulse-radius " " grey2)
      }]
    [:70% {
      :-moz-box-shadow (str "0 0 0 " pulse-radius " " grey2)
      :box-shadow (str "0 0 0 " pulse-radius " " grey2)
      :-webkit-box-shadow (str "0 0 0 " pulse-radius " " grey2)
      }]
    [:100% {
     :-moz-box-shadow (str "0 0 0 0px " grey2)
     :box-shadow (str "0 0 0 0px " grey2)
     :-webkit-box-shadow (str "0 0 0 0px " grey2)
    }])

(defkeyframes border-pulse-white-keyframes
    [:0% {
      :-moz-box-shadow (str "0 0 0 0 " white)
      :box-shadow (str "0 0 0 0 " white)
      :-webkit-box-shadow (str "0 0 0 0 " white)
      }]
    [:50% {
      :-moz-box-shadow (str "0 0 0 " pulse-radius " " white)
      :box-shadow (str "0 0 0 " pulse-radius " " white)
      :-webkit-box-shadow (str "0 0 0 " pulse-radius " " white)
      }]
    [:70% {
      :-moz-box-shadow (str "0 0 0 " pulse-radius " " white)
      :box-shadow (str "0 0 0 " pulse-radius " " white)
      :-webkit-box-shadow (str "0 0 0 " pulse-radius " " white)
      }]
    [:100% {
     :-moz-box-shadow (str "0 0 0 0px " white)
     :box-shadow (str "0 0 0 0px " white)
     :-webkit-box-shadow (str "0 0 0 0px " white)
    }])
