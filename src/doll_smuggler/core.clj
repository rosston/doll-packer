(ns doll-smuggler.core)

(defrecord Doll [name weight value])

(defn fill-handbag
  "Return the set of dolls that maximizes value while fitting within specified weight."
  [max-weight dolls]
  (case (count dolls)
    0 []
    1 (if (<= (:weight (first dolls)) max-weight) dolls [])
    (let [sorted-dolls (reverse (sort-by :value dolls))
          first-doll (first sorted-dolls)
          first-doll-weight (:weight first-doll)]
      (if (<= first-doll-weight max-weight)
        (into [first-doll] (fill-handbag (- max-weight first-doll-weight)
                                         (rest sorted-dolls)))
        (fill-handbag max-weight (rest sorted-dolls))))))
