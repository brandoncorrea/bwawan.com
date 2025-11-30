(ns bwawan.spec-helper
  (:require [c3kit.apron.log :as log]
            [speclj.core :refer [around it]]))

(defn capture-logs-around []
  (around [it] (log/capture-logs (it))))
