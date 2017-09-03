(ns puzzle-challenge.dorokei-test
  (:require [clojure.test :refer [deftest is]]
            [puzzle-challenge.dorokei :as dorokei]))

(deftest solve
  (is (= [{:cop 0 :robber 5 :next :cop}
          {:cop 3 :robber 5 :next :robber}
          {:cop 3 :robber 1 :next :cop}
          {:cop 4 :robber 1 :next :robber}
          {:cop 4 :robber 2 :next :cop}
          {:cop 0 :robber 2 :next :robber}
          {:cop 0 :robber 1 :next :cop}
          {:cop 1 :robber 1 :next :robber}]
         (dorokei/solve {:cop 0 :robber 5 :next :cop})))
  (is (= nil
         (binding [dorokei/*nodes* {0 [1 3]
                                    1 [0 2 5]
                                    2 [1 3]
                                    3 [0 2 4]
                                    4 [3 5]
                                    5 [1 4]}]
           (dorokei/solve {:cop 0 :robber 5 :next :cop}))))
  (is (= [{:cop 0 :robber 5 :next :cop}
          {:cop 3 :robber 5 :next :robber}
          {:cop 3 :robber 1 :next :cop}
          {:cop 4 :robber 1 :next :robber}
          {:cop 4 :robber 2 :next :cop}
          {:cop 0 :robber 2 :next :robber}
          {:cop 0 :robber 1 :next :cop}
          {:cop 1 :robber 1 :next :robber}]
         (binding [dorokei/*nodes* {0 [1 3 4]
                                    1 [0 2 5]
                                    2 [1]
                                    3 [0 4]
                                    4 [0 3 5]
                                    5 [1 4]}]
           (dorokei/solve {:cop 0 :robber 5 :next :cop})))))
