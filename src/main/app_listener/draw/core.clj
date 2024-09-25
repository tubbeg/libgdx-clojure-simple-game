(ns app-listener.draw.core 
  (:require [util.core :as u]
            [macro.core :as m]))

;alias
(def sb-draw u/sprite-batch-draw-f)
(def s-draw u/sprite-draw)

(defn draw-droplets [state]
  (let [sb (-> state (deref) (:sb))
        drops (-> state (deref) (:drops))]
    (loop [dr drops] 
      (if (-> dr (count) (zero?))
        nil
        (let [el (first dr)
              rem (next dr)]
          (s-draw el sb)
          (recur rem))))))

(defn draw [state]
  (let [vp (u/get-viewport state)
        ;bcket (-> state (deref) (:bucket))
        bckground (-> state (deref) (:background))
        sb (-> state (deref) (:sb))
        sprte (-> state (deref) (:sprite))
        cc (u/get-camera-combined vp)
        [h w] (u/viewport-get-h-w vp)]
    (u/clear-black)
    (u/apply-viewport vp)
    (u/sprite-batch-setprojectmatrix sb cc)
    (m/draw sb
            (sb-draw sb bckground 0 0 w h) 
            (s-draw sprte sb)
            (draw-droplets state))))

