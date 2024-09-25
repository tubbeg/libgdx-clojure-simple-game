(ns app-listener.logic.time.core 
  (:require [util.core :as u]))

(defn fix-time [state]
  (let [t (-> state (deref) (:timer))
        d (u/get-delta-time)
        s (+ d t)]
    (if (> s (float 1)) (float 0) s)))

(defn swap-time! [state]
  (let [t (fix-time state)
        m (assoc @state :timer t)]
    (swap! state (fn [_] m))))

(defn time? [state]
  (-> state (deref) (:timer) (= (float 0))))
