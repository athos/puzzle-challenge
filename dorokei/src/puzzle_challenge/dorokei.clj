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
  (let [{turn :next} record]
    (keep (fn [node]
            (when (or (not= turn :robber) (not= (:cop record) node))
              (assoc state
                     :record (assoc record
                                    :next (opponent-of turn)
                                    turn node)
                     :path (conj path record))))
          (get *nodes* (get-in state [:record turn])))))

(defn bad? [visited? {:keys [record path]}]
  (let [{:keys [cop robber next]} record
        too-near-cop? #(some #{%} (get *nodes* cop))]
    (if (= next :cop)
      (let [{prev-robber :robber} (peek path)]
        (and (not (every? too-near-cop? (get *nodes* prev-robber)))
             (too-near-cop? robber)))
      (visited? record))))

(defn solve
  ([init]
   (solve (conj PersistentQueue/EMPTY {:record init :path []}) #{}))
  ([queue visited]
   (let [{:keys [record] :as state} (peek queue), queue (pop queue)]
     (when state
       (if (and (= (:cop record) (:robber record)))
         (conj (:path state) record)
         (let [states (remove (partial bad? visited) (step state))]
           (recur (into queue states)
                  (into visited (map :record) states))))))))
