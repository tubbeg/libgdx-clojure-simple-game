(ns app-adapter.core
  (:import [com.badlogic.gdx.graphics Texture]
           [com.badlogic.gdx.graphics.g2d SpriteBatch]
           [com.badlogic.gdx.utils ScreenUtils])
  (:gen-class
   :name libgdx.adapter.AppAdapt
   :extends com.badlogic.gdx.ApplicationAdapter
   :init init
   :state state
   :prefix "-"))

(defn dispose-sprite-and-texture [sb txt]
  (. sb dispose)
  (. txt dispose))

(defn get-state [t]
  (.state t))

(defn get-state-deref [t]
  (-> t (get-state) (deref)))

(defn clear-screen []
  (let [f1 (float 0.15)
        f3 (float 0.2)
        f4 (float 1)]
   (. ScreenUtils clear f1 f1 f3 f4)))

(def text-path "libgdx.png")

(defn -init []
  [[] (atom {})])

(defn -create [this]
  (let [s (get-state this)
        t (new Texture text-path)
        b (new SpriteBatch)
        m {:sprite b :texture t}]
    (swap! s (fn [_] m))))


(defn -render [this]
  (let [s (get-state-deref this)
        sprite (:sprite s)
        texture (:texture s)]
    (clear-screen)
    (doto sprite
      (.begin)
      (.draw texture (float 140) (float 210))
      (.end))))

(defn -dispose [this]
  (let [s (get-state-deref this)
        sb (:sprite s)
        txt (:texture s)]
    (dispose-sprite-and-texture sb txt)))