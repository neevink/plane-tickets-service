(ns client.views.events
  (:require
   [re-frame.core :as re-frame :refer [dispatch subscribe]]
   [client.events :as events]
   [client.debounce] ; to reg event :)
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

(defn new-event-top [])

(defn new-event-bot [])

(defn edit-event-icon [id]
  [:span [:i.fa-solid.fa-pen-to-square
          {:class (c [:px 3])
           #_#_:on-click #(dispatch [::events/start-ticket-update id])}]])

(defn one-event [{:keys [id name date min-age event-type] :as event}]
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
       [:span {:class (c :text-sm)} date  " " (event-type-icon event-type) " " [:span event-type] " "]
       [:div
        [:span {:class (c :text-xl :text-bold)} [:span name]]]]
      [:span {:class (c :text-sm)} "Минимальный возраст: " min-age]]
     [:div
      {:class (c :text-xl [:pt 3])}]
     [:div {:class (c :flex :flex-col :justify-center [:gap 5])}
      [:div
       {:class [cls/base-class (c :cursor-pointer)]
        :on-click #(dispatch [::events/start-ticket-update id])}
       (edit-event-icon id)
       "Изменить"]

      [:div
       {:class [cls/base-class (c :cursor-pointer)]
        :on-click #(dispatch [::events/toggle-delete])}
       (components/delete-icon)
       "Удалить"]
      #_(when modal-edit-opened?
          (components/modal
           "Изменение билета"
           (edit-ticket-view-top id)
           (edit-ticket-view-bot)
           :modal-medium))
      #_(when modal-delete-opened?
          (components/modal
           "Удаление"
           "Вы уверены в удалении?"
           [:<>
            [:button.deleteBtn
             #_{:on-click #(dispatch [::events/delete-ticket id])}
             "Удалить"]
            [:button.cancelBtn
             #_{:on-click #(dispatch [::events/toggle-delete-false])}
             "Отменить"]]))]]]])

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
       (paging-view (count events-on-page))]]
     (when modal-opened?
       (components/modal
        "Новое мероприятие"
        (new-event-top)
        (new-event-bot)
        :modal-medium))

     [:div {:class (c :grid [:grid-cols 3])}
      [:div {:class [cls/div-center]
             :on-click #(dispatch [::events/toggle-new])}
       "НОВЫЙ"]
      (doall (for [[_ event] events-on-page]
               (one-event event)))]]))

(defn events-page []
  (events-view))

