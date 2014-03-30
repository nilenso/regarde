(ns regarde.models.user
  (:require [korma.core :as sql]
            [regarde.models.entities :as entities]))

(defn all []
  (sql/select entities/users))

(defn find-user [user-attrs]
  (when-let [user (first (sql/select entities/users
                                      (sql/where {:email (:email user-attrs)})))] user))

(defn create-user [user-attrs]
  (sql/insert entities/users (sql/values (select-keys user-attrs [:name :email]))))

(defn rate [exercise user rating]
  (println "rating user" (:email user) " with rating : " rating "for exercise" (:name exercise)))

(defn find-or-create-user [user-attrs]
  (if-let [user (find-user user-attrs)]
    user
    (create-user user-attrs)))

(defn all-except [user]
  (sql/select entities/users
              (sql/where {:id [not= (:id user)]})))

(defn completed [exercise]
  (sql/select entities/users
              (sql/with entities/rating-sets)
              (sql/join entities/rating-sets (= :rating_sets.users_id :id))
              (sql/where {:rating_sets.exercise_id (:id exercise)})))
