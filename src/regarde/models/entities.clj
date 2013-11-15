(ns regarde.models.entities
  (:require [korma.core :as sql]))

(declare rating-sets)

(sql/defentity users
  (sql/has-many rating-sets))

(sql/defentity rating-sets
  (sql/table :rating_sets)
  (sql/belongs-to users))
