(ns regarde.models.rating-test
  [:use expectations]
  (:require [regarde.models.rating :as ratings]))

(let [ratings [{:rating 10 :name "foo" :email "foo@bar.com"}
               {:rating 10 :name "moo" :email "moo@bar.com"}]]
  (expect 50.0 (:rating (first (ratings/normalize ratings)))))
