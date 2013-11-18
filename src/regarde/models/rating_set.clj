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

(defn rating-sets [exercise]
  (sql/select entities/rating-sets
              (sql/where {:exercises_id (:id exercise)})
              (sql/with entities/ratings (sql/with entities/users))))

(defn normalized-rating-sets [exercise]
  (let [sets-of-ratings (into [] (map #(:ratings %) (into [] (rating-sets {:id 1}))))]
    (into [] (map #(ratings/normalize %) sets-of-ratings))))

(defn reduce-user-ratings [ratings]
  (let [name (last (set (map #(:name %) ratings)))
        email (last (set (map #(:email %) ratings)))
        rating (/ (reduce + (map #(:rating %) ratings)) (count ratings))]
    {:name name :email email :rating rating}))

(defn reduce-ratings [ratings]
  (map #(reduce-user-ratings %) ratings))

(defn summarize-rating-sets [normalized-sets-of-rating]
  (let [ user-grouped-ratings (group-by :users_id normalized-sets-of-rating)]
         (let [user-ids (keys user-grouped-ratings) ratings (vals user-grouped-ratings)]
           (into [] (zipmap user-ids (reduce-ratings ratings))))))

(defn summarize [exercise]
  (summarize-rating-sets (flatten (normalized-rating-sets exercise))))
