(ns cipher.core
  (:require [clojure.string :as str])
  (:import (java.text Normalizer)))

(defn to-int
  "takes a lowercase letter character and returns its position in the alphabet: a = 0, b = 1, etc."
  [char-letter]
  (let [ascii-a (int \a)]
    (- (int char-letter) ascii-a)))

(defn to-char [position]
  (let [ascii-a (int \a)]
    (char (+ position ascii-a))))

(defn shift [char-letter number]
  (let [number-of-letters 26]
    (-> (to-int char-letter)
        (+ number)
        (mod number-of-letters)
        to-char)))

(defn encrypt [word key]
  (->> word
       (map #(shift % key))
       (apply str)))

(defn decrypt [word key]
  (encrypt word (- key)))

(defn deaccent [str]
  "Remove accent from string"
  ;; http://www.matt-reid.co.uk/blog_post.php?id=69
  (let [normalized (Normalizer/normalize str java.text.Normalizer$Form/NFD)]
    (str/replace normalized #"\p{InCombiningDiacriticalMarks}+" "")))

(defn get-letters [phrase]
  (->> phrase
       str/lower-case
       deaccent
       (filterv #(Character/isLetter %))
       (apply str)))

(defn encrypt-letters [phrase key]
  (-> (get-letters phrase)
      (encrypt key)))

(defn encrypt-text [text key]
  (-> text
      str/lower-case
      (str/replace #"[a-z]" #(encrypt % key))))

(defn decrypt-text [text key]
  (-> text
      (str/replace #"[a-z]" #(decrypt % key))))

(defn count-letters [character word]
  (->> (vec word)
       (filter #(= character %))
       count))

(def alphabet
  (map to-char (range 26)))



