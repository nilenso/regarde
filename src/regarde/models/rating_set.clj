(ns regarde.models.rating-set
  (:require [regarde.models.exercise :as exercise]
            [korma.core :as sql]
            [regarde.models.entities :as entities]
            [regarde.models.rating :as ratings]))

(defn find [user-id exercise-id]
  (first (sql/select entities/rating-sets
                     (sql/where {:exercises_id (Integer. exercise-id)
                                 :users_id (Integer. user-id)}))))

(defn create [user-id exercise-id]
  (sql/insert entities/rating-sets (sql/values [{:users_id (Integer.  user-id)
                                                 :exercises_id (Integer. exercise-id)}])))

(defn find-or-create [user-id exercise-id]
  (if-let [set (find user-id exercise-id)]
    set
    (create user-id exercise-id)))

(defn normalized-rating-sets [rating-sets]
  (let [sets-of-ratings (map #(:ratings %) rating-sets)]
    ;; TODO: this flatten is cheap. it probably belongs in summarize-rating-sets.
    (flatten (map ratings/normalize sets-of-ratings))))

(defn aggregate-ratings [ratings]
  (let [name (last (set (map #(:name %) ratings)))
        email (last (set (map #(:email %) ratings)))
        rating (/ (reduce + (map #(:rating %) ratings)) (count ratings))]
    {:name name :email email :rating rating}))

(defn summarize-rating-sets [ratings]
  (let [ratings-for-user (vals (group-by :users_id ratings))]
    (map aggregate-ratings ratings-for-user)))

(defn rating-sets [exercise]
  (sql/select entities/rating-sets
              (sql/where {:exercises_id (:id exercise)})
              (sql/with entities/ratings (sql/with entities/users))))

(def summarize (comp summarize-rating-sets normalized-rating-sets rating-sets))
