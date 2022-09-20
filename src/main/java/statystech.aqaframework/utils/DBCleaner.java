package statystech.aqaframework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBCleaner {

    private static final Logger logger = LoggerFactory.getLogger(DBCleaner.class);

    public static String cleanDBafter7783(String errorMessage){
            DBUtils.executeSqlScript("clean_all_lwa_test_data.sql");
            DBUtils.executeSqlScript("clean_new_billing_address.sql");
        return errorMessage;
    }
}
