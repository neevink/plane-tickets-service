(ns client.views.events
  (:require
   [re-frame.core :as re-frame :refer [dispatch subscribe]]
   [client.events :as events]
   [client.debounce] ; to reg event :)
   [client.myclasses :as cls]
   [client.mycomponents :as components]
   [client.subs :as subs])
  (:require-macros [stylo.core :refer [c]]))

(defn new-event-top [])
(defn new-event-bot [])
(defn one-event [event])

(defn paging-view [events-number])

(defn events-view []
  (let [events-on-page @(re-frame/subscribe [::subs/tickets-on-page])
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

     [:div {:class (c :grid [:grid-cols 2])}
      [:div {:class [cls/div-center]
             :on-click #(dispatch [::events/toggle-new])}
       "НОВЫЙ"]
      (doall (for [[_ event] events-on-page]
               (one-event event)))]]))

(defn events-page []
  [:div "Events page"

   (events-view)])

