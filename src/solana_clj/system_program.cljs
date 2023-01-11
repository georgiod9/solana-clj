(ns solana-clj.system-program
  (:require
    ["@solana/web3.js"       :as sol]
    [cljs.core.async         :as    a
                             :refer [go]]
    [cljs.core.async.interop :refer [<p!]]
    [cljs-bean.core          :refer [->js ->clj]]))

(defn assign
  "Generate a transaction instruction that assigns an account to a program.

  Params contains:
  * `accountPubkey` -- Public key of the account which will be assigned a new owner
  * `programId` -- Public key of the program to assign as the owner
  "
  [params]
  (sol/SystemProgram.assign (->js params)))

;; (defn assign-with-seed-params
;;   "Params contains:
;;   * `accountPubkey` -- Public key of the account which will be assigned a new owner
;;   * `basePubkey` -- Base public key to use to derive the address of the assigned account
;;   * `seed` -- Seed to use to derive the address of the assigned account
;;   * `programId` -- Public key of the program to assign as the owner
;;   "
;;   [params]
;;   (sol/SystemProgram.assign))

(defn transfer
  "Generate a transaction instruction that transfers lamports from one account to another.

  Params can be either TransferParams or TransferWithSeedParams.

  TransferParams contains:
  * `fromPubkey` -- Account that will transfer lamports
  * `toPubkey`   -- Account that will receive transferred lamports
  * `lamports`   -- Amount of lamports to transfer

  TransferWithSeedParams contains:
  * `fromPubkey` -- Account that will transfer lamports
  * `basePubkey` -- Base public key to use to derive the funding account address
  * `toPubkey`   -- Account that will receive transferred lamports
  * `lamports`   -- Amount of lamports to transfer
  * `seed`       -- Seed to use to derive the funding account address
  * `programId`  -- program id to use to derive the funding account address
  "
  [params]
  (sol/SystemProgram.transfer (->js params)))

(defn create-account
  "Generate a transaction instruction that creates a new account.

  Params contains:
  * `fromPubkey`       -- The account that will transfer lamports to the created account
  * `newAccountPubkey` -- Public key of the created account
  * `lamports`         -- Amount of lamports to transfer to the created account
  * `space`            -- Amount of space in bytes to allocate to the created account
  * `programId`        -- Public key of the program to assign as the owner of the created account
  "
  [params]
  (sol/SystemProgram.createAccount (->js params)))

(defn create-account-with-seed
  "Generate a transaction instruction that creates a new account at
  an address generated with `from`, a seed, and programId.

  * `fromPubkey`       -- The account that will transfer lamports to the created account
  * `newAccountPubkey` -- Public key of the created account. Must be pre-calculated
                          with `solana-clj.publickey/create-with-seed`
  * `basePubkey`       -- Base public key to use to derive the address of the created account.
                          Must be the same as the base key used to create `newAccountPubkey`
  * `seed`             -- Seed to use to derive the address of the created account.
                          Must be the same as the seed used to create `newAccountPubkey`
  * `lamports`         -- Amount of lamports to transfer to the created account
  * `space`            -- Amount of space in bytes to allocate to the created account
  * `programId`        -- Public key of the program to assign as the owner of the created account
  "
  [params]
  (sol/SystemProgram.createAccountWithSeed (->js params)))
