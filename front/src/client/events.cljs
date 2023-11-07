(ns client.events
  (:require
   [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx]]
   [client.debounce]
   [client.validation :as validation]
   [re-frame-cljs-http.http-fx]
   [clojure.string :as str]
   [client.db :as db]))

(def back-url "https://localhost:8080")

(def back-2-url "https://localhost:9090")

(defn full-url [endpoint]
  (str back-url endpoint))

(defn full-url-2 [endpoint]
  (str back-2-url endpoint))

(defn call-http
  ([db url method on-success on-failure]
   {:db (assoc db :loading? true)
    :http-cljs {:method method
                :params {}
                :with-credentials? false
                :on-success on-success
                :on-failure on-failure
                :url url}})
  ([db url method on-success on-failure params]
   {:db (assoc db :loading? true)
    :http-cljs (merge {:method method
                       :params {}
                       :format :json
                       :with-credentials? false
                       :on-success on-success
                       :on-failure on-failure
                       :url url}
                      params)}))

(defn http-get [db url on-success on-failure]
  (call-http db url :get on-success on-failure))

(defn http-delete [db url on-success on-failure]
  (call-http db url :delete on-success on-failure))

(defn http-post [db url body on-success on-failure]
  (call-http db url :post on-success on-failure {:params body}))

(defn http-put [db url body on-success on-failure]
  {:db (assoc db :loading? true)
   :http-cljs {:method :put
               :params body
               :format :json
               :with-credentials? false
               :on-success on-success
               :on-failure on-failure
               ;; :json-params body
               :url url}})

(defn hack-creation-date [ticket]
  (update ticket :creationDate #(->> % (take 10) (apply str))))

(re-frame/reg-event-db
 ::tickets-downloaded
 (fn [db [_ tickets]]
   (let [tickets (:body tickets)
         tickets (mapv (fn [ticket]
                         (-> (if (:event ticket)
                               (assoc (dissoc ticket :event)
                                      :eventId (-> ticket :event :id))
                               ticket)
                             (hack-creation-date)))
                       tickets)
         tickets-id-map (update-vals (group-by :id tickets) first)]
     (-> db
         (assoc :tickets tickets-id-map)))))

(re-frame/reg-event-db
 ::tickets-not-downloaded
 (fn [db [_ result]]
   (assoc db :http-result result :errors? true)))

(defn filter-added-filters [db]
  (let [mode (get db :mode)]
    (when mode
      (into {}
            (filter
             (fn [[_field {:keys [value]}]]
               (not= value ""))
             (mode (:filters db)))))))

(defn operator-str->backend-operator [operator]
  (case operator
    ">"  "gt"
    "<"  "lt"
    "="  "eq"
    "!="  "ne"
    nil))

(defn filters-map->str [filters-map]
  (when filters-map
    (apply
     str
     (drop 1
           (reduce
            (fn [acc [field {:keys [value operator]}]]
              (str acc "&filter=" (name field)
                   "%5B"
                   (or (operator-str->backend-operator operator) "eq")
                   "%5D%3D" value))
            ""
            filters-map)))))

(defn page-url [db]
  (let [current-page (get-in db [:paging :current-page])
        page-size (get-in db [:paging :page-size])]
    (str "offset=" (* (dec current-page) page-size) "&limit=" page-size)))

(defn sortings->str [db part]
  (let [sorting-map
        (get-in db [part :sorting])]
    (str "&sort="
         (str/join ","
                   (mapv (fn [[_id {field :field sort-order :sort-order}]]
                           (str (when (= "desc" sort-order) "-")
                                (name field))) sorting-map)))))

(defn make-url-with-sort-and-filter [db base]
  (let [mode (:mode db)
        part (if (= mode :events) :event :ticket)]
    (cond->
     (str (full-url base) "?" (page-url db))

      (filters-map->str (filter-added-filters db))
      (str "&" (filters-map->str (filter-added-filters db)))

      (get-in db [part :sorting])

      (str (sortings->str db part)))))

(reg-event-fx
 ::download-tickets
 (fn [{:keys [db]} [_]]
   (prn "url to download tickets"  (make-url-with-sort-and-filter db "/tickets"))
   (http-get db (make-url-with-sort-and-filter db "/tickets")
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
   (prn "url to download events"  (make-url-with-sort-and-filter db "/events"))
   (http-get db (make-url-with-sort-and-filter db "/events")
             [::events-downloaded]
             [::events-not-downloaded])))

(re-frame/reg-event-db
 ::events-counted
 (fn [db [_ events]]
   (-> db
       (assoc :count-events (:body events)))))

(re-frame/reg-event-db
 ::events-not-counted
 (fn [db [_ result]]
   (assoc db :http-result result :errors? true)))

(reg-event-fx
 ::count-events
 (fn [{:keys [db]} _]
   (prn "count events")
   (http-get db  (full-url "/events/count")
             [::events-counted]
             [::events-not-counted])))

(re-frame/reg-event-db
 ::tickets-counted
 (fn [db [_ tickets]]
   (prn "tickets counted")
   (-> db
       (assoc :count-tickets (:body tickets)))))

(re-frame/reg-event-db
 ::tickets-not-counted
 (fn [db [_ result]]
   (prn "tcikets not counted")
   (assoc db :http-result result :errors? true)))

(reg-event-fx
 ::count-tickets
 (fn [{:keys [db]} _]
   (prn "count tickets")
   (http-get db  (full-url "/tickets/count")
             [::tickets-counted]
             [::tickets-not-counted])))

(reg-event-fx
 ::reloading
 (fn [{:keys [db]} _]
   {:db (assoc db :reloading true)}))

(reg-event-fx
 ::reloading-done
 (fn [{:keys [db]} _]
   {:db (assoc db :reloading false)}))

(reg-event-fx
 ::reload-db
 (fn [_ _]
   {:fx [[:dispatch  [::reloading]]
         [:dispatch  [::download-event-types]]
         [:dispatch [::download-ticket-types]]
         [:dispatch  [::count-tickets]]
         [:dispatch  [::count-events]]
         [:dispatch  [::download-events]]
         [:dispatch  [::download-tickets]]
         [:dispatch-debounce
          {:delay 500
           :event [::reloading-done]}]]}))

(reg-event-fx
 ::initialize-db
 (fn [_ _]
   {:db db/default-db
    :fx [[:dispatch  [::download-event-types]]
         [:dispatch  [::download-ticket-types]]
         [:dispatch  [::count-tickets]]
         [:dispatch  [::count-events]]
         [:dispatch  [::download-events]]
         [:dispatch  [::download-tickets]]]}))

(reg-event-fx
 ::set-active-panel
 (fn [{:keys [db]} [_ active-panel]]
   {:db (assoc db :active-panel active-panel)}))

(def hack-ticket
  {:id parse-long
   :coordinates {:x parse-long
                 :y parse-long}
   :type #(if (= "" %) nil %)
   :eventId parse-long
   :refundable #(if (boolean? %) % (parse-boolean %))
   :price parse-double
   :discount parse-double})

(def hack-event
  {:id parse-long
   :eventType #(if (= "" %) nil %)
   :date #(if (= "" %) nil %)
   :minAge parse-long})

(reg-event-fx
 ::save-form
 (fn [{:keys [db]} [_ path value]]
   (prn "save form " path)
   {:db (assoc-in db (into [:form] path)
                  (cond-> value
                    (get-in hack-ticket path)
                    (try
                      ((get-in hack-ticket path) value)
                      (catch js/Error _e
                        value))))}))

(reg-event-fx
 ::save-form-event
 (fn [{:keys [db]} [_ path value]]
   (prn "save form event " path)
   {:db (assoc-in db (into [:event-form] path)
                  (cond->
                   value
                    (get-in hack-event path)
                    (try

                      (prn "helllo " ((get-in hack-event path) value))
                      ((get-in hack-event path) value)
                      (catch js/Error _e
                        value))))}))

(reg-event-fx
 ::validate-form
 (fn [{:keys [db]} [_ _]]
   (let [validate-res (validation/validate-ticket (get db :form))]
     (prn "validate form res is " validate-res)
     {:db (assoc db :form-valid validate-res)})))

(reg-event-fx
 ::validate-event-form
 (fn [{:keys [db]} [_ _]]
   (let [validate-res (validation/validate-event (get db :event-form))]
     {:db (assoc db :event-form-valid validate-res)})))

(re-frame/reg-event-fx
 ::ticket-deleted
 (fn [{:keys [db]} [_ ticket-id]]
   {:db (update db :tickets dissoc ticket-id)
    :dispatch [::toggle-delete-false]}))

(re-frame/reg-event-db
 ::ticket-not-deleted
 (fn [db [_ ticket-id]]
   (assoc (update db :errors conj ["not deleted" ticket-id])
          :toggle-delete false)))

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
   (assoc (update db :errors conj ["not deleted" event-id])
          :toggle-delete false)))

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
   {:db (-> db
            (assoc :mode mode)
            (assoc :filters db/default-filters))}))

(reg-event-fx
 ::toggle-new
 (fn [{:keys [db]} [_]]
   {:db (cond-> (update db :toggle-new not)
          (not (:toggle-new db))
          (dissoc :form :event-form))}))

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
   {:db (assoc-in db [:paging :current-page] value)
    :dispatch (if (= :tickets (:mode db)) [::download-tickets]  [::download-events])}))

(reg-event-fx
 ::start-ticket-update
 (fn [{:keys [db]} [_ ticket-id]]
   {:db
    (-> db
        (assoc :toggle-change true)
        (assoc :form (get-in db [:tickets ticket-id]))
        (assoc-in [:ticket :update-id] ticket-id))}))

(reg-event-fx
 ::end-ticket-update
 (fn [{:keys [db]} [_]]
   (prn "end-ticket-update")
   {:db
    (-> db
        (assoc :toggle-change false)
        (assoc :form nil)
        (update-in [:ticket] dissoc :update-id))}))

(reg-event-fx
 ::start-event-update
 (fn [{:keys [db]} [_ event-id]]
   {:db
    (-> db
        (assoc :toggle-change true)
        (assoc :event-form (get-in db [:events event-id]))
        (assoc-in [:event :update-id] event-id))}))

(reg-event-fx
 ::end-event-update
 (fn [{:keys [db]} [_]]
   {:db
    (-> db
        (assoc :toggle-change false)
        (assoc :event-form nil)
        (update-in [:event] dissoc :update-id))}))

(reg-event-fx
 ::change-filter-1
 (fn [{:keys [db]} [_ mode idx1 idx2 value]]
   (prn "change-filter 1 " mode idx1 idx2 value)
   {:db (assoc-in db [:filters mode idx1 idx2] value)}))

(reg-event-fx
 ::hide-filter
 (fn [{:keys [db]} [_ prop]]
   {:db
    (update-in db [:filters (:mode db) prop :shown] not)}))

(reg-event-fx
 ::change-page-size
 (fn [{:keys [db]} [_ size]]
   (let [parsed (parse-long size)]
     {:db
      (if (and parsed (number? parsed) (<= 1 parsed 100))
        (assoc-in db [:paging :page-size] parsed)
        db)
      :dispatch (if (= :tickets (:mode db)) [::download-tickets]  [::download-events])})))

(reg-event-fx
 ::change-ticket-and-validate
 (fn [{:keys [db]} [_ prop-path value]]
   (prn "calling change-ticket-and-validate")
   {:db db
    :fx [[:dispatch [::save-form prop-path value]]
         [:dispatch [::validate-form]]]}))

(reg-event-fx
 ::change-event-and-validate
 (fn [{:keys [db]} [_ prop-path value]]
   (prn "calling change-event-and-validate with prop path" prop-path "and value" value)
   {:db db
    :fx [[:dispatch [::save-form-event prop-path value]]
         [:dispatch [::validate-event-form]]]}))

(re-frame/reg-event-fx
 ::ticket-added
 (fn [{:keys [db]} [_ ticket-resp]]
   (let [ticket (assoc (dissoc (:body ticket-resp) :event)
                       :eventId (-> ticket-resp :body :event :id))]
     {:db (assoc-in db [:tickets (:id ticket)] ticket)
      :fx [[:dispatch [::count-tickets]]
           [:dispatch [::toggle-new]]]})))

(re-frame/reg-event-db
 ::ticket-not-added
 (fn [db [_ result]]
   (assoc db :http-result result :errors? true)))

(reg-event-fx
 ::save-ticket-http
 (fn [{:keys [db]} [_ ticket]]
   (http-post db (full-url "/tickets")
              (dissoc (assoc ticket :event {:id (:eventId ticket)} ) :eventId)
              [::ticket-added]
              [::ticket-not-added])))

(reg-event-fx
 ::save-ticket-from-form
 (fn [{:keys [db]} [_]]
   {:dispatch [::save-ticket-http (:form db)]}))

(re-frame/reg-event-fx
 ::event-added
 (fn [{:keys [db]} [_ event-resp]]
   (let [event (:body event-resp)]
     {:db (assoc-in db [:events (:id event)] event)
      :fx [[:dispatch [::count-events]]
           [:dispatch [::toggle-new]]]})))

(re-frame/reg-event-db
 ::event-not-added
 (fn [db [_ result]]
   (cond-> (update (assoc db :http-result result :errors? true) :toggle-new not)
     (not (:toggle-new db))
     (dissoc :form :event-form))))

(reg-event-fx
 ::save-event-http
 (fn [{:keys [db]} [_ event]]
   (http-post db (full-url "/events")
              (update event :date (fn [date-str] (when date-str (str date-str ":00.000Z"))))
              [::event-added]
              [::event-not-added])))

(reg-event-fx
 ::save-event-from-form
 (fn [{:keys [db]} [_]]
   {:dispatch [::save-event-http (:event-form db)]}))

(re-frame/reg-event-db
 ::ticket-updated
 (fn [db [_ result]]
   (let [ticket (assoc (dissoc (:body result) :event)
                       :eventId (-> result :body :event :id))
         id (:id ticket)]
     (-> db
         (assoc-in [:tickets id] ticket)
         (assoc :toggle-change false)
         (assoc :form nil)
         (update-in [:ticket] dissoc :update-id)))))

(re-frame/reg-event-db
 ::ticket-not-updated
 (fn [db [_ result]]
   (assoc db :http-result result :errors? true :toggle-change false)))

(reg-event-fx
 ::update-ticket-http
 (fn [{:keys [db]} [_ ticket]]
   (prn "update ticket http " ticket)
   (http-put db
             (full-url (str "/tickets/" (:id ticket)))
             (dissoc (assoc ticket :event {:id (:eventId ticket)} ) :eventId)
             [::ticket-updated]
             [::ticket-not-updated])))

(reg-event-fx
 ::update-ticket-from-form
 (fn [{:keys [db]} [_]]
   {:db db
    :dispatch [::update-ticket-http (:form db)]}))

(re-frame/reg-event-db
 ::event-updated
 (fn [db [_ result]]
   (let [event (:body result)
         id (:id event)]
     (-> db
         (assoc-in [:events id] event)
         (assoc :toggle-change false)
         (assoc :form nil)
         (update-in [:event] dissoc :update-id)))))

(re-frame/reg-event-db
 ::event-not-updated
 (fn [db [_ result]]
   (assoc db :http-result result :errors? true :toggle-change false)))

(reg-event-fx
 ::update-event-http
 (fn [{:keys [db]} [_ event]]
   (http-put db
             (full-url (str "/events/" (:id event)))
             (dissoc event :event)
             [::event-updated]
             [::event-not-updated])))

(reg-event-fx
 ::update-event-from-form
 (fn [{:keys [db]} [_]]
   {:db db
    :dispatch [::update-event-http (:event-form db)]}))

(reg-event-fx
 ::change-filter
 (fn [{:keys [db]} [_ idx1 idx2 value]]
   {:fx [[:dispatch [::change-filter-1
                     (:mode db)
                     idx1 idx2 value]]
         [:dispatch  (if (= :tickets (:mode db)) [::download-tickets]  [::download-events])]]}))

(reg-event-fx
 ::change-sort-1
 (fn [{:keys [db]} [_ sort-id sort-opt value]]
   (let [mode (:mode db)]
     {:db (assoc-in db [(if (= mode :tickets) :ticket :event) :sorting sort-id sort-opt] value)})))

(reg-event-fx
 ::change-sort
 (fn [{:keys [db]} [_ sort-id sort-opt value]]
   {:fx [[:dispatch [::change-sort-1 sort-id sort-opt value]]
         [:dispatch  (if (= :tickets (:mode db)) [::download-tickets] [::download-events])]]}))

(re-frame/reg-event-db
 ::event-types-downloaded
 (fn [db [_ event-types]]
   (let [event-types (:body event-types)]
     (assoc db :event-types event-types))))

(re-frame/reg-event-db
 ::event-types-not-downloaded
 (fn [db [_ result]]
   (assoc db :http-result result :errors? true)))

(reg-event-fx
 ::download-event-types
 (fn [{:keys [db]} _]
   (http-get db (full-url "/events/types")
             [::event-types-downloaded]
             [::event-types-not-downloaded])))

(re-frame/reg-event-db
 ::ticket-types-downloaded
 (fn [db [_ event-types]]
   (let [event-types (:body event-types)]
     (assoc db :ticket-types event-types))))

(re-frame/reg-event-db
 ::ticket-types-not-downloaded
 (fn [db [_ result]]
   (assoc db :http-result result :errors? true)))

(reg-event-fx
 ::download-ticket-types
 (fn [{:keys [db]} _]
   (http-get db (full-url "/tickets/types")
             [::ticket-types-downloaded]
             [::ticket-types-not-downloaded])))

(reg-event-fx
 ::add-sorting
 (fn [{:keys [db]} [_ mode]]
   (let [part (if (= mode :tickets) :ticket :event)
         new-sort-id (if (< 0 (count (get-in db [part :sorting])))
                       (inc (key (apply max-key key (get-in db [part :sorting]))))
                       0)]
     {:db
      (assoc-in db [part :sorting new-sort-id] {:field nil :sort-order nil})})))

(re-frame/reg-event-db
 ::ticket-types-count-downloaded
 (fn [db [_ type-count]]
   (let [event-types (:body type-count)]
     (assoc db :tickets-types-count event-types))))

(re-frame/reg-event-db
 ::ticket-types-count-not-downloaded
 (fn [db [_ result]]
   (assoc db :http-result result :errors? true)))

(reg-event-fx
 ::download-ticket-types-count
 (fn [{:keys [db]} [_]]
   (http-get db (full-url (str "/tickets/type/count?type=" (get db :ticket-type)))
             [::ticket-types-count-downloaded]
             [::ticket-types-count-not-downloaded])))

(re-frame/reg-event-db
 ::ticket-discount-count-downloaded
 (fn [db [_ type-count]]
   (let [event-types (:body type-count)]
     (assoc db :tickets-discount-count event-types
            :ticket-discount-count-opened true))))

(re-frame/reg-event-db
 ::ticket-discount-count-not-downloaded
 (fn [db [_ result]]
   (assoc db :http-result result :errors? true)))

(reg-event-fx
 ::download-ticket-discount-count
 (fn [{:keys [db]} _]
   (http-get db (full-url "/tickets/discount/count")
             [::ticket-discount-count-downloaded]
             [::ticket-discount-count-not-downloaded])))

(re-frame/reg-event-db
 ::ticket-discount-sum-downloaded
 (fn [db [_ type-count]]
   (let [event-types (:body type-count)]
     (assoc db :tickets-discount-sum event-types))))

(re-frame/reg-event-db
 ::ticket-discount-sum-not-downloaded
 (fn [db [_ result]]
   (assoc db :http-result result :errors? true)))

(reg-event-fx
 ::download-ticket-discount-sum
 (fn [{:keys [db]} _]
   (http-get db (full-url "/tickets/discount/sum")
             [::ticket-discount-sum-downloaded]
             [::ticket-discount-sum-not-downloaded])))

(reg-event-fx
 ::change-ticket-type
 (fn [{:keys [db]} [_ ticket-type]]
   {:db (assoc db :ticket-type ticket-type)}))

(reg-event-fx
 ::close-hueta
 (fn [{:keys [db]} [_]]
   {:db (assoc db :ticket-discount-count-opened false)}))

(reg-event-fx
 ::remove-sorting
 (fn [{:keys [db]} [_ mode id]]
   {:db (update-in db
                   [(if (= mode :tickets) :ticket :event) :sorting] dissoc id
                   false)}))

(reg-event-fx
 ::start-ticket-copy
 (fn [{:keys [db]} [_ ticket-id]]
   {:db
    (-> db
        (assoc :toggle-copy true)
        (assoc :form (get-in db [:tickets ticket-id]))
        (assoc-in [:ticket :copy-id] ticket-id))}))

(reg-event-fx
 ::end-ticket-copy
 (fn [{:keys [db]} [_]]
   {:db
    (-> db
        (assoc :toggle-copy false)
        (assoc :form nil)
        (update-in [:ticket] dissoc :copy-id))}))

(reg-event-fx
 ::change-ticket-copy-mode
 (fn [{:keys [db]} [_ mode]]
   {:db
    (-> db
        (assoc :ticket-copy-mode mode))}))

(reg-event-fx
 ::ticket-copied
 (fn [_ [_]]
   {:fx [[:dispatch  [::count-tickets]]
         [:dispatch  [::count-events]]
         [:dispatch  [::download-events]]
         [:dispatch  [::download-tickets]]
         [:dispatch  [::end-ticket-copy]]]}))

(reg-event-fx
 ::copy-ticket-http
 (fn [{:keys [db]} [_ mode ticket]]
   (let [hui1-url (full-url-2 (str "/booking/sell/vip/" (:id ticket) "/1"))
         hui2-url (full-url-2 (str "/booking/sell/discount/" (:id ticket) "/1/" (:discount ticket)))
         url (if (= "hui1" mode) hui1-url hui2-url)]
     (http-post db
                url
                nil
                [::ticket-copied]
                [::ticket-not-added]))))

(reg-event-fx
 ::copy-ticket-from-form
 (fn [{:keys [db]} [_]]
   (let [mode (:ticket-copy-mode db)]
     {:dispatch [::copy-ticket-http mode (:form db)]})))


