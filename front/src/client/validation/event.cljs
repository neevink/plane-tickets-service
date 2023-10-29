(ns client.validation.event
  (:require
   [cljs.spec.alpha :as s]))

(def event-messages
  {::name  "Ожидалась не пустая строка"
   ::date "Ожидалась строка в формате yyyy-MM-dd"
   ::minAge "Ожидалось целое число > 0"
   ::eventType "Ожидался один из: CONCERT, BASEBALL, BASKETBALL, THEATRE_PERFORMANCE"})

(s/def ::name (s/and string? (fn [s] (not= 0 (count s)))))

(s/def ::date (fn [v]
                 (or (= v "") (nil? v)
                 (re-matches
                   #"(\d{4})-(\d{2})-(\d{2})" v))))

(s/def ::minAge #(or
                  (and (number? %) (pos? %))
                  (and (string? %) (pos? (parse-long %)))))

(s/def ::eventType (fn [v]
                     (or
                      (nil? v)
                      (= "" v)
                      (get #{"CONCERT" "BASEBALL" "BASKETBALL" "THEATRE_PERFORMANCE"} v))))

(s/def ::event (s/keys :req-un [::name
                                ::minAge]
                       :opt-un [::eventType
                                ::date]))

