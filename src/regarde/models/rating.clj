(ns regarde.models.rating
  (:require [clojure.string :as str]
            [korma.core :as sql]
            [regarde.models.entities :as entities]
            [regarde.models.errors :as errors])
  (:refer-clojure :exclude [find]))

(def errors (errors/generate-error-check [[#(str/blank? (:rating %)) "Rating cannot be blank."]]))

(defn users-done [exercise]
  (sql/select entities/users
              (sql/join entities/rating-sets (= :rating_sets.users_id :id))
              (sql/where {:rating_sets.exercises_id (:id exercise)})))

(defn users-not-done [exercise]
  (let [all (sql/select entities/users)
        done (users-done exercise)]
    (clojure.set/difference (set all) (set done))))

(defn find [set-id user-id]
  (first (sql/select entities/ratings
                     (sql/where {:rating_sets_id (Integer. set-id)
                                 :users_id (Integer. user-id)}))))

(defn create [attrs]
  (if-let [errors (errors attrs)]
    (assoc attrs :errors errors)
    (sql/insert entities/ratings (sql/values [{:rating (Integer.  (:rating attrs))
                                               :rating_sets_id (Integer. (:rating-set-id attrs))
                                               :users_id (Integer.  (:user-id attrs)) }]))))

(defn update [rating attrs]
  (sql/update
   entities/ratings
   (sql/set-fields {:rating (Integer. (:rating attrs))})
   (sql/where {:id (:id rating)})))

(defn update-or-create [set user-id attrs]
  (if-let [errors (errors attrs)]
    (assoc attrs :errors errors)
    (if-let [rating (find (:id set) user-id)]
      (update rating attrs)
      (create (assoc attrs :rating-sets-id (:id set) :users-id user-id)))))

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
