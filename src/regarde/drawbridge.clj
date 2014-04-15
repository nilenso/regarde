(ns regarde.drawbridge
  (:require [cemerick.drawbridge :as drawbridge]
            [environ.core :refer [env]]
            [ring.middleware.basic-authentication :as basic]
            [ring.middleware.session :as session]))

(declare authenticated?)

(def handler
  (-> (drawbridge/ring-handler)
      (session/wrap-session)
      (basic/wrap-basic-authentication authenticated?)))

(defn- authenticated? [user pass]
  ;; TODO: heroku config:add REPL_USER=[...] REPL_PASSWORD=[...]
  (= [user pass] [(env :repl-user false) (env :repl-password false)]))
