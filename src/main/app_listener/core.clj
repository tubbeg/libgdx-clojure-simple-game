(ns app-listener.core
  (:import [com.badlogic.gdx ApplicationListener]
           [com.badlogic.gdx.graphics.g2d SpriteBatch Sprite]
           [com.badlogic.gdx.graphics Texture]
           [com.badlogic.gdx Gdx Input]
           [com.badlogic.gdx.utils.viewport FitViewport])
  (:require [util.core :as u]
            [app-listener.input.core :as i]
            [app-listener.logic.core :as l]
            [app-listener.draw.core :as d]
            [util.core :as util]))

(def background "background.png")
(def bucket "bucket.png")
(def drop-mp3 "drop.mp3")
(def drop-img "drop.png")
(def music-path "music.mp3")

(defn create-state [sb s vp b d ba mu so dr t]
  {:sb sb
   :sprite s
   :drops dr
   :viewport vp
   :bucket b
   :drop d
   :background ba
   :music mu
   :sound so
   :timer t})

(defn create-props [state]
  (let [b (new Texture bucket)
        d (new Texture drop-img)
        ba (new Texture background)
        mu (u/create-music music-path)
        so (u/create-sound drop-mp3)
        vp (new FitViewport 8 5)
        s (u/make-sprite b 1 1)
        sb (new SpriteBatch)
        dr (-> [] (l/create-droplet vp d))
        t (float 0)
        m (create-state sb s vp b d ba mu so dr t)]
    (swap! state (fn [_] m))))

(comment(defn print-drops [state]
  (let [drops-nr (-> state (deref) (:drops) (count))]
    (println drops-nr)))
)
(defn render-scene [state] 
  (i/input state)
  (l/logic state)
  (d/draw state))

(defn resize-viewport [state w h]
  (let [v (u/get-viewport state)]
    (doto v
      (.update w h true))))


; Implementing the ApplicationListener Java interface
(def app-adapter
  (let [state (atom {})]
   (reify ApplicationListener
     (create [this]
       (create-props state))
     (resize [this width height]
       (resize-viewport state width height))
     (render [this]
       (render-scene state))
     (pause [this])
     (resume [this])
     (dispose [this]))))
