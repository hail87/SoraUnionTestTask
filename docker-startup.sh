#! /bin/sh

mvn -Dtest=LwaTestSuite,SubmitOrderTestSuite,OrderValidationTestSuite,ParcelLineTestSuite test
#mvn -Dtest=LwaTestSuite test
#mvn -Dtest=LwaTestSuite#addProductTest test

