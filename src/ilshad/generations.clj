(ns ilshad.generations
  (:require [datomic.api :as d]
            [clojure.tools.logging :as log]))

(def generations-schema

  [{:db/id #db/id[:db.part/db]
    :db/ident :generation/id
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity
    :db/doc "Serial number of the generation, 1...n"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :generation/data
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity
    :db/doc "Transaction data serialized in to string."
    :db.install/_attribute :db.part/db}])

(defn- query-schema [db]
  (d/q '[:find ?c :where [?c :db/ident :generation/id]]
    db))

(defn- all-ids [db]
  (d/q '[:find ?id :where [?c :generation/id ?id]]
    db))

(defn- ensure-schema [conn]
  (when (-> conn d/db query-schema empty?)
    @(d/transact conn generations-schema)))

(defn install
  "Generation is a plain transaction request, i.e. list of lists
   or list of maps with transaction data. This function takes
   sequence of generations and database connection and installs
   all the generations which are not installed yet."
  [generations conn]
  (ensure-schema conn)
  (let [ids (map first (all-ids (d/db conn)))
        last-id (if (empty? ids) 0 (apply max ids))]
    (doseq [i (range last-id (count generations))
            :let [data (nth generations i)
                  id (inc i)]]
      @(d/transact conn
         (conj data
               {:db/id (d/tempid :db.part/user)
                :generation/id id
                :generation/data (str data)}))
      (log/info (str id "st generation has been installed.")))))
