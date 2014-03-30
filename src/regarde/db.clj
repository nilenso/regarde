(ns regarde.db
  (:require [korma.db :as sql]
            [ragtime.core :refer [connection migrate-all applied-migrations]]
            [ragtime.sql.files]))

(defn db-name [env]
  (cond
   (= env :development) "regarde_dev"
   (= env :test) "regarde_test"
   (= env :production) "regarde_prod"))

(defn setup [env]
  (let [name (db-name env)]
    (sql/defdb database
      (sql/postgres {:db name
                     :user "postgres"
                     :password ""}))))

(defn perform-migration [env]
  (let [connection (connection (str "jdbc:postgresql://localhost:5432/" (db-name env)))
        migrations (ragtime.sql.files/migrations)]
    (migrate-all connection migrations)))

(defn teardown [env]
  (let [connection (connection (str "jdbc:postgresql://localhost:5432/" (db-name env)))
        applied-migrations (reverse (applied-migrations connection))
        migrations (ragtime.sql.files/migrations)]
    (when (> (count applied-migrations) 0)
      (doseq [migration migrations]
        (ragtime.core/rollback connection migration)))))
