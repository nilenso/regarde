(ns regarde.models.rating
  (:require [korma.core :as sql]
            [regarde.models.entities :as entities]))

(defn users-done [exercise]
  (sql/select entities/users
              (sql/join entities/rating-sets (= :rating_sets.users_id :id))
              (sql/where {:rating_sets.exercises_id (:id exercise)})))

(defn users-not-done [exercise]
  (let [all (sql/select entities/users)
        done (users-done exercise)]
    (clojure.set/difference (set all) (set done))))

(defn find [set-id user-id]
  (first (sql/select entities/ratings (sql/where {:rating_sets_id (Integer. set-id)
                                         :users_id (Integer. user-id)}))))

(defn create [set rating-user-id rating]
  (sql/insert entities/ratings (sql/values [{:rating (Integer.  rating)
                                    :rating_sets_id (Integer. (:id set))
                                    :users_id (Integer.  rating-user-id) }])))

(defn update [rating new-rating-value]
  (println "updating rating"))

(defn update-or-create [set user-id value]
  (if-let [rating (find (:id set) user-id)]
    (update rating value)
    (create set user-id value)))

(defn all [] ;; duplication ;; How do I dry this up across models?
  (sql/select entities/ratings))

(defn normalize-rating 
  "Normalize a rating against a given total."
  [rating total]
  (assoc rating :rating (/ (:rating rating) total)))

(defn normalize 
  "Normalize a set of ratings."
  [ratings]
  (let [total (reduce + (map :rating ratings))]
    (map #(normalize-rating % total) ratings)))
