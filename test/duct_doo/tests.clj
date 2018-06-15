(ns duct-doo.tests
  (:require
   [clojure.test :as t :refer [deftest is]]
   [duct-doo.runner :as runner]
   [integrant.core :as ig]
   [clojure.java.io :as io]))

(deftest foo->doo-test
  (let [config {::runner/test {:compiler-opts {:main "duct-doo.test-runner"}
                               :doo-opts      {}}}
        result (runner/test! (::runner/test config))]
    (is (= 0 (:exit result)))))

(deftest foo->doo-test-2
  (let [config {::runner/test {:compiler-opts {:main "duct-doo.test-runner"}
                               :doo-opts      {:js-env :phantom}}}
        result (runner/test! (::runner/test config))]
    (is (= 0 (:exit result)))))

(deftest foo->doo-test-3
  (let [config {::runner/test {:compiler-opts {:main    "duct-doo.test-runner"
                                               :target  :nodejs
                                               ;; :verbose true
                                               }
                               :doo-opts      {:js-env :node}}}
        result (runner/test! (::runner/test config))]
    (is (= 0 (:exit result)))))
