(ns regarde.models.rating
  (:require [korma.core :as sql]
            [regarde.models.user :as user]
            [regarde.models.exercise :as exercise]))

(sql/defentity ratings
  (sql/pk :id)
  (sql/belongs-to user/users)
  (sql/belongs-to exercise/exercises))

(defn find [exercise-id user-id]
  (first (sql/select ratings (sql/where {:exercise_id (Integer.  exercise-id)
                                         :user_id (Integer. user-id)}))))

(defn create [exercise user_id rating]
  (sql/insert ratings (sql/values [{:rating (Integer.  rating) :exercise_id (Integer. (:id exercise))
                                    :user_id (Integer.  user_id) }])))

(defn update [rating new-rating-value]
  (println "updating rating"))

(defn update-or-create [exercise user-id value]
  (if-let [ rating (find (:id exercise) user-id)]
    (update rating value)
    (create exercise user-id value)))

(defn all [] ;; duplication ;; How do I dry this up across models?
  (sql/select ratings))
