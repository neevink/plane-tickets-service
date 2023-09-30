(ns client.views
  (:require
   [re-frame.core :as re-frame :refer [dispatch subscribe]]
   [client.events :as events]
   [client.debounce] ; to reg event :)
   [client.routes :as routes]
   [client.myclasses :as cls]
   [client.subs :as subs])
  (:require-macros [stylo.core :refer [c]]))

(def input-class
  (c [:py 1] [:leading-relaxed]
     :flex-auto
     [:w-min 0]
     [:focus :outline-none]
     [:bg :transparent]
     [:disabled :cursor-not-allowed]))

(def base-class
  (c :inline-flex
     [:w 35]
     [:px 2]
     [:py 2]
     [:border "#FAFAFA"]
     :rounded
     [:bg "#FAFAFA"]
     :transition-all [:duration 200] :ease-in-out
     [:focus-within :outline-none :shadow-none [:border "#2e3633"]]
     [:focus :outline-none :shadow-none [:border "#2e3633"]]
     [:hover [:border "#2e3633"]]))

#_(def dropdown-class
    (c :absolute
       :rounded
       :leading-relaxed
       {:box-shadow "0 3px 6px -4px rgba(0,0,0,.12), 0 6px 16px 0 rgba(0,0,0,.08), 0 9px 28px 8px rgba(0,0,0,.05)"}
       :overflow-x-hidden
       :overflow-y-auto
       [:h-max 60]
       [:py 1]
       [:z 100]
       [:mt "4px"]
       [:top "100%"]
       [:left "-1px"]
       [:right "-1px"]))

(defn modal [close-fn
             heading-label
             modal-content-html
             bottom-html
             & size]
  [:div.darkBG
   [:div.centered
    [:div {:class (if size size :modal-small)}
     [:div.modalHeader
      [:h5.heading heading-label]]
     #_[:button.closeBtn {:on-click close-fn} "x"]

     [:div {:class (if size :modalContent :modalContent-center)}
      modal-content-html]
     [:div.modalActions
      [:div.actionsContainer
       bottom-html]]]]])

(defn type-view [type]
  (cond
    (= type "VIP")
    [:i.fa-regular.fa-star]

    (= type "USUAL")
    [:i.fa-solid.fa-check]

    (= type "BUDGETARY")
    [:i.fa-solid.fa-wheelchair-move]

    (= type "CHEAP")
    [:i.fa-regular.fa-star]))

(defn ticket-prop-change [prop-path label label-id]
  (let [current-value  @(re-frame.core/subscribe [::subs/form-prop prop-path])
        invalid-message @(re-frame.core/subscribe [::subs/form-path-invalid-message prop-path])]
    [:div {:class (c [:px 5])}
     [:label {:for label-id} label]
     [:input {:name label-id
              :on-change
              #(dispatch [:dispatch-debounce
                          {:delay 500
                           :event [::events/save-form prop-path (.. % -target -value)]}])
              :placeholder current-value}]
     [:div (when invalid-message
             (:message invalid-message))]]))

(defn delete-ticket-icon [id]
  (let [modal-opened? @(subscribe [::subs/toggle-delete])]
    [:span [:i.fa-solid.fa-trash
            {:class (c [:px 3])
             :on-click #(dispatch [::events/toggle-delete])}]
     (when modal-opened?
       (modal
        #(dispatch [::events/toggle-delete])
        "Удаление"
        "Вы уверены в удалении?"
        [:<>
         [:button.deleteBtn
          {:on-click #(dispatch [::events/delete-ticket id])}

          "Удалить"]
         [:button.cancelBtn {:on-click #(dispatch [::events/toggle-delete])} "Отменить"]]))]))

(defn edit-ticket-icon [id]
  (let [modal-opened? @(subscribe [::subs/ticket-toggle-change])]
    [:span [:i.fa-solid.fa-pen-to-square
            {:class (c [:px 3])
             :on-click #(dispatch [::events/ticket-update id])}]
     (when modal-opened?
       (modal
         #(dispatch [::events/ticket-toggle-change])
         "Изменение билета"
         [:div "Изменение........." id]  #_"TODO: ticket update"
         [:<>
          [:button.deleteBtn
           {
            #_#_:on-click #(dispatch [::events/delete-ticket id])}

           "Изменить"]
          #_[:button.cancelBtn {:on-click 
                                the-fn

                                } "Отменить"]]
         :modal-medium))]))

(defn one-ticket [{:keys [id name creation-date price discount type event] :as ticket}]
  ^{:key id}
  [:div
   [:div {:class [(c [:bg "#FAFAFA"]
                     [:border :current]
                     :flex-row
                     :flex
                     :justify-between
                     [:p 2]
                     [:m 2]
                     :cursor-pointer
                     [:hover :shadow-inner [:bg :gray-200]]
                     [:rounded :xl])]
          :on-click #(dispatch [::events/ticket-update id])}
    [:div
     [:div
      [:span {:class (c :text-sm)} creation-date " " (type-view type) " " [:span type] " "]
      [:div
       [:span {:class (c :text-xl :text-bold)} [:span (:name event)]]]]
     [:span {:class (c :text-sm)} name]]
    [:div
     {:class (c :text-xl [:pt 3])}
     [:div
      "СКИДКА: " discount "%"]
     [:div "ЦЕНА: " price]]
    [:div {:class (c :flex :flex-col :justify-center [:gap 5])}
     [:div
      (edit-ticket-icon id)
      "Изменить"]
     [:div
      (delete-ticket-icon id)
      "Удалить"]]]])

(defn one-filter [{value :val dir :dir} idx]
  ^{:key idx}
  [:div
   [:i.fa-solid.fa-magnifying-glass]
   [:input
    {:name "search"
     :class (c [:m 3] [:w 70] :rounded :border)
     :on-change
     #(dispatch [:dispatch-debounce
                 {:delay 500
                  :event [::events/change-filter 
                          idx 
                          :value
                          (.. % -target -value)]}])

     :placeholder "Фильтр"}]
   (if (not= 0 idx)
     [:button {:class (c [:mr 2]
                         [:w 6]
                         :border
                         :rounded
                         [:bg "#ff3e4e"]
                         :transition-all [:duration 200] #_:ease-in-out
                         [:focus-within :outline-none :shadow-none [:border "#2e3633"]]
                         [:focus :outline-none :shadow-none [:border "#2e3633"]]
                         [:hover [:border "#2e3633"]]
                         ;; :w-full 
                         ;; :w-min-full 
                         [:h 8])
               :on-click #(dispatch [::events/remove-filter idx])

               } "x"]
     [:button {:class (c [:mr 2]
                         [:w 6])}]

     
     )
   [:select {:class [base-class (c [:px 2] :text-center [:w 35])]
             :on-change #(dispatch [::events/change-filter 
                                    idx
                                    :type
                                    (.. % -target -value)])}
    [:option {:value "name"} "Имя"]
    [:option {:value "x"} "x"]
    [:option {:value "y"} "y"]
    [:option {:value "price"} "Цена"]
    [:option {:value "discount"} "Скидка"]
    [:option {:value "refundable"} "Возвратный"]
    [:option {:value "type"} "Тип"]
    [:option {:value "event"} "Мероприятие"]
    ]
   ]
  
  )
 
(defn filter-view []
  (let [filters @(subscribe [::subs/filters])]
    [:div
     (doall (for [[idx f] (map-indexed vector filters)] 
               (one-filter f idx)))
     [:button {:class cls/plus-button
               :on-click #(dispatch [::events/new-filter])

               } "+"]
     ])
  )

(defn one-sort-view []
  [:div
   [:select  {:class [base-class]}
    [:option {:class base-class
              :value "netu"
              :selected true} "Сортировка"]
    [:option {:value "id"} "id"]
    [:option {:value "name"} "Имя"]
    [:option {:value "x"} "x"]
    [:option {:value "y"} "y"]

    [:option {:value "refundable"} "возвратный"]
    [:option {:value "type"} "Тип билета"]
    [:option {:value "event"} "event"]]
   [:select {:class [base-class (c [:px 2] :text-center [:w 35])]}
    [:option {:value "asc"} "По возрастанию"]
    [:option {:value "decs"} "По убыванию"]]])

(defn sort-view []
  
   [:div 
    (one-sort-view)
    [:button {:class cls/plus-button} "+"]
   
   
    ]
  )

(defn ticket-new-prop [prop-path label label-id descr required?]
  (let [current-value  @(re-frame.core/subscribe [::subs/form-prop prop-path])
        invalid-message @(re-frame.core/subscribe [::subs/form-path-invalid-message prop-path])]
    [:div {:class (c [:px 5] :flex :flex-col)}
     [:label {:for label-id} label (when required? [:span {:style {:color "#dc2626"}} "*"])]
     [:input {:name label-id
              :class (c :border [:h 10] :text-2xl
                        #_[:bg "#AFAFAF"])
              :maxlength 30
              :on-change
              #(dispatch [:dispatch-debounce
                          {:delay 500
                           :event [::events/save-form prop-path (.. % -target -value)]}])
              :placeholder current-value}]
     [:div
      #_{:class (c [:mt -6])}
      [:label {:for label-id
               :class (c :text-lg :font-light :italic)} (when descr descr)]]
     [:div (when invalid-message
             (:message invalid-message))]]))

(defn tickets-header []

  [:div {:class (c :flex :flex-col)}
   (filter-view)
   [:hr {:class (c [:pt 2])}]
   (sort-view)
   ]
  )

(defn page-circle [value & selected]
  [:div {:class (c
                 [:w 10]
                 :cursor-pointer
                 [:pt 2]
                 [:rounded :xl]
                 [:hover :shadow-inner [:bg :gray-200]]
                 :text-center
                 [:h 10])
         :style (when selected {:background-color "#4281f5"
                                :color "white"})
         :on-click #(dispatch [::events/change-page value])}
   value])

(defn paging-view [tickets]
  (when (> tickets 0)
    (let [current-page @(subscribe [::subs/current-page])
          last-page @(subscribe [::subs/last-page]) ]
      [:<>
       (when (> (dec current-page) 1) ; first page
         (page-circle 1))
       (when (< 2 current-page) ; ... 
         "...")
       (when (>= (dec current-page) 1) ; prev page
         (page-circle (dec current-page)))

       (page-circle current-page true) ; current page

       (when (>= last-page (inc current-page))
         (page-circle (inc current-page))) ; next page

       (when (> (- last-page current-page) 1) ; ... if page is not last or prev-last
         "...")

       (when (> (- last-page current-page) 1)
         (page-circle last-page))])))

(defn tickets-view []
  (let [current @(re-frame/subscribe [::subs/current-ticket])
        all-tickets @(re-frame/subscribe [::subs/tickets])
        page-size @(subscribe [::subs/page-size])
        modal-opened?
        @(subscribe [::subs/toggle-new])]
    [:div
     [:div
      [:div 
       {:class (c :flex :items-center :content-center :justify-center
                  [:mb 5] [:mx 10])}
       [:span 
        "Размер страницы:"
        [:input {:class (c [:w 5])
                 :on-change 
                 #(dispatch [:dispatch-debounce
                             {:delay 500
                              :event [::events/change-page-size (.. % -target -value)]}])
                 :placeholder "fixme"}]]]
      [:div {:class (c :flex [:gap 4]
                       :items-center
                       :content-center
                       :justify-center)}
       (paging-view (count all-tickets))]]
     (when modal-opened?
       (modal #(dispatch [::events/toggle-new])
              "Новый билет"
              [:div
               [:div
                {:class (c :grid [:grid-cols 2])}
                (ticket-new-prop [:name]           "Название" "name" nil true)
                (ticket-new-prop [:coordinates :x] "Координата x" "coordinates-x" "(x > - 686)" true)
                (ticket-new-prop [:coordinates :y] "Координата y" "coordinates-y" "(целое число)" true)
                (ticket-new-prop [:price]          "Цена" "price" "(> 0)" true)
                (ticket-new-prop [:discount]       "Скидка" "discount" "(от 0 до 100)" true)
                (ticket-new-prop [:refundable]     "Возвратный" "refundable" "true/false" true)
                (ticket-new-prop [:type]           "Тип" "type" "(VIP, USUAL, BUDGETARY, CHEAP)" false)
                [:div {:class (c [:p 5])}
                 [:label {:for "event"} "Мероприятие"]
                 [:input {:name "event"
                          #_#_:on-change
                            #(dispatch [:dispatch-debounce
                                        {:delay 500
                                         :event [::events/save-form prop-path (.. % -target -value)]}])
                          :placeholder "-"}]]]]

              [:<>
               [:button.cancelBtn "Создать"]
               [:button.cancelBtn {:on-click #(dispatch [::events/toggle-new])} "Отменить"]]

              :modal-medium))

     [:div {:class (c :grid [:grid-cols 2])}
      [:div {:class [(c #_[:bg "#FAFAFA"]
                      [:border 2]
                        [:bg "#fafafa"]
                        [:pt 8]
                        :text-center
                        [:rounded :xl]
                        :text-xl
                        [:m 2]
                        :cursor-pointer
                        [:hover :shadow-inner [:bg :gray-200]])]
             :on-click #(dispatch [::events/toggle-new])}
       "НОВЫЙ"]
      (doall (for [[_ ticket] all-tickets]
               (one-ticket ticket)))]
     ]))

(defn events-page []
  [:div "Events page"])

(defn home-panel []
  (let [mode @(re-frame/subscribe [::subs/mode])]
    [:div {:class (c [:px 15] [:py 2])}
     [:h1 {:class (c :text-center)}
      "SOA Lab2 Slava+Kirill"]
     [:h1 "TODO: pagesize, sort"]
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

       (when (= :events mode)
         (events-page))

       (when (= :tickets mode)
         [:div {:class (c :flex)}
          (tickets-header)
          (tickets-view)])]]]))

(defmethod routes/panels :home-panel [] [home-panel])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    (routes/panels @active-panel)))
