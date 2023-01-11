(ns solana-clj.compute-budget
  (:require ["@solana/web3.js" :as sol]))

(defn set-compute-unit-limit-ix
  [units]
  (let [params #js {"units" units}]
    (sol/ComputeBudgetProgram.setComputeUnitLimit params)))
