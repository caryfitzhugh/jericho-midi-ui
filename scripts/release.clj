(require 'cljs.build.api)

(cljs.build.api/build "src"
  {:optimizations :advanced
   :main 'jericho-midi.core
   :output-to "out/jericho-midi.js"})

(System/exit 0)
