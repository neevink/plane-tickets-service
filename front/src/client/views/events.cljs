(ns client.views.events
  (:require
   [re-frame.core :as re-frame :refer [dispatch subscribe]]
   [client.events :as events]
   [client.debounce] ; to reg event :)
   [goog.string :as gstring]
   [goog.string.format]
   [client.myclasses :as cls]
   [client.mycomponents :as components]
   [client.subs :as subs])
  (:require-macros [stylo.core :refer [c]]))

(defn event-type-icon [event-type]
  (cond
    (= event-type "BASKETBALL")
    [:i.fa-solid.fa-basketball]
    (= event-type "CONCERT")
    [:i.fa-solid.fa-microphone]

    (= event-type "THEATRE_PERFORMANCE")
    [:i.fa-solid.fa-masks-theater]

    (= event-type "BASEBALL")
    [:i.fa-solid.fa-baseball]))

(defn event-new-prop [prop-path label label-id descr required?
                      & [select-values]]
  (let [current-value  @(re-frame.core/subscribe [::subs/event-form-prop prop-path])
        invalid-message @(re-frame.core/subscribe [::subs/event-form-path-invalid-message prop-path])
        on-change-fn
        #(dispatch [:dispatch-debounce
                    {:delay 500
                     :event [::events/change-event-and-validate prop-path (.. % -target -value)]}])]
    [:div {:class (c [:px 5] :flex :flex-col)}

     [:label {:for label-id} label (when required? [:span {:style {:color "#dc2626"}} "*"])]

     (if select-values
       (components/selector
        select-values
        on-change-fn
        {:cls (c :w-full [:mb 2])})
       [:input {:name label-id
                :id label-id
                :class (c :border [:h 10] :text-2xl)
                :maxLength 30
                :on-change on-change-fn
                :placeholder (str current-value)}])

     [:div
      [:label {:for label-id
               :class (c :text-lg :font-light :italic)} (when descr descr)]]
     [:div
      ;; (prn invalid-message)
      (when invalid-message
             invalid-message)]]))

(defn new-event-top []
  (let [event-types @(subscribe [::subs/event-types])]
    [:div
     [event-new-prop [:name] "Название" "name" nil true]
     [event-new-prop [:date] "Дата мероприятия" "date" nil false]
     [event-new-prop [:minAge] "Минимальный возраст" "minAge" nil true]
     [event-new-prop [:eventType] "Тип мероприятия" "type" nil false
                     event-types]]))

(defn new-event-bot []
  (let [form-valid? @(subscribe [::subs/events-form-valid?])]
    [:div {:class (c :flex [:gap 4])}
     (when form-valid?
       [:button.submitBtn {:class (c [:w-min 100] [:bg :green-500])
                           :on-click #(dispatch [::events/save-event-from-form])} "Создать"])
     [:button.cancelBtn {:class (c [:w-min 100])
                         :on-click #(dispatch [::events/toggle-new])} "Отменить"]]))

(defn edit-event-view-top [id]
  (let [name-sub @(subscribe [::subs/event-form-path-invalid-message [:name]])
        date-sub @(subscribe [::subs/event-form-path-invalid-message [:date]])
        min-age-sub @(subscribe [::subs/event-form-path-invalid-message [:minAge]])
        type-sub @(subscribe [::subs/event-form-path-invalid-message [:eventType]])
        event-types @(subscribe [::subs/event-types])]
    [:div
     [:div
      {:class (c :grid [:grid-cols 2])}
      [components/input-with-init-value id [:name] "Название мероприятия" "name" nil true]
      [:div {:class (c :text-l)} (str name-sub)]

      [components/input-with-init-value id [:date] "Дата мероприятия" "date" "YYYY-MM-DD" false]
      [:div {:class (c :text-l)} (str date-sub)]
      [components/input-with-init-value id [:minAge] "Минимальный возраст" "minAge" "Целое число больше 0" true]
      [:div {:class (c :text-l)} (str min-age-sub)]

      [components/input-with-init-value id [:eventType] "Тип мероприятия" "event-type"
       "CONCERT, BASEBALL, BASKETBALL, THEATRE_PERFORMANCE" false
       event-types]
      [:div {:class (c :text-l)} (str type-sub)]]]))

(defn edit-event-view-bot []
  (let [form-valid? @(subscribe [::subs/events-form-valid?])]
    [:<>
     (when form-valid?
       [:button.submitBtn
        {:class (c [:w-min 100])
         :on-click #(dispatch [::events/update-event-from-form])}
        "Изменить"])
     [:button.cancelBtn {:class (c [:w-min 100])
                         :on-click #(dispatch [::events/end-event-update])}
      "Отменить"]]))

(defn edit-event-icon [id]
  [:span [:i.fa-solid.fa-pen-to-square
          {:class (c [:px 3])
           :on-click #(dispatch [::events/start-event-update id])}]])

(defn one-event [{:keys [id name date minAge eventType]}]
  ^{:key id}
  [:div
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
       [:span {:class (c :text-sm)} (take 10 date)  " " (event-type-icon eventType) " " [:span eventType] " "]
       [:div.truncate {:class (c [:w-max 90] [:h-max 9])}
        [:span.truncate
         {:class (c :text-xl :text-bold [:h 9] [:h-max 9])}

         [:span.truncate {:class (c [:h 9] [:h-max 9])}
          [:div.tooltip {:title name}
           (if (> (count name) 15) (str (apply str (take 15 name)) "...") name)
           #_[:div.tooltiptext name]] ] ]]
       ]
      [:span {:class (c :text-sm)} "Минимальный возраст: " minAge]]
     [:div
      {:class (c :text-xl [:pt 3])}]
     [:div {:class (c :flex :flex-col :justify-center [:gap 5])}
      [:div
       {:class [cls/base-class (c :cursor-pointer)]
        :on-click #(dispatch [::events/start-event-update id])}
       (edit-event-icon id)
       "Изменить"]

      [:div
       {:class [cls/base-class (c :cursor-pointer)]
        :on-click #(dispatch [::events/toggle-delete-event id])}
       (components/delete-icon)
       "Удалить"]]]]])

(defn events-view []
  (let [events @(re-frame/subscribe [::subs/events-1])
        modal-opened? @(subscribe [::subs/toggle-new])
        modal-delete-opened? @(subscribe [::subs/toggle-delete])
        event-to-delete-id @(subscribe [::subs/event-to-delete-id])
        modal-edit-opened? @(subscribe [::subs/toggle-change])
        event-to-edit-id @(subscribe [::subs/event-update-id])]
    [:div {:class (c :w-full)}
     [components/paging]
     (when modal-opened?
       [components/modal
        "Новое мероприятие"
        [new-event-top]
        [new-event-bot]
        :modal-medium])
     (when modal-edit-opened?
       [components/modal
        (str "Изменение мероприятия " event-to-edit-id)
        [edit-event-view-top event-to-edit-id]
        [edit-event-view-bot]
        :modal-medium])
     (when modal-delete-opened?
       [components/modal
        "Удаление"
        (gstring/format "Вы уверены в удалении мероприятия %s?" event-to-delete-id)
        [:<>
         [:button.deleteBtn
          {:on-click #(dispatch [::events/delete-event event-to-delete-id])}
          "Удалить"]
         [:button.cancelBtn
          {:on-click #(dispatch [::events/toggle-delete-false])}
          "Отменить"]]])

     [:div {:class (c :grid [:grid-cols 3])}
      [:div {:class [cls/div-center]
             :on-click #(dispatch [::events/toggle-new])}
       "НОВЫЙ"]
      (doall
       (for [event events]
         (one-event event)))]]))


