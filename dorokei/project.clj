(defproject dorokei "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :eftest {:report eftest.report.pretty/report}
  :profiles {:dev {:plugins [[lein-eftest "0.3.1"]]}})
