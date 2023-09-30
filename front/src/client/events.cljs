(ns client.events
  (:require
   [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx]]
   [client.validation :as validation]
   [client.db :as db]))

(reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(reg-event-fx
 ::navigate
 (fn [_ [_ handler]]
   {:navigate handler}))

(reg-event-fx
 ::set-active-panel
 (fn [{:keys [db]} [_ active-panel]]
   {:db (assoc db :active-panel active-panel)}))

(reg-event-fx
 ::select-ticket
 (fn [{:keys [db]} [_ ticket-id]]
   (let [current (:current-ticket db)]
     {:db (assoc db :current-ticket (if (= ticket-id current) nil ticket-id))})))

(reg-event-fx
 ::download-tickets
 (fn [{:keys [db]} [_ tickets]]
   (let [tickets-id-map (update-vals (group-by :id tickets) first)]
     {:db
      (-> db
          (assoc :tickets tickets-id-map))})))

(defn move-ticket [db prev-id new-id]
  (-> (assoc-in db
                [:tickets new-id]
                (get-in db [:tickets prev-id]))
      (update-in [:tickets] dissoc prev-id)))

(reg-event-fx
 ::change-ticket
 (fn [{:keys [db]} [_ ticket-id prop-path new-value]]
   (if (= :id (first prop-path))
     {:db (move-ticket db ticket-id new-value)}
     {:db (assoc-in db (into [:tickets ticket-id] prop-path) new-value)})))

(reg-event-fx
 ::save-form
 (fn [{:keys [db]} [_ path value]]
   {:db (assoc-in db (into [:form] path) value)}))

(reg-event-fx
 ::validate-form
 (fn [{:keys [db]} [_ _]]
   (let [validate-res (validation/validate (get db :form))]
     {:db (assoc db :form-valid validate-res)})))

(reg-event-fx
 ::change-ticket-all
 (fn [{:keys [db]} [_ ticket-id]]
   {:db db
    :dispatch [::validate-form ticket-id]}))

(reg-event-fx
 ::delete-ticket
 (fn [{:keys [db]} [_ ticket-id]]
   {:db (-> (update-in db [:tickets] dissoc ticket-id))
    :dispatch [::toggle-delete]}))

(reg-event-fx
 ::set-mode
 (fn [{:keys [db]} [_ mode]]
   {:db (assoc db :mode mode)}))

(reg-event-fx
 ::toggle-new
 (fn [{:keys [db]} [_]]
   {:db (update db :toggle-new not)}))

(reg-event-fx
 ::toggle-delete
 (fn [{:keys [db]} [_]]
   {:db (update db :toggle-delete not)}))

(reg-event-fx
 ::toggle-delete-false
 (fn [{:keys [db]} [_]]
   {:db (assoc db :toggle-delete false)}))

(reg-event-fx
 ::change-page
 (fn [{:keys [db]} [_ value]]
   {:db (assoc-in db [:paging :current-page] value)}))

(reg-event-fx
 ::ticket-toggle-change
 (fn [{:keys [db]} [_]]
   {:db (update-in db [:ticket :toggle-change] not)}))

(reg-event-fx
 ::ticket-update
 (fn [{:keys [db]} [_ ticket-id]]
   (let [modal-opened? (get-in db [:ticket :toggle-change])]
     (if modal-opened?
       {:db
        (-> db
            (update-in [:ticket :toggle-change] not)
            (update-in [:ticket] dissoc :update-id))}
       {:db
        (-> db
            (update-in [:ticket :toggle-change] not)
            (assoc-in [:ticket :update-id] ticket-id))}))))

(reg-event-fx
 ::change-filter
 (fn [{:keys [db]} [_ idx value]]
   {:db
    (assoc-in db [:filters idx :value] value)}))

(reg-event-fx
 ::hide-filter
 (fn [{:keys [db]} [_ idx]]
   {:db
    (update-in db [:filters idx :shown] not)}))

(reg-event-fx
 ::change-page-size
 (fn [{:keys [db]} [_ size]]
   (let [parsed (parse-long size)]
     {:db
      (if (and parsed (number? parsed) (<= 1 parsed 100))
        (assoc-in db [:paging :page-size] parsed)
        db)})))

(into [:ticket :edit] [:coo :x])

(reg-event-fx
 ::ticket-start-edit
 (fn [{:keys [db]} [_ ticket-id prop]]
   {:db 
    (-> (assoc-in db [:ticket :edit :ticket-id] ticket-id)
        (assoc-in (into [:ticket :edit] prop) true)
        )
    
    }
   ))
