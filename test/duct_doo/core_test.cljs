(ns duct-doo.core-test
  (:require [duct-doo.core :as sut]
            [cljs.test :as t :include-macros true]))

(t/deftest foo-test
  (t/is (= (sut/foo 10) 11)))
