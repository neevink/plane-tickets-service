(ns client.validation.event
  (:require
   [cljs.spec.alpha :as s]))

(def event-messages
  {::name  "Ожидалась не пустая строка"
   ::date "Ожидалась строка в формате YYYY-MM-DD"
   ::minAge "Ожидалось целое число > 0"
   ::eventType "Ожидался один из: CONCERT, BASEBALL, BASKETBALL, THEATRE_PERFORMANCE"})

(s/def ::name (s/and string? (fn [s] (not= 0 (count s)))))
(s/def ::date  (fn [v]
                 (re-matches #"([0-9]([0-9]([0-9][1-9]|[1-9]0)|[1-9]00)|[1-9]000)(-(0[1-9]|1[0-2])(-(0[1-9]|[1-2][0-9]|3[0-1]))?)?"
                             v)))

(s/def ::minAge #(or
                  (and (number? %) (pos? %))
                  (pos? (parse-long %))))

(s/def ::eventType (fn [v]
                     (or
                      (nil? v)
                      (get #{"CONCERT" "BASEBALL" "BASKETBALL" "THEATRE_PERFORMANCE"} v))))

(s/def ::event (s/keys :req-un [::name
                                ::minAge]
                       :opt [::eventType
                             ::date]))
