(ns regarde.models.user
  (:require [korma.core :as sql]))

(sql/defentity users
  (has-many ratings))

(defn create-user [user-attrs]
  (sql/insert users (sql/values (select-keys user-attrs [:name :email]))))

(defn all []
  (sql/select users))

(defn find [id]
  (first (sql/select users
                     (sql/where {:id (Integer. id)}))))

(defn rate [exercise user rating]
  (println "rating user" (:email user) " with rating : " rating "for exercise" (:name exercise)))

(defn find-or-create-user [user-attrs]
  (if-let [user (first (-> (sql/select* users) (sql/where {:email (:email user-attrs)}) (sql/exec)))]
    user
    (create-user user-attrs)))
