(ns regarde.templates
  (:require [clojure.math.numeric-tower :as math]
            [net.cgrand.enlive-html :as html]
            [regarde.models.rating :as rating]
            [regarde.templates.errors :as t/errors]
            [regarde.models.rating-set :as rating-set]))

(html/deftemplate new-user "regarde/templates/new-user.html" [])

(html/defsnippet user-snippet "regarde/templates/users.html"
  [:li]
  [user]
  [:li] (html/content (:name user)))

(html/deftemplate users "regarde/templates/users.html"
  [users current-user]
  [:head :title] (html/content  "Nilenso | List Of Users")
  [:h1] (html/content  (str "Hi " (:name current-user)))
  [:ul] (html/content (map #(user-snippet %) users)))

(html/deftemplate new-exercise "regarde/templates/new-exercise.html"
  [errors]
  [:div.errors] (html/html-content (t/errors/render errors)))

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

(html/defsnippet new-rating-snippet "regarde/templates/new-ratings.html"
  [:li]
  [user]
  [:li :span] (html/content (:name user))
  [:li :input] (html/set-attr :name "rating[]" :name (str "rating[" (:id user) "]")))

(html/defsnippet edit-rating-snippet "regarde/templates/edit-ratings.html"
  [:li]
  [user rating]
  [:li :span] (html/content (:name user))
  [:li :input] (html/set-attr :name "rating[]" :name (str "rating[" (:id user) "]") :value (:rating rating)))

(html/deftemplate new-ratings "regarde/templates/new-ratings.html"
  [exercise users]
  [:div] (html/content (:name exercise))
  [:form] (html/set-attr :action (str "/exercises/" (:id exercise) "/ratings"))
  [:ul] (html/content (map #(new-rating-snippet %) users)))

(html/deftemplate edit-ratings "regarde/templates/edit-ratings.html"
  [exercise rating-set users]
  [:div] (html/content (:name exercise))
  [:form] (html/set-attr :action (str "/exercises/" (:id exercise) "/ratings"))
  [:ul] (html/content (map #(edit-rating-snippet % (rating/find (:id rating-set) (:id %))) users)))

(html/defsnippet summary-user-snippet "regarde/templates/complete-exercise.html"
  [:li.snippet]
  [rating]
  [:li] (html/content (str (:name rating)
                           " - "
                           (math/round (* 100 (:rating rating))))))

(html/deftemplate complete-exercise "regarde/templates/complete-exercise.html"
  [exercise ratings]
  [:p :span] (html/content (:name exercise))
  [:ul.done] (html/content (map #(summary-user-snippet %) ratings)))

(html/defsnippet rating-user-snippet "regarde/templates/incomplete-exercise.html"
  [:li.snippet]
  [user]
  [:li] (html/content (str (:name user) " / " (:email user))))

(html/deftemplate incomplete-exercise "regarde/templates/incomplete-exercise.html"
  [exercise users-done users-not-done current-user]
  [:p :span] (html/content (:name exercise))
  [:p.status] (html/content (str "This exercise is still in progress: "))
  [:ul.not-done] (html/content (map #(rating-user-snippet %) users-not-done))
  [:ul.done] (html/content (map #(rating-user-snippet %) users-done))
  [:p.rating-action :a] (if (rating-set/find (:id current-user) (:id exercise))
                          (html/do-> (html/set-attr :href (str "/exercises/" (:id exercise) "/rating_sets/edit"))
                                (html/content "Edit Rating"))
                          (html/do-> (html/set-attr :href (str "/exercises/" (:id exercise) "/rating_sets/new"))
                                (html/content "New Rating"))))
