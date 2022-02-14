#! /bin/sh

mvn -Dtest=LwaTestSuite,SubmitOrderTestSuite,ParcelLineTestSuite,AddParcelTestSuite,OrderValidationTestSuite,ProductSearchTestSuite test
#mvn -Dtest=LwaTestSuite test
#mvn -Dtest=LwaTestSuite#addProductTest test

