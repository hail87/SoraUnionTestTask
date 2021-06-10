package statystech.aqaframework.TableObjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.steps.DBsteps.BuyerAccountLicenseSteps;

public class BuyerAccountLicenseTable extends TableObject{

    private static final Logger logger = LoggerFactory.getLogger(BuyerAccountLicenseTable.class);

    private final String TABLE_NAME = "buyerAccountLicense";

    public BuyerAccountLicenseTable(){
        super.TABLE_NAME = TABLE_NAME;
    }
}
