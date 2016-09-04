(defproject doll-smuggler "0.1.0-SNAPSHOT"
  :description "Solves the problem at https://github.com/micahalles/doll-smuggler"
  :url "https://bitbucket.org/rosston/doll-smuggler"
  :license {:name "Unlicense"
            :url "http://unlicense.org/"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :profiles {:dev {:dependencies [[org.clojure/test.check "0.9.0"]]
                   :plugins [[jonase/eastwood "0.2.3"]
                             [lein-cljfmt "0.5.3"]]}})
