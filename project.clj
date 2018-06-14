(defproject duct-doo "0.2.0"
  :description "An integrant multimethod for running ClojureScript tests with doo"
  :url "https://github.com/daveduthie/duct-doo"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[doo "0.1.10"]]
  :src-paths ["src"]
  :profiles {:dev {:dependencies   [[org.clojure/clojure "1.9.0"]
                                    [org.clojure/clojurescript "1.10.238"]
                                    [integrant "0.7.0-alpha2"]]
                   :resource-paths ["dev/resources"]
                   :src-paths      ["dev" "test"]}})
