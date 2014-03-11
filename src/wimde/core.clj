(ns wimde.core
  (:require [markdown.core :as md]
            [seesaw.core :as ss])
  (:gen-class :main :true))

(defn convert [input]
  (md/md-to-html-string input))

(def SIZE [1024 :by 600])

(defn make-content []
  (ss/left-right-split
    (ss/scrollable (ss/text :id :input :multi-line? true :font "MONOSPACED-PLAIN-14"))
    (ss/scrollable (ss/editor-pane :id :output :content-type "text/html" :font "MONOSPACED-PLAIN-14" :editable? false))
    :divider-location 1/2))

(defn make-frame []
  (ss/frame :title "WiMDe"
            :size SIZE
            :content (make-content)
            :on-close :exit))

(defn add-listeners [root]
  (let [in (ss/select root [:#input]) out (ss/select root [:#output])]
    (ss/listen
      in
      #{:remove-update :insert-update}
      (fn [e]
        (ss/text! out (convert (ss/config in :text))))))
  root)

(defn -main [& args]
  (ss/invoke-later
    (->
      (make-frame)
      add-listeners
      ss/show!)))