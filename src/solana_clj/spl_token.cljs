(ns solana-clj.spl-token
  (:require ["@solana/spl-token" :as spl-token]
            ["@solana/web3.js" :as sol]
            [cljs-bean.core :refer [->clj bean ->js]]
            [cljs.core.async.interop :refer [<p!]]
            [cljs.core.async :as a :refer [go <!]]
            [solana-clj.extra.buffer-layout :as bl]))

(def ^js AccountLayout (bean spl-token/AccountLayout))
(def ^js MintLayout (bean spl-token/MintLayout))

(def ^js Token spl-token/Token)

(def ^:const token-program-id spl-token/TOKEN_PROGRAM_ID)
(def ^:const associated-token-program-id spl-token/ASSOCIATED_TOKEN_PROGRAM_ID)
(def ^:const native-mint spl-token/NATIVE_MINT)

;; Authority types
(def ^:const authority-type-mint-tokens "MintTokens")
(def ^:const authority-type-freeze-account "FreezeAccount")
(def ^:const authority-type-account-owner "AccountOwner")
(def ^:const authority-type-close-account "CloseAccount")

(defn create-initialize-account-instruction
  "Construct an InitializeAccount instruction
  account: New token account
  mint: Mint account
  owner: Owner of the new account"
  [^sol/Pubkey account ^sol/Pubkey mint ^sol/Pubkey owner]
  (spl-token/createInitializeAccountInstruction account
                                                mint
                                                owner
                                                token-program-id))

;; (defn get-min-balance-rent-for-exempt-mint
;;   [^sol/Connection conn]
;;   (go (<p! (spl-token/Token.getMinBalanceRentForExemptMint conn))))

;; (defn get-min-balance-rent-for-exempt-account
;;   [^sol/Connection conn]
;;   (go (<p! (spl-token/Token.getMinBalanceRentForExemptAccount conn))))

;; (defn get-min-balance-rent-for-exempt-multisig
;;   [^sol/Connection conn]
;;   (go (<p! (spl-token/Token.getMinBalanceRentForExemptMultisig conn))))

(defn get-associated-token-address
  "Get the address for the associated token account.

  associated-program-id: SPL Associated Token program account
  program-id: SPL Token program account
  mint: Token mint account
  owner: Owner of the new account"
  [mint owner & [allow-owner-off-curve]]
  (go (<p! (spl-token/getAssociatedTokenAddress mint
                                                owner
                                                (true? allow-owner-off-curve)
                                                token-program-id
                                                associated-token-program-id))))

(defn create-associated-token-account-instruction
  [payer associated-account owner mint]
  (spl-token/createAssociatedTokenAccountInstruction
    payer
    associated-account
    owner
    mint
    token-program-id
    associated-token-program-id))

(defn create-transfer-instruction
  [source destination owner amount multi-signers]
  (spl-token/createTransferInstruction source
                                       destination
                                       owner
                                       amount
                                       (->js multi-signers)
                                       token-program-id))

(defn get-mint
  [connection address commitment]
  (go (->clj
        (<p!
          (spl-token/getMint connection address commitment token-program-id)))))

(defn create-sync-native-instruction
  [account]
  (spl-token/createSyncNativeInstruction account))

(defrecord TokenAccount [mint owner amount delegate state is-native
                         delegated-amount close-authority])

(def token-account-layout
  (bl/struct ->TokenAccount
             [:pubkey :pubkey :u64 (bl/option32 :pubkey) :u8 (bl/option32 :u64)
              :u64 (bl/option32 :pubkey)]))
