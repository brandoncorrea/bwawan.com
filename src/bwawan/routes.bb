(ns bwawan.routes
  (:require [bwawan.config :as config]
            [ruuter.core :as ruuter]))

(defn- resolve-handler [handler-sym]
  @(requiring-resolve handler-sym))

(defn- refresh-handler [handler-sym]
  (fn [request]
    ((resolve-handler handler-sym) request)))

(defn- maybe-refresh-handler [handler-sym]
  (if (:refresh-handlers? config/env)
    (refresh-handler handler-sym)
    (resolve-handler handler-sym)))

(defn- ->route [[[path method] handler-sym]]
  {:path     path
   :method   method
   :response (maybe-refresh-handler handler-sym)})

(defmacro defroutes [name routes]
  `(def ~name (mapv ->route ~routes)))

(defroutes routes
  {["/" :get]            'bwawan.layout/default
   ["/posts/:slug" :get] 'bwawan.blog/render
   [":not-found" :get]   'bwawan.layout/not-found})

(defn handler [request]
  (ruuter/route routes request))
