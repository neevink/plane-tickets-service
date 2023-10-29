(ns client.myclasses
  (:require-macros [stylo.core :refer [c]]))

(def base-class
  (c :inline-flex
     [:px 2]
     [:py 2]
     [:border "#FAFAFA"]
     :rounded
     [:bg "#FAFAFA"]
     :transition-all [:duration 200] :ease-in-out
     [:focus-within :outline-none :shadow-none [:border "#2e3633"]]
     [:focus :outline-none :shadow-none [:border "#2e3633"]]
     [:hover [:border "#2e3633"]]))

(def div-center
  (c [:border 2]
     [:bg "#fafafa"]
     [:pt 8]
     :text-center
     [:rounded :xl]
     :text-xl
     [:m 2]
     :cursor-pointer
     [:hover :shadow-inner [:bg :gray-200]]))
