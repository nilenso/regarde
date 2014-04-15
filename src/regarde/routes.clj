(ns regarde.routes
  (:require [clojure.java.io :as io]
            [compojure.core :refer [ANY GET POST defroutes]]
            [compojure.route :as route]
            [regarde.controllers.exercises :as exercises]
            [regarde.controllers.ratings :as ratings]
            [regarde.drawbridge :as drawbridge]
            [regarde.models.exercise :as exercise]))

(defroutes app
  (ANY "/repl" {:as req}
       (drawbridge/handler req))
  (GET "/" []
       exercises/list-exercises)
  (GET "/exercises/new" []
       exercises/new-exercise)
  (POST "/exercises" []
        exercises/create-exercise)
  (GET "/exercises" []
       exercises/list-exercises)
  (GET "/exercises/:id" [id :as request]
       (let [ex (exercise/find id)]
         (exercises/show-exercise ex request)))
  (GET "/exercises/:id/rating_sets/new" [id :as request]
       (let [ex (exercise/find id)]
         (ratings/new-exercise-ratings ex request)))
  (GET "/exercises/:id/rating_sets/edit" [id :as request]
       (let [ex (exercise/find id)]
         (ratings/edit-exercise-ratings ex request)))
  (POST "/exercises/:id/ratings" [id]
        ratings/create-ratings)
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))
