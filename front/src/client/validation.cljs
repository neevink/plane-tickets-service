(ns client.validation
  (:require
   [cljs.spec.alpha :as s]))

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

(s/def ::name (s/and string? (fn [s] (not= 0 (count s)))))
(s/def ::x #(or (> (parse-long %) -686)
                (and
                 (integer? %)
                 (> % -686))))

(s/def ::y #(or
             (and (string? %) (parse-long %))
             (integer? %)))

(s/def ::coordinates (s/keys :req-un [::x ::y]))
(s/def ::price
  #(or
    (and (number? %) (pos? %))
    (pos? (parse-double %))))

(s/def ::discount
  #(or
    (and (number? %) (pos? %) (<= 0 % 100))
    (and (string? %)
         (pos? (parse-double %))
         (<= 0 (parse-double %) 100))))

(s/def ::refundable (fn [a] (#{"true" "false" true false} a)))
(s/def ::type (fn [v]
                (or
                 (nil? v)
                 (get #{"VIP" "USUAL" "BUDGETARY" "CHEAP"} v))))
(s/def ::creation-date (fn [v]
                         (re-matches #"([0-9]([0-9]([0-9][1-9]|[1-9]0)|[1-9]00)|[1-9]000)(-(0[1-9]|1[0-2])(-(0[1-9]|[1-2][0-9]|3[0-1]))?)?"
                                     v)))

#_(s/def ::ticket-event (fn [v]))

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
