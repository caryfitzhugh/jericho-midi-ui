(ns test.jericho-midi.subscriptions-test
  (:require
   [jericho-midi.subscriptions :as subscriptions]
   [jericho-midi.events :as events]
   [test.jericho-midi.utils :as u]
   [devcards.core :refer-macros [deftest]]
   [cljs.test :refer-macros [is testing run-tests]]))

(deftest basic-subscriptions
  (is (subscriptions/active-panel {:navigation {:panel true}} nil))
  (is (subscriptions/active-section {:navigation {:section true}} nil))
  (is (subscriptions/loading? {:loading? true} nil))
  (is (subscriptions/error {:error true} nil)))

(deftest teams
  (is (subscriptions/teams {:teams true} nil))
  (is (= (subscriptions/current-team-id {:current-team :1} nil)
         :1))
  (is (= (subscriptions/current-team [{:1 :TEAM} :1] nil)
         :TEAM)))

(deftest users
  (testing "That intials are added to user record"
    (let [usr {:id :1 :username "Cary FitzHugh"}
          usrs {:1 usr}]
      (is (= (subscriptions/users {:users usrs} nil)
             (assoc-in usrs [:1 :initials] "CF"))))))

(deftest layout-view-settings
  (testing "Layout provides just layout/view-settings"
    (is (subscriptions/layout-view-setting {:inner true} [:n/a-key :inner])))

  (is (= (subscriptions/layout-view-settings {:view-settings {:layout :BLOB}} nil)
         :BLOB)))

(deftest current-locale
  (is (= (subscriptions/current-locale {:current-locale :en-PIRATE} nil)
         :en-PIRATE)))
