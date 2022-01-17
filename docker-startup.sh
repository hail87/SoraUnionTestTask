#! /bin/sh

mvn -Dtest=LwaTestSuite,SubmitOrderTestSuite,OrderValidationTestSuite,ParcelLineTestSuite,AddParcelTestSuite test
#mvn -Dtest=LwaTestSuite test
#mvn -Dtest=LwaTestSuite#addProductTest test

