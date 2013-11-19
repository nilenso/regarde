(defproject regarde "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://regarde.herokuapp.com"
  :license {:name "FIXME: choose"
            :url "http://example.com/FIXME"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.1"]
                 [ring/ring-jetty-adapter "1.1.0"]
                 [ring/ring-devel "1.1.0"]
                 [ring-basic-authentication "1.0.1"]
                 [environ "0.2.1"]
                 [expectations "1.4.52"]
                 [enlive "1.1.4"]
                 [com.cemerick/drawbridge "0.0.6"]
                 [org.clojure/java.jdbc "0.3.0-beta1"]
                 [korma "0.3.0-RC6"]
                 [org.postgresql/postgresql "9.3-1100-jdbc4"]
                 [nilenso/clj-oauth2 "0.3.3-SNAPSHOT"]
                 [expectations "1.4.52"]
                 [org.clojure/math.numeric-tower "0.0.2"]]
  :min-lein-version "2.0.0"
  :plugins [[environ/environ.lein "0.2.1"]
            [lein-expectations "0.0.7"]]
  :hooks [environ.leiningen.hooks]
  :profiles {:production {:env {:production true}}})
