(ns util.core
  (:import [com.badlogic.gdx.utils ScreenUtils]
           [com.badlogic.gdx Gdx]
           ; use $ to import inner classes
           [com.badlogic.gdx Input$Keys]
           [com.badlogic.gdx.graphics Color]
           [com.badlogic.gdx.graphics.g2d Sprite])
  (:require [clojure.reflect :as cr]
            [clojure.pprint :as pp]))


(def black (. Color BLACK))
(def right (. Input$Keys RIGHT))
(def left (. Input$Keys LEFT))

(comment (->> String
     cr/reflect
     :members
     (filter #(contains? (:flags %) :public))
     pp/print-table)
)

(defn sprite-set-x [sprite x]
  (doto sprite
    (.setX x)))

(defn sprite-get-x-y [sprite]
  [(.getX sprite) (.getY sprite)])

(defn sprite-get-h-w [sprite]
  [(.getHeight sprite) (.getWidth sprite)])

(defn sprite-translate-y [sprite y]
  (->> y (float) (.translateY sprite)))

(defn sprite-translate-x [sprite x]
  (->> x (float) (.translateX sprite)))

(defn clear-screen []
  (let [f1 (float 0.15)
        f3 (float 0.2)
        f4 (float 1)]
    (. ScreenUtils clear f1 f1 f3 f4)))

(defn gdx-internal-file
  "This function can throw an exception if:
     * The file path is incorrect
     * Gdx has not been initialized"
  [path]
  ; doto is good for setters, but it also
  ; returns the original object
  ; use .. when applicable
  (.. Gdx files (internal path)))

(def msg "Check that Gdx environment has been initialized.
          This function should run in the scope
          of an applicationlistener. Also check
          that the file path is correct")

(defn err-msg [e]
  (println msg)
  (throw e))

(defn try-internal-file [path]
  (try (gdx-internal-file path)
       (catch Exception e
         (err-msg e))))

(defn create-sound
  [path] 
  (let [file (try-internal-file path)]
    (.. Gdx audio (newSound file))))

(defn create-music
  [path]
  (let [file (try-internal-file path)]
    (.. Gdx audio (newMusic file))))

(defn apply-viewport [viewport]
  (doto viewport
    (.apply)))

(defn get-camera-combined [viewport]
  (.. viewport getCamera combined))

(defn sprite-batch-begin [sb]
  (doto sb
    (.begin)))

(defn sprite-batch-draw [sb texture x y a b]
  (doto sb
    (.draw texture x y a b)))

(defn sprite-batch-draw-f [sb texture x y a b]
  (sprite-batch-draw sb texture
                     (float x)
                     (float y)
                     (float a)
                     (float b)))

(defn sprite-batch-end [sb]
  (doto sb
    (.end)))

(defn sprite-batch-setprojectmatrix [sb cam-combo]
  (doto sb
    (.setProjectionMatrix cam-combo)))

(defn viewport-get-width [vp]
  (.getWorldWidth vp))

(defn viewport-get-height [vp]
  (.getWorldHeight vp))

(defn viewport-get-h-w [vp]
  [(viewport-get-height vp)
   (viewport-get-width vp)])

(defn clear-black []
  (. ScreenUtils (clear black)))

(defn sprite-draw [sprite sprite-batch]
  (doto sprite
    (.draw sprite-batch)))

(defn get-viewport [state]
  (-> state (deref) (:viewport)))

(defn make-sprite [texture x y]
  (doto (new Sprite texture)
    (.setSize (float x) (float y))))

(defn key-is-pressed
  "This function will throw an exception if the
   Gdx environment class has not been instantiated"
  [key] 
  (let [i (. Gdx input)]
    (. i isKeyPressed key)))

(defn get-delta-time
  "This function will throw an exception if the
     Gdx environment class has not been instantiated"
  []
  (->
   (. Gdx graphics)
   (.getDeltaTime)))

