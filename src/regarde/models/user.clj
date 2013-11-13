(ns regarde.models.user
  (:require [korma.core :as sql]))

(sql/defentity users)

(defn create-user [user-attrs]
  (sql/insert users (sql/values (select-keys user-attrs [:name :email]))))

(defn all []
  (sql/select users))

(defn find-or-create-user [user-attrs]
  (if-let [user (first (-> (sql/select* users) (sql/where {:email (:email user-attrs)}) (sql/exec)))]
    user
    (create-user user-attrs)))
