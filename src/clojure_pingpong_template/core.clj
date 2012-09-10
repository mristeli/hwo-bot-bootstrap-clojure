(ns clojure-pingpong-template.core
  (:use [clojure.data.json :only (read-json json-str)]
        [clojure.tools.logging :only (info error)])
  (:import [java.net Socket]
           [java.io PrintWriter InputStreamReader BufferedReader])
  (:gen-class :main true))

(def move-up {:msgType "changeDir" :data -1.0})

(defn write [conn data]
  (doto (:out @conn)
    (.println (json-str data))
    (.flush)))

(defn make-move [conn]
  (write conn move-up))

(defn handle-message [conn {msgType :msgType data :data}]
  (case msgType
    joined (info (str "Game joined successfully. Use following URL for visualization: " data))
    gameStarted (info (str "Game started: " (nth data 0) " vs. " (nth data 1)))
    gameIsOn (make-move conn)
    gameIsOver (info (str "Game ended. Winner: " data))
    error (error data)
    'pass))

(defn parse-message [data]
  (try
    (let [msg (read-json data)]
      {:msgType (symbol (:msgType msg))
       :data    (:data msg)})
    (catch Throwable e {:msgType 'error :data (. e getMessage)})))

(defn conn-handler [conn]
  (while (nil? (:exit @conn))
    (let [msg (.readLine (:in @conn))]
      (cond
        (nil? msg) (dosync (alter conn merge {:exit true}))
        :else (handle-message conn (parse-message msg))))))

(defn connect [server]
  (let [socket (Socket. (:name server) (:port server))
        in (BufferedReader. (InputStreamReader. (.getInputStream socket)))
        out (PrintWriter. (.getOutputStream socket))
        conn (ref {:in in :out out})]
    (doto (Thread. #(conn-handler conn)) (.start))
    conn))

(defn -main [team-name hostname port]
  (let [s (connect {:name hostname :port (read-string port)})
        join-message {:msgType "join" :data team-name}]
    (write s join-message)))