(ns client.views
  (:require
   [re-frame.core :as re-frame :refer [dispatch]]
   ;; [client.styles :as styles]
   [client.events :as events]
   [client.debounce] ; to reg event :)
   [client.routes :as routes]
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

(defn delete-ticket-icon [id]
  [:i.fa-solid.fa-trash {:on-click #(dispatch [::events/delete-ticket id])}])

(defn ticket-prop-change [prop-path label label-id]
  (let [current-value  @(re-frame.core/subscribe [::subs/form-prop prop-path])
        invalid-message @(re-frame.core/subscribe [::subs/form-path-invalid-message prop-path])]
    [:div {:class (c [:p 5])}
     [:label {:for label-id} label]
     [:input {:name label-id
              :on-change
              #(dispatch [:dispatch-debounce
                          {:delay 500
                           :event [::events/save-form prop-path (.. % -target -value)]}])
              :placeholder current-value}]
     [:div (when invalid-message
            (:message invalid-message))]]))

(defn ticket-selected [{:keys [id name coordinates creation-date price discount
                               refundable type event] :as ticket}]
  [:div
   {:class (c [:mt -6]
              [:bg :gray-200]
              :border
              [:rounded :xl]
              :flex-row
              :flex
              :justify-between
              [:p 2]
              [:pt 4]
              [:m 2]
              :cursor-pointer
              [:hover :shadow-inner [:bg :gray-200]])}

   [:div
    [:div "Билет" (delete-ticket-icon id)]

    (ticket-prop-change [:name]           "Название:     " "name")
    (ticket-prop-change [:coordinates :x] "Координата x: " "coordinates-x")
    (ticket-prop-change [:coordinates :y] "Координата y: " "coordinates-y")
    (ticket-prop-change [:creation-date]  "Дата покупки: " "creation-date")
    (ticket-prop-change [:price]          "Цена:         " "price")
    (ticket-prop-change [:discount]       "Скидка:       " "discount")
    (ticket-prop-change [:refundable]     "Возвратный:   " "refundable")
    (ticket-prop-change [:type]           "Тип:          " "type")

    [:div
     [:label {:for "event"} "Event: "]
     [:input {:name "event"}]]
    [:button.btn {:class (c [:bg :blue-200] [:rounded :xl] [:w 50] [:h 7])
                  :on-click #(dispatch [::events/change-ticket-all id])}
     "Изменить"]]])

(defn one-ticket [{:keys [id name creation-date price discount type event] :as ticket} show?]
  ^{:key id}
  [:div
   [:div {:class (c [:bg :gray-200]
                    :flex-row
                    :flex
                    :justify-between
                    [:p 2]
                    [:m 2]
                    :cursor-pointer
                    [:hover :shadow-inner [:bg :gray-200]]
                    [:rounded :xl])
          :on-click #(dispatch [::events/select-ticket ticket])}
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
     [:div "ЦЕНА: " price]]]
   (when show?
     (ticket-selected ticket))])

(defn home-panel []
  (let [current @(re-frame/subscribe [::subs/current-ticket])
        all-tickets @(re-frame/subscribe [::subs/tickets])
        mode @(re-frame/subscribe [::subs/mode])]
    [:header {:class (c [:px 15] [:py 2])}
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
         [:div "Events page"])

       (when (= :tickets mode)
        (doall (for [[_ ticket] all-tickets]
                (one-ticket ticket (= (:id ticket) (:id current))))))]]]))

(defmethod routes/panels :home-panel [] [home-panel])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    (routes/panels @active-panel)))
