(ns bwawan.layout-spec
  (:require [bwawan.layout :as sut]
            [clojure.java.io :as io]
            [speclj.core :refer :all]))

(describe "Layout"
  (with-stubs)
  (before (reset! sut/body nil))

  (redefs-around [slurp (stub :slurp {:return "the file content"})])

  (it "default"
    (let [response (sut/default nil)]
      (should= 200 (:status response))
      (should= {"Content-Type" "text/html"} (:headers response))
      (should= "the file content" (:body response))
      (should-have-invoked :slurp {:with [(io/resource "public/index.html")]})))

  (it "not-found"
    (let [response (sut/not-found nil)]
      (should= 404 (:status response))
      (should= {"Content-Type" "text/html"} (:headers response))
      (should= "the file content" (:body response))
      (should-have-invoked :slurp {:with [(io/resource "public/index.html")]})))

  (it "subsequent calls cache index.html content"
    (sut/default nil)
    (sut/not-found nil)
    (should-have-invoked :slurp {:times 1}))

  )
