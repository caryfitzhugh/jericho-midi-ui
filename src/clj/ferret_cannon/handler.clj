(ns jericho-midi.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [not-found resources]]
            [ring.middleware.content-type :as rm-ct]
            [ring.util.response :as rresp]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [clojure.java.io :as io]
            [clojure.data.json :as json]
            [hiccup.page :refer [include-js include-css html5]]
            [jericho-midi.middleware :refer [wrap-middleware]]
            [config.core :refer [env]]))

(defn load-revision-manifest
  [path]
  (when-let [manifest (io/resource path)]
    (-> manifest slurp json/read-str)))

(def revision-manifest
  (memoize load-revision-manifest))

(defn fingerprint-asset-path [asset]
  (let [ manifest (revision-manifest "asset-manifest.json")]
    (or (get manifest asset) asset)))

(def mount-target
  )

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   [:link {:rel "apple-touch-icon" :sizes "57x57"        :href "/favicons/apple-icon-57x57.png"}]
   [:link {:rel "apple-touch-icon" :sizes "60x60"        :href "/favicons/apple-icon-60x60.png"}]
   [:link {:rel "apple-touch-icon" :sizes "72x72"        :href "/favicons/apple-icon-72x72.png"}]
   [:link {:rel "apple-touch-icon" :sizes "76x76"        :href "/favicons/apple-icon-76x76.png"}]
   [:link {:rel "apple-touch-icon" :sizes "114x114"      :href "/favicons/apple-icon-114x114.png"}]
   [:link {:rel "apple-touch-icon" :sizes "120x120"      :href "/favicons/apple-icon-120x120.png"}]
   [:link {:rel "apple-touch-icon" :sizes "144x144"      :href "/favicons/apple-icon-144x144.png"}]
   [:link {:rel "apple-touch-icon" :sizes "152x152"      :href "/favicons/apple-icon-152x152.png"}]
   [:link {:rel "apple-touch-icon" :sizes "180x180"      :href "/favicons/apple-icon-180x180.png"}]
   [:link {:rel "icon" :type "image/png" :sizes "192x192" :href "/favicons/android-icon-192x192.png"}]
   [:link {:rel "icon" :type "image/png" :sizes "32x32"   :href "/favicons/favicon-32x32.png"}]
   [:link {:rel "icon" :type "image/png" :sizes "96x96"   :href "/favicons/favicon-96x96.png"}]
   [:link {:rel "icon" :type "image/png" :sizes "16x16"   :href "/favicons/favicon-16x16.png"}]
   [:link {:rel "manifest" :href "/favicons/manifest.json"}]
   [:meta {:name "msapplication-TileColor" :content "#ffffff"}]
   [:meta {:name "msapplication-TileImage" :content "/favicons/ms-icon-144x144.png"}]
   [:meta {:name "theme-color"             :content "#ffffff"}]

   (include-css "https://fonts.googleapis.com/css?family=Raleway:300,400,700")
   (include-js "https://use.fontawesome.com/97eead68fd.js")

   ;; In prod mode, the bootstrap is all minified into one CSS
   (if (env :dev)
     (include-css "/bootstrap-4.0.0-alpha.5-dist/css/bootstrap-flex.min.css"))
   (include-css (str "/css/"
                    (if (env :dev) "site.css"
                        (fingerprint-asset-path "site.min.css"))))
    ])

(defn loading-page []
  (html5
   (head)
   [:body {:class "body-container"}
    [:div#loading_overlay.layout-fullscreen
      [:div.bubble
        [:img.logo-image {:src "/images/trebuchet-logo-square.png"}]]]
    [:div#app]
    (include-js (str "/js/"
                    (if (env :dev) "app-debug.js"
                        (fingerprint-asset-path "app.js"))))]))

(defn- cache-control-response
  [response request parameters]
   (if (and (#{:get :head} (:request-method request))
            (= (:status response) 200))
      ;; If it was a read and was OK...
      (rresp/header response "Cache-Control" parameters)
      response))

(defn- wrap-cache-control
  [handler parameters]
  (fn [request]
  (println (pr-str request))
  (println (pr-str parameters))

    (-> (handler request)
        (cache-control-response request parameters))))

(defroutes routes
  ;; These are cached "forever"
  (-> (resources "/")
     (wrap-cache-control "public, max-age=31536000, immutable"))

  ;; These are NOT cached at all.
  (-> (GET "*" []
      (-> (rresp/response (loading-page))
          (rresp/status 200)
          (rresp/charset "utf-8")
          (rresp/content-type "text/html")))
      (wrap-cache-control "public, max-age=60"))

  (not-found "Not Found"))

(def app
  (->
    (wrap-middleware #'routes)
    (wrap-defaults (dissoc site-defaults :static))
  ))
