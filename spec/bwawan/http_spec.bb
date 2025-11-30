(ns bwawan.http-spec
  (:require [bwawan.http :as sut]
            [bwawan.routes :as routes]
            [bwawan.spec-helper :as helper]
            [c3kit.apron.log :as log]
            [clojure.string :as str]
            [org.httpkit.server :as http]
            [speclj.core :refer :all]))

(describe "HTTP"
  (with-stubs)
  (helper/capture-logs-around)

  (redefs-around [http/run-server (stub :run-server {:return :the-server})])

  (it "starts a server"
    (let [start (resolve (:start sut/service))
          app   (start {})]
      (should= {::sut/server :the-server} app)
      (should-start-with "Starting HTTP Service" (log/captured-logs-str))
      (should-contain "Server running on http://localhost:8000" (log/captured-logs-str))
      (should-have-invoked :run-server {:with [routes/handler {:port 8000}]})))

  (it "starts a server that is already running"
    (let [start (resolve (:start sut/service))
          app   (start (start {}))]
      (should= {::sut/server :the-server} app)
      (should-have-invoked :run-server {:times 1})))

  (it "stops a server"
    (let [stop (-> sut/service :stop resolve)
          app  (stop {::sut/server (stub :stop-server)})]
      (should= {} app)
      (should= "Stopping HTTP Service" (log/captured-logs-str))
      (should-have-invoked :stop-server {:with [:timeout 100]})))

  (it "stops a server that hasn't been started"
    (let [stop (-> sut/service :stop resolve)
          app  (stop {})]
      (should= {} app)
      (should-be str/blank? (log/captured-logs-str))))

  )