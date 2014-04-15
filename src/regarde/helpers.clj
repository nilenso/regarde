(ns regarde.helpers
  (:require [regarde.authentication :as authentication]
            [regarde.models.user :as user]))

(defn current-user [request]
  (user/find-user (authentication/get-google-user request)))
