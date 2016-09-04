# doll-packer

You have a lot of high-value dolls, and you want to transport them as quickly
as possible. Use `doll-packer` to get the most bang for your buck when packing
your dolls. A lot of dolls can get heavy, so `doll-packer` will return the
collection of dolls that maximizes your total value while staying under
a maximum weight that you define.

We know that the doll delivery business values privacy and discretion, so
`doll-packer` doesn't store any user-specific information or record any info
about your dolls. We just pack your dolls; we don't need to know anything more
than that.

## Usage

Assuming you have leiningen installed, run

```
lein test
```

to ensure that our claims hold up.

To use:

```clojure
(require '[doll-packer.core :as packer])

(packer/pack-dolls 166 [(packer/->Doll "luke" 9 150)
                        (packer/->Doll "anthony" 13 35)
                        (packer/->Doll "candice" 153 200)])
; => [#doll_packer.core.Doll{:name "candice", :weight 153, :value 200}
;     #doll_packer.core.Doll{:name "luke", :weight 9, :value 150}]

; Prefer plain-old hashes?
(packer/pack-dolls 166 [{:name "luke", :weight 9, :value 150}
                        {:name "anthony", :weight 13, :value 35}
                        {:name "candice", :weight 153, :value 200}])
; => [{:name "candice", :weight 153, :value 200}
;     {:name "luke", :weight 9, :value 150}]

; Or maybe you'd like to use a list instead of a vector?
(packer/pack-dolls 166 '({:name "luke", :weight 9, :value 150}
                         {:name "anthony", :weight 13, :value 35}
                         {:name "candice", :weight 153, :value 200}))
; => ({:name "luke", :weight 9, :value 150}
;     {:name "candice", :weight 153, :value 200})
```

## License

[Unlicense](http://unlicense.org/)
