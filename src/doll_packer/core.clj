(ns doll-packer.core)

(declare pack-dolls-internal)

(defrecord Doll [name weight value])

;; Type-based multimethod seems weird when there's defprotocol, but I really
;; wanted the max-weight to be first to make the function easily partial-able.
;; e.g.,
;;   (let [pack-dolls-less-than-400 (partial pack-dolls 400)]
;;     (pack-dolls-less-than-400 some-dolls)
;;     (pack-dolls-less-than-400 some-other-dolls))
(defmulti
  ^{:doc "Return the collection of dolls that maximizes value while fitting within specified weight."
    :arglists '([max-weight dolls])}
  pack-dolls
  (fn [max-weight dolls] (type dolls)))

(defmethod pack-dolls clojure.lang.PersistentVector
  [max-weight dolls]
  (pack-dolls-internal [] max-weight dolls))

(defmethod pack-dolls clojure.lang.PersistentList
  [max-weight dolls]
  (pack-dolls-internal '() max-weight dolls))

(defmethod pack-dolls clojure.lang.PersistentList$EmptyList
  [max-weight dolls]
  (pack-dolls-internal '() max-weight dolls))

(defn- sort-and-recurse-packer
  [sort-fn initial-val max-weight dolls]
  (loop [return-val initial-val
         my-max-weight max-weight
         my-dolls dolls]
    (if (= (count my-dolls) 0)
      return-val
      (let [sorted-dolls (reverse (sort-by sort-fn my-dolls))
            first-doll (first sorted-dolls)
            first-doll-weight (:weight first-doll)]
        (if (<= first-doll-weight my-max-weight)
          (recur (into return-val [first-doll])
                 (- my-max-weight first-doll-weight)
                 (rest sorted-dolls))
          (recur return-val my-max-weight (rest sorted-dolls)))))))

(def ^:private doll-packers
  [(partial sort-and-recurse-packer :value)
   (partial sort-and-recurse-packer (fn [doll] (/ (:value doll) (:weight doll))))])

(defn- highest-value
  "Gets the highest value doll-containing collection"
  [collections]
  (let [sum-value (fn [dolls] (reduce + (map :value dolls)))]
    (reduce
     (fn [result, current] (if (> (sum-value current) (sum-value result)) current result))
     collections)))

(defn- pack-dolls-internal
  [initial-val max-weight dolls]
  (highest-value
   (map (fn [packer] (packer initial-val max-weight dolls)) doll-packers)))
