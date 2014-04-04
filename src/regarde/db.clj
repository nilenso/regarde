(ns regarde.db
  (:require [korma.db :as sql]
            [ragtime.core :refer [connection migrate-all applied-migrations]]
            [ragtime.sql.files]
            [environ.core]))

(defn db-name [env]
  (if-let [env-database (environ.core/env :regarde-database-url)]
    env-database
    (str "jdbc:postgresql://localhost:5432/"
         (cond
          (= env :development) "regarde_dev"
          (= env :test) "regarde_test"
          (= env :production) "regarde_prod"))))

(defn setup [env]
  (sql/defdb database (db-name env)))

(defn perform-migration [env]
  (let [connection (connection (db-name env))
        migrations (ragtime.sql.files/migrations)]
    (migrate-all connection migrations)))

(defn teardown [env]
  (let [connection (connection (db-name env))
        applied-migrations (reverse (applied-migrations connection))
        migrations (ragtime.sql.files/migrations)]
    (when (> (count applied-migrations) 0)
      (doseq [migration migrations]
        (ragtime.core/rollback connection migration)))))
