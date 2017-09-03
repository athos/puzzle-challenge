(ns puzzle-challenge.dorokei
  (:import [clojure.lang PersistentQueue]))

(def ^:dynamic *nodes*
  {0 [1 3 4]
   1 [2 5]
   2 [1 3]
   3 [0 2 4]
   4 [0 3 5]
   5 [1 4]})

(defn- opponent-of [turn]
  (case turn
    :cop :robber
    :cop))

(defn step [{:keys [record path] :as state}]
  (let [{:keys [turn cop]} record]
    (keep (fn [node]
            (when (or (not= turn :robber) (not= cop node))
              (assoc state
                     :record (assoc record
                                    :turn (opponent-of turn)
                                    turn node)
                     :path (conj path record))))
          (get *nodes* (get-in state [:record turn])))))

(defn bad? [{:keys [cop robber turn]}]
  (and (= turn :cop)
       (some #{robber} (get *nodes* cop))))

(defn win? [{:keys [cop robber turn]}]
  (and (= turn :robber)
       (= cop 0)
       (#{2 5} robber)))

(defn solve
  ([init]
   (solve (conj PersistentQueue/EMPTY [{:record init :path []}]) #{}))
  ([queue visited]
   (let [states (peek queue), queue (pop queue)]
     (when states
       (if-let [state (some (fn [{:keys [record] :as state}]
                              (and (win? record) state))
                            states)]
         (conj (:path state) (:record state))
         (let [states' (->> (mapcat step states)
                            (remove (fn [{:keys [record]}]
                                      (or (visited record)
                                          (bad? record)))))]
           (recur (conj queue states')
                  (into visited (map :record) states'))))))))
