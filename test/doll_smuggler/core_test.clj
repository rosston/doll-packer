(ns doll-smuggler.core-test
  (:require [clojure.test :refer :all]
            [doll-smuggler.core :refer :all]))

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

(deftest fill-handbag-test
  (testing "fill-handbag"
    (testing "should return provided dolls when total weight of dolls is less than max-weight"
      (let [dolls [(->Doll "luke" 9 150)
                   (->Doll "anthony" 13 35)]]
        (is (= (fill-handbag 23 dolls) dolls))))
    (testing "should return provided dolls when total weight of dolls is equal to max-weight"
      (let [dolls [(->Doll "luke" 9 150)
                   (->Doll "anthony" 13 35)]]
        (is (= (fill-handbag 22 dolls) dolls))))
    (testing "should return empty vector when no dolls are provided"
      (is (= (fill-handbag 400 []) [])))
    (testing "should return empty vector when one doll is provided but weighs more than max-weight"
      (is (= (fill-handbag 3 [(->Doll "luke" 9 150)]) [])))
    (testing "should return vector containing highest value doll when two dolls are provided but one weighs more than max-weight"
      (let [dolls [(->Doll "anthony" 13 35)
                   (->Doll "luke" 9 150)]]
        (is (= (fill-handbag 10 dolls) [(->Doll "luke" 9 150)]))))
    (testing "should return empty vector when no single doll weighs less than max-weight"
      (let [dolls [(->Doll "luke" 9 150)
                   (->Doll "anthony" 13 35)]]
        (is (= (fill-handbag 8 dolls) []))))
    (testing "should return most valuable set of dolls in simple case"
      (let [dolls [(->Doll "luke" 9 150)
                   (->Doll "anthony" 13 35)
                   (->Doll "candice" 153 200)]]
        (is (equal-enough? (fill-handbag 166 dolls) [(->Doll "luke" 9 150)
                                                     (->Doll "candice" 153 200)]))))
    (testing "should return most valuable set of dolls in complex case (example test)"
      (is (equal-enough? (fill-handbag 400 [(->Doll "luke" 9 150)
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
      (is (equal-enough? (fill-handbag 166 [{:name "luke", :weight 9, :value 150}
                                            {:name "anthony", :weight 13, :value 35}
                                            {:name "candice", :weight 153, :value 200}])
                         [{:name "luke", :weight 9, :value 150}
                          {:name "candice", :weight 153, :value 200}])))
    (testing "should also work with a list of maps"
      (is (equal-enough? (fill-handbag 166 '({:name "luke", :weight 9, :value 150}
                                             {:name "anthony", :weight 13, :value 35}
                                             {:name "candice", :weight 153, :value 200}))
                         '({:name "luke", :weight 9, :value 150}
                           {:name "candice", :weight 153, :value 200}))))))
