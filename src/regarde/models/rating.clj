(ns regarde.models.rating
  (:require [korma.core :as sql]))

(sql/defentity ratings)

(defn users-completed [exercise]
  [])

(defn find [set-id user-id]
  (first (sql/select ratings (sql/where {:set_id (Integer. set-id)
                                         :user_id (Integer. user-id)}))))

(defn create [set rating-user-id rating]
  (sql/insert ratings (sql/values [{:rating (Integer.  rating)
                                    :set_id (Integer. (:id set))
                                    :user_id (Integer.  rating-user-id) }])))

(defn update [rating new-rating-value]
  (println "updating rating"))

(defn update-or-create [set user-id value]
  (if-let [rating (find (:id set) user-id)]
    (update rating value)
    (create set user-id value)))

(defn all [] ;; duplication ;; How do I dry this up across models?
  (sql/select ratings))
