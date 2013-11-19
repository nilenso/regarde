(ns regarde.models.rating-set-test
  [:use expectations]
  (:require [regarde.models.rating-set :as rating-set]))

(let [normalized-rating-sets '({:email "aninda@nilenso.com",
                                :name "Aninda Kundu",
                                :rating 30.0,
                                :rating_sets_id 1,
                                :users_id 1,
                                :id 1}
                               {:email "aninda.kundu@gmail.com",
                                :name "Aninda Kundu",
                                :rating 70.0,
                                :rating_sets_id 1,
                                :users_id 2,
                                :id 4}
                               {:email "aninda@nilenso.com",
                                :name "Aninda Kundu",
                                :rating 70.0,
                                :rating_sets_id 2,
                                :users_id 1,
                                :id 3}
                               {:email "aninda.kundu@gmail.com",
                                :name "Aninda Kundu",
                                :rating 30.0,
                                :rating_sets_id 2,
                                :users_id 2,
                                :id 2})]

  (expect 50.0 (:rating (first (rating-set/summarize-rating-sets normalized-rating-sets))))
  (expect 50.0 (:rating (second (rating-set/summarize-rating-sets normalized-rating-sets)))))
