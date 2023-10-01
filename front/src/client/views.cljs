(ns client.views
  (:require
   [re-frame.core :as re-frame :refer [dispatch subscribe]]
   [client.events :as events]
   [client.debounce] ; to reg event :)
   [client.routes :as routes]
   [client.myclasses :as cls]
   [client.mycomponents :as components]
   [client.subs :as subs])
  (:require-macros [stylo.core :refer [c]]))

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

(defn input-with-init-value [ticket-id prop-path label label-id descr required?
                             & [select-values]]
  (let [ticket @(subscribe [::subs/ticket-by-id ticket-id])
        first-value (get-in ticket prop-path)
        invalid-message @(subscribe [::subs/form-path-invalid-message prop-path])
        opened? @(subscribe [::subs/ticket-edit-prop prop-path])
        on-change-fn
        #(dispatch [:dispatch-debounce
                    {:delay 500
                     :event [::events/change-ticket-and-validate prop-path (.. % -target -value)]}])]
    [:div
     {:class (c :border)}
     [:span
      [:label {:for label-id} label (when required? [:span {:style {:color "#dc2626"}} "*"])]]
     [:div {:class (c :text-sm)}
      (when-not opened?
        [:span
         first-value
         [:i.fa-solid.fa-pen-to-square
          {:class (c [:px 3] :cursor-pointer)
           :on-click #(dispatch [::events/ticket-start-edit ticket-id prop-path])}]])

      (when opened?
        [:div {:class (c :flex :flex-col)}
         (if select-values
           (components/selector
            select-values
            on-change-fn
            {:default-value "="
             :cls (c :w-full [:mb 2])})

           [:input {:name label-id
                    :id label-id
                    :class (c :border [:h 10] :text-2xl)
                    :maxLength 30
                    :on-change on-change-fn}])

         (when-not select-values
           [:label {:for label-id
                    :class (c :text-lg :font-light :italic)} (when descr descr)])
         [:div
          (when invalid-message
            (:message invalid-message))]])]]))

(defn ticket-new-prop [prop-path label label-id descr required?]
  (let [current-value  @(re-frame.core/subscribe [::subs/form-prop prop-path])
        invalid-message @(re-frame.core/subscribe [::subs/form-path-invalid-message prop-path])]
    [:div {:class (c [:px 5] :flex :flex-col)}

     [:label {:for label-id} label (when required? [:span {:style {:color "#dc2626"}} "*"])]
     [:input {:name label-id
              :id label-id
              :class (c :border [:h 10] :text-2xl)
              :maxLength 30
              :on-change
              #(dispatch [:dispatch-debounce
                          {:delay 500
                           :event [::events/save-form prop-path (.. % -target -value)]}])
              :placeholder (str current-value)}]

     [:div
      [:label {:for label-id
               :class (c :text-lg :font-light :italic)} (when descr descr)]]
     [:div (when invalid-message
             (:message invalid-message))]]))

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

(defn delete-ticket-icon []
  [:span [:i.fa-solid.fa-trash
          {:class (c [:px 3])}]])

(defn edit-ticket-icon [id]
  [:span [:i.fa-solid.fa-pen-to-square
          {:class (c [:px 3])
           :on-click #(dispatch [::events/start-ticket-update id])}]])

(defn edit-ticket-view-top [id]
  [:div
   [:div
    {:class (c :grid [:grid-cols 2])}
    (input-with-init-value id [:name] "Название" "name" nil true)
    (input-with-init-value id [:coordinates :x] "Координата x" "coordinates-x" "(x > - 686)" true)
    (input-with-init-value id [:coordinates :y] "Координата y" "coordinates-y" "целое число" true)
    (input-with-init-value id [:price] "Цена" "price" "(> 0)" true)
    (input-with-init-value id [:discount] "Скидка" "discount" "(от 0 до 100)" true)
    (input-with-init-value id [:refundable] "Возвратный" "refundable" "true/false" true [{:value true :desc "Да"}
                                                                                         {:value false :desc "Нет"}])
    (input-with-init-value id [:type] "Тип" "type" "VIP, USUAL, BUDGETARY, CHEAP" false
                           [{:value "VIP" :desc "VIP"}
                            {:value "USUAL" :desc "Обычный"}
                            {:value "BUDGETARY" :desc "Бюджетный"}
                            {:value "CHEAP" :desc "Дешевый"}])
    #_(input-with-init-value id [:event] "event" "event" "todo" true)]])

(defn edit-ticket-view-bot []
  (let [form-valid? @(subscribe [::subs/form-valid?])]
    [:<>
     (when form-valid?
       [:button.submitBtn
        (cond->
         {:class (c [:w-min 100])
          :on-click #(dispatch [::events/update-ticket-from-form])}
          #_#_(not form-valid?)
            (assoc :disabled "true"))
        "Изменить"])
     [:button.cancelBtn {:class (c [:w-min 100])
                         :on-click #(dispatch [::events/ticket-toggle-change])}
      "Отменить"]]))

(defn one-ticket [{:keys [id name creation-date price discount type event] :as ticket}]
  (let [modal-delete-opened? @(subscribe [::subs/toggle-delete])
        modal-edit-opened? @(subscribe [::subs/ticket-toggle-change])]
    ^{:key id}
    [:div
     [:div {:class [(c [:bg "#FAFAFA"]
                       [:border :current]
                       :flex-row
                       :flex
                       :justify-between
                       [:p 2]
                       [:m 2]
                       [:hover :shadow-inner [:bg :gray-200]]
                       [:rounded :xl])]}
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
        {:class [cls/base-class (c :cursor-pointer)]
         :on-click #(dispatch [::events/start-ticket-update id])}
        (edit-ticket-icon id)
        "Изменить"]

       [:div
        {:class [cls/base-class (c :cursor-pointer)]
         :on-click #(dispatch [::events/toggle-delete])}
        (delete-ticket-icon)
        "Удалить"]
       (when modal-edit-opened?
         (components/modal
          "Изменение билета"
          (edit-ticket-view-top id)
          (edit-ticket-view-bot)
          :modal-medium))
       (when modal-delete-opened?
         (components/modal
          "Удаление"
          "Вы уверены в удалении?"
          [:<>
           [:button.deleteBtn
            {:on-click #(dispatch [::events/delete-ticket id])}
            "Удалить"]
           [:button.cancelBtn
            {:on-click #(dispatch [::events/toggle-delete-false])}
            "Отменить"]]))]]]))

(defn filter-view-one [prop only=? label & [selector-values]]
  (let [filter-db @(subscribe [::subs/filters prop])
        shown? (:shown filter-db)]
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
                  [:mb 2]
                  #_[:w 60])}))

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

(defn filter-view []
  [:div
   (filter-view-one :id false "id")
   (filter-view-one :name true "Имя")
   (filter-view-one :x false "Координата x")
   (filter-view-one :y false "Координата y")
   (filter-view-one :price false "Цена")
   (filter-view-one :discount false "Скидка")
   (filter-view-one :refundable true "Возвратный" [true false])
   (filter-view-one :type false "Тип"
                    ["CHEAP" "BUDGETARY" "USUAL" "VIP"])
   (filter-view-one :event true "event")])

(defn sort-view []
  [:div
   [:div {:class (c :flex)}
    (components/selector
     ["id"
      {:value "name" :desc "Имя"}
      "x"
      "y"
      {:value "refundable" :desc "Возвратный"}
      {:value "type" :desc "Тип билета"}
      "event"]
     #())

    (components/selector
     [{:value "netu" :desc "Без сортировки"}
      {:value "asc" :desc "По возрастанию"}
      {:value "desc" :desc "По убыванию"}]
     #()
     {:default-value "netu"
      :cls
      (c [:px 2] :text-center [:w 35])})]])

(defn tickets-header []
  [:div {:class (c :flex :flex-col)}
   (sort-view)
   [:hr {:class (c [:pt 2])}]
   (filter-view)])

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
          last-page @(subscribe [::subs/last-page])]
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

(defn new-ticket-top []
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
              :placeholder "-"}]]]])

(defn new-ticket-bot []
  [:div {:class (c :flex [:gap 4])}
   [:button.submitBtn {:class (c [:w-min 100] [:bg :green-500])} "Создать"]
   [:button.cancelBtn {:class (c [:w-min 100])
                       :on-click #(dispatch [::events/toggle-new])} "Отменить"]])

(defn tickets-view []
  (let [tickets-on-page @(re-frame/subscribe [::subs/tickets-on-page])
        modal-opened?
        @(subscribe [::subs/toggle-new])]
    [:div {:class (c :w-full)}
     [:div
      [:div
       {:class (c :flex :items-center :content-center :justify-center
                  [:mb 5] [:mx 10])}
       [:span
        "Размер страницы:"
        (components/selector [1 5 10 15 20 30 40 50 60]
                             #(dispatch [::events/change-page-size
                                         (.. % -target -value)])
                             {:default-value 5})]]
      [:div {:class (c :flex [:gap 4]
                       :items-center
                       :content-center
                       :justify-center)}
       (paging-view (count tickets-on-page))]]
     (when modal-opened?
       (components/modal
        "Новый билет"
        (new-ticket-top)
        (new-ticket-bot)
        :modal-medium))

     [:div {:class (c :grid [:grid-cols 2])}
      [:div {:class [cls/div-center]
             :on-click #(dispatch [::events/toggle-new])}
       "НОВЫЙ"]
      (doall (for [[_ ticket] tickets-on-page]
               (one-ticket ticket)))]]))

(defn events-page []
  [:div "Events page"])

(defn home-panel []
  (let [mode @(re-frame/subscribe [::subs/mode])]
    [:div {:class (c [:px 15] [:py 2])}
     [:h1 {:class (c :text-center)}
      "SOA Lab2 Slava+Kirill"]
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
