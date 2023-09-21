(ns client.db)

(def example
  {1
   {:id 1 :name "Партер левая сторона, ряд 3, место 8"
    :coordinates {:x 1 :y 2}
    :creation-date "2023-12-13"
    :price 1000.0
    :discount 20.0
    :refundable true
    :type "VIP"
    :event {:id 1 :name "Концерт Доры" :date "2023-12-14" :min-age 10 :event-type "CONCEPT"}}
   
   2
   {:id 2 :name "Партер левая сторона, ряд 3, место 7"
    :coordinates {:x 1 :y 2}
    :creation-date "2023-12-13"
    :price 500.0
    :discount 20.0
    :refundable true
    :type "USUAL"
    :event {:id 1 :name "Концерт Доры" :date "2023-12-14" :min-age 10 :event-type "CONCEPT"}}
   
   3
   {:id 3 :name "Партер левая сторона, ряд 3, место 9"
    :coordinates {:x 1 :y 2}
    :creation-date "2023-12-13"
    :price 300.0
    :discount 20.0
    :refundable true
    :type "BUDGETARY"
    :event {:id 1 :name "Концерт Доры" :date "2023-12-14" :min-age 10 :event-type "CONCEPT"}}
   })


(def default-db
  {:active-panel :home-panel
   ;; :active-panel :client.views/home-panel
   :tickets example
   :mode :tickets})

