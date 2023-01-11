(ns solana-clj.publickey
  (:require ["@solana/web3.js" :as sol]
            [cljs.core.async :refer [go <!]]
            [solana-clj.extra.interop :refer [<p!]]))

(def ^:const MAX_SEED_LENGTH 32)

(extend-type sol/PublicKey
  IEquiv
    (-equiv [o other]
      (try (and o other (.equals o other))
           (catch js/Error _
             (js/console.error "PUBLIC CMP ERROR:" o other)
             false))))

(defn make-public-key
  "Make a new public key.

  init-data could be one of following:
  * number
  * string
  * Buffer
  * Uint8Array
  * Array<number>
  * PublicKeyData
  "
  [init-data]
  (sol/PublicKey. init-data))

(defn create-with-seed
  "Derive a program address from seeds and a program ID."
  [^sol/PublicKey from-pubkey ^js/String seeds program-id]
  (go (<p! (sol/PublicKey.createWithSeed from-pubkey seeds program-id))))

(defn create-program-address
  "Derive a program address from seeds and a program ID."
  [seeds program-id]
  (go (<p! (sol/PublicKey.createProgramAddress seeds program-id))))

(defn find-program-address
  "Find a valid program address

  Valid program addresses must fall off the ed25519 curve.
  This function iterates a nonce until it finds one that when combined with the seeds results in a valid program address."
  [seeds program-id]
  (go (<p! (sol/PublicKey.findProgramAddress seeds program-id))))

(defn to-base-58
  "Return the base-58 representation of the public key."
  [^sol/PublicKey pubkey]
  (.toBase58 pubkey))

(defn to-bytes
  "Return the byte array representation of the public key."
  [^sol/PublicKey pubkey]
  (.toBytes pubkey))

(defn to-buffer
  "Return the Buffer representation of the public key."
  [^sol/PublicKey pubkey]
  (.toBuffer pubkey))

(defn is-on-curve?
  "Return if a pubkey is on the ed25519 curve."
  [uint8array]
  (sol/PublicKey.isOnCurve uint8array))

(def system-program (make-public-key "11111111111111111111111111111111"))
