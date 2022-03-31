#! /bin/sh

mvn -Dtest=LwaTestSuite,SubmitOrderTestSuite,ParcelLineTestSuite,AddParcelTestSuite,OrderValidationTestSuite,IrsTestSuite test

#mvn -Dtest=LwaTestSuite test
#mvn -Dtest=LwaTestSuite#addProductTest
