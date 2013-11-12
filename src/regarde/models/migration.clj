(ns regarde.models.migration
  (:require [clojure.java.jdbc :as j]
            [clojure.java.jdbc.sql :as s]))

(defn create-exercises [db]
  (j/db-do-commands db (str "create table exercises ("
                            " id serial PRIMARY KEY,"
                            " name varchar NOT NULL,"
                            " email varchar NOT NULL,"
                            " created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP);")))

(defn create-users [db]
  (j/db-do-commands db (str "create table users ("
                         " id serial PRIMARY KEY,"
                         " name varchar NOT NULL,"
                         " email varchar NOT NULL,"
                         " created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP);")))

(defn drop-tables [db]
  (j/db-do-commands db "drop table if exists users;")
  (j/db-do-commands db "drop table if exists exercises;"))

(defn -main [] 
  (let [db-spec (System/getenv "REGARDE_DATABASE_URL")
        db {:connection (j/get-connection db-spec)}]
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
    (.close (:connection db))))
