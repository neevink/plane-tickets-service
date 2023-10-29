(ns client.db)

(def default-filters
 {:tickets
  {:id {:value "" :shown true}
   :name {:value "" :shown false}
   :x {:value "" :shown false}
   :y {:value "" :shown false}
   :refundable {:value "" :shown false}
   :type {:value "" :shown false}
   :eventId  {:value "" :shown false}}

  :events {:id
           {:value "" :shown true}
           :name
           {:value "" :shown false}
           :date
           {:value "" :shown false}
           :minAge
           {:value "" :shown false}
           :eventType
           {:value "" :shown false}}}
 )
(def default-db
  {:active-panel :home-panel
   :toggle-new false
   :toggle-change false
   :paging {:current-page 1
            :page-size 5}
   :event {:sorting {0 {:field :id :sort-order nil}}}
   :ticket {:update-id 1
            :sorting {0 {:field :id :sort-order nil}}
            :edit {:ticket-id 1
                   :path {:name false
                         ;; ...
                          }}}
   :filters default-filters
   :mode :events #_:tickets})

