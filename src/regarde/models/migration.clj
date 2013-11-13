(ns regarde.models.migration
  (:require [clojure.java.jdbc :as j]
            [clojure.java.jdbc.ddl :as ddl]))

(defn create-exercises [db]
  (j/db-do-commands db (str "create table exercises ("
                            " id serial PRIMARY KEY,"
                            " name varchar NOT NULL,"
                            " created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP);")))

(defn create-users [db]
  (j/db-do-commands db (str "create table users ("
                         " id serial PRIMARY KEY,"
                         " name varchar NOT NULL,"
                         " email varchar NOT NULL,"
                         " created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP);")))

(defn create-ratings [db]
  (j/db-do-commands db (ddl/create-table :ratings
                                         [:id :serial "PRIMARY KEY"]
                                         [:user_id :integer "NOT NULL"]
                                         [:exercise_id :integer "NOT NULL"]
                                         [:rating :integer "NOT NULL"])))

(defn drop-tables [db]
  (j/db-do-commands db (ddl/drop-table :users))
  (j/db-do-commands db (ddl/drop-table :ratings))
  (j/db-do-commands db (ddl/drop-table :exercises)))

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
    (println "Created users")
    (println "Creating ratings")
    (create-ratings db)
    (println "Created ratings")
    (println " done")
    (.close (:connection db))))
