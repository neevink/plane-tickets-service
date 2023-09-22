(ns client.views
  (:require
   [re-frame.core :as re-frame :refer [dispatch subscribe]]
   ;; [client.styles :as styles]
   [client.events :as events]
   [client.debounce] ; to reg event :)
   [client.routes :as routes]
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
             & small?]
  [:div.darkBG
   [:div.centered
    [:div {:class (if small? :modal-small :modal)}
     [:div.modalHeader
      [:h5.heading heading-label]]
     [:button.closeBtn {:on-click close-fn} "x"]

     [:div.modalContent modal-content-html]
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
         [:button.deleteBtn "Удалить"]
         [:button.cancelBtn {:on-click #(dispatch [::events/toggle-delete])} "Отменить"]]
        true))]))

(defn ticket-selected [{:keys [id name coordinates creation-date price discount
                               refundable type event] :as ticket}]
  [:div
   {:class [(c [:mt -6]
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
               [:hover :shadow-inner [:bg :gray-200]])]}

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

(defn filter-view []
  [:div
   [:i.fa-solid.fa-magnifying-glass]
   [:input
    {:name "search"
     :class (c [:m 3] [:w 70] :rounded)
     #_#_:on-change
       #(dispatch [:dispatch-debounce
                   {:delay 500
                    :event [::events/save-form prop-path (.. % -target -value)]}])
     :placeholder "Фильтр"}]])

(defn input
  [text & [required?]]
  [:label.form-label text (when required? [:span {:style {:color "#dc2626"}} " *"])])

(defn sort-view []
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
    [:option {:value "event"} "event"]
    "hui"]
   [:select {:class [base-class (c [:px 2] :text-center [:w 35])]}
    [:option {:value "asc"} "По возрастанию"]
    [:option {:value "decs"} "По убыванию"]
    "hui2"]])

(defn add-button []
  (let [modal-opened? @(subscribe [::subs/toggle-new])]
    [:div [:button
           {:class (c
                    [:w 35]
                    [:h 9]
                    ;; [:mx 5]
                    [:mt 2]
                    [:py 2]
                    [:border :black]
                    :rounded
                    [:bg "#FAFAFA"]
                    :transition-all
                    [:duration 200]
                    [:hover :shadow-inner [:bg :gray-200]]
                    :ease-in-out
                    [:focus-within :outline-none :shadow-none [:border "#2e3633"]]
                    [:focus :outline-none :shadow-none [:border "#2e3633"]]
                    #_[:hover [:border "#2e3633"]])
            :on-click #(dispatch [::events/toggle-new])}
           "Новый"]
     (when modal-opened?
       (modal #(dispatch [::events/toggle-new])
              "Новый билет"
              [:div
               [:label "Имя"]
               [:input]

               [:label "Тип"]
               [:input]

               [:label "Цена"]
               [:input]]
              [:<>
               [:button.cancelBtn "Создать"]
               [:button.cancelBtn {:on-click #(dispatch [::events/toggle-new])} "Отменить"]]))]))

(defn tickets-header []
  [:div
   {:class (c [:px 4] :flex
              #_[:h 10]
              #_[:width "50%"]
              :justify-between)}
   (add-button)
   (filter-view)
   (sort-view)])

(defn page-circle [value]
  [:div {:class (c
                 ;; [:bg :red-500]
                 [:w 10]
                 [:bg "#FAFAFA"]
                 [:border :black]
                 :cursor-pointer
                 ;; :align-middle
                 [:pt 2]
                 [:rounded :full]
                 [:hover :shadow-inner [:bg :gray-200]]
                 :text-center
                 [:h 10])} value])

(defn tickets-view []
  (let [current @(re-frame/subscribe [::subs/current-ticket])
        all-tickets @(re-frame/subscribe [::subs/tickets])]
    [:div (doall (for [[_ ticket] all-tickets]
                   (one-ticket ticket (= (:id ticket) (:id current)))))
     [:div {:class (c :flex [:gap 4]
                      :items-center
                      :content-center
                      :justify-center)}

      (page-circle 1)
      (page-circle 2)
      "..."
      (page-circle 50)]]))

(defn events-page []
  [:div "Events page"])

(defn home-panel []
  (let [mode @(re-frame/subscribe [::subs/mode])]
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
         (events-page))

       (when (= :tickets mode)
         [:<>
          (tickets-header)
          (tickets-view)])]]]))

(defmethod routes/panels :home-panel [] [home-panel])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    (routes/panels @active-panel)))
