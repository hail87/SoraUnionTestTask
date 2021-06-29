#! /bin/sh

mvn -Dtest=LwaTestSuite,SubmitOrderTestSuite,OrderValidationTestSuite test
