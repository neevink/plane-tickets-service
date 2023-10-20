(ns client.events
  (:require
   [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx]]
   [client.validation :as validation]
   [client.http :as http]
   [re-frame-cljs-http.http-fx]
   [client.db :as db]))

(def back-url "http://localhost:8080")

(defn full-url [endpoint]
  (str back-url endpoint))

(defn call-http [db url method on-success on-failure & params]
  {:db (assoc db :loading? true)
   :http-cljs (merge {:method method
                      :params {}
                      :with-credentials? false
                      :on-success on-success
                      :on-failure on-failure
                      :url url}
                     params)})

(defn http-get [db url on-success on-failure]
  (call-http db url :get on-success on-failure))

(defn http-delete [db url on-success on-failure]
  (call-http db url :delete on-success on-failure))

(defn http-post [db url body on-success on-failure]
  (call-http db url :delete on-success on-failure {:body body}))

(re-frame/reg-event-db
 ::tickets-downloaded
 (fn [db [_ tickets]]
   (let [tickets (:body tickets)
         tickets (mapv (fn [ticket]
                         (if-not (:eventId ticket)
                           (assoc ticket :eventId (get-in ticket [:event :id]))
                           ticket))
                       tickets)
         tickets-id-map (update-vals (group-by :id tickets) first)]
     (-> db
         (assoc :tickets tickets-id-map)))))

(re-frame/reg-event-db
 ::tickets-not-downloaded
 (fn [db [_ result]]
   (assoc db :http-result result :errors? true)))

(reg-event-fx
 ::download-tickets
 (fn [{:keys [db]} _]
   (http-get db (full-url "/tickets")
             [::tickets-downloaded]
             [::tickets-not-downloaded])))

(re-frame/reg-event-db
 ::events-downloaded
 (fn [db [_ events]]
   (let [events (:body events)
         events-id-map (update-vals (group-by :id events) first)]
     (-> db
         (assoc :events events-id-map)))))

(re-frame/reg-event-db
 ::events-not-downloaded
 (fn [db [_ result]]
   (assoc db :http-result result :errors? true)))

(reg-event-fx
 ::download-events
 (fn [{:keys [db]} _]
   (http-get db  (full-url "/events")
             [::events-downloaded]
             [::events-not-downloaded])))

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
   :discount parse-double})

(reg-event-fx
 ::save-form
 (fn [{:keys [db]} [_ path value]]
   {:db (assoc-in db (into [:form] path)
                  (cond-> value
                    (get hack path)
                    (try
                      ((get hack path) value)
                      (catch js/Error _e
                        value))))}))

(reg-event-fx
 ::save-form-event
 (fn [{:keys [db]} [_ path value]]
   {:db (assoc-in db (into [:event-form] path)
                  (cond->
                   value
                    (get hack path)
                    (try
                      ((get hack path) value)
                      (catch js/Error _e
                        value))))}))

(reg-event-fx
 ::validate-form
 (fn [{:keys [db]} [_ _]]
   (let [validate-res (validation/validate-ticket (get db :form))]
     {:db (assoc db :form-valid validate-res)})))

(reg-event-fx
 ::validate-event-form
 (fn [{:keys [db]} [_ _]]
   (let [validate-res (validation/validate-event (get db :form-event))]
     {:db (assoc db :event-form-valid validate-res)})))

(re-frame/reg-event-fx
 ::ticket-deleted
 (fn [{:keys [db]} [_ ticket-id]]
   {:db (update db :tickets dissoc ticket-id)
    :dispatch [::toggle-delete-false]}))

(re-frame/reg-event-db
 ::ticket-not-deleted
 (fn [db [_ ticket-id]]
   (update db :errors conj ["not deleted" ticket-id])))

(reg-event-fx
 ::delete-ticket-http
 (fn [{:keys [db]} [_ ticket-id]]
   (http-delete db (full-url (str "/tickets/" ticket-id))
                [::ticket-deleted ticket-id]
                [::ticket-not-deleted ticket-id])))

(reg-event-fx
 ::delete-ticket
 (fn [{:keys [db]} [_ ticket-id]]
   (js/console.log "ticket id " ticket-id)
   {:db (-> (update-in db [:tickets] dissoc ticket-id))
    :dispatch [::delete-ticket-http ticket-id]}))


(re-frame/reg-event-fx
 ::event-deleted
 (fn [{:keys [db]} [_ ticket-id]]
   {:db (update db :events dissoc ticket-id)
    :dispatch [::toggle-delete-false]}))

(re-frame/reg-event-db
 ::event-not-deleted
 (fn [db [_ event-id]]
   (update db :errors conj ["not deleted" event-id])))

(reg-event-fx
 ::delete-event-http
 (fn [{:keys [db]} [_ event-id]]
   (http-delete db (full-url (str "/events/" event-id))
                [::event-deleted event-id]
                [::event-not-deleted event-id])))

(reg-event-fx
 ::delete-event
 (fn [{:keys [db]} [_ event-id]]
   {:db (-> (update-in db [:events] dissoc event-id))
    :dispatch [::delete-event-http event-id]}))

(reg-event-fx
 ::set-mode
 (fn [{:keys [db]} [_ mode]]
   {:db (assoc db :mode mode)}))

(reg-event-fx
 ::toggle-new
 (fn [{:keys [db]} [_]]
   {:db (update db :toggle-new not)}))

(reg-event-fx
 ::toggle-delete ;; ticket
 (fn [{:keys [db]} [_ id]]
   {:db (assoc-in
         (update db :toggle-delete not)
         [:ticket :to-delete] id)}))

(reg-event-fx
 ::toggle-delete-event
 (fn [{:keys [db]} [_ id]]
   {:db (assoc-in
         (update db :toggle-delete not)
         [:event :to-delete] id)}))

(reg-event-fx
 ::toggle-delete-false
 (fn [{:keys [db]} [_]]
   {:db (assoc db :toggle-delete false)}))

(reg-event-fx
 ::change-page
 (fn [{:keys [db]} [_ value]]
   {:db (assoc-in db [:paging :current-page] value)}))

(reg-event-fx
 ::toggle-change
 (fn [{:keys [db]} [_]]
   (.log js/console (range 200))
   {:db (-> db
            (update-in [:toggle-change] not))}))

(reg-event-fx
 ::start-ticket-update
 (fn [{:keys [db]} [_ ticket-id]]
   (let [modal-opened? (get-in db [:toggle-change])]
     (if modal-opened?
       {:db
        (-> db
            (update-in [:toggle-change] not)
            (assoc :form nil)
            (update-in [:ticket] dissoc :update-id))}
       {:db
        (-> db
            (update-in [:toggle-change] not)
            (assoc :form (get-in db [:tickets ticket-id]))
            (assoc-in [:ticket :update-id] ticket-id))}))))

(reg-event-fx
 ::start-event-update
 (fn [{:keys [db]} [_ event-id]]
   (let [modal-opened? (get-in db [:toggle-change])]
     (if modal-opened?
       {:db
        (-> db
            (update-in [:toggle-change] not)
            (assoc :event-form nil)
            (update-in [:event] dissoc :update-id))}
       {:db
        (-> db
            (update-in [:toggle-change] not)
            (assoc :event-form (get-in db [:events event-id]))
            (assoc-in [:event :update-id] event-id))}))))

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


(re-frame/reg-event-fx
 ::ticket-added
 (fn [{:keys [db]} [_ ticket-resp]]
   (let [ticket (:body ticket-resp)]
     {:db (assoc-in db [:tickets (:id ticket)] ticket)
      :dispatch [::toggle-new]})))

(re-frame/reg-event-db
 ::ticket-not-added
 (fn [db [_ result]]
   (assoc db :http-result result :errors? true)))

(reg-event-fx
 ::save-ticket-http
 (fn [{:keys [db]} [_ ticket]]
   {:db (http-post db (full-url "/tickets")
             [::ticket-added]
             [::ticket-not-added]
             ticket)}))

(reg-event-fx
 ::save-ticket-from-form
 (fn [{:keys [db]} [_]]

   #_"TODO: back"
   {:db (update db :tickets assoc 1000 (get db :form))
    :dispatch [::save-ticket-http]
    ;; #_:fx [#_#_[:dispatch [::save-form prop-path value]]
    ;;        [:dispatch [::validate-form]]]
    }))

(reg-event-fx
 ::save-event-from-form
 (fn [{:keys [db]} [_]]
   #_"TODO: back"
   {:db (update db :events assoc 1000 (get db :event-form))
    :fx [[:dispatch [::toggle-new]]
         #_#_[:dispatch [::save-form prop-path value]]
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







