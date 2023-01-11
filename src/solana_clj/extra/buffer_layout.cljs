(ns solana-clj.extra.buffer-layout
  (:refer-clojure :exclude [array])
  (:require [solana-clj.publickey :as pubkey]
            ["buffer" :as buffer]
            ["bn.js" :as bn]))

(defn buffer-from [^js uint8array] (buffer/Buffer.from uint8array))

(defprotocol IBufferLayout
  (-size [this])
  (-unpack [this buf]))

(defmulti unpack (fn [type _buf] type))

(defmulti size (fn [type] type))

(defmethod unpack :bool [_ ^js buf] (= (.readUIntLE buf 0 1) 1))

(defmethod size :bool [_] 1)

(defmethod unpack :u64 [_ ^js buf] (.readBigUInt64LE buf))

(defmethod size :u64 [_] 8)

(defmethod unpack :i64 [_ ^js buf] (.readBigInt64LE buf 0 8))

(defmethod size :i64 [_] 8)

(defmethod unpack :u16 [_ ^js buf] (.readUInt16LE buf 0 2))

(defmethod size :u16 [_] 2)

(defmethod unpack :u32 [_ ^js buf] (.readUInt32LE buf 0 4))

(defmethod size :u32 [_] 4)

(defmethod unpack :i32 [_ ^js buf] (.readInt32LE buf 0 4))

(defmethod size :u32 [_] 4)

(defmethod unpack :u8 [_ ^js buf] (.readUInt8 buf))

(defmethod size :u8 [_] 1)

(defmethod unpack :pubkey
  [_ ^js buf]
  (pubkey/make-public-key (.slice buf 0 32)))

(defmethod size :pubkey [_] 32)

(defmethod unpack :default [parser-spec ^js buf] (-unpack parser-spec buf))

(defmethod size :default [parser-spec] (-size parser-spec))

(defrecord StructBufferLayout [ctor size layouts])

(extend-type StructBufferLayout
  IBufferLayout
    (-size [{:keys [size]}] size)
    (-unpack [{:keys [ctor layouts]} buf]
      (loop [buf buf
             [layout & rest-layouts] layouts
             ctor-args []]
        (if layout
          (let [offset (size layout)
                value (unpack layout buf)
                next-buf (.slice ^js buf offset)]
            (recur next-buf rest-layouts (conj ctor-args value)))
          (apply ctor ctor-args)))))

(defn struct
  [ctor layouts]
  (let [size (transduce (map size) + layouts)]
    (->StructBufferLayout ctor size layouts)))

(defrecord OptionBufferLayout [type])

(extend-type OptionBufferLayout
  IBufferLayout
    (-size [{:keys [type]}] (inc (size type)))
    (-unpack [{:keys [type]} ^js buf]
      (let [opt (.readIntLE buf 0 1)]
        (when (= opt 1) (unpack type (.slice buf 1))))))

(defn option [type] (->OptionBufferLayout type))

(defrecord Option32BufferLayout [type])

(extend-type Option32BufferLayout
  IBufferLayout
    (-size [{:keys [type]}] (+ 4 (size type)))
    (-unpack [{:keys [type]} ^js buf]
      (let [opt (.readIntLE buf 0 4)]
        (when (= opt 1) (unpack type (.slice buf 4))))))

(defn option32 [type] (->Option32BufferLayout type))

(defrecord ArrayBufferLayout [length type])

(extend-type ArrayBufferLayout
  IBufferLayout
    (-size [{:keys [length type]}] (* length (size type)))
    (-unpack [{:keys [length type]} ^js buf]
      (loop [buf buf
             i 0
             arr []]
        (if (= i length)
          arr
          (recur (.slice ^js buf (size type))
                 (inc i)
                 (conj arr (unpack type buf)))))))

(defn array [length type] (->ArrayBufferLayout length type))

(defrecord EnumBufferLayout [enums])

(extend-type EnumBufferLayout
  IBufferLayout
    (-size [_] 1)
    (-unpack [{:keys [enums]} ^js buf] (nth enums (.readIntLE buf 0 1))))

(defn enum [& enums] (->EnumBufferLayout enums))

(defrecord RawBufferLayout [length]
  IBufferLayout
    (-size [{:keys [length]}] length)
    (-unpack [{:keys [length]} ^js buf] (.slice buf 0 length)))

(defn raw-buffer [length] (->RawBufferLayout length))
