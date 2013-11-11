(ns regarde.db
  (:require [korma.db :as sql]))

(sql/defdb database (sql/postgres {:db "regarde"
                             :user "nid"
                             :password ""}))
