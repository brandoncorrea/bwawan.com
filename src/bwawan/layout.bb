(ns bwawan.layout
  (:require [clojure.java.io :as io]))

(def body (atom nil))

(defn- slurp-resource [path]
  (-> path io/resource slurp))

(defn- load-index []
  (or @body
      (reset! body (slurp-resource "public/index.html"))))

(defn- response [status]
  {:status  status
   :headers {"Content-Type" "text/html"}
   :body    (load-index)})

(defn default [_request] (response 200))
(defn not-found [_request] (response 404))
