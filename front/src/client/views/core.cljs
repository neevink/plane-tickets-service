(ns client.views.core
  (:require
   [re-frame.core :as re-frame :refer [dispatch subscribe]]
   [client.events :as events]
   [client.debounce] ; to reg event :)
   [client.routes :as routes]
   [client.myclasses :as cls]
   [client.mycomponents :as components]
   [client.views.tickets :as tickets]
   [client.views.events :as events-view]
   [client.subs :as subs])
  (:require-macros [stylo.core :refer [c]]))

(def selector-values
  {:tickets ["id"
             {:value "name" :desc "Имя"}
             "x"
             "y"
             {:value "refundable" :desc "Возвратный"}
             {:value "type" :desc "Тип билета"}
             "event"]

   :events  ["id"
             {:value "name" :desc "Название мероприятия"}
             {:value "date" :desc "Дата мероприятия "}
             {:value "min-age" :desc "Минимальный возраст"}
             {:value "event-type" :desc "Тип мероприятия"}]})

(defn sort-view [mode]
  [:div
   [:div {:class (c :flex)}
    (components/selector
     (get selector-values mode)
     #())

    (components/selector
     [{:value "netu" :desc "Без сортировки"}
      {:value "asc" :desc "По возрастанию"}
      {:value "desc" :desc "По убыванию"}]
     #()
     {:default-value "netu"
      :cls
      (c [:px 2] :text-center [:w 35])})]])

(defn filter-view-one [prop only=? label & [selector-values]]
  (let [filter-db @(subscribe [::subs/filters prop])
        shown? (:shown filter-db)]
   ^{:key prop}
    [:div {:class (c :border [:mb 4] [:mt 4] [:p 2])}
     [:div {:class (c :flex :flex-row [:m 1] :justify-between)}
      [:i.fa-solid.fa-magnifying-glass]
      [:h1 label]
      [:button {:class (c [:w 6]
                          :align-right
                          :self-end
                          :float-right
                          :rounded
                          :transition-all [:duration 200]
                          [:focus-within :outline-none :shadow-none [:border "#2e3633"]]
                          [:focus :outline-none :shadow-none [:border "#2e3633"]]
                          [:hover [:border "#2e3633"]]
                          [:h 8])
                :on-click #(dispatch [::events/hide-filter prop])}

       (if shown?
         [:i.fa-regular.fa-eye-slash]
         [:i.fa-regular.fa-eye])]]

     (when shown?
       [:div {:class (c :w-full)}
        (when (not only=?)
          (components/selector
           ["=" ">=" "<=" "!="]
            ;;todo
           #()
           {:default-value "="
            :cls (c
                  :w-full
                  [:mb 2])}))

        (if selector-values
          (components/selector selector-values #() ;;todo
                               {:default-value (first selector-values)})
          [:input
           {:class (c
                    :w-full
                    :rounded :border [:h 8])
            :on-change
            #(dispatch [:dispatch-debounce
                        {:delay 500
                         :event [::events/change-filter
                                 prop
                                 :value
                                 (.. % -target -value)]}])
            :placeholder "Фильтр"}])])]))

(def filter-values
  {:tickets [[:id false "id"]
             [:name true "Имя"]
             [:x false "Координата x"]
             [:y false "Координата y"]
             [:price false "Цена"]
             [:discount false "Скидка"]
             [:refundable true "Возвратный" [true false]]
             [:type false "Тип" ["CHEAP" "BUDGETARY" "USUAL" "VIP"]]

            ;; event true "event"]
             ]
   :events [[:id false "id"]
            [:name true "Название мероприятия"]
            [:date false "Дата проведения"]
            [:min-age false "Минимальный возраст"]
            [:event-type true "Тип мероприятия" ["CONCERT", "BASEBALL", "BASKETBALL", "THEATRE_PERFORMANCE"]]]})

(defn filter-view [mode]
  (let [values (get filter-values mode)]
    [:div
     (doall
      (for [v values]
        (apply filter-view-one v)))]))

(defn header [mode]
  [:div {:class (c :flex :flex-col)}
   (sort-view mode)
   [:hr {:class (c [:pt 2])}]
   (filter-view mode)])

(defn home-panel []
  (let [mode @(re-frame/subscribe [::subs/mode])]
    [:div {:class (c [:px 15] [:py 2])}
     [:h1 {:class (c :text-center)}
      "SOA Lab2 Slava+Kirill24"]
     [:div
      {:class (c :font-mono [:pt 2])}
      [:div
       [:button
        {:on-click #(dispatch [::events/set-mode :tickets])
         :class (c [:px 1] :underline)}
        "Билеты"]
       [:button
        {:on-click #(dispatch [::events/set-mode :events])
         :class (c [:px 1] :underline)}
        "Ивенты"]
       [:div  {:class (c :flex)}
        (header mode)

        (when (= :events mode)
          (events-view/events-page))
        (when (= :tickets mode)
          [:div
           (tickets/tickets-view)])]]]]))

(defmethod routes/panels :home-panel [] [home-panel])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    (routes/panels @active-panel)))
