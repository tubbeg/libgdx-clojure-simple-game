(ns app-listener.input.core 
  (:require [util.core :as u]))


(defn move [sprite]
  (let [d (u/get-delta-time)
        speed (-> 1 (/ (/ 1 2)) (float) (* d))
        rverse (* speed -1)]
    (when (u/key-is-pressed u/right)
      (u/sprite-translate-x sprite speed))
    (when (u/key-is-pressed u/left)
      (u/sprite-translate-x sprite rverse))))


(defn input [state]
  (let [sprite (-> state (deref) (:sprite))]
    (move sprite)))