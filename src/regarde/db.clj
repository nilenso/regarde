(ns regarde.db
  (:require [korma.db :as sql]
            [ragtime.core :refer [connection migrate-all applied-migrations]]
            [ragtime.sql.files]
            [environ.core]))

(defn db-name [env]
  (if-let [database-name (environ.core/env :database-name)]
    database-name
    (cond
     (= env :development) "regarde_dev"
     (= env :test) "regarde_test"
     (= env :production) "regarde_prod")))

(defn- ragtime-db-connection [env]
  (let [user (or (environ.core/env :database-user) "postgres")
        password (or (environ.core/env :database-password) "")
        host (or (environ.core/env :database-host) "localhost")
        subname (str "//" host ":5432/" (db-name env))]
    (ragtime.sql.database.SqlDatabase. {}
                                       {:classname "org.postgresql.Driver"
                                        :subprotocol "postgresql"
                                        :subname subname
                                        :user user
                                        :password password})))

(defn setup [env]
  (sql/defdb database
    (sql/postgres {:db (db-name env)
                   :user (or (environ.core/env :database-user) "postgres")
                   :password (or (environ.core/env :database-password) "")
                   :host (or (environ.core/env :database-host) "localhost")
                   :port "5432"})))

(defn perform-migration [env]
  (let [connection (ragtime-db-connection env)
        migrations (ragtime.sql.files/migrations)]
    (migrate-all connection migrations)))

(defn teardown [env]
  (let [connection (ragtime-db-connection env)
        applied-migrations (reverse (applied-migrations connection))
        migrations (ragtime.sql.files/migrations)]
    (when (> (count applied-migrations) 0)
      (doseq [migration migrations]
        (ragtime.core/rollback connection migration)))))
