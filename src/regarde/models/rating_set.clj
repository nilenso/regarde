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

(defn normalized-rating-sets 
  "Take a collection of rating sets, normalize the ratings of each set,
   and return a single collection comprising all the normalized ratings."
  [rating-sets]
  ;; TODO: this flatten is cheap. it probably belongs in summarize-rating-sets.
  (flatten (map
            (fn [rating-set]
              (ratings/normalize (:ratings rating-set)))
            rating-sets)))

(defn fold-sum [left-rating right-rating]
  (assoc left-rating :rating (+ (:rating left-rating) (:rating right-rating))))

(defn aggregate-ratings 
  "Given collection of ratings, take their average."
  [ratings]
  (update-in (reduce fold-sum ratings) [:rating] #(/ % (count ratings))))

(defn summarize-rating-sets 
  "Take a collection of ratings, group them by user,
   and average all the ratings for each user."
  [ratings]
  (let [ratings-for-user (vals (group-by :users_id ratings))]
    (map aggregate-ratings ratings-for-user)))

(defn rating-sets
  "All rating sets (with their nested ratings) belonging to exercise."
  [exercise]
  (sql/select entities/rating-sets
              (sql/where {:exercises_id (:id exercise)})
              (sql/with entities/ratings (sql/with entities/users))))

(def summarize (comp summarize-rating-sets normalized-rating-sets rating-sets))
