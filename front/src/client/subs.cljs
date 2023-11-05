(ns client.subs
  (:require
   [re-frame.core :as re-frame :refer [reg-sub]]))

(reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))

(reg-sub
 ::tickets
 (fn [db _]
   (get-in db [:tickets])))

(reg-sub
 ::tickets-1
 (fn [db _]
   (vals (get-in db [:tickets]))))

(reg-sub
 ::events
 (fn [db _]
   (get-in db [:events])))

(reg-sub
 ::events-1
 (fn [db _]
   (vals (get-in db [:events]))))

(reg-sub
 ::ticket-by-id
 (fn [db [_ id]]
   (get-in db [:tickets id])))

(reg-sub
 ::event-by-id
 (fn [db [_ id]]
   (get-in db [:events id])))

(reg-sub
 ::mode
 (fn [db [_]]
   (get db :mode)))

(reg-sub
 ::form-prop
 (fn [db [_ prop-path]]
   (get-in db (into [:form] prop-path))))

(reg-sub
 ::event-form-prop
 (fn [db [_ prop-path]]
   (get-in db (into [:event-form] prop-path))))

(reg-sub
 ::form-valid?
 (fn [db [_]]
   (let [form (get db :form-valid)]
     (or (nil? form)
         (= :ok form)
         (and (or (vector? form) (seq? form)) (empty? form))))))

(reg-sub
 ::events-form-valid?
 (fn [db [_]]
   (let [form (get db :event-form-valid)]
     (or (nil? form)
         (= :ok form)
         (and (or (vector? form) (seq? form)) (empty? form))))))

(reg-sub
 ::form-path-invalid-message
 (fn [db [_ prop-path]]
   (when (not= :ok  (get db :form-valid))
     (first (vec (mapv :message (filter
                                 (fn [error-mp]
                                   (= prop-path (into [] (:path error-mp))))
                                 (get db :form-valid))))))))

(reg-sub
 ::event-form-path-invalid-message
 (fn [db [_ prop-path]]
   (when (not= :ok (get db :event-form-valid))
    (first (vec (mapv :message
                      (filter
                       (fn [error-mp]
                        (= prop-path (into [] (:path error-mp))))
                       (get db :event-form-valid))))))))

(reg-sub
 ::toggle-new
 (fn [db [_]]
   (get db :toggle-new)))

(reg-sub
 ::toggle-delete
 (fn [db [_]]
   (get db :toggle-delete)))

(reg-sub
 ::current-page
 (fn [db [_]]
   (get-in db [:paging :current-page])))

(reg-sub
 ::last-page
 (fn [db [_]]
   (let [mode (:mode db)
         entity  (if (= mode :tickets) :count-tickets :count-events)]
     (js/Math.ceil
      (double
       (/ (get-in db [entity])
          (get-in db [:paging :page-size])))))))

(reg-sub
 ::toggle-change
 (fn [db [_]]
   (get-in db [:toggle-change])))

(reg-sub
 ::ticket-update-id
 (fn [db [_]]
   (get-in db [:ticket :update-id])))

(reg-sub
 ::page-size
 (fn [db [_]]
   (get-in db [:paging :page-size])))

(reg-sub
 ::filters
 (fn [db [_ prop]]
   (get-in db [:filters (:mode db) prop])))

(reg-sub
 ::ticket-to-delete-id
 (fn [db [_]]
   (get-in db [:ticket :to-delete])))

(reg-sub
 ::event-to-delete-id
 (fn [db [_]]
   (get-in db [:ticket :to-delete])))

(reg-sub
 ::event-to-delete-id
 (fn [db [_]]
   (get-in db [:event :to-delete])))

(reg-sub
 ::event-update-id
 (fn [db [_]]
   (get-in db [:event :update-id])))

(reg-sub
 ::initialized?
 (fn [db _]
   (and (find db :events)
        (find db :tickets))))

(reg-sub
 ::count-events
 (fn [db _]
   (get db :count-events)))

(reg-sub
 ::count-tickets
 (fn [db _]
   (get db :count-tickets)))

(reg-sub
 ::reloading
 (fn [db _]
   (get db :reloading)))

(reg-sub
 ::ticket-types
 (fn [db _]
   (get db :ticket-types)))

(reg-sub
 ::event-types
 (fn [db _]
   (get db :event-types)))

(reg-sub
 ::sortings
 (fn [db [_ mode]]
   (get-in db [(if (= mode :tickets) :ticket :event) :sorting])))

(reg-sub
 ::tickets-discount-sum
 (fn [db [_]]
   (get db :tickets-discount-sum)))

(reg-sub
 ::tickets-discount-count
 (fn [db [_]]
   (get db :tickets-discount-count)))

(reg-sub
 ::tickets-types-count
 (fn [db [_]]
   (get db :tickets-types-count)))

(reg-sub
 ::ticket-discount-count-opened
 (fn [db [_]]
  (get db :ticket-discount-count-opened)))

(reg-sub
 ::ticket-copy
 (fn [db [_]]
  (get db :toggle-copy)))

(reg-sub
 ::ticket-copy-id
 (fn [db [_]]
  (get-in db [:ticket :copy-id])))

(reg-sub
 ::ticket-copy-mode
 (fn [db [_]]
  (get-in db [:ticket-copy-mode])))

