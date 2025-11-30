(ns bwawan.main-spec
  (:require [bwawan.http :as http]
            [bwawan.main :as sut]
            [bwawan.spec-helper :as helper]
            [c3kit.apron.app :as app]
            [c3kit.apron.log :as log]
            [speclj.core :refer :all]))

(describe "Main"
  (with-stubs)
  (helper/capture-logs-around)

  (redefs-around [sut/add-shutdown-hook (stub :add-shutdown-hook)
                  shutdown-agents       :shutdown-agents
                  app/start!            (stub :app/start!)
                  app/stop!             (stub :app/stop!)])

  (it "services"
    (should= [http/service] sut/services))

  (it "logging"
    (sut/-main)
    (should-contain "Starting bwawan.com" (log/captured-logs-str))
    (should-contain "Setting log level: :warn" (log/captured-logs-str)))

  (it "starts all services"
    (sut/-main)
    (should-have-invoked :app/start! {:with [sut/services]}))

  (it "stops all services on shutdown"
    (sut/-main)
    (should-have-invoked :add-shutdown-hook {:with [sut/stop-all!]})
    (sut/stop-all!)
    (should-have-invoked :app/stop! {:with [sut/services]}))

  (it "shuts down agents on shutdown"
    (sut/-main)
    (should-have-invoked :add-shutdown-hook {:with [:shutdown-agents]}))
  )
