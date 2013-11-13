(ns regarde.models.exercise
  (:require [korma.core :as sql]))

(sql/defentity exercises)

(defn create-exercise [exercise-attrs]
  (sql/insert exercises (sql/values (select-keys exercise-attrs [:name]))))

(defn all []
  (sql/select exercises))

(defn find [exercise-id]
  (first (sql/select exercises
                     (sql/where {:id (Integer. exercise-id)}))))
