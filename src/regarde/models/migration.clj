(ns regarde.models.migration
  (:require [clojure.java.jdbc :as j]
            [clojure.java.jdbc.sql :as s]))

(defn create-exercises [db]
  (j/create-table :exercises
                    [:id :serial "PRIMARY KEY"]
                    [:name :varchar "NOT NULL"]
                    [:created_at :timestamp "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]))

(defn create-users [db]
  (j/create-table :users
                    [:id :serial "PRIMARY KEY"]
                    [:name :varchar "NOT NULL"]
                    [:email :varchar "NOT NULL"]
                    [:created_at :timestamp "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]))

(defn drop-tables [db]
  (j/drop-table :users)
  (j/drop-table :exercises))

(defn -main []
  (let [db (sql/get-connection (System/getenv "REGARDE_DATABASE_URL"))]
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
