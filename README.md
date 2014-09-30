# generations

A Clojure library designed to support simple database schema
migrations for Datomic.

## Usage

```clojure
(def GENERATIONS
  [

   ;; 1st generation
   [{:db/id #db/id[:db.part/db]
     :db/ident :foo/bar
	 ...}

   ;; 2st generation
   [{:db/id #db/id[:db.part/db]
     :db/ident :foo/baz
	 ...}
     ...]

   ])

;; Install all generations which are not installed yet.
(ilshad.generations/install GENERATIONS db-conn))
```

## License

Copyright © 2013 [Ilshad Khabibullin](http://ilshad.com).

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
