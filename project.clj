(defproject ilshad/generations "0.2.0"
  :description "Datomic schema migrations"
  :url "http://github.com/ilshad/generations"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies []
  :deploy-repositories [["clojars" {:sign-releases false}]]
  :profiles {:test {:dependencies [[datomic-free "0.8.3331"]]}})
