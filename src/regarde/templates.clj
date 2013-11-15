(ns regarde.templates
  (:require [net.cgrand.enlive-html :as html]
            [regarde.models.exercise :as exercise]))

(html/deftemplate new-user "regarde/templates/new-user.html" [])

(html/defsnippet user-snippet "regarde/templates/users.html"
  [:li]
  [user]
  [:li] (html/content (:name user)))

(html/defsnippet rating-snippet "regarde/templates/new-ratings.html"
  [:li]
  [user exercise]
  [:li :span] (html/content (:name user))
  [:li :input] (html/set-attr :name "rating[]" :name (str "rating[" (:id user) "]")))

(html/deftemplate users "regarde/templates/users.html"
  [users current-user]
  [:head :title] (html/content  "Nilenso | List Of Users")
  [:h1] (html/content  (str "Hi " (:name current-user)))
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

(html/deftemplate new-ratings "regarde/templates/new-ratings.html"
  [exercise users]
  [:div] (html/content (:name exercise))
  [:ul] (html/content (map #(rating-snippet % exercise) users)))

(html/deftemplate exercise "regarde/templates/exercise.html"
  [exercise users]
  [:p :span] (html/content (:name exercise))
  [:p :b] (html/content (if (exercise/complete? exercise users)
                          "Average Ratings are being calculated"
                          "This exercise is still in progress")))
