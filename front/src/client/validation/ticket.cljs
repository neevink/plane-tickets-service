(ns client.validation.ticket
  (:require
   [cljs.spec.alpha :as s]))

(def ticket-messages
  {::name  "Ожидалась не пустая строка"
  ;; ::coordinates "Ожидались не пустые координаты"
   ::x "Ожидалась x > - 686 (целое число)"
   ::y "Ожидалась y - целое число"
   ::price "Ожидалось целое число больше 0"
   ::discount "Ожидалась скидка - целочисленное число 0 < x <= 100"
   ::type "Ожидался тип: один из VIP, USUAL, BUDGETARY, CHEAP"
   ::refundable "Ожидался true/false"
   ::eventId "Ожидалось выбранное мероприятие"
   ::creationDate "Ожидалась строка в формате YYYY-MM-DD"})

(s/def ::name (s/and string? (fn [s] (not= 0 (count s)))))


(s/def ::x #(or (and
                  (integer? %)
                  (> % -686))
                (and (some? %) (string? %) (> (parse-long %) -686))))

(s/def ::y #(or
             (and (string? %) (parse-long %))
             (integer? %)))

(s/def ::coordinates (s/keys :req-un [::x ::y]))
(s/def ::price
  #(or
    (and (number? %) (pos? %))
    (and (some? %) (pos? (parse-double %)))))

(s/def ::discount
  #(or
    (and (number? %) (pos? %) (<= 0 % 100))
    (and (string? %)
         (pos? (parse-double %))
         (<= 0 (parse-double %) 100))))

(s/def ::refundable (fn [a] (some? (#{"true" "false" true false} a))))

(s/def ::type (fn [v]
                (or
                 (nil? v)
                 (get #{"VIP" "USUAL" "BUDGETARY" "CHEAP"} v))))

(s/def ::creationDate (fn [v]
                        (re-matches #"([0-9]([0-9]([0-9][1-9]|[1-9]0)|[1-9]00)|[1-9]000)(-(0[1-9]|1[0-2])(-(0[1-9]|[1-2][0-9]|3[0-1]))?)?"
                                    v)))

(s/def ::eventId #(number? %))

(s/def ::ticket (s/keys :req-un [::name
                                 ::coordinates
                                 ::price
                                 ::discount
                                 ::eventId
                                 ::refundable
                                 ::creationDate]
                        :opt-un [::type
                                 ]))
