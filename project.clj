(defproject clojure-api "1.0.0-SNAPSHOT"
  :description "REST API example made with closure s2"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.clojure/clojure "1.5.1"]
                 [ring/ring-core "1.2.1"]
                 [ring/ring-jetty-adapter "1.2.1"]
                 [ring/ring-json "0.2.0"]
                 [compojure "1.1.6"]
                 [c3p0/c3p0 "0.9.1.2"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [com.h2database/h2 "1.3.168"]
                 [cheshire "4.0.3"]]
      :plugins [[lein-ring "0.7.3"]]
      :ring {:handler clojure-api.api/app
             :nrepl {:start? true
                     :port 9998}}
      :profiles
      {:dev {:dependencies [[ring-mock "0.1.3"]]}})
