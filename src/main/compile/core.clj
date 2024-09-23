(ns compile.core)

(defn compile-app-adapter []
  (compile 'app-adapter.core))

(comment
  "
   This project uses Ahead-of-Time (AoT) compilation, because
   we need to add properties to the applicationAdapter

   Use the function above to compile the namespace into a
   .class file. You can then import it into your clj namespace
   just like any other Java .class file.
   
  " 
  )

;(compile-app-adapter)