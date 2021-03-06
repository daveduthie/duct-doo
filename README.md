# duct-doo

Multimethods for [Duct](https://github.com/duct-framework/duct) to run ClojureScript tests with [doo](https://github.com/bensu/doo).

## Installation

[![Clojars Project](https://img.shields.io/clojars/v/duct-doo.svg)](https://clojars.org/duct-doo)

Add these coordinates to your build tool: `[duct-doo "0.1.0"]`.

> There's presently (2018-05-10) an [issue](https://github.com/emezeske/lein-cljsbuild/issues/469) when trying to compile core.rrb-vector for ClojureScript. In the mean time, you should add `:exclusions [org.clojure/core.rrb-vector]` to your project.clj, and instead depend on `[quantum/org.clojure.core.rrb-vector "0.0.12"]`.

# Usage

Add the `:duct-doo.runner/test` key to your integrant config:

### Configure

```
;; Example
:duct-doo.runner/test
{:src-paths     ["src" "test"]
 :no-op?        false
 :doo-opts      {:paths  {:karma "karma --port=9000"}
                 :js-env :chrome-headless}
 :compiler-opts {:main "your-project.test-runner"}}
```

- src-paths -> where cljs sources live, default src + test
- compiler-opts -> passed through to CLJS compiler
    - see [ClojureScript - Compiler Options](https://clojurescript.org/reference/compiler-options#asset-path)
    - note that `:main` key is required
- doo-opts -> options to pass through to doo
    - see [doo library options](https://github.com/bensu/doo#library)
    - only `:js-env` key is treated specially

### Write a test runner

Write a CLJS file with the same namespace as you set under the `:main` key:

```
(ns your-project.test-runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [your-project.core-test]   ; CHANGE US
              [your-project.util-test])) ; etc...

(doo-tests 'your-project.core-test  ; CHANGE US TOO
           'your-project.util-test) ;
```

The test-runner could probably be moved inside the library, but I haven't figured out how to do that yet.

### Turning it off

If, for example, you're working on some backend code and you don't want your ClojureScript tests to be run every time you `(reset)` your REPL, set the `no-op?` key to true.

## License

Copyright © 2018 David Duthie

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
