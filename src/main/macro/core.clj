(ns macro.core
  (:require [util.core :as util]))


(defmacro draw [sb & body]
  `(do
     (util/sprite-batch-begin ~sb)
     (do ~@body)
     (util/sprite-batch-end ~sb)))