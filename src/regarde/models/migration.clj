(ns regarde.models.migration
  (:require [clojure.java.jdbc :as sql]))

(defn create-users []
  (sql/with-connection (System/getenv "REGARDE_DATABASE_URL")
    (sql/create-table :users
      [:id :serial "PRIMARY KEY"]
      [:name :varchar "NOT NULL"]
      [:created_at :timestamp "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"])))

(defn -main []
  (println "Creating database structure...")
  (create-users)
  (println " done"))
