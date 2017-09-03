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

(defn step [{:keys [record path visited]}]
  (let [turn (:next record)]
    (keep (fn [node]
            (when (or (not= turn :robber) (not= (:cop record) node))
              (let [record' (assoc record
                                   :next (opponent-of turn)
                                   turn node)]
                (when-not (visited record')
                  {:record record'
                   :path (conj path record)
                   :visited (conj visited record)}))))
          (get *nodes* (get record turn)))))

(defn bad? [{:keys [record path]}]
  (let [{:keys [cop robber next]} record
        too-near-cop? #(some #{%} (get *nodes* cop))]
    (when (= next :cop)
      (let [{prev-robber :robber} (peek path)]
        (and (some (complement too-near-cop?) (get *nodes* prev-robber))
             (too-near-cop? robber))))))

(defn solve [init]
  (loop [queue (conj PersistentQueue/EMPTY
                     {:record init :path [] :visited #{}})]
    (let [{:keys [record] :as state} (peek queue), queue (pop queue)]
      (when state
        (if (and (= (:cop record) (:robber record)))
          (conj (:path state) record)
          (let [states (remove bad? (step state))]
            (recur (into queue states))))))))
