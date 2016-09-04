(ns doll-packer.core)

(declare pack-dolls-internal)

(defrecord Doll [name weight value])

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

(defn- pack-dolls-internal
  [initial-val max-weight dolls]
  (loop [return-val initial-val
         my-max-weight max-weight
         my-dolls dolls]
    (if (= (count my-dolls) 0)
      return-val
      (let [sorted-dolls (reverse (sort-by :value my-dolls))
            first-doll (first sorted-dolls)
            first-doll-weight (:weight first-doll)]
        (if (<= first-doll-weight my-max-weight)
          (recur (into return-val [first-doll])
                 (- my-max-weight first-doll-weight)
                 (rest sorted-dolls))
          (recur return-val my-max-weight (rest sorted-dolls)))))))
