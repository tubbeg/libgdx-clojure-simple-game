(ns app-adapter.core
  (:import [com.badlogic.gdx.graphics Texture]
           [com.badlogic.gdx.graphics.g2d SpriteBatch])
  (:require [util.core :as util])
  (:gen-class
   :name libgdx.adapter.AppAdapt
   :extends com.badlogic.gdx.ApplicationAdapter
   :init init
   :state state
   :prefix "-"))

(defn dispose-sprite-and-texture [sb txt]
  (. sb dispose)
  (. txt dispose))

(defn get-state-deref [t]
  (-> (.state t) (deref)))

(defn print-err
  "Remove this!"
  []
  (println "Just in case if you are wondering why the
            program is crashing.")
  (println "It's because you are missing libgdx.png
            in your assets directory"))

(def text-path "libgdx.png")

(defn -init []
  [[] (atom {})])

(defn -create [this]
  (print-err)
  (let [s (.state this)
        t (new Texture text-path)
        b (new SpriteBatch)
        m {:sprite b :texture t}]
    (swap! s (fn [_] m))))

(defn -render [this]
  (let [s (get-state-deref this)
        sprite (:sprite s)
        texture (:texture s)]
    (util/clear-screen)
    (doto sprite
      (.begin)
      (.draw texture (float 140) (float 210))
      (.end))))

(defn -dispose [this]
  (let [s (get-state-deref this)
        sb (:sprite s)
        txt (:texture s)]
    (dispose-sprite-and-texture sb txt)))