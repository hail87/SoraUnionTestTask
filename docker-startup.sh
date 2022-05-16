#! /bin/sh

mvn -Dtest=LwaTestSuite,SubmitOrderTestSuite,ParcelLineTestSuite,AddParcelTestSuite,OrderValidationTestSuite,IrsTestSuite,CancelWarehouseOrderLineTestSuite test

#mvn -Dtest=LwaTestSuite test
#mvn -Dtest=LwaTestSuite#addProductTest
