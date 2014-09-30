(ns ilshad.generations
  (:require [datomic.api :as d]))

(defn- generations-schema [dbv]
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
  (when (empty? (generations-schema (d/db conn)))
    @(d/transact conn genarations-schema)))

(defn- all-generations-ids [dbv]
  (d/q '[:find ?id
         :in $
         :where
         [?c :generation/id ?id]]
    dbv))

(defn install [generations conn]
  (ensure-generations-schema conn)
  (let [dbv (d/db conn)
        ids (all-generations-ids dbv)
        max-id (if (empty? ids) 0 (apply max ids))]
    (doseq [i (range max-id (count generations))
            :let [data (nth generations i)
                  id (inc i)]]
      @(d/transact conn
         (conj data {:db/id (d/tempid :db.part/user)
                     :generation/id id
                     :generation/data (str data)}))
      (println (str id "st generation has been installed successfully.")))))
