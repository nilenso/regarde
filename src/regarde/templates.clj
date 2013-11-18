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
  [:li :a] (html/content (:name exercise))
  [:li :a] (html/set-attr :href (str "/exercises/" (:id exercise))))

(html/deftemplate exercises "regarde/templates/exercises.html"
  [exercises]
  [:head :title] (html/content "Nilenso | List of Exercises")
  [:ul] (html/content (map #(exercise-snippet %) exercises))
  [:p :a] (html/set-attr :href "/exercises/new"))

(html/deftemplate new-ratings "regarde/templates/new-ratings.html"
  [exercise users]
  [:div] (html/content (:name exercise))
  [:ul] (html/content (map #(rating-snippet % exercise) users)))

(html/deftemplate complete-exercise "regarde/templates/complete-exercise.html"
  [exercise users]
  [:p :span] (html/content (:name exercise))
  [:p :b] (html/content "Average Ratings are being calculated"))

(html/defsnippet rating-user-snippet "regarde/templates/incomplete-exercise.html"
  [:li.snippet]
  [user]
  [:li] (html/content (str (:name user) " / " (:email user))))

(html/deftemplate incomplete-exercise "regarde/templates/incomplete-exercise.html"
  [exercise users-done users-not-done]
  [:p :span] (html/content (:name exercise))
  [:p.status] (html/content (str "This exercise is still in progress: "))
  [:ul.not-done] (html/content (map #(rating-user-snippet %) users-not-done))
  [:ul.done] (html/content (map #(rating-user-snippet %) users-done)))
