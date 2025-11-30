(ns bwawan.routes-spec
  (:require [speclj.core :refer :all]
            [bwawan.routes :as sut]))

(require 'bwawan.blog
         'bwawan.layout)

(defmacro test-handler [uri method handler]
  `(it (str ~uri " -> " '~handler)
     (with-redefs [~handler (stub :handler)]
       (sut/handler {:uri ~uri :request-method ~method})
       (should-have-invoked :handler))))

(describe "Routes"
  (with-stubs)

  (test-handler "/" :get bwawan.layout/default)
  (test-handler "/posts/FOO" :get bwawan.blog/render)
  (test-handler "/blah" :get bwawan.layout/not-found)
  )
