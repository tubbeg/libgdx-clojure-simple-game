(ns app-listener.logic.core
  (:import [com.badlogic.gdx.math MathUtils Rectangle]
           [com.badlogic.gdx.graphics.g2d Sprite])
  (:require [util.core :as u]
            [app-listener.logic.time.core :as t]))

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

(defn no-collision? [bucket-rect droplet]
  (let [[x y] (u/sprite-get-x-y droplet)
        [h w] (u/sprite-get-h-w droplet)
        drop-rect (doto (new Rectangle)
                    (.set x y h w))]
    (-> drop-rect
        (.overlaps bucket-rect)
        (not))))

(defn remove-at-collision! [state]
  (let [s @state
        b (:sprite s)
        dr (:drops s)
        [x y] (u/sprite-get-x-y b)
        [h w] (u/sprite-get-h-w b)
        brect (doto (new Rectangle)
                (.set x y h w))
        flter (-> #(no-collision? brect %)
                  (filter dr))
        m (assoc s :drops flter)]
    (swap! state (fn [_] m))))

(defn logic [state]
  (t/swap-time! state) 
  (when (t/time? state)
    (add-new-drop-to-state! state))
  (remove-droplets! state)
  (remove-at-collision! state)
  (clamp-sprite-x state)
  (move-droplets state))