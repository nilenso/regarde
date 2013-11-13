(ns authentication
  (:require
    [cheshire.core :refer [parse-string]]
    [clj-oauth2.client :as oauth2]
    [clj-oauth2.ring :as oauth2-ring]
    [environ.core :refer [env]]))

(def login-uri
  "https://accounts.google.com")

(def google-com-oauth2
  {:authorization-uri (str login-uri "/o/oauth2/auth")
   :access-token-uri (str login-uri "/o/oauth2/token")
   :redirect-uri "http://localhost:3000/oauth2callback"
   :client-id (env :google-client-id)
   :client-secret (env :google-client-secret)
   :access-query-param :access_token
   :scope ["https://www.googleapis.com/auth/userinfo.email"
           "https://www.googleapis.com/auth/userinfo.profile"]
   :grant-type "authorization_code"
   :access-type "online"
   :approval_prompt ""
   :get-state oauth2-ring/get-state-from-session
   :put-state oauth2-ring/put-state-in-session
   :get-target oauth2-ring/get-target-from-session
   :put-target oauth2-ring/put-target-in-session
   :get-oauth2-data oauth2-ring/get-oauth2-data-from-session
   :put-oauth2-data oauth2-ring/put-oauth2-data-in-session})

(def auth-req
  (oauth2/make-auth-request google-com-oauth2))

(defn- google-access-token [request]
  (oauth2/get-access-token google-com-oauth2 (:params request) auth-req))

(defn- google-user-email [access-token]
  (let [response (oauth2/get "https://www.googleapis.com/oauth2/v1/userinfo" {:oauth2 access-token})]
    (parse-string (:body response) true)))

(defn get-google-user [request]
  (google-user-email
   (google-access-token request)))
