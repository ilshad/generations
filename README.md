# generations

[![Clojars Project](https://img.shields.io/clojars/v/ilshad/generations.svg)](https://clojars.org/ilshad/generations)

A Clojure library designed to support simple database schema migrations
for [Datomic](http://datomic.com).

## Usage

This library allows to simply keep syncronized schema attributes in all
the databases on different machines, such as developers' local machines,
staging, production, etc.

For this, we use the idea of "generation": it is a plain transaction
request, a list of lists or list of maps with transaction data. And you
have to keep all the generations in a vector. To evolve database schema,
new generation should be added to the end of this vector.

In other words, you always keep schema related transactions ordered in
this vector:

```clojure
(def my-project-generations
  [

   ;; -*- Generation 1 -*-

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

   ;; -*- Generation 2 -*-

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

`ilshad.generations/install` automatically installs only generations
which are not installed yet:

```clojure
(ilshad.generations/install my-project-generations db-conn)
```

For example, put this into your main function to ensure actual database
schema always installed:

```clojure
(ns my-project
  (:require [datomic.api :as d]
            [ilshad.generations :as generations]))

(defn -main [& args]
  (let [uri "datomic:dev://localhost:4334/my-database"]
    (when (d/create-database uri)
	  (log/info "New database has been created:" uri))
    (generations/install my-project-generations (d/connect uri))))
```

To achieve its goal, this library installs `:generation/id` and
`:generation/data` attributes into the database and thus it stores
an information about all the generations.

## License

Copyright © 2013-2016 [Ilshad Khabibullin](http://ilshad.com).

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
