#! /bin/sh

mvn -Dtest=LwaTestSuite,SubmitOrderTestSuite,ParcelLineTestSuite,AddParcelTestSuite,OrderValidationTestSuite,IrsTestSuite,CancelWarehouseOrderLineTestSuite,UI_SmokeTestSuite test

#mvn -Dtest=LwaTestSuite test
#mvn -Dtest=LwaTestSuite#addProductTest
