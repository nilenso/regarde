(ns regarde.templates
  (:require [net.cgrand.enlive-html :as html]))

(html/deftemplate new-user "regarde/templates/new-user.html" [])

(html/defsnippet user-snippet "regarde/templates/users.html"
  [:li]
  [user]
  [:li] (html/content (:name user)))

(html/deftemplate users "regarde/templates/users.html"
  [users]
  [:head :title] (html/content  "Nilenso | List Of Users")
  [:ul] (html/content (map #(user-snippet %) users)))

(html/deftemplate new-exercise "regarde/templates/new-exercise.html" [])

(html/defsnippet exercise-snippet "regarde/templates/exercises.html"
  [:li]
  [exercise]
  [:li] (html/content (:name exercise)))

(html/deftemplate exercises "regarde/templates/exercises.html"
  [exercises]
  [:head :title] (html/content "Nilenso | List of Exercises")
  [:ul](html/content (map #(exercise-snippet %) exercises)))
