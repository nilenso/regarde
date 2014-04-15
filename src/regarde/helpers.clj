(ns regarde.helpers
  (require [regarde.models.user :as user]
           [regarde.authentication :as authentication]))

(defn current-user [request]
  (user/find-user (authentication/get-google-user request)))
