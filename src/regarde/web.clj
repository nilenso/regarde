(ns regarde.web
  (:require [clj-oauth2.ring :as oauth2-ring]
            [clojure.java.io :as io]
            [compojure.handler :refer [site]]
            [environ.core :refer [env]]
            [regarde.authentication :as authentication]
            [regarde.db :as db]
            [regarde.models.user :as user]
            [regarde.routes :refer [app]]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.session.cookie :as cookie]
            [ring.middleware.stacktrace :as trace]))

(defn wrap-error-page [handler]
  (fn [req]
    (try (handler req)
         (catch Exception e
           {:status 500
            :headers {"Content-Type" "text/html"}
            :body (slurp (io/resource "500.html"))}))))

(defn wrap-find-or-create-user [handler]
  (fn [request]
    (when (:oauth2 (:session request))
      (user/find-or-create-user (authentication/get-google-user request)))
    (handler request)))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 3000))
        store (cookie/cookie-store {:key (env :session-secret)})]
    (db/setup (if (env :production)
                :production
                :development))
    (jetty/run-jetty (-> #'app
                         (trace/wrap-stacktrace)
                         (oauth2-ring/wrap-oauth2 authentication/google-com-oauth2)
                         (wrap-find-or-create-user)
                         ((if (env :production)  wrap-error-page identity))
                         ((if (env :development) wrap-reload identity))
                         (site {:session {:store store}}))
                     {:port port :join? false})))
