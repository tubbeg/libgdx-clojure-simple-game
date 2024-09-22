(ns launcher.core
  (:import [com.badlogic.gdx.backends.lwjgl3
            Lwjgl3ApplicationConfiguration Lwjgl3Application]
           [libgdx.adapter AppAdapt]))


(defn get-display-mode []
  (. Lwjgl3ApplicationConfiguration getDisplayMode))

(defn get-refresh-rate [display-mode]
  (.refreshRate display-mode))

(defn create-config []
  (let [fps (-> (get-display-mode) (get-refresh-rate))]
   (doto (new Lwjgl3ApplicationConfiguration)
     (.setTitle "MyAwesomeGame")
     (.useVsync true)
     (.setForegroundFPS fps)
     (.setWindowedMode 640 480)
     ;setWindowIcon is not working for some reason
     ;(.setWindowIcon "libgdx128.png" "libgdx64.png" "libgdx32.png" "libgdx16.png")
     )))

(defn launch-application []
  (let [app (new AppAdapt)
        config (create-config)]
   (new Lwjgl3Application app config)))

(launch-application)