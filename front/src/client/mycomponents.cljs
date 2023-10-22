(ns client.mycomponents
  (:require
   [re-frame.core :as re-frame :refer [dispatch subscribe]]
   [client.events :as events]
   [client.subs :as subs]
   [goog.string :as gstring]
   [goog.string.format]
   [client.myclasses :as cls])
  (:require-macros [stylo.core :refer [c]]))

(defn paging-label [first-index last-index max-resources]
  [:span {:class (c :font-light) :style {:color "gray"}}
   (gstring/format "%s-%s of %s" (str first-index)
                   (str last-index) (str max-resources))])

(defn selector [selector-values on-change-fn & {cls :cls
                                                default-value :default-value}]
  [:select {:class
            (if cls
              [cls/base-class cls]
              [cls/base-class (c :w-full)])
            :defaultValue (or default-value nil)
            :on-change on-change-fn}
   (when-not default-value
     [:option {:class cls/base-class
               :value (or default-value nil)} "-"])
   (doall (for [value selector-values]
            ^{:key value}
            [:option {:class cls/base-class
                      :value (if
                              (map? value)
                               (:value value)
                               value)}
             (str (if (map? value)
                    (:desc value)
                    value))]))])

(defn modal [heading-label
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

(defn delete-icon []
  [:span [:i.fa-solid.fa-trash
          {:class (c [:px 3])}]])

(defn page-circle [value & selected]
  [:div {:class (c [:w 10]
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


