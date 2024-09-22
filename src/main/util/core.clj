(ns util.core
  (:import [com.badlogic.gdx.utils ScreenUtils]))

(defn clear-screen []
  (let [f1 (float 0.15)
        f3 (float 0.2)
        f4 (float 1)]
    (. ScreenUtils clear f1 f1 f3 f4)))