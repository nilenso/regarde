(ns regarde.models.user
  (:require [korma.core :as sql]))

(sql/defentity users)

(defn create-user [user-attrs]
  (sql/insert users (sql/values (select-keys user-attrs [:name]))))

(defn all []
  (sql/select users))

