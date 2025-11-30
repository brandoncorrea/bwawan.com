(ns bwawan.http
  (:require [bwawan.routes :as routes]
            [c3kit.apron.app :as app]
            [c3kit.apron.log :as log]
            [org.httpkit.server :as http]))

(def PORT 8000)

(defn start [app]
  (log/info "Starting HTTP Service")
  (let [server (or (::server app)
                   (http/run-server routes/handler {:port PORT}))]
    (log/info (str "Server running on http://localhost:" PORT))
    (assoc app ::server server)))

(defn stop [app]
  (when-let [server (::server app)]
    (log/info "Stopping HTTP Service")
    (server :timeout 100))
  (dissoc app ::server))

(def service (app/service 'bwawan.http/start 'bwawan.http/stop))
