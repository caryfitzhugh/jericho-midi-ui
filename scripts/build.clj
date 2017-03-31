(require 'cljs.build.api)

(cljs.build.api/build "src" {
  :main 'jericho-midi.core
  :output-to "out/jericho-midi.js"})

(System/exit 0)
