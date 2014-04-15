(ns regarde.controllers.exercises
  (require [regarde.templates :as templates]
           [regarde.models.exercise :as exercise]
           [regarde.models.user :as user]
           [regarde.models.rating-set :as rating-set]
           [ring.util.response :as resp]
           [regarde.helpers :as helpers]))

(defn new-exercise [request]
  (templates/new-exercise))

(defn create-exercise [request]
  (exercise/create-exercise (:params request))
  (resp/redirect "/exercises"))

(defn list-exercises [request]
  (templates/exercises (exercise/all)))

(defn show-exercise [exercise request]
  (let [users (user/all)]
    (if (exercise/complete? (exercise/users-done exercise) users)
      (templates/complete-exercise exercise
                                   (rating-set/summarize exercise))
      (templates/incomplete-exercise exercise
                                     (exercise/users-done exercise)
                                     (exercise/users-not-done exercise)
                                     (helpers/current-user request)))))
