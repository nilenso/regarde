(ns regarde.models.exercise
  (:require [korma.core :as sql]))

(sql/defentity exercises)

(defn create-exercise [exercise-attrs]
  (sql/insert exercises (sql/values (select-keys exercise-attrs [:name]))))

(defn all []
  (sql/select exercises))

(defn complete? [exercise users]
  (let [[all-users completed-users]
        [
         (into [] (map #(:id %) users))
         (into [] (map #(:id %) (regarde.models.rating/users-completed exercise)))
         ]]
    (if (== 0 (compare all-users completed-users)) true false)))

(defn find [exercise-id]
  (first (sql/select exercises
                     (sql/where {:id (Integer. exercise-id)}))))
