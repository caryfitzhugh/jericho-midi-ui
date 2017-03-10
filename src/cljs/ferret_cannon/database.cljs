(ns jericho-midi.database
  (:require [reagent.core :as reagent]
            [cljs.spec :as s])
  )

(s/def ::error boolean?)
(s/def ::loading? boolean?)
(s/def ::name string?)

(s/def ::db-spec
  (s/keys :req-un [ ::error
                    ::name
                    ::loading? ]))
(def default-db
  { :loading? false
    :error    false
    :name     "Jericho MIDI"
    :test {

           }
    :config {
    }
    :navigation {
      :panel nil
      :section nil
      :modal nil
    }
    :view-settings {
      ;; These are cleared whenever you move between panels.
    }
    :current-locale :en
  })
