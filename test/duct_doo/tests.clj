(ns duct-doo.tests
  (:require
   [clojure.test :as t :refer [deftest is]]
   [duct-doo.runner :as runner]
   [integrant.core :as ig]
   [clojure.java.io :as io]))

(def config
  (ig/read-string (-> "config.edn" io/resource slurp)))

;; (clojure.pprint/pprint config)

;; (clojure.pprint/pprint (System/getProperty "java.class.path"))

(def result
  (runner/test! (:duct-doo.runner/test config)))

(deftest foo->doo-test
  (is (= 0 (:exit result))))
