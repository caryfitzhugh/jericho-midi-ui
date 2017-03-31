(require 'cljs.build.api)

(cljs.build.api/watch "src" {
  :main 'jericho-midi.core
  :output-to "out/jericho-midi.js"})
