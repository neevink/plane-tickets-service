(ns client.db)

(def example-tickets
  {1
   {:id 1 :name "Партер левая сторона, ряд 3, место 8"
    :coordinates {:x 1 :y 2}
    :creationDate "2023-12-13"
    :price 1000.0
    :discount 20.0
    :refundable true
    :type "VIP"
    :eventId 1}

   2
   {:id 2 :name "Партер левая сторона, ряд 3, место 7"
    :coordinates {:x 1 :y 2}
    :creationDate "2023-12-13"
    :price 500.0
    :discount 20.0
    :refundable true
    :type "USUAL"
    :eventId 2}

   3
   {:id 3 :name "Партер левая сторона, ряд 3, место 9"
    :coordinates {:x 1 :y 2}
    :creationDate "2023-12-13"
    :price 300.0
    :discount 20.0
    :refundable true
    :type "BUDGETARY"
    :eventId 3}

   4
   {:id 4 :name "Партер левая сторона, ряд 3, место 9"
    :coordinates {:x 1 :y 2}
    :creationDate "2023-12-13"
    :price 300.0
    :discount 20.0
    :refundable true
    :type "BUDGETARY"
    :eventId 4}

   5
   {:id 5 :name "Партер левая сторона, ряд 3, место 9"
    :coordinates {:x 1 :y 2}
    :creationDate "2023-12-13"
    :price 300.0
    :discount 20.0
    :refundable true
    :type "BUDGETARY"
    :eventId 5}})

(def example-events
  {1
   {:id 1 :name "Дора" :date "2023-12-14" :min-age 10 :event-type "CONCERT"}

   2
   {:id 2 :name "Лепс" :date "2023-12-14" :min-age 10 :event-type "CONCERT"}

   3
   {:id 3 :name "Розенбаум" :date "2023-12-14" :min-age 10 :event-type "CONCERT"}

   4
   {:id 4 :name "Лазарев" :date "2023-12-14" :min-age 10 :event-type "CONCERT"}

   5
   {:id 5 :name "Иванушки" :date "2023-12-14" :min-age 10 :event-type "CONCERT"}})

(def default-db
  {:active-panel :home-panel
   :tickets example-tickets
   :events example-events
   :toggle-new false
   :toggle-change false
   :paging {:current-page 1 :last-page 5
            :page-size 7}
   :ticket {:update-id 1
            :edit {:ticket-id 1
                   :path {:name false
                         ;; ...
                          }}}
   :filters
   {:id {:value "" :shown true}
    :name {:value "" :shown false}
    :x {:value "" :shown false}
    :y {:value "" :shown false}
    :refundable {:value "" :shown false}
    :type {:value "" :shown false}
    :event  {:value "" :shown false}}
   :mode :events #_:tickets})

