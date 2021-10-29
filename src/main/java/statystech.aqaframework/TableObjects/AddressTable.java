package statystech.aqaframework.TableObjects;

import edu.emory.mathcs.backport.java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.utils.DataUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddressTable extends TableObject{

    private static final Logger logger = LoggerFactory.getLogger(TableObject.class);

    private final String TABLE_NAME = "address";

    public AddressTable(){
        super.TABLE_NAME = TABLE_NAME;
    }

    private final List<String> encryptedColums = List.of("addressLine1", "addressLine2","city","companyName","eMail",
            "firstName","lastName","middleName","phoneNumber1","phoneNumber2","postalCode","vAddressLine1",
            "vAddressLine2","vAddressLine3","vCity","vPostalCode");

    public void encryptRows() throws SQLException {
        super.encryptRows(encryptedColums);
    }
}
