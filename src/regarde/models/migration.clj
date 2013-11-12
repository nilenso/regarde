(ns regarde.models.migration
  (:require [clojure.java.jdbc :as j]
            [clojure.java.jdbc.sql :as s]))

(defn create-exercises [db]
  (j/db-do-commands db (str "create table exercises ("
                            "id serial PRIMARY KEY"
                            "name varchar NOT NULL"
                            "email varchar NOT NULL"
                            "created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP"))
  ;; (j/create-table :exercises
  ;;                   [:id :serial "PRIMARY KEY"]
  ;;                   [:name :varchar "NOT NULL"]
  ;;                   [:created_at :timestamp "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"])
  )

(defn create-users [db]
  (j/db-do-commands db (str "create table users ("
                         "id serial PRIMARY KEY"
                         "name varchar NOT NULL"
                         "email varchar NOT NULL"
                         "created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP);"))
  ;; (j/create-table :users
  ;;                   [:id :serial "PRIMARY KEY"]
  ;;                   [:name :varchar "NOT NULL"]
  ;;                   [:email :varchar "NOT NULL"]
  ;;                   [:created_at :timestamp "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"])
  )

(defn drop-tables [db]
  (j/db-do-commands db "drop table users;")
  (j/db-do-commands db "drop table exercises;"))

(defn -main []
  (println (str "hi? " (System/getenv "REGARDE_DATABASE_URL")))
  (let [db-spec {:connection-uri (System/getenv "REGARDE_DATABASE_URL")}
        db (j/get-connection db-spec)]
    (println "Deleting tables...")
    (drop-tables db)
    (println "Creating database structure...")
    (println "Creating exercises...")
    (create-exercises db)
    (println "Created exercises...")
    (println "Creating database structure...")
    (create-users db)
    (println "Created users...")
    (println " done")
    (.close db)))
