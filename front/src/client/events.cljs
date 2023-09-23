(ns client.events
  (:require
   [re-frame.core :as re-frame :refer [reg-event-db reg-event-fx]]
   [cljs.spec.alpha :as s]
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
     {:db (assoc db :tickets tickets-id-map)})))

(defn move-ticket [db prev-id new-id]
 (-> (assoc-in db 
               [:tickets new-id] 
               (get-in db [:tickets prev-id]))
     (update-in [:tickets] dissoc prev-id)))

(reg-event-fx
 ::change-ticket
 (fn [{:keys [db]} [_ ticket-id prop-path new-value]]
  (if (= :id (first prop-path))
   (do
    (println "hello " ticket-id new-value)
    {:db (move-ticket db ticket-id new-value)})
   {:db (assoc-in db (into [:tickets ticket-id] prop-path) new-value)})))

(def my-messages
 {::name  "Ожидалась не пустая строка"
  ;; ::coordinates "Ожидались не пустые координаты"
  ::x "Ожидалась x > - 686 (целое число)"
  ::y "Ожидалась y - целое число"
  ::price "Ожидалось целое число больше 0"
  ::discount "Ожидалась скидка - целочисленное число"
  ::type "Ожидался тип: один из VIP, USUAL, BUDGETARY, CHEAP"
  ::refundable "Ожидался true/false"
  ::creation-date "Ожидалась строка в формате YYYY-MM-DD"})

(s/def ::ne (fn [s] (not= 0 (count s))))

(s/def ::name (s/and string? (fn [s] (not= 0 (count s)))))
(s/def ::x (s/and integer? #(> % -686)))
(s/def ::y (s/and integer?))

(s/def ::coordinates (s/keys :req-un [::x ::y]))
(s/def ::price (s/and number? pos?))
(s/def ::discount (s/and number? pos? #(<= % 100)))
(s/def ::refundable (fn [a] (#{"true" "false" true false} a)))
(s/def ::type (fn [v] 
                      (or 
                       (nil? v)
                       (get #{"VIP" "USUAL" "BUDGETARY" "CHEAP"} v))))
(s/def ::creation-date (fn [v] 
                        (re-matches #"([0-9]([0-9]([0-9][1-9]|[1-9]0)|[1-9]00)|[1-9]000)(-(0[1-9]|1[0-2])(-(0[1-9]|[1-2][0-9]|3[0-1]))?)?" 
                                 v)))

#_(s/def ::ticket-event (fn [v] 
                         ))
                       
(s/def ::ticket (s/keys :req-un [::name 
                                 ::coordinates
                                 ::price 
                                 ::discount 
                                 ::type 
                                 ::refundable 
                                 ::creation-date]))

(defn get-message
  [via messages]
  (->> (reverse via)
       (some messages)))

(defn validate [new-ticket]
 (if (s/valid? ::ticket new-ticket) :ok
  (filter (fn [m] (-> m :path not-empty)) 
          (mapv 
           (fn [{path :path via :via}]
            {:path path
             :message (get-message via my-messages)})
           (:cljs.spec.alpha/problems (s/explain-data ::ticket new-ticket))))))


(reg-event-fx 
 ::save-form
 (fn [{:keys [db]} [_ path value]]
  {:db (assoc-in db (into [:form ] path) value)}))

(reg-event-fx 
 ::validate-form
 (fn [{:keys [db]} [_ _]]
  (let [validate-res (validate (get db :form))]
   {:db (assoc db :form-valid validate-res)})))


(reg-event-fx
 ::change-ticket-all
 (fn [{:keys [db]} [_ ticket-id]]
  {:db db
   :dispatch [::validate-form ticket-id]}))

(reg-event-fx
 ::delete-ticket
 (fn [{:keys [db]} [_ ticket-id]]
  {:db (-> (update-in db [:tickets] dissoc ticket-id)
           )
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
