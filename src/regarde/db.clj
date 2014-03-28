(ns regarde.db
  (:require [korma.db :as sql]
            [ragtime.core :refer [connection migrate-all rollback]]
            [ragtime.sql.files :refer [migrations]]))

(defn db-name [env]
  (cond
   (= env :development) "regarde_dev"
   (= env :test) "regarde_test"
   (= env :production) "regarde_prod"))

(defn setup [env]
  (let [db-name (db-name env)]
    (sql/defdb database
      (sql/postgres {:db db-name
                     :user "postgres"
                     :password ""}))))

(defn perform-migration [env]
  (let [connection (connection (str "jdbc:postgresql://localhost:5432/" (db-name env)))
        migrations (migrations)]
    (migrate-all connection migrations)))

(defn teardown [env]
  (let [connection (connection (str "jdbc:postgresql://localhost:5432/" (db-name env)))
        migrations (reverse (migrations))]
    (doseq [migration migrations]
      (rollback connection migration))))
