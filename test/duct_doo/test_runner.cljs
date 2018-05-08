(ns duct-doo.test-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [duct-doo.core-test]))

(doo-tests 'duct-doo.core-test)


