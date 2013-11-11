(ns regarde.models.exercise
  (:require [korma.core :as sql]))

(sql/defentity exercises)

(defn create-exercise [exercise-attrs]
  (sql/insert exercises (sql/values (select-keys exercise-attrs [:name]))))

(defn list []
  (sql/select exercises))
