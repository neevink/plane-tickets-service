(ns client.views.tickets
  (:require
   [re-frame.core :as re-frame :refer [dispatch subscribe]]
   [client.events :as events]
   [client.debounce] ; to reg event :)
   [client.myclasses :as cls]
   [goog.string :as gstring]
   [goog.string.format]
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
                    {:delay 300
                     :event [::events/change-ticket-and-validate prop-path (.. % -target -value)]}])]
    [:div
     {:class (c :border)}
     [:span
      [:label {:for label-id} label
       (when required? [:span {:style {:color "#dc2626"}} "*"])]]
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
                    :class (c :text-lg :font-light :italic)} (when descr descr)])])
      [:div {:class (c :text-sm)}
       (when invalid-message
         (:message invalid-message))]]]))

(defn ticket-new-prop [prop-path label label-id descr required?
                       & [select-values default-value]]
  (let [current-value  @(re-frame.core/subscribe [::subs/form-prop prop-path])
        invalid-message @(re-frame.core/subscribe [::subs/form-path-invalid-message prop-path])
        on-change-fn
        #(dispatch [:dispatch-debounce
                    {:delay 300
                     :event [::events/change-ticket-and-validate prop-path (.. % -target -value)]}])]
    [:div {:class (c [:px 5] :flex :flex-col)}

     [:label {:for label-id} label (when required? [:span {:style {:color "#dc2626"}} "*"])]
     (if select-values
       (components/selector
        select-values
        on-change-fn
        {:cls (c :w-full [:mb 2])
         :default-value (or nil default-value)})

       [:input {:name label-id
                :id label-id
                :class (c :border [:h 10] :text-2xl)
                :maxLength 30
                :on-change
                on-change-fn
                :placeholder (str current-value)}])

     [:div
      [:label {:for label-id
               :class (c :text-lg :font-light :italic)} (when descr descr)]]
     [:div {:class (c :text-sm)}
      (when invalid-message
             (:message invalid-message))]]))

(defn edit-ticket-icon []
  [:span [:i.fa-solid.fa-pen-to-square
          {:class (c [:px 3])}]])

(defn edit-ticket-view-top [id]
  [:div
   [:div
    {:class (c :grid [:grid-cols 2])}
    (input-with-init-value id [:name] "Название" "name" nil true)
    (input-with-init-value id [:coordinates :x] "Координата x" "coordinates-x" "(x > - 686)" true)
    (input-with-init-value id [:coordinates :y] "Координата y" "coordinates-y" "целое число" true)
    (input-with-init-value id [:creationDate] "Дата создания" "creation-date" "YYYY-MM-DD" true)
    (input-with-init-value id [:price] "Цена" "price" "(> 0)" true)
    (input-with-init-value id [:discount] "Скидка" "discount" "(от 0 до 100)" true)
    (input-with-init-value id [:refundable] "Возвратный" "refundable" "true/false" true [{:value true :desc "Да"}
                                                                                         {:value false :desc "Нет"}])
    (input-with-init-value id [:type] "Тип" "type" "VIP, USUAL, BUDGETARY, CHEAP" false
                           [{:value "VIP" :desc "VIP"}
                            {:value "USUAL" :desc "Обычный"}
                            {:value "BUDGETARY" :desc "Бюджетный"}
                            {:value "CHEAP" :desc "Дешевый"}])
    ;; todo
    #_(input-with-init-value id [:event] "event" "event" "todo" true)]])

(defn edit-ticket-view-bot []
  (let [form-valid? @(subscribe [::subs/form-valid?])]
    [:<>
     (when form-valid?
       [:button.submitBtn
        {:class (c [:w-min 100])
         :on-click #(dispatch [::events/update-ticket-from-form])}
        "Изменить"])
     [:button.cancelBtn {:class (c [:w-min 100])
                         :on-click #(dispatch [::events/toggle-change])}
      "Отменить"]]))

(defn one-ticket [id]
  (let [ticket @(subscribe [::subs/ticket-by-id id])
        event  @(subscribe [::subs/event-by-id (:eventId ticket)])]
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
        [:div "ID: " id]
        [:span {:class (c :text-sm)}
         (.toLocaleString (js/Date. (:creationDate ticket))) " " (type-view (:type ticket)) " " [:span (:type ticket)] " "]
        [:div
         [:span {:class (c :text-xl :text-bold)}
          [:span (if (:name event)
                   (str "Мероприятие: " (:name event))
                   "Неизвестное мероприятие")]]]]
       [:span {:class (c :text-sm)} (str "Название билета: " (:name ticket))]
       [:div {:class (c :text-sm)} (if (:refundable ticket) "Возвратный" "Невозвратный")]
       ]
      [:div
       {:class (c :text-xl [:pt 3])}
       [:div
        "СКИДКА: " (:discount ticket) "%"]
       [:div "ЦЕНА: " (:price ticket)]
       [:div (str "(" (:x (:coordinates ticket)) ","  (:y (:coordinates ticket)) ")")]]
      [:div {:class (c :flex :flex-col :justify-center [:gap 5])}
       [:div
        {:class [cls/base-class (c :cursor-pointer)]
         :on-click #(dispatch [::events/start-ticket-update id])}
        (edit-ticket-icon)
        "Изменить"]

       [:div
        {:class [cls/base-class (c :cursor-pointer)]
         :on-click #(dispatch [::events/toggle-delete id])}
        [components/delete-icon]
        "Удалить"]]]]))

(defn new-ticket-top []
  (let
   [events @(subscribe [::subs/events])
    nice-events (mapv (fn [[event-id event]]
                        {:value (long event-id)
                         :desc
                         (str
                          (:name event) " "
                          (.toLocaleString (js/Date. (:date event))))})
                      events)]
    [:div
     {:class (c :grid [:grid-cols 2])}
     [ticket-new-prop [:name]           "Название" "name" nil true]
     [ticket-new-prop [:coordinates :x] "Координата x" "coordinates-x" "(x > - 686)" true]
     [ticket-new-prop [:coordinates :y] "Координата y" "coordinates-y" "(целое число)" true]
     [ticket-new-prop [:price]          "Цена" "price" "(> 0)" true]
     [ticket-new-prop [:discount]       "Скидка" "discount" "(от 0 до 100)" true]
     [ticket-new-prop [:refundable]     "Возвратный" "refundable" nil true
      [{:value true :desc "Да"}
       {:value false :desc "Нет"}] true]
     [ticket-new-prop [:type] "Тип" "type" "" false
      [{:value "VIP" :desc "VIP"}
       {:value "USUAL" :desc "Обычный"}
       {:value "BUDGETARY" :desc "Бюджетный"}
       {:value "CHEAP" :desc "Дешевый"}]]
     [ticket-new-prop [:eventId] "Мероприятие" "eventId" "" false
      nice-events]]))

(defn new-ticket-bot []
  (let [form-valid? @(subscribe [::subs/form-valid?])]
    [:div {:class (c :flex [:gap 4])}
     (when form-valid?
       [:button.submitBtn {:class (c [:w-min 100] [:bg :green-500])
                           :on-click #(dispatch [::events/save-ticket-from-form])} "Создать"])
     [:button.cancelBtn {:class (c [:w-min 100])
                         :on-click #(dispatch [::events/toggle-new])} "Отменить"]]))

(defn tickets-view []
  (let [tickets-on-page @(re-frame/subscribe [::subs/tickets-on-page])
        modal-opened? @(subscribe [::subs/toggle-new])
        modal-delete-opened? @(subscribe [::subs/toggle-delete])
        to-delete-id @(subscribe [::subs/ticket-to-delete-id])
        modal-edit-opened?   @(subscribe [::subs/toggle-change])
        to-edit-id @(subscribe [::subs/ticket-update-id])
        count-tickets @(subscribe [::subs/count-tickets])
        page-number @(subscribe [::subs/current-page])
        page-size @(subscribe [::subs/page-size])]
    [:div {:class (c :w-full)}
     [:div
      {:class (c :flex :content-between :justify-between
                 [:mb 5] [:mx 10])}
      [:span
       (components/paging-label
         (inc (* (dec page-number) page-size))
         (+ (* (dec page-number) page-size)
            (count tickets-on-page))
         count-tickets)

       [components/selector [1 5 10 15 20 30 40 50 60]
        #(dispatch [::events/change-page-size
                    (.. % -target -value)])
        {:default-value 5
         :cls (c [:w 15] [:ml 3])}]]
      [:div {:class (c :flex [:gap 4]
                       :items-center
                       :content-center
                       :justify-center)}
       [components/paging-view (count tickets-on-page)]]]


     (when modal-opened?
       (components/modal
        "Новый билет"
        [new-ticket-top]
        [new-ticket-bot]
        :modal-medium))

     [:div {:class (c :grid [:grid-cols 2])}
      [:div {:class [cls/div-center]
             :on-click #(dispatch [::events/toggle-new])}
       "НОВЫЙ"]
      (doall
       (for [[ticket-id _ticket] tickets-on-page]
         ^{:key ticket-id}
         [one-ticket ticket-id
          (fn [] (dispatch [::events/delete-ticket ticket-id]))]))
      (when modal-edit-opened?
        [components/modal
         (str "Изменение билета " to-edit-id)
         [edit-ticket-view-top to-edit-id]
         [edit-ticket-view-bot]
         :modal-medium])
      (when modal-delete-opened?
        [components/modal
         "Удаление"
         (gstring/format "Вы уверены в удалении билета %s?" to-delete-id)
         [:<>
          [:button.deleteBtn
           {:on-click #(dispatch [::events/delete-ticket to-delete-id])}
           "Удалить"]
          [:button.cancelBtn
           {:on-click #(dispatch [::events/toggle-delete-false])}
           "Отменить"]]])]]))
