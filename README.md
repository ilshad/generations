# generations

A Clojure library designed to support simple database schema migrations
for [Datomic](http://datomic.com).

## Installation

Leiningen coordinates:

```clojure
[ilshad/generations "0.3.0"]
```

## Usage

This library allows to simply keep syncronized schema attributes on all
the databases on different machines, such as developers local machines,
staging, production, etc.

For this, we use concept of "generation": it is a plain transaction
request, i.e. it is a list of lists or list of maps with transaction
data. You have to keep all the generations in a vector. To evolve
database schema, new generation should be added to the end of this
vector. In other words, you always keep schema-related transaction
data ordered in this vector:

```clojure
(def my-project-generations
  [

   ;; 1st generation
   [
	   {:db/id #db/id[:db.part/db]
	    :db/ident :user/id
        :db/valueType :db.type/uuid
        ...
	    :db.install/_attribute :db.part/db}

	   {:db/id #db/id[:db.part/db]
	    :db/ident :user/email
        :db/valueType :db.type/string
        ...
	    :db.install/_attribute :db.part/db}

	   {:db/id #db/id[:db.part/db]
	    :db/ident :enums
	    :db.install/_parition :db.part/db}
   ]

   ;; 2st generation
   [
	   {:db/id #db/id[:enums] :db/ident :locale/en}
	   {:db/id #db/id[:enums] :db/ident :locale/nl}
	   {:db/id #db/id[:enums] :db/ident :locale/it}

	   {:db/id #db/id[:db.part/db]
	    :db/ident :user/address
        :db/valueType :db.type/string
        ...
	    :db.install/_attribute :db.part/db}

	]

	...

   ])
```

`install` automatically installs only generations which are not
installed yet:

```clojure
(ilshad.generations/install my-project-generations db-conn)
```

For example, put this into main function to ensure actual database
schema always installed:

```clojure
(ns my-project
  (:require [datomic.api :as d]
            [ilshad.generations :as g]))

(defn -main [& args]
  (let [uri "datomic:dev://localhost:4334/my-database"]
    (when (d/create-database uri)
	  (log/info "New database was created"))
    (g/install GENERATIONS println (d/connect uri))))
```

To achieve its goal, this library installs `:generation/id` and
`:generation/data` attributes into database and so it stores
an information about all the generations.

## License

Copyright © 2013-2016 [Ilshad Khabibullin](http://ilshad.com).

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
