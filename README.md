# generations

A Clojure library designed to support simple database schema migrations
for Datomic.

## Installation

Leiningen coordinates:

```clojure
[ilshad/generations "0.2.0"]
```

## Usage

Save Datomic schema into vector of vectors (generations). For example:

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

`install` automatically installs only generations which are not
installed yet:

```clojure
(ilshad.generations/install GENERATIONS my-logging-funciton db-conn)
```

For example, put this into main function to ensure actual
database schema always installed.

```clojure
(ns my-project
  (:require [datomic.api :as d]
	        [clojure.tools.logging :as log]
            [ilshad.generations :as g]))

(defn -main [& args]
  (let [uri "datomic:dev://localhost:4334/my-database"]
    (when (d/create-database uri)
	  (log/info "New database was created"))
    (g/install GENERATIONS log/info (d/connect uri))))
```

This library installs `:generation/id` and `:generation/data`
into database and so it stores information about all generations.

## License

Copyright © 2013 [Ilshad Khabibullin](http://ilshad.com).

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
