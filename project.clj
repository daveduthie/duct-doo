(defproject duct-doo "0.1.0-SNAPSHOT"
  :description "An integrant multimethod for running ClojureScript tests with doo"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[doo "0.1.10"]]
  :src-paths ["src" "test"]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.9.0"]
                                  [org.clojure/clojurescript "1.10.238"]
                                  [integrant "0.7.0-alpha2"]]}})
