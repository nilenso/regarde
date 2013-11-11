(ns regarde.models.user
    (:require [clojure.java.jdbc :as sql]))

(defn create-user [user-attrs]
  (sql/with-connection "postgresql://localhost:5432/regarde"
    (sql/insert-record :users {:name (:name user-attrs)})))

(defn list []
  (sql/with-connection "postgresql://localhost:5432/regarde"
    (sql/with-query-results results
      ["select * from users"]
      (into [] results))))

