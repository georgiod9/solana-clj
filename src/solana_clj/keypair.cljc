(ns solana-clj.keypair
  (:require
   #?@(:cljs
       [["@solana/web3.js" :as sol]])))

#?(:cljs
   (defn make-keypair
     ([]
      (sol/Keypair.))
     ([keypair]
      (sol/Keypair. keypair))))

#?(:cljs
   (defn generate
     []
     (sol/Keypair.generate)))

#?(:cljs
   (defn from-secret-key
     [^js/Uint8Array secret-key & {:keys [skipValidation]}]
     (sol/Keypair.fromSecretKey secret-key #js {:skipValidation skipValidation})))

#?(:cljs
   (defn from-seed
     [^js/Uint8Array seed]
     (sol/Keypair.fromSeed seed)))

#?(:cljs
   (defn secret-key
     [^sol/Keypair keypair]
     (.-secretKey keypair)))

#?(:cljs
   (defn public-key
     [^sol/Keypair keypair]
     (.-publicKey keypair)))
