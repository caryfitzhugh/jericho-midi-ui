(ns jericho-midi.core)

(def manufacturer-regex #"(?i)jerichomidi")

(def midi-interfaces (atom {}))

(defn expand-midi-ports [midi-ports]
  (map (fn [midi-port]
      {:name (.-name midi-port)
      :manufacturer (.-manufacturer midi-port)
      :id (.-id midi-port)
      :port midi-port})
      midi-ports))

(defn filter-for-manufacturer [mfr-regex midi-ports]
  (filter (fn [port]
      (re-find mfr-regex (:manufacturer port)))
      midi-ports))

(defn ^:export initialize-midi [cb]
  (.then
    (.requestMIDIAccess js/navigator (js-obj "sysex" true))
    (fn [res]
      (let [inputs (-> res (.-inputs) (.values) (es6-iterator-seq) )
            outputs (-> res (.-outputs) (.values) (es6-iterator-seq))]

        (reset! midi-interfaces { :inputs  (->> inputs
                                               (expand-midi-ports)
                                               (filter-for-manufacturer manufacturer-regex))

                                  :outputs (->> outputs
                                               (expand-midi-ports)
                                               (filter-for-manufacturer manufacturer-regex))
                                 })))))

(defn ^:export get-interfaces [cb]
  "Return the interfaces"
  (cb (pr-str @midi-interfaces))
  )
