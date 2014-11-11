(ns ilshad.generations
  (:require [datomic.api :as d]))

(defn- query-generations-schema [dbv]
  (d/q '[:find ?c
         :in $
         :where
         [?c :db/ident :generation/id]]
    dbv))

(def generations-schema
  [{:db/id #db/id[:db.part/db]
    :db/ident :generation/id
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity
    :db/doc "Generation number, 1...n"
    :db.install/_attribute :db.part/db}
   {:db/id #db/id[:db.part/db]
    :db/ident :generation/data
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity
    :db/doc "tx-data, in string."
    :db.install/_attribute :db.part/db}])

(defn- ensure-generations-schema [conn]
  (when (-> conn d/db query-generations-schema empty?)
    @(d/transact conn generations-schema)))

(defn- all-generations-ids [dbv]
  (d/q '[:find ?id
         :in $
         :where
         [?c :generation/id ?id]]
    dbv))

(defn install
  "Install all generations which are not installed yet.
   Arguments:
   - generations - vector of vectors,
   - logger function (for example `println`),
   - database connection."
  [generations logger conn]
  (ensure-generations-schema conn)
  (let [ids (map first (all-generations-ids (d/db conn)))
        last-id (if (empty? ids) 0 (apply max ids))]
    (doseq [i (range last-id (count generations))
            :let [data (nth generations i)
                  id (inc i)]]
      @(d/transact conn
         (conj data
               {:db/id (d/tempid :db.part/user)
                :generation/id id
                :generation/data (str data)}))
      (logger (str id "st generation has been installed successfully.")))))
