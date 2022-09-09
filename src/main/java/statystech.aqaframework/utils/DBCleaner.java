package statystech.aqaframework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

public class DBCleaner {

    private static final Logger logger = LoggerFactory.getLogger(DBCleaner.class);

    public static String cleanDBafter7783(String errorMessage){
            DBUtils.cleanDB("clean_all_lwa_test_data.sql");
            DBUtils.cleanDB("clean_new_billing_address.sql");
        return errorMessage;
    }
}
