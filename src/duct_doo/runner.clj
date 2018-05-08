(ns duct-doo.runner
  (:require
   [cljs.build.api :as b]
   [doo.core :as doo]
   [integrant.core :as ig]))

(defonce namespaces
  (atom #{}))

(def base-doo-opts
  {:js-env  :chrome-headless
   :paths   {:karma "karma --port=9000"}
   :verbose true
   :debug   false})

(defn doo
  [opts]
  (let [{:keys [js-env compiler-opts] :as doo-opts}
        (merge base-doo-opts opts)]
    (prn ::doo)
    (doo/run-script js-env compiler-opts doo-opts)))

(def default-src-paths ["src" "test"])
(def path (System/getProperty "user.dir"))

(def base-compiler-opts
  {:output-dir     "target/test"
   :output-to      "target/test/tests.js"
   :asset-path     "target/test"
   :verbose        false ; set to true to see what's being recompiled
   :compiler-stats true
   :cache-analysis true})

(defn merge-compiler-opts
  [base-compiler-opts compiler-opts]
  (-> base-compiler-opts
      (merge compiler-opts)
      ;; Ensure asset path is absolute for the sake of the Karma test runner
      (update :asset-path #(str path "/" %))))

(defn compile!
  [src-paths opts]
  (prn ::compile)
  (b/build
   (apply b/inputs (or src-paths default-src-paths))
   opts))

(defn compile-and-test!
  [{:keys [src-paths compiler-opts doo-opts]}]
  (let [opts (merge-compiler-opts base-compiler-opts compiler-opts)]
    (compile! src-paths opts)
    (doo (assoc doo-opts :compiler-opts opts))))

;; Example config:
;; 
;; :duct-doo.runner/test
;; {:src-paths     ["src" "test"]
;;  :no-op?        false
;;  :doo-opts      {:paths  {:karma "karma --port=9000"}
;;                  :js-env :chrome-headless}
;;  :compiler-opts {:main "my-project.ttest-runner"}}

;; src-paths -> where cljs sources live, default src + test
;; compiler-opts -> passed through to CLJS compiler
;;                  (note that :main keys is required)
;; doo-opts -> options to pass through to doo
;;             (only :js-env key is treated specially)
;; karma-port -> port to run karma server on

(defn test!
  [{:keys [no-op? compiler-opts test-namespaces] :as opts}]
  (reset! namespaces test-namespaces)
  (cond
    (not (:main compiler-opts))
    (throw (IllegalArgumentException. "No :main key specified"))
    no-op? (prn ::no-op)
    :else
    #_ (throw (ex-info "foo" {}))
    (compile-and-test! opts)))

(defmethod ig/init-key ::test
  [_ opts]
  (test! opts))

(defmethod ig/halt-key! ::test
  [_ _])
