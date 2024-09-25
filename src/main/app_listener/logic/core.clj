(ns app-listener.logic.core
  (:import [com.badlogic.gdx.math MathUtils]
           [com.badlogic.gdx.graphics.g2d Sprite])
  (:require [util.core :as u]))

(defn create-droplet [dr vp txt]
  (let [[vph vpw] (u/viewport-get-h-w vp)
        s (float 1)
        r (-> vpw (- s) (rand))
        d  (doto (new Sprite txt)
             (.setSize s s)
             (.setX (float r))
             (.setY (float (- vph 1))))]
    (conj dr d)))

(defn add-new-drop-to-state! [state]
  (let [s @state
        droplets (s :drops)
        vp (s :viewport)
        txt (s :drop)
        coll (create-droplet droplets vp txt)
        m (assoc s :drops coll)]
    (swap! state (fn [_] m))))

(defn clamp-clh [current low-lim high-lim] 
   (. MathUtils clamp
      (float current)
      (float low-lim)
      (float high-lim)))

(defn clamp-sprite-x [state]
  (let [vp (-> state (deref) (:viewport))
        sprite (-> state (deref) (:sprite))
        sw (.getWidth sprite)
        [_ w] (u/viewport-get-h-w vp)
        ws (- w sw)]
    (as-> sprite $
      (.getX $)
      (clamp-clh $ 0 ws)
      (u/sprite-set-x sprite $))))

(defn move-drop [t drop]
  (u/sprite-translate-y drop t))

(defn move-droplets [state]
  (let [droplets (-> state (deref) (:drops))
        f (float -2)
        t (-> (u/get-delta-time) (* f))] 
    (loop [dr droplets] 
      (if (zero? (count dr))
        nil
        (let [frst (first dr)
              rem (next dr)]
          (move-drop t frst)
          (recur rem))))))

(defn sprite-y-greater-than-height? [sprite]
  (let [f1 (float -1)
        sh (-> sprite (.getHeight) (* f1))]
   (-> sprite (.getY) (> sh))))

(defn filter-droplets [coll]
  (filter sprite-y-greater-than-height? coll))

(defn remove-droplets! [state]
  (let [droplets (-> state (deref) (:drops))
        flter (filter-droplets droplets)
        m (assoc @state :drops flter)] 
    (swap! state (fn [_] m))))

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

(defn logic [state]
  (swap-time! state) 
  (when (time? state)
    (add-new-drop-to-state! state))
  (remove-droplets! state)
  (clamp-sprite-x state)
  (move-droplets state))