(ns regarde.controllers.ratings
  (:require [regarde.helpers :as helpers]
            [regarde.models.rating :as rating]
            [regarde.models.rating-set :as rating-set]
            [regarde.models.user :as user]
            [regarde.templates :as templates]
            [ring.util.response :as resp]))

(defn create-ratings [request]
  (let [current-user (helpers/current-user request)]
    (let [set (rating-set/find-or-create (:id current-user) (:id (:params request))) ]
      (doseq [r (:rating (:params request))]
        (rating/update-or-create set (first r) (second r)))))
  (resp/redirect "/"))

(defn new-exercise-ratings [exercise request]
  (let [users (user/all-except (helpers/current-user request))]
    (templates/new-ratings exercise users)))

(defn edit-exercise-ratings [exercise request]
  (let [users (user/all-except (helpers/current-user request))
        current-user (helpers/current-user request)
        rating-set (rating-set/find (:id current-user) (:id exercise))]
    (templates/edit-ratings exercise rating-set users)))
