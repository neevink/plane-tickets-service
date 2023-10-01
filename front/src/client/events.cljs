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

(def hack
 {:id parse-long
  :coordinates {:x parse-long
                :y parse-long}
  :price parse-double
  :discount parse-double })

(reg-event-fx
 ::save-form
 (fn [{:keys [db]} [_ path value]]
   {:db (assoc-in db (into [:form] path) 
                  (cond-> value
                   (get hack path)
                   (try 
                    ((get hack path) value)
                    (catch js/Error _e
                     value
                     )
                    )
                   
                   ))}))

(reg-event-fx
 ::validate-form
 (fn [{:keys [db]} [_ _]]
   (let [validate-res (validation/validate (get db :form))]
     (println "VALIDATION " validate-res)
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
   {:db (-> db
            (update-in [:ticket :toggle-change] not))}))

(reg-event-fx
 ::start-ticket-update
 (fn [{:keys [db]} [_ ticket-id]]
   (let [modal-opened? (get-in db [:ticket :toggle-change])]
     (if modal-opened?
       {:db
        (-> db
            (update-in [:ticket :toggle-change] not)
            (assoc :form nil)
            (update-in [:ticket] dissoc :update-id))}
       {:db
        (-> db
            (update-in [:ticket :toggle-change] not)
            (assoc :form (get-in db [:tickets ticket-id]))
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

(reg-event-fx
 ::ticket-start-edit
 (fn [{:keys [db]} [_ ticket-id prop]]
   (let
    [val-path  (into [:ticket :edit :path] prop)
     old-value (get-in db val-path)]
     {:db
      (if old-value
        (-> db
            (assoc-in [:ticket :edit :ticket-id] ticket-id)
            (update-in val-path not))
        (-> (assoc-in db [:ticket :edit :ticket-id] ticket-id)
            (assoc-in val-path true)))})))

(reg-event-fx
 ::change-ticket-and-validate
 (fn [{:keys [db]} [_ prop-path value]]
   {:db db
    :fx [[:dispatch [::save-form prop-path value]]
         [:dispatch [::validate-form]]]}))

(reg-event-fx
 ::save-ticket-from-form
 (fn [{:keys [db]} [_]]
   {:db (conj db :tickets (get db :form))
    #_#_:fx [[:dispatch [::save-form prop-path value]]
             [:dispatch [::validate-form]]]}))

(reg-event-fx
 ::update-ticket-from-form
 (fn [{:keys [db]} [_]]
   (let [id (get-in db [:ticket :update-id])]
     {:db (assoc-in db [:tickets id]
                    (merge
                     (get-in db [:tickets id])
                     #_{:id (get-in db [:ticket :update-id])}
                     (get-in db [:form])))
      #_#_:fx [[:dispatch [::save-form prop-path value]]
               [:dispatch [::validate-form]]]})))
