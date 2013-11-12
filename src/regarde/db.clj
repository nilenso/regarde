(ns regarde.db
  (:require [korma.db :as sql]))

(sql/defdb database (sql/postgres {:db "regarde_dev"
                                   :user "postgres"
                                   :password ""}))
