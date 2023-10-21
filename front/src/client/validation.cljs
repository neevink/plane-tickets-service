(ns client.validation
  (:require
   [client.validation.ticket :as ticket-validation]
   [client.validation.event :as event-validation]
   [cljs.spec.alpha :as s]))

(defn get-message
  [via messages]
  (->> (reverse via)
       (some messages)))

(defn validate-event [new-event]
  (prn "validate-event " new-event)
  (if (s/valid? ::event-validation/event new-event) :ok
      (filter (fn [m] (-> m :path not-empty))
              (mapv
               (fn [{path :path via :via}]
                 {:path path
                  :message (get-message via event-validation/event-messages)})
               (:cljs.spec.alpha/problems (s/explain-data ::event-validation/event new-event))))))

(defn validate-ticket [new-ticket]
  (prn "ticket " new-ticket)
  (if (s/valid? ::ticket-validation/ticket new-ticket) :ok
      (filter (fn [m] (-> m :path not-empty))
              (mapv
               (fn [{path :path via :via}]
                 {:path path
                  :message (get-message via ticket-validation/ticket-messages)})
               (:cljs.spec.alpha/problems (s/explain-data ::ticket-validation/ticket new-ticket))))))
