(ns regarde.models.user
  (:require [korma.core :as sql]))

(sql/defentity users
  (has-may regarde.models.rating-sets))

(defn create-user [user-attrs]
  (let [created-user-id (sql/insert users (sql/values (select-keys user-attrs [:name :email])))]
    (find created-user-id)))

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

(defn completed [exercise]
  (sql/select users
              (sql/join regarde.models.rating-set/rating-sets (= :rating-sets.user_id :id))
              (sql/where {:rating-sets.exercise_id (:id exercise)})))
