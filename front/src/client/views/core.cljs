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

(def sort-selector-values
  {:tickets ["id"
             {:value "name" :desc "Имя"}
             "x"
             "y"
             {:value "creationDate" :desc "Дата создания"}
             {:value "price" :desc "Цена"}
             {:value "discount" :desc "Скидка"}
             {:value "refundable" :desc "Возвратный"}
             {:value "type" :desc "Тип мероприятия"}]

   :events  ["id"
             {:value "name" :desc "Название мероприятия"}
             {:value "date" :desc "Дата мероприятия"}
             {:value "minAge" :desc "Минимальный возраст"}
             {:value "eventType" :desc "Тип ивента"}]})

(defn one-sort [id mode]
  [:div
   [:div {:class (c :flex)}
    [:div {:class (c [:pr 5]
                     [:pt 2]
                     :cursor-pointer)
           :on-click #(dispatch [::events/remove-sorting mode id])}
     "[X]"]
    (components/selector
      (get sort-selector-values mode)
      #(dispatch [::events/change-sort id :field  (.. % -target -value)]))

    (components/selector
      [{:value "netu" :desc "Без сортировки"}
       {:value "asc" :desc "По возрастанию"}
       {:value "desc" :desc "По убыванию"}]
      #(dispatch [::events/change-sort id :sort-order (.. % -target -value)])
      {:default-value "netu"
       :cls
       (c [:px 2] :text-center [:w 35])})]])

(defn sort-view [mode]
  (let [sortings @(subscribe [::subs/sortings mode])]
    [:div
     [:div {:class (c :text-center :font-bold)}
      [:h3 "Сортировки"]
      (when sortings (for [[sort-id _sort-map] sortings]
        ^{:key sort-id}
        [one-sort sort-id mode]))
      (when (< (count sortings) 5)
        [:div {:class [(c :cursor-pointer
                        [:my 2]
                        [:border "#FAFAFA"]
                        :rounded
                        [:bg "#FAFAFA"]
                        :transition-all [:duration 200] :ease-in-out
                        [:focus-within :outline-none :shadow-none [:border "#2e3633"]]
                        [:focus :outline-none :shadow-none [:border "#2e3633"]]
                        [:hover [:border "#2e3633"]]) (c :text-center :w-full)]
             :on-click
             #(dispatch [::events/add-sorting mode])}
       [:i.fa-solid.fa-plus]])]]))

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
           ["=" ">" "<" "!="]
           #(dispatch
             [:dispatch-debounce
              {:delay 300
               :event [::events/change-filter
                       prop
                       :operator
                       (.. % -target -value)]}])
           {:default-value "="
            :cls (c :w-full [:mb 2])}))

        (if selector-values
          (components/selector selector-values
                               #(dispatch [::events/change-filter
                                           prop
                                           :value
                                           (.. % -target -value)])                               {:default-value (first selector-values)})
          [:input
           {:class (c
                    :w-full
                    :rounded :border [:h 8])
            :maxLength 30
            :on-change
            #(dispatch [:dispatch-debounce
                        {:delay 1000
                         :event [::events/change-filter
                                 prop
                                 :value
                                 (.. % -target -value)]}])
            :placeholder "Фильтр"}])])]))

(defn get-filter-values [mode]
  (let [ticket-types @(subscribe [::subs/ticket-types])
        event-types @(subscribe [::subs/event-types])]
    (get {:tickets [[:id false "id"]
                    [:name true "Имя"]
                    [:coordinateX false "Координата x"]
                    [:coordinateY false "Координата y"]
                    [:creationDate false "Дата создания"]
                    [:price false "Цена"]
                    [:discount false "Скидка"]
                    [:refundable true "Возвратный" [true false]]
                    [:type false "Тип" ticket-types]]
          :events [[:id false "id"]
                   [:name true "Название мероприятия"]
                   [:date false "Дата проведения"]
                   [:minAge false "Минимальный возраст"]
                   [:eventType true "Тип мероприятия"
                    event-types]]} mode)))

(defn filter-view [mode]
  (let [values (get-filter-values mode)]
    [:div
     [:h3 {:class (c :text-center :font-bold)} "Фильтры"]
     (doall
      (for [v values]
        (apply filter-view-one v)))]))

(def button-cls (c :cursor-pointer
                   [:my 2]
                   [:border "#FAFAFA"]
                   :rounded
                   [:bg "#FAFAFA"]
                   :transition-all [:duration 200] :ease-in-out
                   [:focus-within :outline-none :shadow-none [:border "#2e3633"]]
                   [:focus :outline-none :shadow-none [:border "#2e3633"]]
                   [:hover [:border "#2e3633"]]))

(defn header [mode]
  (let [tickets-discount-sum @(subscribe [::subs/tickets-discount-sum])
        tickets-discount-count @(subscribe [::subs/tickets-discount-count])
        tickets-types-count @(subscribe [::subs/tickets-types-count])
        ticket-types @(subscribe [::subs/ticket-types])
        ticket-discount-count-opened? @(subscribe [::subs/ticket-discount-count-opened])]
    [:div {:class (c :flex :flex-col)}
     [:h3 {:class (c :text-center :font-bold)} "Крутые запросы"]
     [:div {:class [(c :flex :flex-col) (c :text-center :w-full)]}

      [:div {:class (c :flex)}
       [:div
        [:button {:class button-cls
                  :on-click
                  #(dispatch [::events/download-ticket-discount-sum])}
         "Сумма скидок"]
        [:h5 tickets-discount-sum]]
       [:div
        [:button {:class button-cls
                  :on-click
                  #(dispatch [::events/download-ticket-discount-count])}
         "Количество билетов в зависимости от скидки"]
        (when ticket-discount-count-opened?
          [components/modal
           "Количество билетов в зависимости от скидки"
           [:div
            [:table {:class [(c :border :table :w-full)
                             cls/div-center]}

             [:thead
              [:tr [:th "Скидка"]
               [:th "Количество билетов"]]]
             (for [one-map tickets-discount-count]
               ^{:key (:discount one-map)}
               [:tr
                [:td (:discount one-map)]
                [:td (:count one-map)]])]]
           [:button.cancelBtn {:class (c [:w-min 100])
                               :on-click #(dispatch [::events/close-hueta])}
            "Очень информативно, спасибо"]
           :modal-medium])]]
      [:hr {:class (c [:pt 2])}]

      [:div
       [:button {:class button-cls
                 :on-click
                 #(dispatch [::events/download-ticket-types-count])}
        "Количество билетов меньших заданного типа"]
       [components/selector ticket-types #(dispatch [::events/change-ticket-type (.. % -target -value)])]

       [:h5 tickets-types-count]]]

     [:hr {:class (c [:pt 2])}]
     (sort-view mode)
     [:hr {:class (c [:pt 2])}]
     (filter-view mode)]))

(defn home-panel []
  (let [mode @(re-frame/subscribe [::subs/mode])
        reloading? @(subscribe [::subs/reloading])]
    [:div {:class (c [:px 15] [:py 2])}
     [:h1 {:class (c :text-center)}
      "SOA Lab2 Slava+Kirill"]
     [:div
      {:class (c :font-mono [:pt 2])}
      [:div
       [:button
        {:on-click #(dispatch [::events/set-mode :tickets])
         :class
         [(when (= :tickets mode) (c :font-bold))

          (c [:px 1] :underline)]}
        "Билеты"]
       [:button
        {:on-click #(dispatch [::events/set-mode :events])
         :class [(when (= :events mode) (c :font-bold))
                 (c [:px 1] :underline)]}
        "Ивенты"]
       [:i.fa-solid.fa-rotate-right.rotate
        {:on-click
         #(dispatch [::events/reload-db])
         :class [(if reloading? :down :down1)
                 (c :cursor-pointer)]}]

       [:div {:class (c :flex)}
        [header mode]
        (when (= :events mode)
          [events-view/events-view])
        (when (= :tickets mode)
          [tickets/tickets-view])]]]]))

(defmethod routes/panels :home-panel [] [home-panel])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])
        all-data? @(re-frame/subscribe [::subs/initialized?])]
    (if-not
     all-data?
      [:div.another-center
       {:style
        {:text-align "center"}}
       [:center
        [:img
         {:src "loading2.gif"}]]]
      [routes/panels @active-panel])))
