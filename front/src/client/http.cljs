(ns client.http
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))

(defn read-response [response-chan]
  (go (let [resp (<! response-chan)]
        resp)))

(defn get-http [endpoint]
  (read-response (go (let [response (<! (http/get endpoint
                                   {:with-credentials? false}))]
        (select-keys response [:body :status])))))

(defn events []
  (:body (get-http "http://localhost:8080/events")))

(defn tickets []
  (prn "downloading tickets")
  (:body (get-http "http://localhost:8080/tickets")))

