(ns regarde.models.user-expectations
  (:use [expectations])
  (:require [regarde.models.user :as user]
            [regarde.models.entities :as entities]
            [korma.core :as sql]
            [regarde.db :as db]))

(defmacro test-with-db [& forms]
  `(do
     (db/setup :test)
     (db/teardown :test)
     (db/perform-migration :test)
     ~@forms))

(defn create-user [attrs]
  (sql/insert entities/users
              (sql/values (merge {:name "John" :email "john@example.com"} attrs))))

(test-with-db
 (let [first-user (create-user {:name "Tim"})
       second-user (create-user {:name "Asif"})
       third-user (create-user {:name "John"})]
   (expect [first-user second-user] (user/all-except third-user))))
