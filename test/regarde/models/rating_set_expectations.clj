(ns regarde.models.rating-set-expectations
  [:use expectations]
  (:require [regarde.models.rating-set :as rating-set]))

(let [db-rating-sets '({:ratings
                        [{:email "deobald@gmail.com",
                          :name "Steven Deobald",
                          :id_2 1,
                          :rating 1234,
                          :rating_sets_id 1,
                          :users_id 1,
                          :id 2}
                         {:email "steven@nilenso.com",
                          :name "Steven Deobald",
                          :id_2 2,
                          :rating 345,
                          :rating_sets_id 1,
                          :users_id 2,
                          :id 1}],
                        :completed false,
                        :users_id 2,
                        :exercises_id 1,
                        :id 1}
                       {:ratings
                        [{:email "deobald@gmail.com",
                          :name "Steven Deobald",
                          :id_2 1,
                          :rating 123,
                          :rating_sets_id 2,
                          :users_id 1,
                          :id 4}
                         {:email "steven@nilenso.com",
                          :name "Steven Deobald",
                          :id_2 2,
                          :rating 53,
                          :rating_sets_id 2,
                          :users_id 2,
                          :id 3}],
                        :completed false,
                        :users_id 1,
                        :exercises_id 1,
                        :id 2})
      normalized (rating-set/normalize-rating-sets db-rating-sets)]
  (expect {:rating 1234/1579} (in (nth normalized 0)))
  (expect {:rating 345/1579} (in (nth normalized 1)))
  (expect {:rating 123/176} (in (nth normalized 2)))
  (expect {:rating 53/176} (in (nth normalized 3)))
  (expect {:users_id 1 :name "Steven Deobald" :email "deobald@gmail.com"} (in (first normalized))))

(let [ratings [{:name "Steven" :email "steven@nilenso.com" :rating 20}
               {:name "Steven" :email "steven@nilenso.com" :rating 10}
               {:name "Steven" :email "steven@nilenso.com" :rating 30}]]
  (expect {:name "Steven" :email "steven@nilenso.com" :rating 20} (rating-set/aggregate-ratings ratings)))

(let [ratings-for-asif [{:email "asif@nilenso.com",
                         :name "Asif",
                         :rating 3/10,
                         :rating_sets_id 1,
                         :users_id 1,
                         :id 1}
                        {:email "asif@nilenso.com",
                         :name "Asif",
                         :rating 1/10,
                         :rating_sets_id 2,
                         :users_id 1,
                         :id 3}]
      ratings-for-aninda [{:email "aninda.kundu@gmail.com",
                           :name "Aninda Kundu",
                           :rating 7/10,
                           :rating_sets_id 1,
                           :users_id 2,
                           :id 4}
                          {:email "aninda.kundu@gmail.com",
                           :name "Aninda Kundu",
                           :rating 9/10,
                           :rating_sets_id 2,
                           :users_id 2,
                           :id 2}]
      normalized-ratings (concat ratings-for-asif ratings-for-aninda)]
  (expect 2/10 (:rating (first (rating-set/summarize-rating-sets normalized-ratings))))
  (expect 8/10 (:rating (second (rating-set/summarize-rating-sets normalized-ratings)))))
