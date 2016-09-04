(defproject doll-packer "0.1.0-SNAPSHOT"
  :description "A smart and efficient packer of dolls for all your high-value dolls"
  :url "https://github.com/rosston/doll-packer"
  :license {:name "Unlicense"
            :url "http://unlicense.org/"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :profiles {:dev {:dependencies [[org.clojure/test.check "0.9.0"]]
                   :plugins [[jonase/eastwood "0.2.3"]
                             [lein-cljfmt "0.5.3"]]}})
