(ns regarde.web
  (:import java.io.StringWriter)
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [ring.middleware.stacktrace :as trace]
            [ring.middleware.session :as session]
            [ring.middleware.session.cookie :as cookie]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.basic-authentication :as basic]
            [ring.middleware.params :as params]
            [ring.middleware.keyword-params :as keyword-params]
            [ring.util.response :as resp]
            [cemerick.drawbridge :as drawbridge]
            [environ.core :refer [env]]
            [clojure.pprint :as pp]
            [regarde.models.user :as user]
            [regarde.db]
            [regarde.models.exercise :as exercise]
            [regarde.templates :as templates]
            [authentication]
            [clj-oauth2.client :as oauth2]
            [clj-oauth2.ring :as oauth2-ring]))

(defn- authenticated? [user pass]
  ;; TODO: heroku config:add REPL_USER=[...] REPL_PASSWORD=[...]
  (= [user pass] [(env :repl-user false) (env :repl-password false)]))

(def ^:private drawbridge
  (-> (drawbridge/ring-handler)
      (session/wrap-session)
      (basic/wrap-basic-authentication authenticated?)))

(defn new-user [request]
  (templates/new-user))

(defn new-exercise [request]
  (templates/new-exercise))

(defn create-exercise [request]
  (exercise/create-exercise (:params request))
  (resp/redirect "/exercises"))

(defn list-users [request]
  (templates/users (user/list)))

(defn list-exercises [request]
  (templates/exercises (exercise/list)))

(defn find-or-create-user [request]
  (user/find-or-create-user (authentication/get-google-user request))
  (resp/redirect "/"))

(defroutes app
  (ANY "/repl" {:as req}
       (drawbridge req))
  (GET "/" []
       list-users)
  (GET "/exercises/new" []
       new-exercise)
  (POST "/exercises" []
        create-exercise)
  (GET "/exercises" []
       list-exercises)
  (GET "/sign-in" []
       (resp/redirect (:uri authentication/auth-req)))
  (GET "/oauth2callback" []
       find-or-create-user)
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))

(defn wrap-error-page [handler]
  (fn [req]
    (try (handler req)
         (catch Exception e
           {:status 500
            :headers {"Content-Type" "text/html"}
            :body (slurp (io/resource "500.html"))}))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))
        ;; TODO: heroku config:add SESSION_SECRET=$RANDOM_16_CHARS
        store (cookie/cookie-store {:key (env :session-secret)})]
    (jetty/run-jetty (-> #'app
                         keyword-params/wrap-keyword-params
                         params/wrap-params
                         (oauth2-ring/wrap-oauth2 authentication/google-com-oauth2)
                         session/wrap-session
                         ((if (env :production)
                            wrap-error-page
                            trace/wrap-stacktrace))
                         (site {:session {:store store}}))
                     {:port port :join? false})))


;; For interactive development:
(.stop server)
(def server (-main 3000))
