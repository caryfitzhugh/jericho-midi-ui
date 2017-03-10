(defproject jericho-midi "0.2.1-SNAPSHOT"
  :description "UX for Jericho MIDI Programming"
  :license "Copyright 2017"
  :min-lein-version "2.7.1"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [reagent "0.6.0"]
                 [reagent-utils "0.2.0"]
                 [ns-tracker "0.3.1" ]
                 [ring "1.5.0" :exclusions [org.clojure/tools.namespace org.clojure/java.classpath]]
                 [ring-server "0.4.0" :exclusions [org.clojure/tools.namespace org.clojure/java.classpath]]
                 [ring/ring-defaults "0.2.1"]
                 [compojure "1.5.1"]
                 [garden "1.3.2"]
                 [hiccup "1.0.5"]
                 [re-frame "0.9.1"]
                 [yogthos/config "0.8"]
                 [org.clojure/clojurescript "1.9.293" :scope "provided"]
                 [cljs-ajax "0.5.8" :exclusions [commons-codec]]
                 [clj-time "0.12.2"]
                 [com.andrewmcveigh/cljs-time "0.4.0"]
                 [secretary "1.2.3"]
                 [thi.ng/color "1.2.0"]
                 [com.taoensso/tempura "1.0.0-RC4"]
                 [com.cemerick/url "0.1.1"]
                 [binaryage/devtools "0.8.3"]
                 [day8.re-frame/http-fx "0.1.3"]
                 [org.clojure/data.json "0.2.6"]
                 [venantius/accountant "0.1.7" :exclusions [org.clojure/tools.reader]]]

  :plugins [[lein-environ "1.0.2"]
            [lein-ring "0.10.0"]
            [lein-figwheel "0.5.7" :exclusions [org.clojure/clojure]]
            [lein-garden "0.2.8" :exclusions [org.clojure/clojure org.clojure/java.classpath org.clojure/tools.namespace]]
            [lein-cljsbuild "1.1.1" :exclusions [org.clojure/clojure]]
            [lein-buster "0.1.0"]
            [lein-asset-minifier "0.2.7" :exclusions [org.clojure/clojure]]]

  :buster {:files ["resources/public/css/site.min.css"
                   "resources/public/js/app.js"]
          :manifest "resources/asset-manifest.json"}

  ;; Ring server handler and uber-war name
  :ring {:handler jericho-midi.handler/app
         :uberwar-name "jericho-midi.uber.war"}
  :aliases {
      "test-all" ["do" ["cljsbuild" "test"] "test"]
    }
  ;; Uberjar name
  :uberjar-name "jericho-midi.uber.jar"

  ;; This is the main function, skip AOT
  :main ^:skip-aot jericho-midi.server

  :clean-targets ^{:protect false}
    [:target-path
      [:cljsbuild :builds :app :compiler :output-dir]
      [:cljsbuild :builds :app :compiler :output-to]
      [:cljsbuild :builds :browser-test :compiler :output-dir]
      [:cljsbuild :builds :browser-test :compiler :output-to]
      [:cljsbuild :builds :test :compiler :output-dir]
      [:cljsbuild :builds :test :compiler :output-to]
      [:buster :manifest]
      "resources/public/css/site.min-*.css"
      "resources/public/js/app-*.js"
      "resources/public/css/site.css"
      "resources/public/css/site.css"
      "resources/public/css/site.min.css"]

  :source-paths   ["src/clj" "src/cljc"]
  :resource-paths ["resources" "target/cljsbuild"]
  :minify-assets  {:assets {"resources/public/css/site.min.css" [
                                                                 "resources/public/bootstrap-4.0.0-alpha.5-dist/css/bootstrap-flex.min.css"
                                                                 "resources/public/css/site.css"
                                                                ]}}

  :cljsbuild      {
        :test-commands {"test" ["lein" "doo" "phantom" "test" "once"]}
        :builds {
          :prod {
                  :source-paths ["src/cljs" "src/cljc" "env/prod/cljs"]
                  :jar true
                  :compiler {
                            :output-to "resources/public/js/app.js"
                            :output-dir "resources/public/js"
                            :optimizations :advanced
                            :pretty-print  false}}

          :dev {
                :source-paths ["src/cljs" "src/cljc" "env/dev/cljs"]
                :compiler {
                            :main "jericho-midi.dev"
                            :asset-path "/js/out"
                            :output-to "resources/public/js/app-debug.js"
                            :output-dir "resources/public/js/out"
                            :source-map true
                            :optimizations :none
                            :pretty-print  true}}

          :browser-test {
             :source-paths ["src/cljs" "src/cljc" "test/cljs"]
             :figwheel {:devcards true}
             :compiler { :output-to "resources/public/test/browser/all-tests.js"
                         :output-dir "resources/public/test/browser/out"
                         :asset-path "/test/browser/out"
                         :source-map true
                         :pretty-print true
                         :main "test.jericho-midi.devcards"
                         :optimizations :none}}
           :test {
             :source-paths ["src/cljs" "src/cljc" "test/cljs"]
             :figwheel {:devcards true}
             :compiler { :output-to "resources/public/test/cli/all-tests.js"
                         :output-dir "resources/public/test/cli/out"
                         :source-map true
                         :pretty-print true
                         :main "test.jericho-midi.devcards"
                         :optimizations :none}}}}

   :garden {:builds [{
                        :id "main"
                        :source-paths ["src/clj"]
                        :stylesheet jericho-midi.styles/main
                        :compiler { :output-to "resources/public/css/site.css" } } ] }


  :figwheel
    { :http-server-root "public"
      :server-port 3000
      :nrepl-middleware ["cemerick.piggieback/wrap-cljs-repl"]
      :css-dirs ["resources/public/css"]
      :ring-handler jericho-midi.handler/app}

  :profiles {
    :dev {:repl-options {:init-ns jericho-midi.repl}
                   :dependencies [
                                  [prone "1.1.2"]
                                  [figwheel-sidecar "0.5.7" :exclusions [org.clojure/core.async]]
                                  [org.clojure/tools.nrepl "0.2.12"]
                                  [com.cemerick/piggieback "0.2.2-SNAPSHOT"]
                                  [devcards "0.2.2" :exclusions [cljsjs/react-dom]]
                                  [doo "0.1.7" :exclusions [com.google.javascript/closure-compiler com.google.javascript/closure-compiler-externs]]
                                  ]

                   :source-paths ["env/dev/clj"]
                   :plugins [[lein-figwheel "0.5.7"]
                             [lein-doo "0.1.7" :exclusions [com.google.javascript/closure-compiler com.google.javascript/closure-compiler-externs]]
                             ]
                   :env {:dev true}}

             :uberjar {:hooks [minify-assets.plugin/hooks leiningen.buster]
                       :aot :all
                       :prep-tasks [ ["garden" "once"]
                                     ["cljsbuild" "once" "prod"]
                                     "compile"
                                     "buster"
                                     ]
                       :source-paths ["env/prod/clj"]
                       :env {:production true}
                       :omit-source true}})
