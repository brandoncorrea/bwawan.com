(ns bwawan.blog
  (:require [clojure.java.io :as io]
            [nextjournal.markdown :as md]))

; TODO: Untested - This will probably look different for ajax

(def hiccup-cache (atom {}))

(defn- file->hiccup [name]
  (-> (str "posts/" name ".md")
      io/resource
      slurp
      md/->hiccup))

(defn- load-post [name]
  (let [hiccup (file->hiccup name)]
    (swap! hiccup-cache assoc name hiccup)
    hiccup))

(defn- forget-blog [name]
  (swap! hiccup-cache dissoc name))

(defn- fetch-post [name]
  (or (get @hiccup-cache name)
      (load-post name)))

(defn render [request]
  {:status  200
   :headers {"Content-Type" "application/edn"}
   :body    (str (fetch-post "test"))})