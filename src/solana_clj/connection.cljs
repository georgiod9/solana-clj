(ns solana-clj.connection
  (:require ["@solana/web3.js" :as sol]
            [cljs.core.async :as a :refer [go]]
            [solana-clj.extra.interop :refer [<p!]]
            [cljs-bean.core :refer [->js ->clj]]))

(def ^:const BLOCKHASH_CACHE_TIMEOUT_MS sol/BLOCKHASH_CACHE_TIMEOUT_MS)

(defn make-connection
  "Make a connection, connect to the `endpoint` url.

  Options supported:
  * `commitment` -- Optional commitment level
  * `wsEndpoint` -- Optional endpoint URL to the fullnode JSON RPC PubSub WebSocket Endpoint
  * `httpHeaders` -- Optional HTTP headers object
  * `fetchMiddleware` -- Optional fetch middleware callback
  * `disableRetryOnRateLimit` -- Optional Disable retring calls when server responds with HTTP 429 (Too Many Requests)
  "
  ([endpoint] (sol/Connection. endpoint))
  ([endpoint config] (sol/Connection. endpoint (->js config))))

(defn get-commitment [^sol/Connection conn] (.commitment conn))

(defn get-balance-and-context
  "Fetch the balance for the specified public key, return with context."
  [^sol/Connection conn ^sol/Pubkey pubkey & [commitment]]
  (go (->clj (<p! (.getBalanceAndContext conn pubkey commitment)))))

(defn get-balance
  [^sol/Connection conn ^sol/Pubkey pubkey & [commitment]]
  (go (<p! (.getBalance conn pubkey commitment))))

(defn get-block-time
  [^sol/Connection conn slot]
  (go (<p! (.getBlockTime conn slot))))

(defn get-minimum-ledger-slot
  [^sol/Connection conn]
  (go (<p! (.getMinimumLedgerSlot conn))))

(defn get-first-available-block
  [^sol/Connection conn]
  (go (<p! (.getFirstAvailableBlock conn))))

(defn get-supply
  [^sol/Connection conn & [commitment]]
  (go (<p! (.getSupply conn commitment))))

(defn get-token-mint-supply
  [^sol/Connection conn ^sol/Pubkey token-mint-address & [commitment]]
  (go (<p! (.getTokenMintSupply conn token-mint-address commitment))))

(defn get-token-account-balance
  [^sol/Connection conn ^sol/Pubkey token-address & [commitment]]
  (go (->clj (<p! (.getTokenAccountBalance conn token-address commitment)))))

(defn get-token-accounts-by-owner
  [^sol/Connection conn ^sol/Pubkey owner-address token-accounts-filter &
   [commitment]]
  (go (->clj (<p! (.getTokenAccountsByOwner conn
                                            ^sol/Pubkey owner-address
                                            (->js token-accounts-filter)
                                            commitment)))))

(defn get-parsed-token-accounts-by-owner
  [^sol/Connection conn ^sol/Pubkey owner-address token-accounts-filter &
   [commitment]]
  (go (->clj (<p! (.getParsedTokenAccountsByOwner conn
                                                  ^sol/Pubkey owner-address
                                                  (->js token-accounts-filter)
                                                  commitment)))))

(defn get-largest-accounts
  ([^sol/Connection conn] (go (<p! (.getLargestAccounts conn))))
  ([^sol/Connection conn config]
   (go (<p! (.getLargestAccounts conn (->js config))))))

(defn get-token-largest-accounts
  ([^sol/Connection conn token-mint-address & [commitment]]
   (go (<p! (.getTokenLargestAccounts conn token-mint-address commitment)))))

(defn get-account-info-and-context
  [^sol/Connection conn ^sol/Pubkey pubkey & [commitment]]
  (go (<p! (.getAccountInfoAndContext conn pubkey commitment))))

(defn get-parsed-account-info
  [^sol/Connection conn ^sol/Pubkey pubkey & [commitment]]
  (go (->clj (<p! (.getParsedAccountInfo conn pubkey commitment)))))

(defn get-account-info
  [^sol/Connection conn ^sol/Pubkey pubkey & [commitment]]
  (go (->clj (<p! (.getAccountInfo conn pubkey commitment)))))

(defn get-multiple-accounts-info
  [^sol/Connection conn ^sol/Pubkey pubkeys & [commitment]]
  (go (->clj (<p! (.getMultipleAccountsInfo conn (->js pubkeys) commitment)))))

(defn get-parsed-transaction
  [^sol/Connection conn signature & [commitment]]
  (go (->clj (<p! (.getParsedTransaction conn signature commitment)))))

(defn get-stack-activation
  [^sol/Connection conn ^sol/Pubkey pubkey & [commitment epoch]]
  (go (<p! (.getStackActivation conn pubkey commitment epoch))))

(defn get-program-accounts
  [^sol/Connection conn ^sol/Pubkey program-id & [config]]
  (go (->clj (<p! (.getProgramAccounts conn program-id (->js config))))))

(defn get-parsed-program-accounts
  [^sol/Connection conn ^sol/Pubkey program-id & [config]]
  (go (->clj (<p! (.getParsedProgramAccounts conn program-id (->js config))))))

(defn confirm-transaction
  [^sol/Connection conn signature & [commitment]]
  (go (->clj (<p! (.confirmTransaction conn signature commitment)))))

(defn send-transaction
  [^sol/Connection conn tx signers & [opts]]
  (go (<p! (.sendTransaction conn tx (->js signers) (->js opts)))))

(defn send-raw-transaction
  [^sol/Connection conn raw-tx & [opts]]
  (go (<p! (.sendRawTransaction conn raw-tx (->js opts)))))

(defn get-recent-blockhash
  [^sol/Connection conn & [commitment]]
  (go (->clj (<p! (.getRecentBlockhash conn commitment)))))

(defn get-version [^sol/Connection conn] (go (<p! (.getVersion conn))))

(defn get-minimum-balance-for-rent-exemption
  [^sol/Connection conn data-length & [commitment]]
  (go (<p! (.getMinimumBalanceForRentExemption conn data-length commitment))))

(defn get-latest-blockhash
  [^sol/Connection conn opts]
  (go (->clj (<p! (.getLatestBlockhash conn (->js opts))))))

(defn get-latest-blockhash-and-context
  [^sol/Connection conn]
  (go (->clj (<p! (.getLatestBlockhashAndContext conn)))))
