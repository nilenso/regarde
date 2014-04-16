(ns regarde.controllers.rating-sets
  (:require [regarde.helpers :as helpers]
            [regarde.models.rating :as rating]
            [regarde.models.rating-set :as rating-set]
            [regarde.models.user :as user]
            [regarde.models.exercise :as exercise]
            [regarde.templates :as templates]
            [ring.util.response :as resp]))

(defn create [request]
  (let [current-user (helpers/current-user request)
        ratings (map
                 (fn [[user-id rating]] {:user-id user-id :rating rating})
                 (:rating (:params request)))]
    (rating-set/create {:user-id (:id current-user)
                        :exercise-id (:exercise-id (:params request))
                        :ratings ratings})
    (resp/redirect "/")))

(defn update [request]
  (let [current-user (helpers/current-user request)
        set (rating-set/find (:id current-user) (:exercise-id (:params request)))
        ratings (map
                 (fn [[user-id rating]] {:user-id user-id :rating rating})
                 (:rating (:params request)))]
    (rating-set/update set {:ratings ratings})
    (resp/redirect "/")))

(defn new [exercise-id request]
  (let [users (user/all-except (helpers/current-user request))
        exercise (exercise/find exercise-id)]
    (templates/new-ratings exercise users)))

(defn edit [exercise-id request]
  (let [exercise (exercise/find exercise-id)
        users (user/all-except (helpers/current-user request))
        current-user (helpers/current-user request)
        rating-set (rating-set/find (:id current-user) (:id exercise))]
    (templates/edit-ratings exercise rating-set users)))
