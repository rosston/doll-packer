(ns doll-smuggler.core)

(defrecord Doll [name weight value])

(defn fill-handbag
  "Return the set of dolls that maximizes value while fitting within specified weight."
  [max-weight dolls]
  (loop [return-val []
         my-max-weight max-weight
         my-dolls dolls]
    (case (count my-dolls)
      0 return-val
      1 (if (<= (:weight (first my-dolls)) my-max-weight)
          (into return-val my-dolls)
          return-val)
      (let [sorted-dolls (reverse (sort-by :value my-dolls))
            first-doll (first sorted-dolls)
            first-doll-weight (:weight first-doll)]
        (if (<= first-doll-weight my-max-weight)
          (recur (into return-val [first-doll])
                 (- my-max-weight first-doll-weight)
                 (rest sorted-dolls))
          (recur return-val my-max-weight (rest sorted-dolls)))))))
