(ns regarde.models.exercise-test
  (:use expectations)
  (:require [regarde.models.exercise :as exercise]))

(let [completed-users [{:id 1 :name "Aninda" :email "aninda@nilenso.com"}]
      all-users [{:id 1 :name "Aninda" :email "aninda@nilenso.com"}]]
  (expect (exercise/complete? completed-users all-users)))

(let [completed-users [{:id 1 :name "Aninda" :email "aninda@nilenso.com"}]
      all-users [{:id 1 :name "Aninda" :email "aninda@nilenso.com"}
                 {:id 2 :name "Steven" :email "steven@nilenso.com"}]]
  (expect not (exercise/complete? completed-users all-users)))
