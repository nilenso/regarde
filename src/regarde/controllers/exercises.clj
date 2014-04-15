(ns regarde.controllers.exercises
  (:require [regarde.helpers :as helpers]
            [regarde.models.exercise :as exercise]
            [regarde.models.rating-set :as rating-set]
            [regarde.models.user :as user]
            [regarde.templates :as templates]
            [ring.util.response :as resp]))

(defn new-exercise [request]
  (templates/new-exercise))

(defn create-exercise [request]
  (exercise/create-exercise (:params request))
  (resp/redirect "/exercises"))

(defn list-exercises [request]
  (templates/exercises (exercise/all)))

(defn show-exercise [exercise-id request]
  (let [users (user/all)
        exercise (exercise/find exercise-id)]
    (if (exercise/complete? (exercise/users-done exercise) users)
      (templates/complete-exercise exercise
                                   (rating-set/summarize exercise))
      (templates/incomplete-exercise exercise
                                     (exercise/users-done exercise)
                                     (exercise/users-not-done exercise)
                                     (helpers/current-user request)))))
