(ns doll-packer.core-test
  (:require [clojure.test :refer :all]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.set :refer [difference]]
            [doll-packer.core :refer :all]))

(defn total-weight
  "Get the total weight of a doll-containing collection"
  [dolls]
  (reduce + (map :weight dolls)))

(defn sort-by-value
  "Sort a doll-containing collection by value, descending"
  [dolls]
  (reverse (sort-by (juxt :value :weight :name) dolls)))

(defn equal-enough?
  "Determine if two or more doll-containing collections have the same contents, ignoring order"
  [& collections]
  (or (apply = collections)
      (and (apply = (map type collections))
           (apply = (map sort-by-value collections)))))

(deftest pack-dolls-test
  (testing "pack-dolls"
    (testing "should return provided dolls when total weight of dolls is less than max-weight"
      (let [dolls [(->Doll "luke" 9 150)
                   (->Doll "anthony" 13 35)]]
        (is (= (pack-dolls 23 dolls) dolls))))
    (testing "should return provided dolls when total weight of dolls is equal to max-weight"
      (let [dolls [(->Doll "luke" 9 150)
                   (->Doll "anthony" 13 35)]]
        (is (= (pack-dolls 22 dolls) dolls))))
    (testing "should return empty vector when no dolls are provided"
      (is (= (pack-dolls 400 []) [])))
    (testing "should return empty vector when one doll is provided but weighs more than max-weight"
      (is (= (pack-dolls 3 [(->Doll "luke" 9 150)]) [])))
    (testing "should return vector containing highest value doll when two dolls are provided but one weighs more than max-weight"
      (is (= (pack-dolls 10 [(->Doll "anthony" 13 35)
                             (->Doll "luke" 9 150)])
             [(->Doll "luke" 9 150)])))
    (testing "should return empty vector when no single doll weighs less than max-weight"
      (is (= (pack-dolls 8 [(->Doll "luke" 9 150)
                            (->Doll "anthony" 13 35)])
             [])))
    (testing "should return most valuable set of dolls in simple case"
      (is (equal-enough? (pack-dolls 166 [(->Doll "luke" 9 150)
                                          (->Doll "anthony" 13 35)
                                          (->Doll "candice" 153 200)])
                         [(->Doll "luke" 9 150)
                          (->Doll "candice" 153 200)])))
    (testing "should return most valuable set of dolls in complex case (example test)"
      (is (equal-enough? (pack-dolls 400 [(->Doll "luke" 9 150)
                                          (->Doll "anthony" 13 35)
                                          (->Doll "candice" 153 200)
                                          (->Doll "dorothy" 50 160)
                                          (->Doll "puppy" 15 60)
                                          (->Doll "thomas" 68 45)
                                          (->Doll "randal" 27 60)
                                          (->Doll "april" 39 40)
                                          (->Doll "nancy" 23 30)
                                          (->Doll "bonnie" 52 10)
                                          (->Doll "marc" 11 70)
                                          (->Doll "kate" 32 30)
                                          (->Doll "tbone" 24 15)
                                          (->Doll "tommy" 48 10)
                                          (->Doll "uma" 73 40)
                                          (->Doll "grumpkin" 42 70)
                                          (->Doll "dusty" 43 75)
                                          (->Doll "grumpy" 22 80)
                                          (->Doll "eddie" 7 20)
                                          (->Doll "tory" 18 12)
                                          (->Doll "sally" 4 50)
                                          (->Doll "babe" 30 10)])
                         [(->Doll "sally" 4 50)
                          (->Doll "eddie" 7 20)
                          (->Doll "grumpy" 22 80)
                          (->Doll "dusty" 43 75)
                          (->Doll "grumpkin" 42 70)
                          (->Doll "marc" 11 70)
                          (->Doll "randal" 27 60)
                          (->Doll "puppy" 15 60)
                          (->Doll "dorothy" 50 160)
                          (->Doll "candice" 153 200)
                          (->Doll "anthony" 13 35)
                          (->Doll "luke" 9 150)])))
    (testing "should also work with a collection of maps"
      (is (equal-enough? (pack-dolls 166 [{:name "luke", :weight 9, :value 150}
                                          {:name "anthony", :weight 13, :value 35}
                                          {:name "candice", :weight 153, :value 200}])
                         [{:name "luke", :weight 9, :value 150}
                          {:name "candice", :weight 153, :value 200}])))
    (testing "should also work with a list of maps"
      (is (equal-enough? (pack-dolls 166 '({:name "luke", :weight 9, :value 150}
                                           {:name "anthony", :weight 13, :value 35}
                                           {:name "candice", :weight 153, :value 200}))
                         '({:name "luke", :weight 9, :value 150}
                           {:name "candice", :weight 153, :value 200}))))))

(defn can-fit-another
  "Determine if the resulting collection can fit another doll without breaking the max weight."
  [max-weight source-dolls packed-dolls]
  (let [remaining-dolls (difference (set source-dolls) (set packed-dolls))]
    (some (fn [x] (>= max-weight (total-weight (into [x] packed-dolls))))
          remaining-dolls)))

(def doll-gen
  (gen/fmap (partial apply ->Doll)
            (gen/tuple (gen/not-empty gen/string-alphanumeric)
                       gen/nat
                       gen/nat)))

(defspec vector-returns-vector
  100
  (prop/for-all [v (gen/vector doll-gen)
                 max gen/nat]
                (= (type (pack-dolls max v)) (type []))))

(defspec list-returns-list
  100
  (prop/for-all [l (gen/list doll-gen)
                 max gen/nat]
                (let [result (pack-dolls max l)]
                  (or (= (type result) (type '()))
                      (= (type result) (type '(1)))))))

(defspec less-than-max
  1000
  (prop/for-all [v (gen/vector doll-gen)
                 max gen/nat]
                (>= max (total-weight (pack-dolls max v)))))

;; Seems difficult to do a generated test that we've maximized the value of the
;; dolls without re-implementing the function under test. So: test that, once
;; we fill the handbag, it's not possible to add any other doll to the handbag
;; without going over the max weight.
(defspec max-value-ish
  1000
  (prop/for-all [v (gen/vector doll-gen)
                 max gen/nat]
                (not (can-fit-another max v (pack-dolls max v)))))
