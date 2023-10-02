(ns client.mycomponents
  (:require
   [client.myclasses :as cls])
  (:require-macros [stylo.core :refer [c]]))

(defn selector [selector-values on-change-fn & {cls :cls
                                                default-value :default-value}]
  [:select {:class
            (if cls
              [cls/base-class cls]
              [cls/base-class (c :w-full)])
            :defaultValue (or default-value "netu")
            :on-change on-change-fn}
   (when-not default-value
     [:option {:class cls/base-class
               :value "netu"} "-"])
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


