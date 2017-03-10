(ns jericho-midi.view-macros
  )

(defmacro evt
  "Shortcut macro to dispatch the args as a vector"
  [ & dispatch-array]
  `(fn [fn-evt#] (re-frame.core/dispatch ~(vec dispatch-array))
     (.stopPropagation fn-evt#)
     (.preventDefault fn-evt#)))

(defmacro item-list
  [func items & {:keys [tag id-key]}]
  `[~(or tag :div)
      (doall (map-indexed (fn [idx# itm#]
                        [~(or tag :div) {:key (str "list-itm-" idx# "-" ((or ~id-key :id) itm#))}
                          [~func itm#]
                        ]) ~items))])

(defn comp->keyword
  [fn-components]
  (let [fn-components (flatten (list fn-components))
        fn-name (last fn-components)
        fn-keyword (keyword
                    (clojure.string/join "/"
                      (map name fn-components)))]
    [fn-name fn-keyword]))


(defmacro fc-sub
  [fn-components & rest]
  (let [[fn-name fn-keyword] (comp->keyword fn-components)]
    `(do
        (def ~fn-name ~(last rest))
        (re-frame.core/reg-sub
          ~fn-keyword
          ~@(drop-last rest)
          ~fn-name))))

(defmacro fc-evt
  [fn-components & rest]
  (let [[fn-name fn-keyword] (comp->keyword fn-components)]
    `(do
        (def ~fn-name ~(last rest))
        (re-frame.core/reg-event-db
          ~fn-keyword
          ~@(drop-last rest)
          ~fn-name))))

(def fc-view-inner-suffix "-")

(defmacro fc-view-impl [wrapper]
  (symbol (str (name wrapper) fc-view-inner-suffix)))

(defmacro fc-view [wrapper inputs & args]
  (let [inner (symbol (str (name wrapper) fc-view-inner-suffix))
        subscriptions (reduce (fn [tot [k v]] (assoc tot k (list 're-frame.core/subscribe v))) {} (partition 2 (drop-last args)))
        subscription-names (keys subscriptions)
        let-subscriptions (reduce (fn [all [k v]]
                            (-> all
                              (conj k)
                              (conj v))) [] subscriptions)
        derefd-subscription-names (map (fn [sn] `(deref ~sn)) subscription-names)
        body (last args)]
  `(do
    (defn ~inner [~@inputs ~@subscription-names] ~body)

    (defn ~wrapper ~inputs
      (let ~let-subscriptions
        (fn [] [~inner ~@inputs ~@derefd-subscription-names])))
   )
  ))

(defmacro user-route
  [name path let-expr & body]
  `(secretary.core/defroute ~name ~path ~let-expr
     (let [cu# (re-frame.core/subscribe [:current-user])]
      (if (deref cu#)
        (do ~@body)
         (accountant.core/navigate! (str "/?return-to=" ~path))))))
