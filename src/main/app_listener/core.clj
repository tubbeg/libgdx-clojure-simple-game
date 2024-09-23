(ns app-listener.core
  (:import [com.badlogic.gdx ApplicationListener]
           [com.badlogic.gdx.graphics.g2d SpriteBatch]
           [com.badlogic.gdx.graphics Texture])
  (:require [util.core :as util]))



(comment
  "libGDX has a design flaw which is the Gdx state.
   It is defined in the com.badlogic.gdx.Gdx package.
   
   It's essentially a global variable.
   
   Some classes, like the Texture will fail during
   its constructor if the global variable has the wrong
   state.

   As a rule of thumb, your classes should never throw an
   exception in its constructor. The constructor should
   behave almost like an atom. 

   And using global variables is generally speaking a
   bad idea.

   The GDX state is why we can't initialize Textures without
   implementing applicationlistener. This makes it harder
   to test the application.
   "
  )

(def image-path "hello_world.png")

(defn create-props [state]
  (let [t (new Texture image-path)
        b (new SpriteBatch)]
    (swap! state (fn [_] {:sprite b :texture t}))))

(defn render-image [state]
  (let [s (:sprite @state)
        t (:texture @state)
        f1 (float 140)
        f2 (float 210)]
    (util/clear-screen)
    (doto s
      (.begin)
      (.draw t f1 f2)
      (.end))))

(defn dispose-sprite-texture [state]
  (let [s (:sprite @state)
        t (:texture @state)]
    (.dispose s)
    (.dispose t)))

; Implementing the ApplicationListener Java interface
(def app-adapter
  (let [state (atom {})]
   (reify ApplicationListener
     (create [this]
       (create-props state))
     (resize [this width height])
     (render [this]
       (render-image state))
     (pause [this])
     (resume [this])
     (dispose [this]
       (dispose-sprite-texture state)))))
