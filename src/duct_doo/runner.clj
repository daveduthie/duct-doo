(ns duct-doo.runner
  (:require
   [cljs.build.api :as b]
   [clojure.string :as str]
   [doo.core :as doo]
   [integrant.core :as ig]))

(def base-doo-opts
  {:js-env  :chrome-headless
   :paths   {:karma "karma --port=9000"}
   :verbose false
   :debug   false})

(defn doo
  [opts]
  (prn ::doo)
  (let [{:keys [js-env compiler-opts] :as doo-opts} (merge base-doo-opts opts)
        result                                      (doo/run-script js-env compiler-opts doo-opts)]
    (when-not (= (:exit result) 0) (println (:out result)))
    result))

(def default-src-paths ["src" "test"])
(def path (System/getProperty "user.dir"))

(def base-compiler-opts
  {:output-dir     "target/test"
   :output-to      "target/test/tests.js"
   :asset-path     "target/test"
   :verbose        false ; set to true to see what's being recompiled
   :compiler-stats true
   :cache-analysis true
   :aot-cache      true})

(defn merge-compiler-opts
  "Doo seems to automatically make asset path absolute when targeting node"
  [base-compiler-opts compiler-opts node?]
  (-> base-compiler-opts
      (merge compiler-opts)
      ;; Ensure asset path is absolute for the sake of the Karma test runner
      (update :asset-path (fn [p]
                            (if (or node? (str/starts-with? p "/"))
                              p
                              (str path "/" p))))))

(defn compile!
  [src-paths opts]
  (b/build
   (apply b/inputs (or src-paths default-src-paths))
   opts))

(defn compile-and-test!
  [{:keys [src-paths compiler-opts doo-opts]}]
  (let [node? (= (:js-env doo-opts) :node)
        opts  (merge-compiler-opts base-compiler-opts compiler-opts node?)]
    (compile! src-paths opts)
    (doo (assoc doo-opts :compiler-opts opts))))

;; Example config:
;; 
;; :duct-doo.runner/test
;; {:src-paths     ["src" "test"]
;;  :no-op?        false
;;  :doo-opts      {:paths  {:karma "karma --port=9000"}
;;                  :js-env :chrome-headless}
;;  :compiler-opts {:main "my-project.test-runner"}}

;; src-paths -> where cljs sources live, default src + test
;; compiler-opts -> passed through to CLJS compiler
;;                  (note that :main keys is required)
;; doo-opts -> options to pass through to doo
;;             (only :js-env key is treated specially)
;; karma-port -> port to run karma server on

(defn test!
  [{:keys [no-op? compiler-opts] :as opts}]
  (cond
    (not (:main compiler-opts)) (throw (IllegalArgumentException. "No :main key specified"))
    no-op?                      (prn ::no-op)
    :else                       (compile-and-test! opts)))

(defmethod ig/init-key ::test
  [_ opts]
  (test! opts))

(defmethod ig/halt-key! ::test
  [_ _])
