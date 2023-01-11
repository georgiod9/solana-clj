(ns solana-clj.transaction
  (:require
   ["@solana/web3.js" :as sol]
   [cljs-bean.core  :refer [->js]]))

(def ^:const PACKET_DATA_SIZE sol/PACKET_DATA_SIZE)

(defn make-transaction-instruction
  "Construct a transaction instruction.

  opts contains:
  * `keys` -- Array of account meta
  * `programId`
  * `data` -- transaction instruction data"
  [opts]
  (sol/TransactionInstruction. (->js opts)))

(defn make-transaction
  "Construct an empty Transaction.

  opts contains:
  * `recentBlockhash` -- A recent blockhash
  * `nonceInfo`       -- Optional nonce information used for offline nonce'd transactions
  * `feePayer`        -- The transaction fee payer
  * `signatures`      -- One or more signatures
  "
  ([] (sol/Transaction.))
  ([opts] (sol/Transaction. (->js opts))))

(defn get-signature
  "The first (payer) Transaction signature."
  [^sol/Transaction tx]
  (.signature tx))

(defn add
  "Add one or more instructions to this Transaction."
  [^sol/Transaction tx & items]
  (apply js-invoke tx "add" items))

(defn compile-message "Compile transaction data." [^sol/Transaction tx] (.compileMessage tx))

(defn serialize-message
  "Get a buffer of the Transaction data that need to be covered by signatures."
  [^sol/Transaction tx]
  (.serializeMessage tx))

(defn sign
  "Sign the Transaction with the specified signers. Multiple signatures may
   be applied to a Transaction. The first signature is considered `primary`
   and is used identify and confirm transactions.

   If the Transaction `feePayer` is not set, the first signer will be used
   as the transaction fee payer account.

   Transaction fields should not be modified after the first call to `sign`,
   as doing so may invalidate the signature and cause the Transaction to be
   rejected.

   The Transaction must be assigned a valid `recentBlockhash` before invoking this method"
  [^sol/Transaction tx & signers]
  (apply js-invoke tx "sign" signers))

(defn partial-sign
  "Partially sign a transaction with the specified accounts. All accounts must
  correspond to either the fee payer or a signer account in the transaction
  instructions.

  All the caveats from the `sign` method apply to `partialSign`
  "
  [^sol/Transaction tx & signers]
  (apply js-invoke tx "partialSign" signers))

(defn add-signature
  "Add an externally created signature to a transaction. The public key
  must correspond to either the fee payer or a signer account in the transaction
  instructions."
  [^sol/Transaction tx pubkey signature]
  (.addSignature tx pubkey signature))
