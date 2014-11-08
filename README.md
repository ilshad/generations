# generations

A Clojure library designed to support simple database migrations for Datomic.

## Installation

Leiningen coordinates:

```clojure
[ilshad/generations "0.1.0"]
```

## Usage

Save Datomic schema into vector of vectors (generations). For
example:

```clojure
(def GENERATIONS
  [

   ;; 1st generation
   [
	   {:db/id #db/id[:db.part/db]
	    :db/ident :enums
	    :db.install/_parition :db.part/db}
	]

   ;; 2st generation
   [
	   {:db/id #db/id[:enums] :db/ident :locale/en}
	   {:db/id #db/id[:enums] :db/ident :locale/nl}
	   {:db/id #db/id[:enums] :db/ident :locale/it}
	]

	...

   ])
```

Follow command automatically pass into db/transact only
generations which are not installed yet:

```clojure
(require '[ilshad.generations :as g])

(g/install GENERATIONS conn)
```

This library installs :generation/id and :generation/data
into database and so it stores information about all
generations.

## License

Copyright © 2013 [Ilshad Khabibullin](http://ilshad.com).

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
