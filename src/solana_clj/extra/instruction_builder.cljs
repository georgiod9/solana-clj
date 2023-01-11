(ns solana-clj.extra.instruction-builder
  "Helpers for building instruction data."
  (:require ["buffer" :as buffer]
            ["bn.js" :refer [BN]]))

(defn make-instruction-data
  "Make instruction data."
  [& val-size-pairs]
  (->> val-size-pairs
       (map
        (fn [[v s]]
          (if (instance? buffer/Buffer v)
            v
            (buffer/Buffer.from (.toArray (BN. v) "le" s)))))
       into-array
       buffer/Buffer.concat))

(comment
  (println
   (.-length (buffer/Buffer.from "abcd"))
   (make-instruction-data [15 1] [100 4])))
