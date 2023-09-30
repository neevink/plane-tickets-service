(ns client.myclasses
  (:require-macros [stylo.core :refer [c]]))

(def plus-button
  (c [:mb 5]
     :border
     :rounded
     [:bg "#FAFAFA"]
     :transition-all [:duration 200] #_:ease-in-out
     [:focus-within :outline-none :shadow-none [:border "#2e3633"]]
     [:focus :outline-none :shadow-none [:border "#2e3633"]]
     [:hover [:border "#2e3633"]]
     :w-full 
     :w-min-full 
     [:h 8]))


