(ns bwawan.main
  (:require [bwawan.http :as http]
            [c3kit.apron.app :as app]
            [c3kit.apron.log :as log]))

(def services [http/service])

(defn stop-all! [] (app/stop! services))

(defn add-shutdown-hook [f]
  (.addShutdownHook (Runtime/getRuntime) (Thread. f)))

(defn -main []
  (log/info "Starting bwawan.com")
  (log/set-level! :warn)
  (run! add-shutdown-hook [stop-all! shutdown-agents])
  (app/start! services))
