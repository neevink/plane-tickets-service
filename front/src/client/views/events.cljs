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
        {:default-value "="
         :cls (c :w-full [:mb 2])})
       [:input {:name label-id
                :id label-id
                :class (c :border [:h 10] :text-2xl)
                :maxLength 30
                :on-change on-change-fn
                :placeholder (str current-value)}])

     [:div
      [:label {:for label-id
               :class (c :text-lg :font-light :italic)} (when descr descr)]]
     [:div (when invalid-message
             (:message invalid-message))]]))

(defn new-event-top []
  [:div
   (event-new-prop [:name] "Название" "name" nil true)
   (event-new-prop [:date] "Дата мероприятия" "date" nil false)
   (event-new-prop [:minAge] "Минимальный возраст" "minAge" nil true)
   (event-new-prop [:eventType] "Тип мероприятия" "type" "(CONCERT, BASEBALL, BASKETBALL, THEATRE_PERFORMANCE)" false
                   [{:value "CONCERT" :desc "Концерт"}
                    {:value "BASEBALL" :desc "Бейсбол"}
                    {:value "BASKETBALL" :desc "Баскетбол"}
                    {:value "THEATRE_PERFORMANCE" :desc "Театр"}])])

(defn new-event-bot []
  (let [form-valid? @(subscribe [::subs/events-form-valid?])]
    [:div {:class (c :flex [:gap 4])}
     (when form-valid?
       [:button.submitBtn {:class (c [:w-min 100] [:bg :green-500])
                           :on-click #(dispatch [::events/save-event-from-form])} "Создать"])
     [:button.cancelBtn {:class (c [:w-min 100])
                         :on-click #(dispatch [::events/toggle-new])} "Отменить"]]))

(defn input-with-init-value [event-id prop-path label label-id descr required?
                             & [select-values]]
  (let [event @(subscribe [::subs/event-by-id event-id])
        first-value (get-in event prop-path)
        invalid-message @(subscribe [::subs/event-form-path-invalid-message prop-path])
        opened? @(subscribe [::subs/event-edit-prop prop-path])
        on-change-fn
        #(dispatch [:dispatch-debounce
                    {:delay 500
                     :event [::events/change-event-and-validate prop-path (.. % -target -value)]}])]
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
           :on-click #(dispatch [::events/event-start-edit event-id prop-path])}]])

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
      [:div
       (when invalid-message
         (:message invalid-message))]]]))

(defn edit-event-view-top [id]
  [:div
   [:div
    {:class (c :grid [:grid-cols 2])}
    (input-with-init-value id [:name] "Название мероприятия" "name" nil true)
    (input-with-init-value id [:date] "Дата мероприятия" "date" "YYYY-MM-DD" false)
    (input-with-init-value id [:minAge] "Минимальный возраст" "min-age" "Целое число больше 0" true)
    (input-with-init-value id [:eventType] "Тип мероприятия" "event-type"
                           "CONCERT, BASEBALL, BASKETBALL, THEATRE_PERFORMANCE" false
                           [{:value "CONCERT" :desc "Концерт"}
                            {:value "BASEBALL" :desc "Бейсбол"}
                            {:value "BASKETBALL" :desc "Баскетбол"}
                            {:value "THEATRE_PERFORMANCE" :desc "Театр"}])]])

(defn edit-event-view-bot []
  (let [form-valid? @(subscribe [::subs/events-form-valid?])]
    [:<>
     (when form-valid?
       [:button.submitBtn
        {:class (c [:w-min 100])
         :on-click #(dispatch [::events/update-event-from-form])}
        "Изменить"])
     [:button.cancelBtn {:class (c [:w-min 100])
                         :on-click #(dispatch [::events/toggle-change])}
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
       [:div
        [:span {:class (c :text-xl :text-bold)} [:span name]]]]
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

(defn paging-view [events-number]

  (when (> events-number 0)
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

(defn events-view []
  (let [events-on-page @(re-frame/subscribe [::subs/events-on-page])
        modal-edit-opened? @(subscribe [::subs/toggle-change])
        modal-delete-opened? @(subscribe [::subs/toggle-delete])
        modal-opened? @(subscribe [::subs/toggle-new])
        event-to-delete-id @(subscribe [::subs/event-to-delete-id])
        event-to-edit-id @(subscribe [::subs/event-to-edit-id])]

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
       (paging-view (count events-on-page))]]
     (when modal-opened?
       (components/modal
        "Новое мероприятие"
        [new-event-top]
        [new-event-bot]
        :modal-medium))
     (when modal-edit-opened?
       (components/modal
        (str "Изменение мероприятия " event-to-edit-id)
        (edit-event-view-top event-to-edit-id)
        (edit-event-view-bot)
        :modal-medium))
     (when modal-delete-opened?
       (components/modal
        "Удаление"
        (gstring/format "Вы уверены в удалении мероприятия %s?" event-to-delete-id)
        [:<>
         [:button.deleteBtn
          {:on-click #(dispatch [::events/delete-event event-to-delete-id])}
          "Удалить"]
         [:button.cancelBtn
          {:on-click #(dispatch [::events/toggle-delete-false])}
          "Отменить"]]))

     [:div {:class (c :grid [:grid-cols 3])}
      [:div {:class [cls/div-center]
             :on-click #(dispatch [::events/toggle-new])}
       "НОВЫЙ"]
      (doall
       (for [[_ event] events-on-page]
         (one-event event)))]]))


