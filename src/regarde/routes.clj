(ns regarde.routes
  (:require [clojure.java.io :as io]
            [compojure.core :refer [ANY GET POST defroutes]]
            [compojure.route :as route]
            [regarde.controllers.exercises :as exercises]
            [regarde.controllers.rating-sets :as rating-sets]
            [regarde.drawbridge :as drawbridge]))

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
       (exercises/show-exercise id request))
  (GET "/exercises/:id/rating_set/new" [id :as request]
       (rating-sets/new id request))
  (GET "/exercises/:id/rating_set/edit" [id :as request]
       (rating-sets/edit id request))
  (POST "/exercises/:id/rating_set" [id]
        rating-sets/create)
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))
