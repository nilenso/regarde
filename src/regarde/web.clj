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
            [cemerick.drawbridge :as drawbridge]
            [environ.core :refer [env]]
            [net.cgrand.enlive-html :as html]
            [clojure.pprint :as pp]
            [regarde.models.user :as user]
            [clojure.java.jdbc :as sql]))

(defn- authenticated? [user pass]
  ;; TODO: heroku config:add REPL_USER=[...] REPL_PASSWORD=[...]
  (= [user pass] [(env :repl-user false) (env :repl-password false)]))

(def ^:private drawbridge
  (-> (drawbridge/ring-handler)
      (session/wrap-session)
      (basic/wrap-basic-authentication authenticated?)))

(html/deftemplate new-user-template "regarde/templates/new-user.html"
  []
  )

(html/defsnippet user-snippet "regarde/templates/users.html"
  [:li]
  [user]
  [:li] (html/content (:name user)))

(html/deftemplate users-template "regarde/templates/users.html"
  [users]
  [:head :title] (html/content  "Changed title")
  [:ul] (html/content (map #(user-snippet %) users)))

(def users (atom []))

(defn new-user [request]
  (new-user-template))

(defn create-user [request]
  ;; TODO: try swap! on an atom 
  ;; (let [w (StringWriter.)]
  ;;   (pp/pprint request w)
  ;;   (str "<pre>" (.toString w) "</pre>"))
  (user/create-user (:params request))
  )

(defn list-users [request]
  (users-template (user/list))
  )

(defroutes app
  (ANY "/repl" {:as req}
       (drawbridge req))
  (GET "/" []
       list-users)
  (GET "/users/new" []
       new-user)
  (POST "/users" []
        create-user)
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
                         ((if (env :production)
                            wrap-error-page
                            trace/wrap-stacktrace))
                         (site {:session {:store store}}))
                     {:port port :join? false})))


;; For interactive development:
;; (.stop server)
;; (def server (-main))
