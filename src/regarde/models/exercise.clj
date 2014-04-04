(ns regarde.models.exercise
  (:require [korma.core :as sql]
            [regarde.models.rating]
            [regarde.models.entities :as entities])
  (:refer-clojure :exclude [find]))

(defn create-exercise [exercise-attrs]
  (sql/insert entities/exercises (sql/values (select-keys exercise-attrs [:name]))))

(defn all []
  (sql/select entities/exercises))

(defn users-done [exercise]
  (regarde.models.rating/users-done exercise))

(defn users-not-done [exercise]
  (regarde.models.rating/users-not-done exercise))

(defn complete? [users-completed users]
  (let [[all-users completed-users]
        [(into [] (map #(:id %) users))
         (into [] (map #(:id %) users-completed))]]
    (== 0 (compare all-users completed-users))))

(defn find [exercise-id]
  (first (sql/select entities/exercises
                     (sql/where {:id (Integer. exercise-id)}))))
