package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.TableObjects.AddressTable;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.DBUtils;

public class AddressSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(AddressSteps.class);

    AddressTable addressTable = new AddressTable();

    public String checkAddressExist(int id){
        if (!addressTable.checkRowWithIDExist(id)){
            return String.format("There is no row with id '%d' found at the AddressTable", id);
        }
        return "";
    }


}