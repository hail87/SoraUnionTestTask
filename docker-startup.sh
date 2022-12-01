#! /bin/sh

mvn -Dtest=ApiSmokeTestSuite,LwaTestSuite,SubmitOrderTestSuite,ParcelLineTestSuite,AddParcelTestSuite,OrderValidationTestSuite,IrsTestSuite,CancelWarehouseOrderLineTestSuite,CatalogManagementTestSuite,UI_SmokeTestSuite test
#mvn -Dtest=LwaTestSuite test
#mvn -Dtest=LwaTestSuite#addProductTest
