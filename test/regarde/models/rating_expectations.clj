(ns regarde.models.rating-expectations
  [:use expectations]
  (:require [regarde.models.rating :as ratings]))

(let [ratings [{:rating 15 :name "foo" :email "foo@bar.com"}
               {:rating 37 :name "moo" :email "moo@bar.com"}]]
  (expect 15/52 (:rating (first (ratings/normalize ratings))))
  (expect 37/52 (:rating (second (ratings/normalize ratings)))))

(let [rating {:rating 35}]
  (expect {:rating 35/102} (ratings/normalize-rating rating 102)))
