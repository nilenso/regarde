(ns regarde.models.entities
  (:require [korma.core :refer [belongs-to defentity has-many table]]))

(declare rating-sets ratings exercises)

(defentity users
  (has-many rating-sets)
  (has-many ratings))

(defentity rating-sets
  (table :rating_sets)
  (belongs-to users)
  (has-many ratings)
  (belongs-to exercises))

(defentity ratings
  (belongs-to users)
  (belongs-to rating-sets))

(defentity exercises
  (has-many rating-sets))
