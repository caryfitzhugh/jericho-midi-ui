(ns ^:figwheel-no-load jericho-midi.dev
  (:require [jericho-midi.core :as core]
            [devtools.core :as devtools]
            [figwheel.client :as figwheel :include-macros true]))

(enable-console-print!)

(devtools/install!)

(figwheel/watch-and-reload
  :websocket-url "ws://localhost:3000/figwheel-ws"
  :jsload-callback core/mount-root)

(core/init!)
