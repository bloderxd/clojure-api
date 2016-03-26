(ns clojure-api.api
      (:import com.mchange.v2.c3p0.ComboPooledDataSource)
      (:require cheshire.core)
      (:require ring.util.response)
      (:require [compojure.handler :as handler]
                [ring.middleware.json :as middleware]
                [clojure.java.jdbc :as sql]
                [compojure.route :as route]))

(def db-config
      {:classname "org.h2.Driver"
       :subprotocol "h2"
       :subname "mem:users"
       :user ""
       :password ""})

(defn pool
      [config]
      (let [cpds (doto (ComboPooledDataSource.)
                   (.setDriverClass (:classname config))
                   (.setJdbcUrl (str "jdbc:" (:subprotocol config) ":" (:subname config)))
                   (.setUser (:user config))
                   (.setPassword (:password config))
                   (.setMaxPoolSize 6)
                   (.setMinPoolSize 1)
                   (.setInitialPoolSize 1))]
        {:datasource cpds}))

    (def pooled-db (delay (pool db-config)))

    (defn db-connection [] @pooled-db)

(sql/with-connection (db-connection)
    (sql/create-table :users [:id "varchar(256)" "primary key"]
                             [:name "varchar(1024)"]
                             [:age :varchar]))

(defn get-users []
       (response
         (sql/with-connection (db-connection)
           (sql/with-query-results results
             ["select * from users"]
             (into [] results)))))

 (defroutes app-routes
     (context "/users" []
       (GET "/" [] (get-users))
       (POST "/" {body :body} (create-new-user body))
       (route/not-found "Not Found")))

(def app
    (-> (handler/api app-routes)
        (middleware/wrap-json-body)
        (middleware/wrap-json-response)))
