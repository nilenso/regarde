(ns regarde.models.exercise
  (:require [korma.core :as sql]
            [regarde.models.rating]))

(sql/defentity exercises)

(defn create-exercise [exercise-attrs]
  (sql/insert exercises (sql/values (select-keys exercise-attrs [:name]))))

(defn all []
  (sql/select exercises))

(defn users-completed [exercise]
  (regarde.models.rating/users-completed exercise))

(defn complete? [users-completed users]
  (let [[all-users completed-users]
        [(into [] (map #(:id %) users))
         (into [] (map #(:id %) users-completed))]]
    (== 0 (compare all-users completed-users))))

(defn find [exercise-id]
  (first (sql/select exercises
                     (sql/where {:id (Integer. exercise-id)}))))
