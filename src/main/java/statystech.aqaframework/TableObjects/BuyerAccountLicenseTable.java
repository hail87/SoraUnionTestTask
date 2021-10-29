package statystech.aqaframework.TableObjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.steps.DBsteps.BuyerAccountLicenseSteps;
import statystech.aqaframework.utils.DataUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BuyerAccountLicenseTable extends TableObject{

    private static final Logger logger = LoggerFactory.getLogger(BuyerAccountLicenseTable.class);

    private final String TABLE_NAME = "buyerAccountLicense";

    public BuyerAccountLicenseTable(){
        super.TABLE_NAME = TABLE_NAME;
    }

    private final List<String> encryptedColums = List.of("licenseName", "firstName","lastName","middleName",
            "profession","specialty");

    public void encryptRows() throws SQLException {
        super.encryptRows(encryptedColums);
    }
}
