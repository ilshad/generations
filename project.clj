(defproject ilshad/generations "0.3.0-SNAPSHOT"
  :description "Migrations for Datomic schema"
  :url "http://github.com/ilshad/generations"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.logging "0.3.1"]]
  :deploy-repositories [["clojars" {:sign-releases false}]]
  :profiles {:dev {:dependencies [[com.datomic/datomic-free "0.9.5394"]
                                  [org.clojure/tools.nrepl "0.2.12"]]
                   :plugins [[cider/cider-nrepl "0.13.0"]
                             [refactor-nrepl "2.2.0"]]}})
